package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class ThemNhanKhauController {

    @FXML private TextField tfHoTen;
    @FXML private TextField tfCCCD;
    @FXML private DatePicker dpNgaySinh;
    @FXML private ComboBox<String> cbGioiTinh;
    @FXML private TextField tfQueQuan;
    @FXML private TextField tfNgheNghiep;
    @FXML private TextField tfMaHoKhau;
    @FXML private Button btnHuy;

    public void initialize() {
        // Khởi tạo ComboBox Giới tính
        cbGioiTinh.getItems().addAll("Nam", "Nữ", "Khác");
        cbGioiTinh.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleSave() {
        // 1. Kiểm tra dữ liệu đầu vào
        if (tfHoTen.getText().isEmpty() || dpNgaySinh.getValue() == null) {
            showAlert("Lỗi", "Vui lòng nhập tên và ngày sinh!");
            return;
        }

        // 2. Lấy dữ liệu
        String hoTen = tfHoTen.getText();
        String cccd = tfCCCD.getText();
        LocalDate ngaySinh = dpNgaySinh.getValue();
        String gioiTinh = cbGioiTinh.getValue();
        String queQuan = tfQueQuan.getText();
        String ngheNghiep = tfNgheNghiep.getText();
        String maHo = tfMaHoKhau.getText(); // Có thể rỗng

        // 3. Kết nối SQL và Lưu
        String sql = "INSERT INTO nhankhau (HoTen, SoCMND_CCCD, NgaySinh, GioiTinh, QueQuan, NgheNghiep, MaHoKhau) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hoTen);
            stmt.setString(2, cccd);
            stmt.setDate(3, java.sql.Date.valueOf(ngaySinh));
            stmt.setString(4, gioiTinh);
            stmt.setString(5, queQuan);
            stmt.setString(6, ngheNghiep);

            if (maHo.isEmpty()) {
                stmt.setNull(7, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(7, maHo);
            }

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                showAlert("Thành công", "Đã thêm nhân khẩu mới!");
                closeWindow(); // Đóng cửa sổ sau khi lưu xong
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi SQL", "Không thể lưu: " + e.getMessage());
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