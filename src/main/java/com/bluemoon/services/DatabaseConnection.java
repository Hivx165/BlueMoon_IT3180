package com.bluemoon.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // 1. Thông tin kết nối
    // Nếu bạn dùng SQL Express, sửa localhost:1433 thành localhost\\SQLEXPRESS
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=bluemoon_db;"
            + "encrypt=true;trustServerCertificate=true;";

    // 2. Tài khoản SQL Server (đã kích hoạt ở Bước 1)
    private static final String USER = "sa";
    private static final String PASS = "123456"; // <-- Thay mật khẩu bạn vừa đặt vào đây

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Nạp Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Mở kết nối
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(">> Kết nối SQL Server THÀNH CÔNG!");

        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy thư viện Driver JDBC!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi: Không thể kết nối đến SQL Server!");
            System.err.println("Chi tiết: " + e.getMessage());
        }
        return conn;
    }

    // Hàm main để chạy thử ngay lập tức
    public static void main(String[] args) {
        getConnection();
    }
}