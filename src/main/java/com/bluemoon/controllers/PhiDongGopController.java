package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import com.bluemoon.utils.ControllerUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PhiDongGopController {

    @FXML private TextField txtMaHo;
    @FXML private TextField txtSoTien;
    @FXML private ComboBox<String> cbLoaiQuy;
    @FXML private TextArea txtGhiChu;

    @FXML
    public void initialize() {
        cbLoaiQuy.getItems().addAll("Quỹ Khuyến Học", "Quỹ Từ Thiện", "Quỹ Hội Người Cao Tuổi");
    }

    @FXML
    private void handleSave() {
        String sql = "INSERT INTO PhiDongGop (MaHoKhau, LoaiQuy, SoTien, GhiChu, NgayDong) VALUES (?, ?, ?, ?, GETDATE())";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtMaHo.getText());
            pstmt.setString(2, cbLoaiQuy.getValue());
            pstmt.setDouble(3, Double.parseDouble(txtSoTien.getText()));
            pstmt.setString(4, txtGhiChu.getText());

            pstmt.executeUpdate();
            ControllerUtil.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã ghi nhận đóng góp!");

        } catch (Exception e) {
            ControllerUtil.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể lưu: " + e.getMessage());
        }
    }
}