package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class ThemHoKhauController {

    @FXML private TextField tfMaHoKhau;
    @FXML private TextField tfDiaChi;
    @FXML private DatePicker dpNgayLap;
    @FXML private TextField tfDienTich;
    @FXML private Button btnHuy;

    public void initialize() {
        // Mặc định chọn ngày hiện tại
        dpNgayLap.setValue(LocalDate.now());
    }

    @FXML
    private void handleSave() {
        // 1. Kiểm tra nhập liệu
        if (tfMaHoKhau.getText().isEmpty() || tfDiaChi.getText().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập Mã hộ và Địa chỉ!");
            return;
        }

        try {
            String maHo = tfMaHoKhau.getText();
            String diaChi = tfDiaChi.getText();
            LocalDate ngayLap = dpNgayLap.getValue();

            // Xử lý diện tích (chống lỗi nếu nhập chữ)
            float dienTich = 0;
            if (!tfDienTich.getText().isEmpty()) {
                dienTich = Float.parseFloat(tfDienTich.getText());
            }

            // 2. Lưu vào SQL
            String sql = "INSERT INTO hokhau (MaHoKhau, DiaChi, NgayLap, DienTichHo) VALUES (?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, maHo);
                stmt.setString(2, diaChi);
                stmt.setDate(3, java.sql.Date.valueOf(ngayLap));
                stmt.setFloat(4, dienTich);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    showAlert("Thành công", "Đã thêm hộ khẩu mới: " + maHo);
                    closeWindow();
                }
            }

        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Diện tích phải là số!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi SQL", "Không thể lưu (Có thể trùng Mã Hộ): " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

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