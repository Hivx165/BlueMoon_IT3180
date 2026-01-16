package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class NhanKhauController {

    @FXML private TableView<NhanKhauDisplay> nhanKhauTable;
    @FXML private TableColumn<NhanKhauDisplay, String> colMaHo;
    @FXML private TableColumn<NhanKhauDisplay, String> colHoTen;
    @FXML private TableColumn<NhanKhauDisplay, String> colNgaySinh;
    @FXML private TableColumn<NhanKhauDisplay, String> colGioiTinh;
    @FXML private TableColumn<NhanKhauDisplay, String> colCCCD;
    @FXML private TableColumn<NhanKhauDisplay, String> colSDT;
    @FXML private TableColumn<NhanKhauDisplay, String> colQuanHe;
    @FXML private TableColumn<NhanKhauDisplay, String> colTrangThai;
    @FXML private TableColumn<NhanKhauDisplay, String> colGhiChu;
    @FXML private TextField searchField;

    private ObservableList<NhanKhauDisplay> listNhanKhau = FXCollections.observableArrayList();

    public void initialize() {
        colMaHo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMaHo()));
        colHoTen.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoTen()));
        colNgaySinh.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNgaySinh()));
        colGioiTinh.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGioiTinh()));
        colCCCD.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCccd()));
        colSDT.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSdt()));
        colQuanHe.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getQuanHe()));
        colTrangThai.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTrangThai()));
        colGhiChu.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGhiChu()));

        loadData();
    }

    private void loadData() {
        listNhanKhau.clear();
        String sql = "SELECT * FROM nhankhau";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String trangThai = rs.getInt("TamVang") == 1 ? "Tạm vắng" :
                                   rs.getInt("TamTru") == 1 ? "Tạm trú" : "Thường trú";

                listNhanKhau.add(new NhanKhauDisplay(
                        rs.getString("MaHoKhau"),
                        rs.getString("HoTen"),
                        rs.getDate("NgaySinh").toString(),
                        rs.getString("GioiTinh"),
                        rs.getString("SoCMND_CCCD"),
                        rs.getString("SoDT"),
                        rs.getString("QuanHe"),
                        trangThai,
                        rs.getString("GhiChu")
                ));
            }
            nhanKhauTable.setItems(listNhanKhau);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            showAlert("Thông báo", "Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        ObservableList<NhanKhauDisplay> filteredList = FXCollections.observableArrayList();
        for (NhanKhauDisplay nhanKhau : listNhanKhau) {
            if (nhanKhau.getHoTen().toLowerCase().contains(keyword) ||
                nhanKhau.getCccd().toLowerCase().contains(keyword) ||
                nhanKhau.getSdt().toLowerCase().contains(keyword)) {
                filteredList.add(nhanKhau);
            }
        }

        nhanKhauTable.setItems(filteredList);

        if (filteredList.isEmpty()) {
            showAlert("Kết quả", "Không tìm thấy nhân khẩu nào phù hợp với từ khóa!");
        }
    }

    @FXML
    private void handleRefresh() {
        // Reload the data into the TableView
        loadData();
        showAlert("Thông báo", "Dữ liệu đã được làm mới!");
    }

    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bluemoon/views/ThemNhanKhau.fxml")); // Fixed resource path
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Thêm Nhân Khẩu");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait(); // Wait for the form to close

            // Refresh the TableView after the form is closed
            handleRefresh();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form Thêm Nhân Khẩu: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        NhanKhauDisplay selected = nhanKhauTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Cảnh báo", "Vui lòng chọn một nhân khẩu để xóa!");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Xác nhận");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Bạn có chắc chắn muốn xóa nhân khẩu này?");
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            String sql = "DELETE FROM nhankhau WHERE SoCMND_CCCD = '" + selected.getCccd() + "'";

            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {

                int rowsAffected = stmt.executeUpdate(sql);
                if (rowsAffected > 0) {
                    showAlert("Thông báo", "Xóa nhân khẩu thành công!");
                    handleRefresh(); // Refresh the TableView
                } else {
                    showAlert("Lỗi", "Không thể xóa nhân khẩu. Vui lòng thử lại!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Lỗi", "Không thể xóa nhân khẩu: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static class NhanKhauDisplay {
        private final String maHo;
        private final String hoTen;
        private final String ngaySinh;
        private final String gioiTinh;
        private final String cccd;
        private final String sdt;
        private final String quanHe;
        private final String trangThai;
        private final String ghiChu;

        public NhanKhauDisplay(String maHo, String hoTen, String ngaySinh, String gioiTinh, String cccd,
                               String sdt, String quanHe, String trangThai, String ghiChu) {
            this.maHo = maHo;
            this.hoTen = hoTen;
            this.ngaySinh = ngaySinh;
            this.gioiTinh = gioiTinh;
            this.cccd = cccd;
            this.sdt = sdt;
            this.quanHe = quanHe;
            this.trangThai = trangThai;
            this.ghiChu = ghiChu;
        }

        public String getMaHo() { return maHo; }
        public String getHoTen() { return hoTen; }
        public String getNgaySinh() { return ngaySinh; }
        public String getGioiTinh() { return gioiTinh; }
        public String getCccd() { return cccd; }
        public String getSdt() { return sdt; }
        public String getQuanHe() { return quanHe; }
        public String getTrangThai() { return trangThai; }
        public String getGhiChu() { return ghiChu; }
    }
}
