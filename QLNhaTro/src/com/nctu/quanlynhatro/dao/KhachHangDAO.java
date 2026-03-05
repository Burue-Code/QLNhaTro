package com.nctu.quanlynhatro.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nctu.quanlynhatro.model.KhachHang;

public class KhachHangDAO {
	private Connection conn;

	public KhachHangDAO(Connection conn) {
		this.conn = conn;
	}

	public List<KhachHang> getAll() {
		List<KhachHang> list = new ArrayList<>();
		String sql = "SELECT * FROM KhachHang WHERE TrangThaiXoa = 0";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				KhachHang kh = new KhachHang(rs.getLong("MaKH"), rs.getString("TenKH"), rs.getString("DiaChi"),
						rs.getBoolean("GioiTinh"), rs.getDate("NgaySinh").toLocalDate(), rs.getString("SDT"),
						rs.getString("Gmail"), rs.getString("SoCCCD"), rs.getLong("MaKHChinh"));
				list.add(kh);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean isExistByCCCD(String cccd) {
		String sql = "SELECT TrangThaiXoa FROM khachhang WHERE SoCCCD = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, cccd);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

// thêm dư liệu
	public boolean insert(String tenKH, String diaChi, LocalDate ngaySinh, String sdt, boolean gioiTinh, String soCCCD,
			String gmail, long maKHC) {
		String sql = """
				    INSERT INTO khachhang
				    (TenKH, DiaChi, NgaySinh, SDT, GioiTinh, SoCCCD, Gmail, MaKHChinh, TrangThaiXoa)
				    VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, tenKH);
			ps.setString(2, diaChi);

			if (ngaySinh == null) {
				ps.setNull(3, Types.DATE);
			} else {
				ps.setDate(3, java.sql.Date.valueOf(ngaySinh));
			}

			ps.setString(4, sdt);
			ps.setBoolean(5, gioiTinh);
			ps.setString(6, soCCCD);
			ps.setString(7, gmail);

			if (maKHC > 0) {
				ps.setLong(8, maKHC);
			} else {
				ps.setNull(8, Types.BIGINT);
			}

			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<KhachHang> search(String keyword) {
		List<KhachHang> list = new ArrayList<>();
		String sql = """
				    SELECT * FROM khachhang
				    WHERE TrangThaiXoa = 0
				    AND (MaKH LIKE ? OR TenKH LIKE ?)
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			String key = "%" + keyword + "%";
			ps.setString(1, key);
			ps.setString(2, key);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				KhachHang kh = new KhachHang();
				kh.setMaKH(rs.getLong("MaKH"));
				kh.setTenKH(rs.getString("TenKH"));
				kh.setDiaChi(rs.getString("DiaChi"));
				kh.setSdt(rs.getString("SDT"));
				kh.setGioiTinh(rs.getBoolean("GioiTinh"));

				Date ns = rs.getDate("NgaySinh");
				if (ns != null) {
					kh.setNgaySinh(ns.toLocalDate());
				}

				list.add(kh);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static boolean laSoCCCDHopLe(String cccd) {
		if (cccd == null || cccd.length() != 12 || !cccd.matches("\\d+")) {
			return false;
		}

		int maTinh = Integer.parseInt(cccd.substring(0, 3));
		int gioiTinhVaTheKy = Integer.parseInt(cccd.substring(3, 4));

		if (maTinh < 1 || maTinh > 96) {
			return false;
		}
		if (gioiTinhVaTheKy < 0 || gioiTinhVaTheKy > 3) {
			return false;
		}

		return true;
	}

	// UpdelData

	public KhachHang findById(Long maKH) {
		String sql = "SELECT * FROM khachhang WHERE MaKH = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, maKH);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				KhachHang kh = new KhachHang();
				kh.setMaKH(rs.getLong("MaKH"));
				kh.setTenKH(rs.getString("TenKH"));
				kh.setDiaChi(rs.getString("DiaChi"));
				kh.setSdt(rs.getString("SDT"));
				kh.setGioiTinh(rs.getBoolean("GioiTinh"));
				kh.setCccd(rs.getString("SoCCCD"));
				kh.setGmail(rs.getString("Gmail"));
				kh.setKhachHangChinh(rs.getLong("MaKHChinh"));
				kh.getPhong().setMaPhong(rs.getLong("MaPhong"));
				Date ns = rs.getDate("NgaySinh");
				if (ns != null) {
					kh.setNgaySinh(ns.toLocalDate());
				}
				return kh;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isKhachHangChinh(Long maKH) {
		String sql = "SELECT COUNT(*) FROM khachhang WHERE MaKHChinh = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, maKH);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<KhachHang> getKhachHangPhu(Long maKHChinh) {
		List<KhachHang> list = new ArrayList<>();
		String sql = "SELECT * FROM khachhang WHERE TrangThaiXoa = 0 AND MaKHChinh = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, maKHChinh);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				KhachHang kh = new KhachHang();
				kh.setMaKH(rs.getLong("MaKH"));
				kh.setTenKH(rs.getString("TenKH"));
				kh.setDiaChi(rs.getString("DiaChi"));
				kh.setSdt(rs.getString("SDT"));
				kh.setGioiTinh(rs.getBoolean("GioiTinh"));
				list.add(kh);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public KhachHang getKhachHangById(long maKH) {
		String sql = "SELECT * FROM KhachHang WHERE MaKH = ?";
		try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maKH);
			try (java.sql.ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					KhachHang kh = new KhachHang();
					kh.setMaKH(rs.getLong("MaKH"));
					kh.setTenKH(rs.getString("TenKH"));
					kh.setDiaChi(rs.getString("DiaChi"));
					kh.setSdt(rs.getString("SDT"));
					kh.setCccd(rs.getString("SoCCCD"));
					kh.setGioiTinh(rs.getBoolean("GioiTinh"));
					if (rs.getDate("NgaySinh") != null) {
						kh.setNgaySinh(rs.getDate("NgaySinh").toLocalDate());
					}
					return kh; // Trả về 1 đối tượng
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public java.util.List<KhachHang> getKhachHangByPhong(long maPhong) {
		java.util.List<KhachHang> list = new java.util.ArrayList<>();
		String sql = "SELECT * FROM KhachHang WHERE MaPhong = ? AND TrangThaiXoa = 0";
		try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maPhong);
			try (java.sql.ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					KhachHang kh = new KhachHang();
					kh.setMaKH(rs.getLong("MaKH"));
					kh.setTenKH(rs.getString("TenKH"));
					kh.setDiaChi(rs.getString("DiaChi"));
					kh.setGioiTinh(rs.getBoolean("GioiTinh"));
					kh.setKhachHangChinh(rs.getLong("MaKHChinh")); // Để phân biệt chủ hộ
					if (rs.getDate("NgaySinh") != null) {
						kh.setNgaySinh(rs.getDate("NgaySinh").toLocalDate());
					}

					list.add(kh); // Thêm vào danh sách
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list; // Trả về danh sách
	}

	public boolean updateKhachHang(KhachHang kh, List<Long> danhSachKHPhu) {
		String updateKH = """
				    UPDATE khachhang SET
				    TenKH = ?, DiaChi = ?, NgaySinh = ?, SDT = ?, GioiTinh = ?,
				    SoCCCD = ?, Gmail = ?, MaKHChinh = ?, TrangThaiXoa = 0
				    WHERE MaKH = ?
				""";

		try {
			conn.setAutoCommit(false); // 🔥 TRANSACTION

			// Update khách hàng chính
			try (PreparedStatement ps = conn.prepareStatement(updateKH)) {
				ps.setString(1, kh.getTenKH());
				ps.setString(2, kh.getDiaChi());

				if (kh.getNgaySinh() == null) {
					ps.setNull(3, Types.DATE);
				} else {
					ps.setDate(3, Date.valueOf(kh.getNgaySinh()));
				}

				ps.setString(4, kh.getSdt());
				ps.setBoolean(5, kh.getGioiTinh());
				ps.setString(6, kh.getCccd());
				ps.setString(7, kh.getGmail());

				if (kh.getKhachHangChinh() > 0) {
					ps.setLong(8, kh.getKhachHangChinh());
				} else {
					ps.setNull(8, Types.BIGINT); // Truyền NULL vào database nếu không có KH chính
				}
				// =====================================

				ps.setLong(9, kh.getMaKH());
				ps.executeUpdate();
			}

			conn.commit();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// =================================================================
	// HÀM XÓA KHÁCH HÀNG (XÓA MỀM)
	// =================================================================
	public boolean delete(long maKH) {
		// Cập nhật TrangThaiXoa = 1 thay vì xóa cứng để bảo toàn dữ liệu lịch sử
		String sql = "UPDATE khachhang SET TrangThaiXoa = 1 WHERE MaKH = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maKH);

			// executeUpdate() trả về số dòng bị ảnh hưởng, > 0 nghĩa là xóa thành công
			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 1. LẤY CHI TIẾT 1 KHÁCH HÀNG
	public Map<String, Object> getChiTietKhachHang(long maKH) {
		Map<String, Object> map = new HashMap<>();

		// Sử dụng LEFT JOIN phòng trường hợp khách hàng chưa xếp phòng/hợp đồng
		String sql = "SELECT kh.MaKH, kh.TenKH, kh.DiaChi, kh.GioiTinh, kh.NgaySinh, "
				+ "kh.SDT, kh.Gmail, kh.SoCCCD, p.SoPhong, hd.MaHD, "
				+ "(SELECT TenKH FROM KhachHang WHERE MaKH = hd.MaKH) AS TenKhachChinh " + "FROM KhachHang kh "
				+ "LEFT JOIN Phong p ON kh.MaPhong = p.MaPhong "
				+ "LEFT JOIN HopDong hd ON p.MaPhong = hd.MaPhong AND hd.TrangThaiXoa = 0 " + "WHERE kh.MaKH = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, maKH);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					map.put("MaKH", rs.getLong("MaKH"));
					map.put("TenKH", rs.getString("TenKH"));
					map.put("DiaChi", rs.getString("DiaChi"));
					map.put("GioiTinh", rs.getBoolean("GioiTinh")); // boolean (0/1)
					map.put("NgaySinh", rs.getDate("NgaySinh"));
					map.put("SDT", rs.getString("SDT"));
					map.put("Gmail", rs.getString("Gmail"));
					map.put("SoCCCD", rs.getString("SoCCCD"));
					map.put("SoPhong", rs.getString("SoPhong"));
					map.put("MaHD", rs.getString("MaHD"));
					map.put("TenKhachChinh", rs.getString("TenKhachChinh"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	// 2. LẤY KHÁCH HÀNG PHỤ THUỘC (Ở chung phòng, ngoại trừ người đang xem)
	public List<Map<String, Object>> getKhachHangPhuThuocTheoPhong(String maPhong, long maKHHienTai) {
		List<Map<String, Object>> list = new ArrayList<>();
		if (maPhong == null || maPhong.isEmpty()) {
			return list;
		}

		String sql = "SELECT MaKH, TenKH, DiaChi, GioiTinh, NgaySinh, SDT "
				+ "FROM KhachHang WHERE MaPhong = ? AND MaKH != ? AND TrangThaiXoa = 0";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, maPhong);
			ps.setLong(2, maKHHienTai);
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
