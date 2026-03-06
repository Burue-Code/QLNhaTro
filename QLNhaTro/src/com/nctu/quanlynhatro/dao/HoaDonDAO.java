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

import com.nctu.quanlynhatro.model.HoaDon;
import com.nctu.quanlynhatro.model.KhachHangGoiY;
import com.nctu.quanlynhatro.model.PhuongThucThanhToan;

public class HoaDonDAO {

	private Connection conn;

	public HoaDonDAO(Connection conn) {
		this.conn = conn;
	}

	public List<HoaDon> getAll() {
		List<HoaDon> list = new ArrayList<>();
		String sql = """
				    SELECT
				        TT.MaHoaDon,
				        TT.NgayTT,
				        TT.SoTienTT,
				        TT.TongTienPP,
				        TT.LoaiTT,
				        TT.GhiChu,
				        PT.MaPT AS MaPT,
				        PT.TenPT
				    FROM HoaDon TT
				    INNER JOIN PTThanhToan PT ON TT.MaPT = PT.MaPT
				    WHERE TT.TrangThaiXoa = 0
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				HoaDon hd = new HoaDon(rs.getLong("MaHoaDon"), rs.getTimestamp("NgayTT").toLocalDateTime(),
						rs.getDouble("SoTienTT"), rs.getDouble("TongTienPP"), rs.getString("LoaiTT"),
						rs.getString("GhiChu"));

				PhuongThucThanhToan pt = new PhuongThucThanhToan();
				pt.setMaPT(rs.getLong("MaPT"));
				pt.setTenPT(rs.getString("TenPT"));

				hd.setPhuongThucThanhToan(pt);
				list.add(hd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Map<String, String> getKhachHangGoiY() {
		Map<String, String> mapKH = new HashMap<>();
		String sql = """
				    SELECT hd.MaHD, kh.TenKH
				    FROM HopDong hd
				    JOIN KhachHang kh ON hd.MaKH = kh.MaKH
				    WHERE hd.TrangThaiXoa = 0
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				mapKH.put(rs.getString("MaHD"), rs.getString("TenKH"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapKH;
	}

	public Map<String, Object> getThongTinHopDong(long maHD) {
		Map<String, Object> info = new HashMap<>();
		String sql = """
				    SELECT p.MaPhong, nt.MaNT, p.SoPhong, nt.TenNT, hd.GiaThue
				    FROM HopDong hd
				    JOIN Phong p ON hd.MaPhong = p.MaPhong
				    JOIN NhaTro nt ON nt.MaNT = p.MaNT
				    WHERE hd.TrangThaiXoa = 0 AND hd.MaHD = ?
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					info.put("MaPhong", rs.getLong("MaPhong"));
					info.put("MaNT", rs.getInt("MaNT"));
					info.put("SoPhong", rs.getString("SoPhong"));
					info.put("TenNT", rs.getString("TenNT"));
					info.put("GiaThue", rs.getDouble("GiaThue"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public List<Map<String, Object>> getPhuPhiByHopDong(long maHD) {
		List<Map<String, Object>> listPP = new ArrayList<>();
		String sql = """
				    SELECT pp.MaPP, pp.TenPP, pp.Gia
				    FROM HopDong hd
				    JOIN Phong p ON hd.MaPhong = p.MaPhong
				    JOIN chitietphuphi ctpp ON p.MaPhong = ctpp.MaPhong
				    JOIN PhuPhi pp ON ctpp.MaPP = pp.MaPP
				    WHERE hd.MaHD = ?
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new HashMap<>();
					row.put("MaPP", rs.getLong("MaPP"));
					row.put("TenPP", rs.getString("TenPP"));
					row.put("Gia", rs.getDouble("Gia"));
					listPP.add(row);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listPP;
	}

	public List<Map<String, Object>> getDienNuocChuaThanhToan(long maHD) {
		List<Map<String, Object>> listDN = new ArrayList<>();
		String sql = """
				    SELECT dn.MaDN, DATE_FORMAT(dn.ThangNam, '%m/%Y') AS ThangNam, dn.TongTien
				    FROM PhieuDienNuoc dn
				    JOIN Phong p ON p.MaPhong = dn.MaPhong
				    JOIN HopDong hd ON hd.MaPhong = p.MaPhong
				    WHERE dn.TrangThaiXoa = 0
				      AND hd.MaHD = ?
				      AND (dn.MaHoaDon IS NULL OR dn.MaHoaDon = 0)
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new HashMap<>();
					row.put("MaDN", rs.getLong("MaDN"));
					row.put("ThangNam", rs.getString("ThangNam"));
					row.put("TongTien", rs.getDouble("TongTien"));
					listDN.add(row);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listDN;
	}

	public String insertHoaDon(HoaDon hd, List<Long> listMaDN_DaChon) {
		try {
			conn.setAutoCommit(false);
			String sqlInsertHD = """
					    INSERT INTO HoaDon (NgayTT, SoTienTT, MaHD, MaPT, GhiChu, TrangThaiXoa, LoaiTT, TongTienPP)
					    VALUES (?, ?, ?, ?, ?, 0, ?, ?)
					""";

			long maHoaDonMoi = 0;
			try (PreparedStatement ps = conn.prepareStatement(sqlInsertHD, Statement.RETURN_GENERATED_KEYS)) {
				ps.setTimestamp(1, java.sql.Timestamp.valueOf(hd.getNgayThanhToan()));
				ps.setDouble(2, hd.getTongTien());
				ps.setLong(3, hd.getHopDong().getMaHD());
				ps.setLong(4, hd.getPhuongThucThanhToan().getMaPT());
				ps.setString(5, hd.getGhiChu());
				ps.setString(6, hd.getLoaiThanhToan());
				ps.setDouble(7, hd.getTongTienPP());

				ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						maHoaDonMoi = rs.getLong(1);
					}
				}
			}

			if (!"Tiền Trọ".equals(hd.getLoaiThanhToan()) && listMaDN_DaChon != null && !listMaDN_DaChon.isEmpty()) {
				String sqlUpdateDN = "UPDATE PhieuDienNuoc SET MaHoaDon = ? WHERE MaDN = ?";
				try (PreparedStatement psDN = conn.prepareStatement(sqlUpdateDN)) {
					for (Long maDN : listMaDN_DaChon) {
						psDN.setLong(1, maHoaDonMoi);
						psDN.setLong(2, maDN);
						psDN.executeUpdate();
					}
				}
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

	public List<KhachHangGoiY> searchKhachHang(String keyword) {
		List<KhachHangGoiY> list = new ArrayList<>();
		String sql = """
				    SELECT hd.MaHD, kh.MaKH, kh.TenKH
				    FROM HopDong hd
				    JOIN KhachHang kh ON hd.MaKH = kh.MaKH
				    WHERE hd.TrangThaiXoa = 0
				      AND (kh.TenKH LIKE ? OR kh.MaKH LIKE ?)
				    LIMIT 10
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			String key = "%" + keyword + "%";
			ps.setString(1, key);
			ps.setString(2, key);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(new KhachHangGoiY(rs.getLong("MaHD"), rs.getLong("MaKH"), rs.getString("TenKH")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public HoaDon getHoaDonById(long maHD) {
		String sql = "SELECT * FROM HoaDon WHERE MaHoaDon = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					HoaDon hd = new HoaDon();
					hd.setMaHoaDon(rs.getLong("MaHoaDon"));
					hd.getHopDong().setMaHD(rs.getLong("MaHD"));
					java.sql.Timestamp ts = rs.getTimestamp("NgayTT");
					if (ts != null) {
						hd.setNgayThanhToan(ts.toLocalDateTime());
					}
					hd.setTongTien(rs.getDouble("SoTienTT"));
					hd.setTongTienPP(rs.getDouble("TongTienPP"));
					hd.getPhuongThucThanhToan().setMaPT(rs.getInt("MaPT"));
					hd.setGhiChu(rs.getString("GhiChu"));
					hd.setLoaiThanhToan(rs.getString("LoaiTT"));
					return hd;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Map<String, Object>> getDienNuocByMaHoaDon(long maHoaDon) {
		List<Map<String, Object>> listDN = new ArrayList<>();
		String sql = "SELECT MaDN, DATE_FORMAT(ThangNam, '%m/%Y') as ThangNam, TongTien FROM PhieuDienNuoc WHERE MaHoaDon = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHoaDon);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new HashMap<>();
					row.put("MaDN", rs.getLong("MaDN"));
					row.put("ThangNam", rs.getString("ThangNam"));
					row.put("TongTien", rs.getDouble("TongTien"));
					listDN.add(row);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listDN;
	}

	public String getTenKhachHangByMaHD(long maHD) {
		String sql = "SELECT k.TenKH FROM KhachHang k JOIN HopDong h ON k.MaKH = h.MaKH WHERE h.MaHD = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("TenKH");
			}
		} catch (Exception e) {
		}
		return "";
	}

	public String updateHoaDon(HoaDon hd, List<Long> listMaDN_Moi) {
		try {
			conn.setAutoCommit(false);
			String sqlUpdateHD = "UPDATE HoaDon SET SoTienTT=?, MaPT=?, GhiChu=?, LoaiTT=?, TongTienPP=?, NgayTT=? WHERE MaHoaDon=?";
			try (PreparedStatement ps = conn.prepareStatement(sqlUpdateHD)) {
				ps.setDouble(1, hd.getTongTien());
				ps.setLong(2, hd.getPhuongThucThanhToan().getMaPT());
				ps.setString(3, hd.getGhiChu());
				ps.setString(4, hd.getLoaiThanhToan());
				ps.setDouble(5, hd.getTongTienPP());
				ps.setTimestamp(6, java.sql.Timestamp.valueOf(hd.getNgayThanhToan()));
				ps.setLong(7, hd.getMaHoaDon());
				ps.executeUpdate();
			}

			String sqlResetDN = "UPDATE PhieuDienNuoc SET MaHoaDon = NULL WHERE MaHoaDon = ?";
			try (PreparedStatement psReset = conn.prepareStatement(sqlResetDN)) {
				psReset.setLong(1, hd.getMaHoaDon());
				psReset.executeUpdate();
			}

			if (!"Tiền Trọ".equals(hd.getLoaiThanhToan()) && listMaDN_Moi != null && !listMaDN_Moi.isEmpty()) {
				String sqlUpdateDN = "UPDATE PhieuDienNuoc SET MaHoaDon = ? WHERE MaDN = ?";
				try (PreparedStatement psDN = conn.prepareStatement(sqlUpdateDN)) {
					for (Long maDN : listMaDN_Moi) {
						psDN.setLong(1, hd.getMaHoaDon());
						psDN.setLong(2, maDN);
						psDN.executeUpdate();
					}
				}
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

	public boolean deleteHoaDon(long maHoaDon) {
		String sqlDeleteHD = "UPDATE HoaDon SET TrangThaiXoa = 1 WHERE MaHoaDon = ?";
		String sqlResetDN = "UPDATE PhieuDienNuoc SET MaHoaDon = NULL WHERE MaHoaDon = ?";

		try {
			conn.setAutoCommit(false);
			try (PreparedStatement psDN = conn.prepareStatement(sqlResetDN)) {
				psDN.setLong(1, maHoaDon);
				psDN.executeUpdate();
			}

			try (PreparedStatement psHD = conn.prepareStatement(sqlDeleteHD)) {
				psHD.setLong(1, maHoaDon);
				int rowEffect = psHD.executeUpdate();
				if (rowEffect > 0) {
					conn.commit();
					return true;
				} else {
					conn.rollback();
					return false;
				}
			}
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

	public Map<String, Object> getChiTietHoaDon(long maHD) {
		Map<String, Object> map = new HashMap<>();
		String sql = "SELECT hd.MaHoaDon, hd.NgayTT, hd.SoTienTT, hd.TongTienPP, hd.LoaiTT, hd.GhiChu, "
				+ "hop.MaHD AS MaHopDong, kh.TenKH, nt.TenNT, p.SoPhong, p.Gia, pt.TenPT " + "FROM HoaDon hd "
				+ "INNER JOIN HopDong hop ON hd.MaHD = hop.MaHD " + "INNER JOIN KhachHang kh ON hop.MaKH = kh.MaKH "
				+ "INNER JOIN Phong p ON hop.MaPhong = p.MaPhong " + "INNER JOIN NhaTro nt ON p.MaNT = nt.MaNT "
				+ "LEFT JOIN PTThanhToan pt ON hd.MaPT = pt.MaPT " + "WHERE hd.MaHoaDon = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					map.put("MaHoaDon", rs.getLong("MaHoaDon"));
					map.put("NgayTT", rs.getTimestamp("NgayTT"));
					map.put("SoTienTT", rs.getDouble("SoTienTT"));
					map.put("TongTienPP", rs.getDouble("TongTienPP"));
					map.put("LoaiTT", rs.getString("LoaiTT"));
					map.put("GhiChu", rs.getString("GhiChu"));
					map.put("MaHopDong", rs.getLong("MaHopDong"));
					map.put("TenKH", rs.getString("TenKH"));
					map.put("TenNT", rs.getString("TenNT"));
					map.put("SoPhong", rs.getString("SoPhong"));
					map.put("Gia", rs.getDouble("Gia"));
					map.put("TenPT", rs.getString("TenPT"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public List<Map<String, Object>> getDienNuocMaHoaDon(long maHoaDon) {
		List<Map<String, Object>> list = new ArrayList<>();
		String sql = "SELECT MaDN, ThangNam, TongTien FROM PhieuDienNuoc WHERE MaHoaDon = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHoaDon);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> dn = new HashMap<>();
					dn.put("MaDN", rs.getLong("MaDN"));
					dn.put("ThangNam", rs.getDate("ThangNam"));
					dn.put("TongTien", rs.getDouble("TongTien"));
					list.add(dn);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}