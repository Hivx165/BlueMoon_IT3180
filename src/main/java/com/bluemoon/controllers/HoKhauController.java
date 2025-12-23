package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class HoKhauController {

    @FXML
    private TableView<HoKhauDisplay> hoKhauTable;

    @FXML private TableColumn<HoKhauDisplay, String> colMaHoKhau;
    @FXML private TableColumn<HoKhauDisplay, String> colTenChuHo;
    @FXML private TableColumn<HoKhauDisplay, String> colDiaChi;
    @FXML private TableColumn<HoKhauDisplay, Date> colNgayLap;
    @FXML private TableColumn<HoKhauDisplay, Float> colDienTich;

    @FXML
    private TextField searchField;

    private ObservableList<HoKhauDisplay> listHoKhau = FXCollections.observableArrayList();

    public void initialize() {
        // 1. Cấu hình cột
        colMaHoKhau.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        colTenChuHo.setCellValueFactory(new PropertyValueFactory<>("tenChuHo"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colNgayLap.setCellValueFactory(new PropertyValueFactory<>("ngayLap"));
        colDienTich.setCellValueFactory(new PropertyValueFactory<>("dienTich"));

        // 2. Load dữ liệu
        loadData();
    }

    // --- TẢI DỮ LIỆU TỪ SQL ---
    private void loadData() {
        listHoKhau.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Kỹ thuật JOIN: Lấy thông tin Hộ Khẩu + Tên người có quan hệ là 'Chủ hộ'
            String sql = "SELECT hk.MaHoKhau, hk.DiaChi, hk.NgayLap, hk.DienTichHo, nk.HoTen AS TenChuHo " +
                    "FROM hokhau hk " +
                    "LEFT JOIN nhankhau nk ON hk.MaHoKhau = nk.MaHoKhau AND nk.QuanHe = N'Chủ hộ'";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tenChu = rs.getString("TenChuHo");
                if (tenChu == null) tenChu = "(Chưa có chủ hộ)";

                listHoKhau.add(new HoKhauDisplay(
                        rs.getString("MaHoKhau"),
                        tenChu,
                        rs.getString("DiaChi"),
                        rs.getDate("NgayLap"),
                        rs.getFloat("DienTichHo")
                ));
            }
            hoKhauTable.setItems(listHoKhau);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi tải dữ liệu", e.getMessage());
        }
    }

    // --- XỬ LÝ TÌM KIẾM ---
    @FXML
    private void handleSearch() {
        String key = searchField.getText().toLowerCase();
        ObservableList<HoKhauDisplay> filter = FXCollections.observableArrayList();
        for (HoKhauDisplay hk : listHoKhau) {
            if (hk.getMaHoKhau().toLowerCase().contains(key) ||
                    hk.getDiaChi().toLowerCase().contains(key)) {
                filter.add(hk);
            }
        }
        hoKhauTable.setItems(filter);
    }

    @FXML
    private void handleRefresh() {
        loadData();
        searchField.clear();
    }

    // --- XỬ LÝ THÊM MỚI ---
    @FXML
    private void handleAdd() {
        try {
            // Load giao diện Thêm Hộ Khẩu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bluemoon/views/ThemHoKhau.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Thêm Hộ Khẩu Mới");
            stage.setScene(new Scene(root));

            // Chặn cửa sổ chính cho đến khi form đóng lại
            stage.initModality(Modality.WINDOW_MODAL);
            if (hoKhauTable.getScene() != null) {
                stage.initOwner(hoKhauTable.getScene().getWindow());
            }

            stage.showAndWait(); // Chờ người dùng thao tác xong

            // Tải lại dữ liệu sau khi thêm
            handleRefresh();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form thêm: " + e.getMessage());
        }
    }

    @FXML
    private void handleEdit() {
        showAlert("Thông báo", "Chức năng Sửa đang được cập nhật.");
    }

    // --- XỬ LÝ XÓA ---
    @FXML
    private void handleDelete() {
        HoKhauDisplay selected = hoKhauTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Cảnh báo", "Vui lòng chọn hộ khẩu cần xóa!");
            return;
        }

        // Xác nhận xóa
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn xóa hộ khẩu mã: " + selected.getMaHoKhau() + " ?\n" +
                "Lưu ý: Tất cả nhân khẩu trong hộ này cũng sẽ bị xóa!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteHoKhau(selected.getMaHoKhau());
        }
    }

    private void deleteHoKhau(String maHo) {
        // Cần xóa Nhân khẩu trước (do khóa ngoại), sau đó mới xóa Hộ khẩu
        // Hoặc nếu CSDL đã set Cascade Delete thì chỉ cần xóa Hộ khẩu
        String sqlDeleteNhanKhau = "DELETE FROM nhankhau WHERE MaHoKhau = ?";
        String sqlDeleteHoKhau = "DELETE FROM hokhau WHERE MaHoKhau = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Xóa nhân khẩu thuộc hộ này trước
            try (PreparedStatement stmt1 = conn.prepareStatement(sqlDeleteNhanKhau)) {
                stmt1.setString(1, maHo);
                stmt1.executeUpdate();
            }

            // 2. Xóa hộ khẩu
            try (PreparedStatement stmt2 = conn.prepareStatement(sqlDeleteHoKhau)) {
                stmt2.setString(1, maHo);
                stmt2.executeUpdate();
            }

            conn.commit(); // Xác nhận thực hiện
            showAlert("Thành công", "Đã xóa hộ khẩu " + maHo);
            handleRefresh();

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback(); // Hoàn tác nếu lỗi
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            showAlert("Lỗi SQL", "Không thể xóa: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (Exception e) {}
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // --- INNER CLASS (Model hiển thị) ---
    public static class HoKhauDisplay {
        private final SimpleStringProperty maHoKhau;
        private final SimpleStringProperty tenChuHo;
        private final SimpleStringProperty diaChi;
        private final SimpleObjectProperty<Date> ngayLap;
        private final SimpleFloatProperty dienTich;

        public HoKhauDisplay(String ma, String ten, String dc, Date ngay, float dt) {
            this.maHoKhau = new SimpleStringProperty(ma);
            this.tenChuHo = new SimpleStringProperty(ten);
            this.diaChi = new SimpleStringProperty(dc);
            this.ngayLap = new SimpleObjectProperty<>(ngay);
            this.dienTich = new SimpleFloatProperty(dt);
        }

        public String getMaHoKhau() { return maHoKhau.get(); }
        public String getTenChuHo() { return tenChuHo.get(); }
        public String getDiaChi() { return diaChi.get(); }
        public Date getNgayLap() { return ngayLap.get(); }
        public float getDienTich() { return dienTich.get(); }
    }
}