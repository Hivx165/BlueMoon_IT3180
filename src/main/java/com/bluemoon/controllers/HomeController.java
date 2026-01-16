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

    @FXML
    private BorderPane mainBorderPane; // Vùng chứa nội dung chính

    @FXML private Label nhanKhauCount;
    @FXML private Label hoKhauCount;
    @FXML private Label welcomeLabel;

    private Parent dashboardView; // Lưu lại trang Dashboard để tái sử dụng

    public void initialize() {
        System.out.println(">> HomeController đã khởi động.");

        // 1. Lưu giao diện Dashboard ban đầu (để nút Trang chủ quay về đây)
        if (mainBorderPane != null) {
            dashboardView = (Parent) mainBorderPane.getCenter();
        }

        // 2. Tải số liệu thống kê
        loadStatistics();
    }

    // --- HÀM TẢI SỐ LIỆU CHO DASHBOARD ---
    private void loadStatistics() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Đếm Nhân khẩu
            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM nhankhau");
            if (rs1.next()) {
                nhanKhauCount.setText(String.valueOf(rs1.getInt(1)));
            }

            // Đếm Hộ khẩu
            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM hokhau");
            if (rs2.next()) {
                hoKhauCount.setText(String.valueOf(rs2.getInt(1)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi tải thống kê Dashboard: " + e.getMessage());
        }
    }

    // --- CÁC HÀM XỬ LÝ SỰ KIỆN MENU (OnAction) ---

    @FXML
    private void handleTrangChu(ActionEvent event) {
        System.out.println(">> Click: Trang Chủ");
        if (dashboardView != null) {
            mainBorderPane.setCenter(dashboardView);
            loadStatistics(); // Cập nhật lại số liệu mới nhất
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

    // --- MỚI THÊM: Xử lý nút Tạm Trú ---
    @FXML
    private void handleTamTru(ActionEvent event) {
        System.out.println(">> Click: Tạm Trú");
        switchView("TamTru.fxml");
    }

    // --- MỚI THÊM: Xử lý nút Tạm Vắng ---
    @FXML
    private void handleTamVang(ActionEvent event) {
        System.out.println(">> Click: Tạm Vắng");
        switchView("TamVang.fxml");
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
        switchView("PhiDongGopView.fxml");
    }

    @FXML
    private void handlePhiSinhHoat(ActionEvent event) {
        System.out.println(">> Click: Phí Sinh Hoạt");
        switchView("PhiSinhHoat.fxml");
    }

    @FXML
    private void handleThongKe(ActionEvent event) {
        System.out.println(">> Click: Thống Kê");
        switchView("ThongKeView.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        System.out.println(">> Click: Đăng Xuất");
        try {
            // 1. Đóng cửa sổ hiện tại
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // 2. Mở màn hình Login
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

    // --- HÀM CHUYỂN CẢNH DÙNG CHUNG ---
    private void switchView(String fxmlFileName) {
        try {
            // LƯU Ý: Đảm bảo các file .fxml (TamTru.fxml, TamVang.fxml...)
            // đều nằm trong thư mục /com/bluemoon/views/
            String path = "/com/bluemoon/views/" + fxmlFileName;
            URL fileUrl = getClass().getResource(path);

            if (fileUrl == null) {
                System.err.println("❌ LỖI: Không tìm thấy file giao diện: " + path);
                System.err.println("👉 Vui lòng kiểm tra xem file " + fxmlFileName + " đã ở trong package /views chưa.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent view = loader.load();

            // Thay thế nội dung vùng giữa (Center)
            mainBorderPane.setCenter(view);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi tải file FXML: " + e.getMessage());
        }
    }
}