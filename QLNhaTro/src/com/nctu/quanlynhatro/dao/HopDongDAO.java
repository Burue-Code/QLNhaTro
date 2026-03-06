package com.nctu.quanlynhatro.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nctu.quanlynhatro.model.HopDong;
import com.nctu.quanlynhatro.model.KhachHang;

public class HopDongDAO {

	private Connection conn;

	public HopDongDAO(Connection conn) {
		this.conn = conn;
	}

	public List<HopDong> getAll() {
		List<HopDong> list = new ArrayList<>();
		String sql = "SELECT HD.MaHD, HD.NgayLapHD, HD.NgayKT, HD.GiaThue, HD.SoNguoiO, HD.TrangThaiHD, HD.GhiChu, KH.TenKH "
				+ "FROM HopDong HD " + "INNER JOIN KhachHang KH ON HD.MaKH = KH.MaKH " + "WHERE hd.TrangThaiXoa = 0 ";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Date ngayKT = rs.getDate("NgayKT");
				HopDong hd = new HopDong(rs.getLong("MaHD"), rs.getString("TenKH"),
						rs.getDate("NgayLapHD").toLocalDate(), ngayKT != null ? ngayKT.toLocalDate() : null,
						rs.getDouble("GiaThue"), rs.getInt("SoNguoiO"), rs.getString("TrangThaiHD"),
						rs.getString("GhiChu"));
				list.add(hd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public String insertHopDong(long maKH, int maPhong, LocalDate ngayBD, LocalDate ngayKT, double giaThue,
			int soNguoiO, String ghiChu, String trangThai, List<Long> listMaKHPhu) {
		String checkQuery = "SELECT TrangThaiXoa FROM HopDong WHERE MaKH = ? AND MaPhong = ?";
		String insertHD = "INSERT INTO HopDong(MaKH, MaPhong, NgayLapHD, NgayKT, GiaThue, SoNguoiO, GhiChu, TrangThaiHD, TrangThaiXoa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)";
		String updateKHPhu = "UPDATE KhachHang SET MaKHChinh = ?, MaPhong = ? WHERE MaKH = ?";
		String updateKHChinh = "UPDATE KhachHang SET MaPhong = ? WHERE MaKH = ?";
		String updatePhong = "UPDATE Phong SET MaHD = ?, TrangThaiPhong = 'Đã thuê' WHERE MaPhong = ?";

		try {
			conn.setAutoCommit(false);
			try (PreparedStatement psCheck = conn.prepareStatement(checkQuery)) {
				psCheck.setLong(1, maKH);
				psCheck.setInt(2, maPhong);
				try (ResultSet rs = psCheck.executeQuery()) {
					if (rs.next()) {
						return "Hợp đồng cho khách hàng và phòng này đã tồn tại!";
					}
				}
			}

			int maHDMoi = 0;
			try (PreparedStatement psInsert = conn.prepareStatement(insertHD, Statement.RETURN_GENERATED_KEYS)) {
				psInsert.setLong(1, maKH);
				psInsert.setInt(2, maPhong);
				psInsert.setDate(3, ngayBD != null ? java.sql.Date.valueOf(ngayBD) : null);
				psInsert.setDate(4, ngayKT != null ? java.sql.Date.valueOf(ngayKT) : null);
				psInsert.setDouble(5, giaThue);
				psInsert.setInt(6, soNguoiO);
				psInsert.setString(7, ghiChu);
				psInsert.setString(8, trangThai);
				psInsert.executeUpdate();

				try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						maHDMoi = generatedKeys.getInt(1);
					} else {
						throw new SQLException("Thêm hợp đồng thất bại, không lấy được ID.");
					}
				}
			}

			if (listMaKHPhu != null && !listMaKHPhu.isEmpty()) {
				try (PreparedStatement psUpdateKHPhu = conn.prepareStatement(updateKHPhu)) {
					for (Long maKHPhu : listMaKHPhu) {
						psUpdateKHPhu.setLong(1, maKH);
						psUpdateKHPhu.setInt(2, maPhong);
						psUpdateKHPhu.setLong(3, maKHPhu);
						psUpdateKHPhu.executeUpdate();
					}
				}
			}

			try (PreparedStatement psUpdateKHChinh = conn.prepareStatement(updateKHChinh)) {
				psUpdateKHChinh.setInt(1, maPhong);
				psUpdateKHChinh.setLong(2, maKH);
				psUpdateKHChinh.executeUpdate();
			}

			try (PreparedStatement psUpdatePhong = conn.prepareStatement(updatePhong)) {
				psUpdatePhong.setInt(1, maHDMoi);
				psUpdatePhong.setInt(2, maPhong);
				psUpdatePhong.executeUpdate();
			}

			conn.commit();
			return "SUCCESS";
		} catch (Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException rollbackEx) {
			}
			return "Lỗi hệ thống: " + ex.getMessage();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException closeEx) {
			}
		}
	}

	public HopDong getHopDongById(long maHD) {
		HopDong hd = null;
		String sql = "SELECT * FROM HopDong WHERE MaHD = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					hd = new HopDong();
					hd.setMaHD(rs.getLong("MaHD"));
					if (rs.getDate("NgayLapHD") != null) {
						hd.setNgayLap(rs.getDate("NgayLapHD").toLocalDate());
					}
					if (rs.getDate("NgayKT") != null) {
						hd.setNgayKetThuc(rs.getDate("NgayKT").toLocalDate());
					}
					hd.setGiaThue(rs.getDouble("GiaThue"));
					hd.setSoNguoiO(rs.getInt("SoNguoiO"));
					hd.setGhiChu(rs.getString("GhiChu"));
					hd.setTrangThai(rs.getString("TrangThaiHD"));

					int maPhong = rs.getInt("MaPhong");
					hd.setPhong(new PhongDAO(conn).getPhongById(maPhong));

					long maKH = rs.getLong("MaKH");
					KhachHang khChinh = new KhachHangDAO(conn).getKhachHangById(maKH);
					hd.setTenKH(khChinh != null ? khChinh.getTenKH() : "Không xác định");
					hd.setDanhSachKhachHang(new KhachHangDAO(conn).getKhachHangByPhong(maPhong));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hd;
	}

	public String updateHopDong(long maHD, long maKHChinh, int maPhongMoi, int maPhongCu, LocalDate ngayBD,
			LocalDate ngayKT, double giaThue, int soNguoi, String ghiChu, List<Long> listMaKHPhu) {
		try {
			conn.setAutoCommit(false);
			String sqlUpdateHD = "UPDATE HopDong SET MaKH=?, MaPhong=?, NgayLapHD=?, NgayKT=?, GiaThue=?, SoNguoiO=?, GhiChu=? WHERE MaHD=?";
			try (PreparedStatement ps = conn.prepareStatement(sqlUpdateHD)) {
				ps.setLong(1, maKHChinh);
				ps.setInt(2, maPhongMoi);
				ps.setDate(3, java.sql.Date.valueOf(ngayBD));
				ps.setDate(4, java.sql.Date.valueOf(ngayKT));
				ps.setDouble(5, giaThue);
				ps.setInt(6, soNguoi);
				ps.setString(7, ghiChu);
				ps.setLong(8, maHD);
				ps.executeUpdate();
			}

			if (maPhongMoi != maPhongCu) {
				try (PreparedStatement psOld = conn
						.prepareStatement("UPDATE Phong SET TrangThaiPhong='Trống', MaHD=NULL WHERE MaPhong=?")) {
					psOld.setInt(1, maPhongCu);
					psOld.executeUpdate();
				}
				try (PreparedStatement psNew = conn
						.prepareStatement("UPDATE Phong SET TrangThaiPhong='Đã thuê', MaHD=? WHERE MaPhong=?")) {
					psNew.setLong(1, maHD);
					psNew.setInt(2, maPhongMoi);
					psNew.executeUpdate();
				}
				try (PreparedStatement psMove = conn
						.prepareStatement("UPDATE KhachHang SET MaPhong=? WHERE MaPhong=?")) {
					psMove.setInt(1, maPhongMoi);
					psMove.setInt(2, maPhongCu);
					psMove.executeUpdate();
				}
			}

			try (PreparedStatement psReset = conn
					.prepareStatement("UPDATE KhachHang SET MaKHChinh=NULL WHERE MaKHChinh=?")) {
				psReset.setLong(1, maKHChinh);
				psReset.executeUpdate();
			}

			if (listMaKHPhu != null && !listMaKHPhu.isEmpty()) {
				try (PreparedStatement psPhu = conn
						.prepareStatement("UPDATE KhachHang SET MaKHChinh=?, MaPhong=? WHERE MaKH=?")) {
					for (Long maPhu : listMaKHPhu) {
						psPhu.setLong(1, maKHChinh);
						psPhu.setInt(2, maPhongMoi);
						psPhu.setLong(3, maPhu);
						psPhu.executeUpdate();
					}
				}
			}

			try (PreparedStatement psMain = conn
					.prepareStatement("UPDATE KhachHang SET MaPhong=?, MaKHChinh=NULL WHERE MaKH=?")) {
				psMain.setInt(1, maPhongMoi);
				psMain.setLong(2, maKHChinh);
				psMain.executeUpdate();
			}

			conn.commit();
			return "SUCCESS";
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
			}
			return "Lỗi: " + e.getMessage();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception ex) {
			}
		}
	}

	public boolean deleteHopDong(long maHD) {
		try {
			conn.setAutoCommit(false);
			int maPhong = 0;
			try (PreparedStatement psGet = conn.prepareStatement("SELECT MaPhong FROM HopDong WHERE MaHD = ?")) {
				psGet.setLong(1, maHD);
				try (ResultSet rs = psGet.executeQuery()) {
					if (rs.next()) {
						maPhong = rs.getInt("MaPhong");
					}
				}
			}

			if (maPhong > 0) {
				try (PreparedStatement psKH = conn
						.prepareStatement("UPDATE KhachHang SET MaPhong = NULL, MaKHChinh = NULL WHERE MaPhong = ?")) {
					psKH.setInt(1, maPhong);
					psKH.executeUpdate();
				}
			}

			try (PreparedStatement psP = conn
					.prepareStatement("UPDATE Phong SET MaHD = NULL, TrangThaiPhong = 'Còn trống' WHERE MaHD = ?")) {
				psP.setLong(1, maHD);
				psP.executeUpdate();
			}

			try (PreparedStatement psHD = conn
					.prepareStatement("UPDATE HopDong SET TrangThaiXoa = 1, TrangThaiHD = 'Đã hủy' WHERE MaHD = ?")) {
				psHD.setLong(1, maHD);
				psHD.executeUpdate();
			}

			conn.commit();
			return true;
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
			}
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception ex) {
			}
		}
	}

	public Map<String, Object> getChiTietHopDong(long maHD) {
		Map<String, Object> map = new HashMap<>();
		String sql = "SELECT hd.MaHD, kh.TenKH, hd.NgayLapHD, hd.NgayKT, nt.TenNT, p.SoPhong, hd.SoNguoiO, hd.GiaThue, hd.TrangThaiHD, hd.GhiChu "
				+ "FROM HopDong hd INNER JOIN KhachHang kh ON hd.MaKH = kh.MaKH INNER JOIN Phong p ON hd.MaPhong = p.MaPhong "
				+ "INNER JOIN NhaTro nt ON p.MaNT = nt.MaNT WHERE hd.MaHD = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					map.put("MaHD", rs.getLong("MaHD"));
					map.put("TenKH", rs.getString("TenKH"));
					map.put("NgayLapHD", rs.getDate("NgayLapHD"));
					map.put("NgayKT", rs.getDate("NgayKT"));
					map.put("TenNT", rs.getString("TenNT"));
					map.put("SoPhong", rs.getString("SoPhong"));
					map.put("SoNguoiO", rs.getInt("SoNguoiO"));
					map.put("Gia", rs.getDouble("GiaThue"));
					map.put("TrangThaiHD", rs.getString("TrangThaiHD"));
					map.put("GhiChu", rs.getString("GhiChu"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public List<Map<String, Object>> getKhachHangPhuThuoc(long maHD) {
		List<Map<String, Object>> list = new ArrayList<>();
		String sql = "SELECT kh.MaKH, kh.TenKH, kh.DiaChi, kh.GioiTinh, kh.NgaySinh, kh.SDT FROM KhachHang kh "
				+ "INNER JOIN HopDong hd ON kh.MaPhong = hd.MaPhong WHERE hd.MaHD = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> kh = new HashMap<>();
					kh.put("MaKH", rs.getLong("MaKH"));
					kh.put("TenKH", rs.getString("TenKH"));
					kh.put("DiaChi", rs.getString("DiaChi"));
					kh.put("GioiTinh", rs.getBoolean("GioiTinh") ? "Nam" : "Nữ");
					kh.put("NgaySinh", rs.getDate("NgaySinh"));
					kh.put("SDT", rs.getString("SDT"));
					list.add(kh);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}