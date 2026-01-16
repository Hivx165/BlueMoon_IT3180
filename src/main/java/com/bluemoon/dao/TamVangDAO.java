package com.bluemoon.dao;

import com.bluemoon.models.TamVangModel;
import com.bluemoon.services.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TamVangDAO {

    public static List<TamVangModel> getAllTamVang() {
        List<TamVangModel> tamVangList = new ArrayList<>();
        String query = "SELECT SoCMND_CCCD, HoTen, TuNgay, DenNgay, LyDo FROM nhankhau WHERE TamVang = 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TamVangModel tamVang = new TamVangModel();
                tamVang.setSoCCCD(rs.getString("SoCMND_CCCD"));
                tamVang.setHoTen(rs.getString("HoTen"));

                Date tuNgay = rs.getDate("TuNgay");
                Date denNgay = rs.getDate("DenNgay");
                if (tuNgay != null) tamVang.setTuNgay(tuNgay.toLocalDate());
                if (denNgay != null) tamVang.setDenNgay(denNgay.toLocalDate());

                tamVang.setLyDo(rs.getString("LyDo"));
                tamVangList.add(tamVang);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách tạm vắng: " + e.getMessage());
        }
        return tamVangList;
    }

    // 1. TÌM KIẾM: Lấy tên người dân theo CCCD
    public static String getHoTenByCCCD(String cccd) {
        String query = "SELECT HoTen FROM nhankhau WHERE SoCMND_CCCD = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cccd);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("HoTen");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy
    }

    // 2. CẬP NHẬT: Đăng ký tạm vắng (Update TamVang = 1)
    public static boolean updateTamVang(TamVangModel tamVang) {
        String query = "UPDATE nhankhau SET TamVang = 1, TuNgay = ?, DenNgay = ?, LyDo = ? WHERE SoCMND_CCCD = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(tamVang.getTuNgay()));
            stmt.setDate(2, Date.valueOf(tamVang.getDenNgay()));
            stmt.setString(3, tamVang.getLyDo());
            stmt.setString(4, tamVang.getSoCCCD()); // Điều kiện WHERE

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật tạm vắng: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteTamVangByCCCD(String soCCCD) {
        String query = "UPDATE nhankhau SET TamVang = 0 WHERE SoCMND_CCCD = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, soCCCD);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa tạm vắng: " + e.getMessage());
            return false;
        }
    }
}