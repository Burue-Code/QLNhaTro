package com.nctu.quanlynhatro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nctu.quanlynhatro.model.PhuongThucThanhToan;

public class PhuongThucThanhToanDAO {
	private Connection conn;

	public PhuongThucThanhToanDAO(Connection conn) {
		this.conn = conn;
	}

	public List<PhuongThucThanhToan> getAll() {
		List<PhuongThucThanhToan> list = new ArrayList<>();
		String sql = "SELECT MaPT, TenPT FROM PTThanhToan WHERE TrangThaiXoa = 0";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				PhuongThucThanhToan pt = new PhuongThucThanhToan(rs.getLong("MaPT"), rs.getString("TenPT"));
				list.add(pt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Map<Integer, String> getPhuongThucThanhToan() {
		Map<Integer, String> list = new HashMap<>();
		String sql = "SELECT MaPT, TenPT FROM PTThanhToan WHERE TrangThaiXoa = 0";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.put(rs.getInt("MaPT"), rs.getString("TenPT"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean isTenPTExistForUpdate(int maPT, String tenPT) {
		String sql = "SELECT 1 FROM PTThanhToan WHERE TenPT = ? AND MaPT <> ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, tenPT);
			ps.setInt(2, maPT);
			return ps.executeQuery().next();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isTenPTExist(String tenPT) {
		String sql = "SELECT 1 FROM PTThanhToan WHERE TenPT = ? AND TrangThaiXoa = 0";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, tenPT);
			return ps.executeQuery().next();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean insert(String tenPT) {
		String sql = "INSERT INTO PTThanhToan(TenPT, TrangThaiXoa) VALUES (?, 0)";
		if (isTenPTExist(tenPT)) {
			return false;
		}

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, tenPT);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(PhuongThucThanhToan pt) {
		String sql = "UPDATE PTThanhToan SET TenPT = ? WHERE MaPT = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, pt.getTenPT());
			ps.setLong(2, pt.getMaPT());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteSoft(long maPT) {
		String sql = "UPDATE PTThanhToan SET TrangThaiXoa = 1 WHERE MaPT = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maPT);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}