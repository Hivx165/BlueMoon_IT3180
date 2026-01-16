package com.bluemoon.dao;

import com.bluemoon.models.PhiSinhHoatModel;
import com.bluemoon.services.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhiSinhHoatDAO {

    // CHỈ LẤY HÓA ĐƠN CHƯA THANH TOÁN (DaDong = 0)
    public static List<PhiSinhHoatModel> getUnpaid() {
        List<PhiSinhHoatModel> list = new ArrayList<>();
        String query = "SELECT * FROM phisinhhoat WHERE DaDong = 0"; // <-- Thay đổi quan trọng

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                PhiSinhHoatModel m = new PhiSinhHoatModel();
                m.setId(rs.getInt("ID"));
                m.setMaHoKhau(rs.getString("MaHoKhau"));
                m.setThang(rs.getInt("Thang"));
                m.setNam(rs.getInt("Nam"));
                m.setTienDien(rs.getDouble("TienDien"));
                m.setTienNuoc(rs.getDouble("TienNuoc"));
                m.setTongTien(rs.getDouble("TongTien"));
                m.setDaDong(rs.getInt("DaDong"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Các hàm insert, delete, pay, checkMaHoKhauExist GIỮ NGUYÊN như cũ
    public static boolean insert(PhiSinhHoatModel model) {
        if (!checkMaHoKhauExist(model.getMaHoKhau())) return false;
        String query = "INSERT INTO phisinhhoat (MaHoKhau, Thang, Nam, TienDien, TienNuoc, TongTien, DaDong) VALUES (?, ?, ?, ?, ?, ?, 0)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, model.getMaHoKhau());
            stmt.setInt(2, model.getThang());
            stmt.setInt(3, model.getNam());
            stmt.setDouble(4, model.getTienDien());
            stmt.setDouble(5, model.getTienNuoc());
            stmt.setDouble(6, model.getTongTien());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM phisinhhoat WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean pay(int id) {
        String query = "UPDATE phisinhhoat SET DaDong = 1 WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean checkMaHoKhauExist(String maHoKhau) {
        String query = "SELECT COUNT(*) FROM hokhau WHERE MaHoKhau = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maHoKhau);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}