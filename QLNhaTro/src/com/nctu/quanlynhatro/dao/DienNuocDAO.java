package com.nctu.quanlynhatro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nctu.quanlynhatro.model.PhieuDienNuoc;
import com.nctu.quanlynhatro.model.Phong; // Import Model Phong để setSoPhong

public class DienNuocDAO {
	private Connection conn;

	public DienNuocDAO(Connection conn) {
		this.conn = conn;
	}

	public List<PhieuDienNuoc> getAll() {
		List<PhieuDienNuoc> list = new ArrayList<>();
		String sql = "SELECT dn.*, p.SoPhong, p.MaPhong " + "FROM PhieuDienNuoc dn "
				+ "INNER JOIN Phong p ON dn.MaPhong = p.MaPhong " + "WHERE dn.TrangThaiXoa = 0 "
				+ "ORDER BY dn.ThangNam DESC";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				java.sql.Date d = rs.getDate("ThangNam");
				LocalDate ld = (d != null) ? d.toLocalDate() : null;

				PhieuDienNuoc pdn = new PhieuDienNuoc(rs.getLong("MaDN"), ld, rs.getFloat("ChiSoDienCu"),
						rs.getFloat("ChiSoDienMoi"), rs.getFloat("ChiSoNuocCu"), rs.getFloat("ChiSoNuocMoi"),
						rs.getDouble("TienDien"), rs.getDouble("TienNuoc"), rs.getDouble("GiaDienTaiThoiDiem"),
						rs.getDouble("GiaNuocTaiThoiDiem"), rs.getDouble("TongTien"), rs.getString("TrangThaiDN"));

				if (pdn.getPhong() == null) {
					pdn.setPhong(new Phong());
				}
				pdn.getPhong().setSoPhong(rs.getInt("SoPhong"));
				pdn.getPhong().setMaPhong(rs.getLong("MaPhong"));

				list.add(pdn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Map<String, Object> getGiaDienNuocHienTai() {
		String sql = "SELECT MaGiaDN, GiaDien, GiaNuoc FROM GiaDienNuoc ORDER BY MaGiaDN DESC LIMIT 1";
		Map<String, Object> map = new HashMap<>();
		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				map.put("MaGiaDN", rs.getInt("MaGiaDN"));
				map.put("GiaDien", rs.getDouble("GiaDien"));
				map.put("GiaNuoc", rs.getDouble("GiaNuoc"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	public Map<String, Integer> getListNhaTro() {
		Map<String, Integer> map = new HashMap<>();
		String sql = "SELECT MaNT, TenNT FROM NhaTro WHERE TrangThaiXoa = 0";
		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				map.put(rs.getString("TenNT"), rs.getInt("MaNT"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public Map<String, Integer> getListPhong(int maNT) {
		Map<String, Integer> map = new HashMap<>();

		String sql = "SELECT MaPhong, SoPhong FROM Phong WHERE MaNT = ? AND TrangThaiPhong = N'Đã thuê'";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, maNT);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					map.put(rs.getString("SoPhong"), rs.getInt("MaPhong"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public Map<String, Double> getChiSoCu(int maPhong) {
		Map<String, Double> result = new HashMap<>();
		result.put("Dien", 0.0);
		result.put("Nuoc", 0.0);

		String sql = "SELECT ChiSoDienMoi, ChiSoNuocMoi FROM PhieuDienNuoc WHERE MaPhong = ? AND TrangThaiXoa = 0 ORDER BY ThangNam DESC LIMIT 1";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maPhong);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result.put("Dien", rs.getDouble("ChiSoDienMoi"));
					result.put("Nuoc", rs.getDouble("ChiSoNuocMoi"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean checkTonTai(int maPhong, java.time.LocalDate thangNam) {
		String sql = "SELECT 1 FROM PhieuDienNuoc WHERE MaPhong = ? AND ThangNam = ? AND TrangThaiXoa = 0";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maPhong);
			ps.setDate(2, java.sql.Date.valueOf(thangNam));
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean insertPhieu(int maPhong, java.sql.Date thangNam, double dCu, double dMoi, double nCu, double nMoi,
			double tDien, double tNuoc, double tong, double giaDien, double giaNuoc, int maGiaDN) {

		String sql = "INSERT INTO PhieuDienNuoc(MaPhong, ThangNam, ChiSoDienCu, ChiSoDienMoi, ChiSoNuocCu, ChiSoNuocMoi, TienDien, TienNuoc, TongTien, GiaDienTaiThoiDiem, GiaNuocTaiThoiDiem, TrangThaiDN, TrangThaiXoa, MaGiaDN) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Chưa thanh toán', 0, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maPhong);
			ps.setDate(2, thangNam);
			ps.setDouble(3, dCu);
			ps.setDouble(4, dMoi);
			ps.setDouble(5, nCu);
			ps.setDouble(6, nMoi);
			ps.setDouble(7, tDien);
			ps.setDouble(8, tNuoc);
			ps.setDouble(9, tong);
			ps.setDouble(10, giaDien);
			ps.setDouble(11, giaNuoc);
			ps.setInt(12, maGiaDN);

			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public PhieuDienNuoc getById(long maDN) {
		String sql = "SELECT dn.*, p.SoPhong, nt.TenNT " + "FROM PhieuDienNuoc dn "
				+ "JOIN Phong p ON dn.MaPhong = p.MaPhong " + "JOIN NhaTro nt ON p.MaNT = nt.MaNT "
				+ "WHERE dn.MaDN = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maDN);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					PhieuDienNuoc p = new PhieuDienNuoc();
					p.setMaDN(rs.getLong("MaDN"));
					p.setThangNam(rs.getDate("ThangNam").toLocalDate());
					p.setChiSoDienCu(rs.getFloat("ChiSoDienCu"));
					p.setChiSoDienMoi(rs.getFloat("ChiSoDienMoi"));
					p.setChiSoNuocCu(rs.getFloat("ChiSoNuocCu"));
					p.setChiSoNuocMoi(rs.getFloat("ChiSoNuocMoi"));
					p.setTienDien(rs.getDouble("TienDien"));
					p.setTienNuoc(rs.getDouble("TienNuoc"));
					p.setTongTien(rs.getDouble("TongTien"));
					p.setGiaDienTaiThoiDiem(rs.getDouble("GiaDienTaiThoiDiem"));
					p.setGiaNuocTaiThoiDiem(rs.getDouble("GiaNuocTaiThoiDiem"));
					p.setGhiChu(rs.getString("TenNT"));
					if (p.getPhong() == null) {
						p.setPhong(new com.nctu.quanlynhatro.model.Phong());
					}
					p.getPhong().setSoPhong(rs.getInt("SoPhong"));

					return p;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean updatePhieu(PhieuDienNuoc p) {
		String sql = "UPDATE PhieuDienNuoc SET " + "ChiSoDienCu=?, ChiSoDienMoi=?, ChiSoNuocCu=?, ChiSoNuocMoi=?, "
				+ "TienDien=?, TienNuoc=?, TongTien=?, GiaDienTaiThoiDiem=?, GiaNuocTaiThoiDiem=? " + "WHERE MaDN=?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDouble(1, p.getChiSoDienCu());
			ps.setDouble(2, p.getChiSoDienMoi());
			ps.setDouble(3, p.getChiSoNuocCu());
			ps.setDouble(4, p.getChiSoNuocMoi());
			ps.setDouble(5, p.getTienDien());
			ps.setDouble(6, p.getTienNuoc());
			ps.setDouble(7, p.getTongTien());
			ps.setDouble(8, p.getGiaDienTaiThoiDiem());
			ps.setDouble(9, p.getGiaNuocTaiThoiDiem());
			ps.setLong(10, p.getMaDN());

			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean delete(long maDN) {
		String sql = "UPDATE PhieuDienNuoc SET TrangThaiXoa = 1 WHERE MaDN = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maDN);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}