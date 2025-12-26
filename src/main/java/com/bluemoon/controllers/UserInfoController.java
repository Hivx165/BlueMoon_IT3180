package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserInfoController {

    @FXML private Label lbUserName;
    @FXML private Label lbHoTen;
    @FXML private Label lbEmail;
    @FXML private Label lbSDT;

    // Biến này nên được set từ LoginController khi đăng nhập thành công
    // Ví dụ: UserInfoController.currentUsername = "admin";
    public static String currentUsername = "admin"; 

    public void initialize() {
        loadInfo();
    }

    private void loadInfo() {
        String sql = "SELECT * FROM users WHERE UserName = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, currentUsername);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lbUserName.setText(rs.getString("UserName"));
                lbHoTen.setText(rs.getString("HoTen"));
                lbEmail.setText(rs.getString("Email"));
                lbSDT.setText(rs.getString("SoDT"));
            } else {
                lbUserName.setText("Không tìm thấy user");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}