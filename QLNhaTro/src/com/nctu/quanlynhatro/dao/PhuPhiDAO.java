package com.nctu.quanlynhatro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nctu.quanlynhatro.model.PhuPhi;

public class PhuPhiDAO {

	private Connection conn;

	public PhuPhiDAO(Connection conn) {
		this.conn = conn;
	}

	public List<PhuPhi> getAll() {
		List<PhuPhi> list = new ArrayList<>();
		String sql = "SELECT MaPP, TenPP, Gia FROM phuphi WHERE TrangThaiXoa = 0";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				list.add(new PhuPhi(rs.getLong("MaPP"), rs.getString("TenPP"), rs.getDouble("Gia")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Map<Integer, String> getPhuPhiCB() {
		Map<Integer, String> map = new HashMap<>();
		String sql = "SELECT MaPP, TenPP FROM phuphi WHERE TrangThaiXoa = 0";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				map.put(rs.getInt("MaPP"), rs.getString("TenPP"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public String getSoTienByMaPP(long maPP) {
		String sql = "SELECT Gia FROM PhuPhi WHERE MaPP = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maPP);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("Gia");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

	public List<PhuPhi> getPhuPhiByMaPhong(long maPhong) {
		List<PhuPhi> list = new ArrayList<>();
		String sql = """
				    SELECT pp.MaPP, pp.TenPP, pp.Gia
				    FROM ChiTietPhuPhi ct
				    JOIN PhuPhi pp ON ct.MaPP = pp.MaPP
				    WHERE ct.MaPhong = ?
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maPhong);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PhuPhi pp = new PhuPhi();
				pp.setMaPP(rs.getInt("MaPP"));
				pp.setTenPP(rs.getString("TenPP"));
				pp.setGia(rs.getDouble("Gia"));
				list.add(pp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean isTenPhuPhiExist(String tenPP) {
		String sql = "SELECT 1 FROM phuphi WHERE TenPP = ? AND TrangThaiXoa = 0";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, tenPP);
			return ps.executeQuery().next();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isTenPhuPhiExistForUpdate(int maPP, String tenPP) {
		String sql = "SELECT 1 FROM phuphi WHERE TenPP = ? AND MaPP <> ? AND TrangThaiXoa = 0";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, tenPP);
			ps.setInt(2, maPP);
			return ps.executeQuery().next();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean insert(String tenPP, double gia) {
		String sql = "INSERT INTO phuphi (TenPP, Gia, TrangThaiXoa) VALUES (?, ?, 0)";
		if (isTenPhuPhiExist(tenPP)) {
			return false;
		}

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, tenPP);
			ps.setDouble(2, gia);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(PhuPhi pp) {
		String sql = "UPDATE phuphi SET TenPP = ?, Gia = ? WHERE MaPP = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, pp.getTenPP());
			ps.setDouble(2, pp.getGia());
			ps.setLong(3, pp.getMaPP());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}