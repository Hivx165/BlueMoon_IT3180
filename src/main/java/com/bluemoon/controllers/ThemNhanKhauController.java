package com.bluemoon.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.bluemoon.services.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class ThemNhanKhauController {

    @FXML private TextField tfHoTen;
    @FXML private TextField tfCCCD;
    @FXML private DatePicker dpNgaySinh;
    @FXML private ComboBox<String> cbGioiTinh;

    @FXML private TextField tfSDT;
    @FXML private TextField tfQuanHe;
    @FXML private TextArea taGhiChu;

    @FXML private TextField tfQueQuan;
    @FXML private TextField tfNgheNghiep;
    @FXML private TextField tfMaHoKhau;
    @FXML private Button btnHuy;

    public void initialize() {
        cbGioiTinh.getItems().addAll("Nam", "Nữ", "Khác");
        cbGioiTinh.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleSave() {
        if (tfHoTen.getText().isEmpty() || tfCCCD.getText().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập Họ tên và số CCCD/CMND!");
            return;
        }

        if (dpNgaySinh.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn Ngày sinh!");
            return;
        }

        try {
            String hoTen = tfHoTen.getText();
            String cccd = tfCCCD.getText();
            LocalDate ngaySinhLocal = dpNgaySinh.getValue();
            String gioiTinh = cbGioiTinh.getValue();
            String sdt = tfSDT.getText();
            String quanHe = tfQuanHe.getText();
            String queQuan = tfQueQuan.getText();
            String ngheNghiep = tfNgheNghiep.getText();
            String maHo = tfMaHoKhau.getText();
            String ghiChu = taGhiChu.getText();

            // Ensure the SQL INSERT matches the schema
            String sql = "INSERT INTO nhankhau (SoCMND_CCCD, MaHoKhau, HoTen, NgaySinh, GioiTinh, " +
                     "QueQuan, NgheNghiep, SoDT, QuanHe, GhiChu) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, cccd);

                if (maHo == null || maHo.trim().isEmpty()) {
                    stmt.setNull(2, java.sql.Types.VARCHAR);
                } else {
                    stmt.setString(2, maHo);
                }

                stmt.setString(3, hoTen);

                if (ngaySinhLocal == null) {
                    stmt.setNull(4, java.sql.Types.DATE);
                } else {
                    stmt.setDate(4, java.sql.Date.valueOf(ngaySinhLocal));
                }

                stmt.setString(5, gioiTinh);
                stmt.setString(6, queQuan);
                stmt.setString(7, ngheNghiep);
                stmt.setString(8, sdt);
                stmt.setString(9, quanHe);
                stmt.setString(10, ghiChu);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    showAlert("Thành công", "Đã thêm nhân khẩu mới!");
                    closeWindow();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi SQL", "Không thể lưu dữ liệu (Có thể trùng số CCCD): \n" + e.getMessage());
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
