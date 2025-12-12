package com.bluemoon.controllers; // 1. Khai báo Package

// 2. Các thư viện cần thiết (Import)
import com.bluemoon.services.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// 3. Khai báo Class (BẮT BUỘC)
public class LoginController {

    // 4. Khai báo các biến giao diện (FXML)
    @FXML
    private TextField usernameField; // Ô nhập tài khoản

    @FXML
    private PasswordField passwordField; // Ô nhập mật khẩu

    @FXML
    private Button loginButton; // Nút đăng nhập

    // 5. Hàm xử lý khi bấm nút Đăng nhập
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Gọi hàm checkLogin để kiểm tra
        if (checkLogin(username, password)) {
            try {
                // Đóng cửa sổ Login hiện tại
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                currentStage.close();

                // Mở màn hình chính (HomeView)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bluemoon/views/HomeView.fxml"));
                Parent root = loader.load();

                Stage homeStage = new Stage();
                homeStage.setTitle("BlueMoon Apartment - Trang chủ");
                homeStage.setScene(new Scene(root));
                homeStage.show();

            } catch (Exception e) {
                e.printStackTrace();
                errorAlert("Lỗi hệ thống", "Không thể mở màn hình chính: " + e.getMessage());
            }
        } else {
            errorAlert("Đăng nhập thất bại", "Sai tên đăng nhập hoặc mật khẩu!");
        }
    }

    // 6. Hàm kiểm tra tài khoản với SQL Server
    private boolean checkLogin(String username, String password) {
        // Câu lệnh SQL kiểm tra user
        String query = "SELECT * FROM users WHERE UserName = ? AND Password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Gán giá trị vào dấu ?
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có kết quả trả về -> Đăng nhập đúng

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 7. Hàm hiển thị thông báo lỗi
    private void errorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}