package com.nctu.quanlynhatro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.nctu.quanlynhatro.model.GiaDienNuoc;

public class GiaDienNuocDAO {
	private Connection conn;

	public GiaDienNuocDAO(Connection conn) {
		this.conn = conn;
	}

	// 1. Lấy giá điện nước hiện tại (Mới nhất)
	public GiaDienNuoc getGiaHienTai() {
		// Sử dụng LIMIT 1 cho MariaDB/MySQL
		String sql = "SELECT * FROM GiaDienNuoc WHERE TrangThaiXoa = 0 ORDER BY MaGiaDN DESC LIMIT 1";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return new GiaDienNuoc(rs.getLong("MaGiaDN"), // Khớp với Model (long)
						rs.getDouble("GiaDien"), rs.getDouble("GiaNuoc"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; // Trả về null nếu chưa có dữ liệu giá nào
	}

	// 2. Thêm giá mới (Insert để lưu lịch sử, không update đè)
	public boolean insertGiaMoi(double giaDien, double giaNuoc) {
		String sql = "INSERT INTO GiaDienNuoc(GiaDien, GiaNuoc, TrangThaiXoa) VALUES (?, ?, 0)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDouble(1, giaDien);
			ps.setDouble(2, giaNuoc);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}