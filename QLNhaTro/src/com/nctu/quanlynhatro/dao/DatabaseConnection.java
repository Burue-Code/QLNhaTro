package com.nctu.quanlynhatro.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static String url = "jdbc:sqlserver://localhost:1433;databaseName=QLTroNew;encrypt=true;trustServerCertificate=true";
    private static String url1 = "jdbc:sqlserver://localhost:1433;databaseName=QLNhaTro;encrypt=true;trustServerCertificate=true";
    private static String user;
    private static String password;

    // Khối static này sẽ chạy ngay khi class được gọi đến lần đầu tiên
    static {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            prop.load(fis);
            
            // Lấy dữ liệu từ file properties
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");
            
        } catch (IOException e) {
            System.err.println("LỖI: Không tìm thấy hoặc không đọc được file config.properties!");
            e.printStackTrace();
        }
    }

    // Kết nối tới DB Chính
    public static Connection getQLTroNew() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Kết nối tới DB Accounts
    public static Connection getQLNhaTroAccounts() {
        try {
            // Đã sửa lỗi: Sử dụng url1 thay vì url
            return DriverManager.getConnection(url1, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Hàm main để test
    public static void main(String[] args) {
        System.out.println("Đang kiểm tra kết nối...");
        
        try (Connection cn = getQLTroNew()) {
            if (cn != null) System.out.println("=> Kết nối QLTroNew: THÀNH CÔNG!");
        } catch (SQLException e) { e.printStackTrace(); }

        try (Connection cn1 = getQLNhaTroAccounts()) {
            if (cn1 != null) System.out.println("=> Kết nối QLNhaTro: THÀNH CÔNG!");
        } catch (SQLException e) { e.printStackTrace(); }
    }
}