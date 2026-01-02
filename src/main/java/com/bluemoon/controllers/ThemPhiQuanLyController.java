package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class ThemPhiQuanLyController {

    @FXML private TextField tfMaHoKhau;
    @FXML private TextField tfDienTich;
    @FXML private TextField tfDonGia;
    @FXML private TextField tfNam;
    @FXML private Label lblThanhTien;
    @FXML private Button btnHuy;

    private float dienTichHienTai = 0;

    public void initialize() {
        tfNam.setText(String.valueOf(LocalDate.now().getYear()));

        // Tự động tính tiền khi nhập đơn giá
        tfDonGia.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateTotal();
        });
    }

    // 1. Kiểm tra Mã hộ để lấy Diện tích
    @FXML
    private void handleCheck() {
        String maHo = tfMaHoKhau.getText();
        if (maHo.isEmpty()) return;

        String sql = "SELECT DienTichHo FROM hokhau WHERE MaHoKhau = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dienTichHienTai = rs.getFloat("DienTichHo");
                tfDienTich.setText(String.valueOf(dienTichHienTai));
                calculateTotal();
            } else {
                showAlert("Lỗi", "Không tìm thấy Mã hộ khẩu: " + maHo);
                tfDienTich.setText("");
                dienTichHienTai = 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateTotal() {
        try {
            if (dienTichHienTai > 0 && !tfDonGia.getText().isEmpty()) {
                float donGia = Float.parseFloat(tfDonGia.getText());
                float thanhTien = dienTichHienTai * donGia;
                lblThanhTien.setText(String.format("%,.0f VND", thanhTien));
            }
        } catch (NumberFormatException e) {
            // Ignore
        }
    }

    // 2. Lưu vào bảng phiquanly
    @FXML
    private void handleSave() {
        if (tfMaHoKhau.getText().isEmpty() || tfDonGia.getText().isEmpty() || tfDienTich.getText().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đủ thông tin và bấm Kiểm tra!");
            return;
        }

        try {
            String maHo = tfMaHoKhau.getText();
            float donGia = Float.parseFloat(tfDonGia.getText());
            int nam = Integer.parseInt(tfNam.getText());
            float thanhTien = dienTichHienTai * donGia;

            // Kiểm tra tồn tại
            if (checkExist(maHo, nam)) {
                updatePhi(maHo, donGia, thanhTien, nam);
            } else {
                insertPhi(maHo, donGia, thanhTien, nam);
            }

            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Đơn giá hoặc Năm phải là số!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi hệ thống", e.getMessage());
        }
    }

    private boolean checkExist(String maHo, int nam) throws Exception {
        String sql = "SELECT COUNT(*) FROM phiquanly WHERE MaHoKhau = ? AND Nam = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maHo);
            stmt.setInt(2, nam);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    private void insertPhi(String maHo, float gia, float tien, int nam) throws Exception {
        String sql = "INSERT INTO phiquanly (MaHoKhau, GiaPhi, TienNopMoiThang, Nam) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maHo);
            stmt.setFloat(2, gia);
            stmt.setFloat(3, tien);
            stmt.setInt(4, nam);
            stmt.executeUpdate();
            showAlert("Thành công", "Đã thêm mới phí quản lý!");
        }
    }

    private void updatePhi(String maHo, float gia, float tien, int nam) throws Exception {
        String sql = "UPDATE phiquanly SET GiaPhi = ?, TienNopMoiThang = ? WHERE MaHoKhau = ? AND Nam = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setFloat(1, gia);
            stmt.setFloat(2, tien);
            stmt.setString(3, maHo);
            stmt.setInt(4, nam);
            stmt.executeUpdate();
            showAlert("Thành công", "Đã cập nhật phí quản lý!");
        }
    }

    @FXML private void handleCancel() { closeWindow(); }

    private void closeWindow() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}