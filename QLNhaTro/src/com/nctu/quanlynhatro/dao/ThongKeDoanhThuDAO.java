package com.nctu.quanlynhatro.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThongKeDoanhThuDAO {
	private Connection conn;

	public ThongKeDoanhThuDAO(Connection conn) {
		this.conn = conn;
	}

	public double getTongDoanhThu(LocalDate from, LocalDate to) {
		String sql = "SELECT SUM(SoTienTT) FROM HoaDon WHERE TrangThaiXoa = 0 AND DATE(NgayTT) BETWEEN ? AND ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, Date.valueOf(from));
			ps.setDate(2, Date.valueOf(to));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getDouble(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getSoLuongHoaDon(LocalDate from, LocalDate to) {
		String sql = "SELECT COUNT(*) FROM HoaDon WHERE TrangThaiXoa = 0 AND DATE(NgayTT) BETWEEN ? AND ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, Date.valueOf(from));
			ps.setDate(2, Date.valueOf(to));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getSoKhachMoi(LocalDate from, LocalDate to) {
		String sql = "SELECT COUNT(*) FROM HopDong WHERE TrangThaiXoa = 0 AND NgayLapHD BETWEEN ? AND ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, Date.valueOf(from));
			ps.setDate(2, Date.valueOf(to));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Map<String, Double> getDoanhThuTheoNgay(LocalDate from, LocalDate to) {
		Map<String, Double> data = new LinkedHashMap<>();
		String sql = "SELECT DATE_FORMAT(NgayTT, '%d/%m') as Ngay, SUM(SoTienTT) as TongTien FROM HoaDon "
				+ "WHERE TrangThaiXoa = 0 AND DATE(NgayTT) BETWEEN ? AND ? GROUP BY DATE(NgayTT) "
				+ "ORDER BY DATE(NgayTT) ASC";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, Date.valueOf(from));
			ps.setDate(2, Date.valueOf(to));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					data.put(rs.getString("Ngay"), rs.getDouble("TongTien"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
}