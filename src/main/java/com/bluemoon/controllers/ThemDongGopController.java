package com.bluemoon.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.bluemoon.services.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class ThemDongGopController {

    @FXML private TextField tfMaHoKhau;
    @FXML private TextField tfTenKhoanThu;
    @FXML private TextField tfSoTien;
    @FXML private DatePicker dpNgayDong;
    @FXML private Button btnHuy;

    public void initialize() {
        dpNgayDong.setValue(LocalDate.now()); // Mặc định là hôm nay
    }

    @FXML
    private void handleSave() {
        // 1. Kiểm tra nhập liệu
        if (tfMaHoKhau.getText().isEmpty() || tfTenKhoanThu.getText().isEmpty() || tfSoTien.getText().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        String maHo = tfMaHoKhau.getText();
        String tenKhoan = tfTenKhoanThu.getText();

        try {
            float soTien = Float.parseFloat(tfSoTien.getText());
            LocalDate ngayDong = dpNgayDong.getValue();

            // 2. Kiểm tra mã hộ có tồn tại không?
            if (!checkMaHoTonTai(maHo)) {
                showAlert("Lỗi", "Mã hộ khẩu " + maHo + " không tồn tại trong hệ thống!");
                return;
            }

            // 3. Lưu vào SQL
            String sql = "INSERT INTO donggop (MaHoKhau, TenKhoanDongGop, SoTien, NgayDong) VALUES (?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, maHo);
                stmt.setString(2, tenKhoan);
                stmt.setFloat(3, soTien);
                stmt.setDate(4, java.sql.Date.valueOf(ngayDong));

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    showAlert("Thành công", "Đã ghi nhận đóng góp!");
                    closeWindow();
                }
            }

        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Số tiền phải là số hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi hệ thống", e.getMessage());
        }
    }

    // Hàm kiểm tra mã hộ trong CSDL
    private boolean checkMaHoTonTai(String maHo) {
        String sql = "SELECT COUNT(*) FROM hokhau WHERE MaHoKhau = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maHo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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