package com.bluemoon.dao;

import com.bluemoon.models.ThongKeHoKhauModel;
import com.bluemoon.services.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeDAO {

    // 1. Thống kê Nhân khẩu (Biểu đồ tròn)
    public static Map<String, Integer> getNhanKhauStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("ThuongTru", 0);
        stats.put("TamTru", 0);
        stats.put("TamVang", 0);

        String query = "SELECT " +
                "SUM(CASE WHEN TamTru = 0 AND TamVang = 0 THEN 1 ELSE 0 END) AS ThuongTru, " +
                "SUM(CASE WHEN TamTru = 1 THEN 1 ELSE 0 END) AS TamTru, " +
                "SUM(CASE WHEN TamVang = 1 THEN 1 ELSE 0 END) AS TamVang " +
                "FROM nhankhau";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                stats.put("ThuongTru", rs.getInt("ThuongTru"));
                stats.put("TamTru", rs.getInt("TamTru"));
                stats.put("TamVang", rs.getInt("TamVang"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    // 2. Thống kê Tổng các nguồn thu (Biểu đồ cột)
    public static Map<String, Double> getKhoanThuStats() {
        Map<String, Double> stats = new HashMap<>();
        stats.put("PhiSinhHoat", 0.0);
        stats.put("PhiDichVu", 0.0);
        stats.put("PhiDongGop", 0.0);

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Phí sinh hoạt: Chỉ tính ĐÃ ĐÓNG (DaDong = 1)
            ResultSet rs1 = stmt.executeQuery("SELECT SUM(TongTien) FROM phisinhhoat WHERE DaDong = 1");
            if (rs1.next()) stats.put("PhiSinhHoat", rs1.getDouble(1));

            // Phí dịch vụ: Tổng tiền nộp mỗi tháng
            ResultSet rs2 = stmt.executeQuery("SELECT SUM(TienNopMoiThang) FROM phidichvu");
            if (rs2.next()) stats.put("PhiDichVu", rs2.getDouble(1));

            // Phí đóng góp: Tổng tiền ủng hộ
            ResultSet rs3 = stmt.executeQuery("SELECT SUM(SoTien) FROM donggop");
            if (rs3.next()) stats.put("PhiDongGop", rs3.getDouble(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    // 3. (QUAN TRỌNG) Thống kê chi tiết theo từng Mã Hộ Khẩu cho Bảng
    public static List<ThongKeHoKhauModel> getThongKeTheoHoKhau() {
        List<ThongKeHoKhauModel> list = new ArrayList<>();

        // Sử dụng Sub-query để tính tổng từng loại phí cho từng Mã Hộ Khẩu
        // ISNULL để xử lý trường hợp hộ đó không có khoản phí nào (trả về 0)
        String query = "SELECT h.MaHoKhau, " +
                "ISNULL((SELECT SUM(TongTien) FROM phisinhhoat p WHERE p.MaHoKhau = h.MaHoKhau AND p.DaDong = 1), 0) AS TongSinhHoat, " +
                "ISNULL((SELECT SUM(TienNopMoiThang) FROM phidichvu d WHERE d.MaHoKhau = h.MaHoKhau), 0) AS TongDichVu, " +
                "ISNULL((SELECT SUM(SoTien) FROM donggop g WHERE g.MaHoKhau = h.MaHoKhau), 0) AS TongDongGop " +
                "FROM hokhau h";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String maHK = rs.getString("MaHoKhau");
                double sinhHoat = rs.getDouble("TongSinhHoat");
                double dichVu = rs.getDouble("TongDichVu");
                double dongGop = rs.getDouble("TongDongGop");

                // Chỉ thêm vào danh sách nếu có phát sinh dòng tiền
                if (sinhHoat + dichVu + dongGop >= 0) {
                    list.add(new ThongKeHoKhauModel(maHK, sinhHoat, dichVu, dongGop));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}