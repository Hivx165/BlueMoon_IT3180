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

    // Giả sử ta có biến static lưu user hiện tại sau khi login (bạn cần set cái này ở LoginController)
    public static String currentUser = "admin"; 

    public void initialize() {
        loadUserInfo();
    }

    private void loadUserInfo() {
        String query = "SELECT * FROM users WHERE UserName = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, currentUser);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                lbUserName.setText(rs.getString("UserName"));
                lbHoTen.setText(rs.getString("HoTen"));
                lbEmail.setText(rs.getString("Email"));
                lbSDT.setText(rs.getString("SoDT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}