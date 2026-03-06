package com.nctu.quanlynhatro.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static String DB_URL = "jdbc:mysql://localhost:3306/QLNhaTro";
	private static String USER_NAME = "root";
	private static String PASSWORD = "";

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
		} catch (ClassNotFoundException e) {
			System.out.println("❌ Không tìm thấy Driver");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("❌ Lỗi kết nối database");
			e.printStackTrace();
		}
		return conn;
	}

}