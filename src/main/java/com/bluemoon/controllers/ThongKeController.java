package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ThongKeController {

    @FXML private Label lbTongNhanKhau;
    @FXML private Label lbTongHoKhau;
    @FXML private Label lbTongTamTru;
    @FXML private Label lbTongTamVang;
    @FXML private Label lbTongThuPhi; // Tổng tiền đã thu được

    public void initialize() {
        loadStats();
    }

    private void loadStats() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Đếm Nhân Khẩu
            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM nhankhau");
            if (rs1.next()) lbTongNhanKhau.setText(String.valueOf(rs1.getInt(1)));

            // 2. Đếm Hộ Khẩu
            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM hokhau");
            if (rs2.next()) lbTongHoKhau.setText(String.valueOf(rs2.getInt(1)));

            // 3. Đếm Tạm Trú
            ResultSet rs3 = stmt.executeQuery("SELECT COUNT(*) FROM tamtru");
            if (rs3.next()) lbTongTamTru.setText(String.valueOf(rs3.getInt(1)));

            // 4. Đếm Tạm Vắng
            ResultSet rs4 = stmt.executeQuery("SELECT COUNT(*) FROM tamvang");
            if (rs4.next()) lbTongTamVang.setText(String.valueOf(rs4.getInt(1)));

            // 5. Tổng tiền (ví dụ từ bảng thanhtoan)
            ResultSet rs5 = stmt.executeQuery("SELECT SUM(SoTien) FROM thanhtoan");
            if (rs5.next()) {
                double total = rs5.getDouble(1);
                lbTongThuPhi.setText(String.format("%,.0f VNĐ", total));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}