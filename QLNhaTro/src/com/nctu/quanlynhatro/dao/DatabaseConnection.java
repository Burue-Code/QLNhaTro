package com.nctu.quanlynhatro.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
	
	
	private static String url = "jdbc:sqlserver://localhost:1433;databaseName=QLTroNew;encrypt=true;trustServerCertificate=true";
	private static String url1 = "jdbc:sqlserver://localhost:1433;databaseName=QLNhaTro;encrypt=true;trustServerCertificate=true";
	private static String user = "sa"; // Tài khoản SQL Server của bạn
	private static String password = "1234"; // Mật khẩu của bạn
	
	
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
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
//	public static void main(String[] args) {
//	    Connection cn = getQLTroNew();
//	    if (cn != null) {
//	        System.out.println("Kết nối thành công tới SQL Server!");
//	        try {
//	            cn.close(); // Đóng kết nối sau khi test xong
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        }
//	    } else {
//	        System.out.println("Kết nối thất bại. Hãy kiểm tra lại mật khẩu hoặc tên Database.");
//	    }
//	    Connection cn1 = getQLNhaTroAccounts();
//	    if (cn != null) {
//	        System.out.println("Kết nối thành công tới SQL Server 01!");
//	        try {
//	            cn.close(); // Đóng kết nối sau khi test xong
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        }
//	    } else {
//	        System.out.println("Kết nối thất bại 01. Hãy kiểm tra lại mật khẩu hoặc tên Database.");
//	    }
//	}
}
