package com.nctu.quanlynhatro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nctu.quanlynhatro.model.NhaTro;

public class NhaTroDAO {
	private Connection conn;

	public NhaTroDAO(Connection conn) {
		this.conn = conn;
	}

	public List<NhaTro> getAll() {
		List<NhaTro> list = new ArrayList<>();
		String sql = "SELECT * FROM NhaTro WHERE TrangThaiXoa = 0";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(new NhaTro(rs.getLong("MaNT"), rs.getString("TenNT"), rs.getString("DiaChi"),
						rs.getInt("SLPhong"), rs.getNString("TrangThaiNT"), rs.getString("GhiChu")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Map<Integer, String> getNhaTroConPhong() {
		Map<Integer, String> map = new HashMap<>();
		String sql = "SELECT nt.MaNT, nt.TenNT FROM nhatro nt WHERE nt.TrangThaiXoa = 0";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				map.put(rs.getInt("MaNT"), rs.getString("TenNT"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public boolean deleteSoft(long maNT) {
		String sql = "UPDATE nhatro SET TrangThaiXoa = 1 WHERE MaNT = ?";
		try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, maNT);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isTenNhaTroExist(String tenNT) {
		String sql = "SELECT 1 FROM nhatro WHERE TenNT = ? AND TrangThaiXoa = 0";
		try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, tenNT);
			return ps.executeQuery().next();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isTenNhaTroExistForUpdate(int maNT, String tenNT) {
		String sql = "SELECT 1 FROM nhatro WHERE TenNT = ? AND MaNT <> ? AND TrangThaiXoa = 0";
		try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, tenNT);
			ps.setInt(2, maNT);
			return ps.executeQuery().next();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean insert(String tenNT, int slPhong, String diaChi, String ghiChu, String trangThai) {
		String sql = "INSERT INTO nhatro (TenNT, SLPhong, DiaChi, GhiChu, TrangThaiNT, TrangThaiXoa) VALUES (?, ?, ?, ?, ?, 0)";
		if (isTenNhaTroExist(tenNT)) {
			return false;
		}

		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, tenNT);
			ps.setInt(2, slPhong);
			ps.setString(3, diaChi);
			ps.setString(4, ghiChu);
			ps.setString(5, trangThai);
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(NhaTro nt) {
		String sql = "UPDATE nhatro SET TenNT = ?, SLPhong = ?, DiaChi = ?, GhiChu = ?, TrangThaiNT = ? WHERE MaNT = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, nt.getTenNT());
			ps.setInt(2, nt.getSLPhong());
			ps.setString(3, nt.getDiaChi());
			ps.setString(4, nt.getGhiChu());
			ps.setString(5, nt.getTrangThaiNT());
			ps.setLong(6, nt.getMaNT());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}