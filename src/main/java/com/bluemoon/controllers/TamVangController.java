package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import com.bluemoon.utils.ControllerUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class TamVangController {
    @FXML private TextField txtCCCD;
    @FXML private TextField txtNoiDen;
    @FXML private DatePicker dpTuNgay, dpDenNgay;
    @FXML private TextArea txtLyDo;

    @FXML
    private void handleConfirm() {
        String sql = "INSERT INTO TamVang (CCCD, NoiDen, TuNgay, DenNgay, LyDo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtCCCD.getText());
            pstmt.setString(2, txtNoiDen.getText());
            pstmt.setDate(3, java.sql.Date.valueOf(dpTuNgay.getValue()));
            pstmt.setDate(4, java.sql.Date.valueOf(dpDenNgay.getValue()));
            pstmt.setString(5, txtLyDo.getText());

            pstmt.executeUpdate();
            ControllerUtil.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã lưu thông tin tạm vắng.");
        } catch (Exception e) {
            ControllerUtil.showAlert(Alert.AlertType.ERROR, "Lỗi", e.getMessage());
        }
    }
}