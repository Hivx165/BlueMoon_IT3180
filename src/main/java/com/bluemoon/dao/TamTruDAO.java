package com.bluemoon.dao;

import com.bluemoon.models.TamTruModel;
import com.bluemoon.services.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TamTruDAO {

    // Lấy danh sách những người đang tạm trú để hiển thị lên bảng
    public static List<TamTruModel> getAll() {
        List<TamTruModel> tamTruList = new ArrayList<>();
        String query = "SELECT * FROM nhankhau WHERE TamTru = 1";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                TamTruModel tamTru = new TamTruModel();
                tamTru.setSoCCCD(resultSet.getString("SoCMND_CCCD"));
                tamTru.setHoTen(resultSet.getString("HoTen"));
                tamTru.setDiaChiTamTru(resultSet.getString("DiaChiTamTru"));

                // Kiểm tra null khi lấy ngày
                Date tuNgay = resultSet.getDate("TuNgay");
                Date denNgay = resultSet.getDate("DenNgay");
                if (tuNgay != null) tamTru.setTuNgay(tuNgay.toLocalDate());
                if (denNgay != null) tamTru.setDenNgay(denNgay.toLocalDate());

                tamTru.setGhiChu(resultSet.getString("GhiChu"));
                tamTruList.add(tamTru);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách tạm trú: " + e.getMessage());
        }
        return tamTruList;
    }

    // 1. TÌM KIẾM: Kiểm tra xem CCCD này có trong nhân khẩu chưa
    public static TamTruModel findByCCCD(String cccd) {
        String query = "SELECT * FROM nhankhau WHERE SoCMND_CCCD = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cccd);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TamTruModel model = new TamTruModel();
                model.setSoCCCD(rs.getString("SoCMND_CCCD"));
                model.setHoTen(rs.getString("HoTen"));
                model.setDiaChiTamTru(rs.getString("DiaChiTamTru"));
                // Các thông tin khác có thể lấy thêm nếu cần
                return model;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy
    }

    // 2. CẬP NHẬT: Đăng ký tạm trú cho người đã có trong hệ thống
    public static boolean updateTamTru(TamTruModel model) {
        // Cập nhật TamTru = 1 và các thông tin liên quan
        String query = "UPDATE nhankhau SET TamTru = 1, DiaChiTamTru = ?, TuNgay = ?, DenNgay = ?, GhiChu = ? " +
                "WHERE SoCMND_CCCD = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, model.getDiaChiTamTru());
            statement.setDate(2, Date.valueOf(model.getTuNgay()));
            statement.setDate(3, Date.valueOf(model.getDenNgay()));
            statement.setString(4, model.getGhiChu());
            statement.setString(5, model.getSoCCCD()); // Điều kiện WHERE

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật tạm trú: " + e.getMessage());
            return false;
        }
    }

    // Hàm xóa (Hủy tạm trú)
    public static boolean deleteBySoCCCD(String soCCCD) {
        String query = "UPDATE nhankhau SET TamTru = 0 WHERE SoCMND_CCCD = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, soCCCD);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa tạm trú: " + e.getMessage());
            return false;
        }
    }
}