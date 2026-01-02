package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import java.sql.ResultSet;
import java.sql.Statement;

public class PhiDichVuController {

    @FXML private TableView<PhiDichVuDisplay> tablePhiDichVu;
    @FXML private TableColumn<PhiDichVuDisplay, String> colMaHo;
    @FXML private TableColumn<PhiDichVuDisplay, Float> colDienTich;
    @FXML private TableColumn<PhiDichVuDisplay, Float> colDonGia;
    @FXML private TableColumn<PhiDichVuDisplay, Float> colTongTien;
    @FXML private TableColumn<PhiDichVuDisplay, Integer> colNam;

    @FXML private TextField searchField;

    private ObservableList<PhiDichVuDisplay> list = FXCollections.observableArrayList();

    public void initialize() {
        // 1. Cấu hình cột (Phải trùng tên biến trong Class nội bộ bên dưới)
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHo"));
        colDienTich.setCellValueFactory(new PropertyValueFactory<>("dienTich"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        colNam.setCellValueFactory(new PropertyValueFactory<>("nam"));

        // 2. Tải dữ liệu ban đầu
        loadData();
    }

    // --- TẢI DỮ LIỆU TỪ SQL ---
    private void loadData() {
        list.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // JOIN bảng phidichvu và hokhau để lấy diện tích thực tế của hộ đó
            String sql = "SELECT pdv.MaHoKhau, hk.DienTichHo, pdv.GiaPhi, pdv.TienNopMoiThang, pdv.Nam " +
                    "FROM phidichvu pdv " +
                    "JOIN hokhau hk ON pdv.MaHoKhau = hk.MaHoKhau";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new PhiDichVuDisplay(
                        rs.getString("MaHoKhau"),
                        rs.getFloat("DienTichHo"),
                        rs.getFloat("GiaPhi"),
                        rs.getFloat("TienNopMoiThang"),
                        rs.getInt("Nam")
                ));
            }
            tablePhiDichVu.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải dữ liệu: " + e.getMessage());
        }
    }

    // --- XỬ LÝ TÌM KIẾM ---
    @FXML
    private void handleSearch() {
        String key = searchField.getText().toLowerCase();
        ObservableList<PhiDichVuDisplay> filter = FXCollections.observableArrayList();
        for (PhiDichVuDisplay p : list) {
            if (p.getMaHo().toLowerCase().contains(key)) {
                filter.add(p);
            }
        }
        tablePhiDichVu.setItems(filter);
    }

    @FXML
    private void handleRefresh() {
        loadData();
        searchField.clear();
    }

    // --- XỬ LÝ THÊM MỚI (Mở form ThemPhiDichVu.fxml) ---
    @FXML
    private void handleAdd() {
        try {
            // Load giao diện thiết lập phí
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bluemoon/views/ThemPhiDichVu.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Thiết lập Phí Dịch Vụ");
            stage.setScene(new Scene(root));

            // Chặn cửa sổ chính (Modal)
            stage.initModality(Modality.WINDOW_MODAL);
            if (tablePhiDichVu.getScene() != null) {
                stage.initOwner(tablePhiDichVu.getScene().getWindow());
            }

            stage.showAndWait(); // Chờ nhập xong

            // Sau khi đóng form, tải lại bảng
            handleRefresh();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form thêm mới: " + e.getMessage());
        }
    }

    @FXML
    private void handleEdit() {
        showAlert("Thông báo", "Chức năng Cập nhật đang được phát triển.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // --- CLASS NỘI BỘ (MODEL HIỂN THỊ) ---
    public static class PhiDichVuDisplay {
        private final SimpleStringProperty maHo;
        private final SimpleFloatProperty dienTich;
        private final SimpleFloatProperty donGia;
        private final SimpleFloatProperty tongTien;
        private final SimpleIntegerProperty nam;

        public PhiDichVuDisplay(String ma, float dt, float dg, float tong, int nam) {
            this.maHo = new SimpleStringProperty(ma);
            this.dienTich = new SimpleFloatProperty(dt);
            this.donGia = new SimpleFloatProperty(dg);
            this.tongTien = new SimpleFloatProperty(tong);
            this.nam = new SimpleIntegerProperty(nam);
        }

        public String getMaHo() { return maHo.get(); }
        public float getDienTich() { return dienTich.get(); }
        public float getDonGia() { return donGia.get(); }
        public float getTongTien() { return tongTien.get(); }
        public int getNam() { return nam.get(); }
    }
}