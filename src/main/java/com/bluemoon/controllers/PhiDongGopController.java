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
import java.sql.ResultSet;
import java.sql.Statement;

public class PhiDongGopController {

    @FXML private TableView<PhiDongGopDisplay> tablePhiDongGop;
    @FXML private TableColumn<PhiDongGopDisplay, String> colMaHo;
    @FXML private TableColumn<PhiDongGopDisplay, String> colTenKhoanThu;
    @FXML private TableColumn<PhiDongGopDisplay, Float> colSoTien;
    @FXML private TableColumn<PhiDongGopDisplay, Date> colNgayDong;

    @FXML private TextField searchField;

    private ObservableList<PhiDongGopDisplay> list = FXCollections.observableArrayList();

    public void initialize() {
        // 1. Cấu hình cột (Map với tên getter trong Class nội bộ bên dưới)
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHo"));
        colTenKhoanThu.setCellValueFactory(new PropertyValueFactory<>("tenKhoanThu"));
        colSoTien.setCellValueFactory(new PropertyValueFactory<>("soTien"));
        colNgayDong.setCellValueFactory(new PropertyValueFactory<>("ngayDong"));

        // 2. Tải dữ liệu ban đầu
        loadData();
    }

    // --- TẢI DỮ LIỆU TỪ SQL ---
    private void loadData() {
        list.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM donggop";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(new PhiDongGopDisplay(
                        rs.getString("MaHoKhau"),
                        rs.getString("TenKhoanDongGop"),
                        rs.getFloat("SoTien"),
                        rs.getDate("NgayDong")
                ));
            }
            tablePhiDongGop.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải dữ liệu: " + e.getMessage());
        }
    }

    // --- XỬ LÝ TÌM KIẾM ---
    @FXML
    private void handleSearch() {
        String key = searchField.getText().toLowerCase();
        ObservableList<PhiDongGopDisplay> filter = FXCollections.observableArrayList();
        for (PhiDongGopDisplay p : list) {
            if (p.getTenKhoanThu().toLowerCase().contains(key) ||
                    p.getMaHo().toLowerCase().contains(key)) {
                filter.add(p);
            }
        }
        tablePhiDongGop.setItems(filter);
    }

    @FXML
    private void handleRefresh() {
        loadData();
        searchField.clear();
    }

    // --- XỬ LÝ THÊM MỚI (Mở Form ThemDongGop.fxml) ---
    @FXML
    private void handleAdd() {
        try {
            // Load giao diện nhập liệu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bluemoon/views/ThemDongGop.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ghi nhận đóng góp");
            stage.setScene(new Scene(root));

            // Chặn cửa sổ chính (Modal)
            stage.initModality(Modality.WINDOW_MODAL);

            // Lấy cửa sổ cha để chặn
            if (tablePhiDongGop.getScene() != null) {
                stage.initOwner(tablePhiDongGop.getScene().getWindow());
            }

            stage.showAndWait(); // Chờ đến khi nhập xong và đóng form

            // Sau khi đóng form, tải lại bảng để hiện dữ liệu mới
            handleRefresh();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form thêm mới: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // --- CLASS NỘI BỘ (MODEL HIỂN THỊ) ---
    public static class PhiDongGopDisplay {
        private final SimpleStringProperty maHo;
        private final SimpleStringProperty tenKhoanThu;
        private final SimpleFloatProperty soTien;
        private final SimpleObjectProperty<Date> ngayDong;

        public PhiDongGopDisplay(String ma, String ten, float tien, Date ngay) {
            this.maHo = new SimpleStringProperty(ma);
            this.tenKhoanThu = new SimpleStringProperty(ten);
            this.soTien = new SimpleFloatProperty(tien);
            this.ngayDong = new SimpleObjectProperty<>(ngay);
        }

        public String getMaHo() { return maHo.get(); }
        public String getTenKhoanThu() { return tenKhoanThu.get(); }
        public float getSoTien() { return soTien.get(); }
        public Date getNgayDong() { return ngayDong.get(); }
    }
}