package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import com.bluemoon.utils.ControllerUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class TamTruController {
    @FXML private TextField txtCCCD, txtHoTen, txtMaHo;
    @FXML private DatePicker dpTuNgay, dpDenNgay;
    @FXML private TextField txtLyDo;

    @FXML
    private void handleRegister() {
        String sql = "INSERT INTO TamTru (CCCD, HoTen, MaHoKhau, TuNgay, DenNgay, LyDo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtCCCD.getText());
            pstmt.setString(2, txtHoTen.getText());
            pstmt.setString(3, txtMaHo.getText());
            pstmt.setDate(4, java.sql.Date.valueOf(dpTuNgay.getValue()));
            pstmt.setDate(5, java.sql.Date.valueOf(dpDenNgay.getValue()));
            pstmt.setString(6, txtLyDo.getText());

            pstmt.executeUpdate();
            ControllerUtil.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng ký tạm trú thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}