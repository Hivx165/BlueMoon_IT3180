package com.bluemoon.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class HomeController {

    @FXML
    private BorderPane mainBorderPane; // Liên kết với fx:id bên FXML

    // 1. Xử lý nút Hộ Khẩu
    @FXML
    private void handleHoKhau(ActionEvent event) {
        System.out.println(">> Đã bấm nút Hộ Khẩu"); // Kiểm tra xem nút có ăn không
        switchView("HoKhauView.fxml");
    }

    // 2. Xử lý nút Nhân Khẩu
    @FXML
    private void handleNhanKhau(ActionEvent event) {
        System.out.println(">> Đã bấm nút Nhân Khẩu");
        switchView("NhanKhauView.fxml");
    }

    // 3. Xử lý nút Trang Chủ
    @FXML
    private void handleTrangChu(ActionEvent event) {
        System.out.println(">> Đã bấm nút Trang Chủ");
        // Có thể load lại trang thống kê hoặc clear center
    }

    // 4. Xử lý Đăng xuất
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bluemoon/views/LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm chuyển cảnh dùng chung
    private void switchView(String fxmlFileName) {
        try {
            String path = "/com/bluemoon/views/" + fxmlFileName;
            URL fileUrl = getClass().getResource(path);

            if (fileUrl == null) {
                System.err.println("❌ Lỗi: Không tìm thấy file " + path);
                return; // Dừng lại nếu không thấy file
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent view = loader.load();

            // Thay đổi nội dung vùng giữa
            mainBorderPane.setCenter(view);
            System.out.println("✅ Đã chuyển sang màn hình: " + fxmlFileName);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi tải giao diện: " + e.getMessage());
        }
    }
}