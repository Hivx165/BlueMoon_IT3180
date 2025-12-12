package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField; // Ô nhập tên đăng nhập

    @FXML
    private PasswordField passwordField; // Ô nhập mật khẩu

    // Hàm này sẽ chạy khi bấm nút Login
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (checkLogin(username, password)) {
            infoAlert("Đăng nhập thành công!", "Chào mừng quản trị viên: " + username);
            // Sau này sẽ thêm code chuyển sang màn hình chính (HomeView) ở đây
        } else {
            errorAlert("Đăng nhập thất bại!", "Sai tên đăng nhập hoặc mật khẩu.");
        }
    }

    // Kiểm tra thông tin trong CSDL SQL Server
    private boolean checkLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE UserName = ? AND Password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có kết quả trả về -> đúng tài khoản
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void infoAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void errorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}