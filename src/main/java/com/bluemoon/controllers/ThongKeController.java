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

    public void initialize() {
        countData();
    }

    private void countData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Đếm nhân khẩu
            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM nhankhau");
            if (rs1.next()) lbTongNhanKhau.setText(String.valueOf(rs1.getInt(1)));

            // Đếm hộ khẩu
            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM hokhau");
            if (rs2.next()) lbTongHoKhau.setText(String.valueOf(rs2.getInt(1)));
            
            // Đếm tạm trú
            ResultSet rs3 = stmt.executeQuery("SELECT COUNT(*) FROM tamtru");
            if (rs3.next()) lbTongTamTru.setText(String.valueOf(rs3.getInt(1)));

            // Đếm tạm vắng
            ResultSet rs4 = stmt.executeQuery("SELECT COUNT(*) FROM tamvang");
            if (rs4.next()) lbTongTamVang.setText(String.valueOf(rs4.getInt(1)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}