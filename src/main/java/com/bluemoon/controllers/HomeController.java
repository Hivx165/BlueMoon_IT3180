package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HomeController {

    // Liên kết với giao diện
    @FXML
    private BorderPane mainBorderPane; // Khung chứa nội dung chính

    @FXML
    private Label nhanKhauCount;
    @FXML
    private Label hoKhauCount;
    @FXML
    private Label welcomeLabel;

    // Biến lưu giữ giao diện Dashboard (để khi bấm trang chủ không phải load lại)
    private Parent dashboardView;

    // --- KHỞI TẠO ---
    public void initialize() {
        System.out.println(">> HomeController khởi động...");

        // 1. Lưu lại giao diện Dashboard ban đầu
        if (mainBorderPane != null) {
            dashboardView = (Parent) mainBorderPane.getCenter();
        }

        // 2. Tải số liệu thống kê
        loadStatistics();
    }

    // --- LOGIC TẢI DỮ LIỆU ---
    private void loadStatistics() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Đếm nhân khẩu
            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM nhankhau");
            if (rs1.next()) {
                nhanKhauCount.setText(String.valueOf(rs1.getInt(1)));
            }

            // Đếm hộ khẩu
            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM hokhau");
            if (rs2.next()) {
                hoKhauCount.setText(String.valueOf(rs2.getInt(1)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi tải thống kê: " + e.getMessage());
        }
    }

    // --- XỬ LÝ SỰ KIỆN MENU ---

    @FXML
    private void handleTrangChu(ActionEvent event) {
        System.out.println(">> Click: Trang Chủ");
        if (dashboardView != null) {
            mainBorderPane.setCenter(dashboardView);
            loadStatistics(); // Cập nhật lại số liệu
        }
    }

    @FXML
    private void handleNhanKhau(ActionEvent event) {
        System.out.println(">> Click: Nhân Khẩu");
        switchView("NhanKhauView.fxml");
    }

    @FXML
    private void handleHoKhau(ActionEvent event) {
        System.out.println(">> Click: Hộ Khẩu");
        switchView("HoKhauView.fxml");
    }

    @FXML
    private void handleThuPhi(ActionEvent event) {
        System.out.println(">> Click: Phí Dịch Vụ");
        switchView("PhiDichVuView.fxml");
    }

    @FXML
    private void handlePhiQuanLy(ActionEvent event) {
        System.out.println(">> Click: Phí Quản Lý");
        switchView("PhiQuanLyView.fxml");
    }

    @FXML
    private void handlePhiDongGop(ActionEvent event) {
        System.out.println(">> Click: Phí Đóng Góp");
        // Gọi đến file view mới đổi tên
        switchView("PhiDongGopView.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        System.out.println(">> Click: Đăng xuất");
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bluemoon/views/LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("BlueMoon Apartment - Login");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- HÀM HỖ TRỢ CHUYỂN CẢNH ---
    private void switchView(String fxmlFileName) {
        try {
            String path = "/com/bluemoon/views/" + fxmlFileName;
            URL fileUrl = getClass().getResource(path);

            if (fileUrl == null) {
                System.err.println("❌ LỖI: Không tìm thấy file " + path);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent view = loader.load();
            mainBorderPane.setCenter(view); // Thay thế nội dung ở giữa

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi tải giao diện: " + e.getMessage());
        }
    }
}