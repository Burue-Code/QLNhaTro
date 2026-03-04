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

	// Thêm

	// =================================================================
	// 1. LẤY DANH SÁCH KHÁCH HÀNG GỢI Ý (KÈM MÃ HỢP ĐỒNG)
	// =================================================================
	public Map<String, String> getKhachHangGoiY() {
		Map<String, String> mapKH = new HashMap<>();
		// Query lấy Tên KH và Mã HĐ
		String sql = """
				    SELECT hd.MaHD, kh.TenKH
				    FROM HopDong hd
				    JOIN KhachHang kh ON hd.MaKH = kh.MaKH
				    WHERE hd.TrangThaiXoa = 0
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				// Key: MaHD, Value: TenKH (Hoặc ngược lại tùy nhu cầu tìm kiếm)
				mapKH.put(rs.getString("MaHD"), rs.getString("TenKH"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapKH;
	}

	// =================================================================
	// 2. LẤY THÔNG TIN HỢP ĐỒNG (NHÀ TRỌ, PHÒNG, GIÁ THUÊ...)
	// =================================================================
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

	// =================================================================
	// 3. LẤY DANH SÁCH PHỤ PHÍ THEO HỢP ĐỒNG
	// =================================================================
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

	// =================================================================
	// 4. LẤY DANH SÁCH ĐIỆN NƯỚC CHƯA THANH TOÁN (Của HĐ đó)
	// =================================================================
	public List<Map<String, Object>> getDienNuocChuaThanhToan(long maHD) {
		List<Map<String, Object>> listDN = new ArrayList<>();
		// Lưu ý: DATE_FORMAT là hàm của MySQL, nếu dùng SQL Server thì là FORMAT
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

	// =================================================================
	// 5. THÊM HÓA ĐƠN MỚI (TRANSACTION)
	// =================================================================
	public String insertHoaDon(HoaDon hd, List<Long> listMaDN_DaChon) {
		try {
			conn.setAutoCommit(false); // Bắt đầu Transaction

			// --- BƯỚC 1: INSERT BẢNG HOADON ---
			String sqlInsertHD = """
					    INSERT INTO HoaDon (NgayTT, SoTienTT, MaHD, MaPT, GhiChu, TrangThaiXoa, LoaiTT, TongTienPP)
					    VALUES (?, ?, ?, ?, ?, 0, ?, ?)
					""";

			long maHoaDonMoi = 0;

			try (PreparedStatement ps = conn.prepareStatement(sqlInsertHD, Statement.RETURN_GENERATED_KEYS)) {
				ps.setTimestamp(1, java.sql.Timestamp.valueOf(hd.getNgayThanhToan()));
				ps.setDouble(2, hd.getTongTien());
				ps.setLong(3, hd.getHopDong().getMaHD());
				ps.setLong(4, hd.getPhuongThucThanhToan().getMaPT()); // Cần thêm field này trong Model HoaDon
				ps.setString(5, hd.getGhiChu());
				ps.setString(6, hd.getLoaiThanhToan()); // "Tất Cả", "Tiền Trọ", "Điện Nước"
				ps.setDouble(7, hd.getTongTienPP());

				int affectedRows = ps.executeUpdate();
				if (affectedRows == 0) {
					throw new Exception("Insert Hóa Đơn thất bại!");
				}

				// Lấy ID tự sinh (MaHoaDon)
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						maHoaDonMoi = rs.getLong(1);
					} else {
						throw new Exception("Không lấy được ID hóa đơn mới!");
					}
				}
			}

			// --- BƯỚC 2: CẬP NHẬT MÃ HÓA ĐƠN VÀO PHIẾU ĐIỆN NƯỚC (Nếu có chọn) ---
			// Chỉ cập nhật nếu loại thanh toán KHÔNG PHẢI là "Tiền Trọ" (tức là có trả tiền
			// điện)
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

			conn.commit(); // Xác nhận thành công
			return "SUCCESS";

		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
			} // Quay lui nếu lỗi
			return "Lỗi: " + e.getMessage();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception ex) {
			}
		}
	}

	// Tìm kiếm khách hàng (cho chức năng gợi ý)
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

	// updete
	// 1. Lấy thông tin Hóa Đơn theo ID (để đổ lên form sửa)
	public HoaDon getHoaDonById(long maHD) {
		String sql = "SELECT * FROM HoaDon WHERE MaHoaDon = ?";
		try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			try (java.sql.ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					HoaDon hd = new HoaDon();
					hd.setMaHoaDon(rs.getLong("MaHoaDon"));
					hd.getHopDong().setMaHD(rs.getLong("MaHD"));

					// Lấy ngày giờ (Timestamp)
					java.sql.Timestamp ts = rs.getTimestamp("NgayTT");
					if (ts != null) {
						hd.setNgayThanhToan(ts.toLocalDateTime());
					}

					hd.setTongTien(maHD);
					hd.setTongTienPP(maHD);
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

	// 2. Lấy danh sách Điện Nước ĐÃ CÓ trong hóa đơn (để hiển thị lại)
	public List<Map<String, Object>> getDienNuocByMaHoaDon(long maHoaDon) {
		List<Map<String, Object>> listDN = new ArrayList<>();
		// Lưu ý: Cú pháp DATE_FORMAT cho MySQL, SQL Server dùng FORMAT
		String sql = "SELECT MaDN, DATE_FORMAT(ThangNam, '%m/%Y') as ThangNam, TongTien FROM PhieuDienNuoc WHERE MaHoaDon = ?";
		try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHoaDon);
			try (java.sql.ResultSet rs = ps.executeQuery()) {
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

	// 3. Helper: Lấy tên khách hàng từ mã hợp đồng
	public String getTenKhachHangByMaHD(long maHD) {
		String sql = "SELECT k.TenKH FROM KhachHang k JOIN HopDong h ON k.MaKH = h.MaKH WHERE h.MaHD = ?";
		try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			java.sql.ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("TenKH");
			}
		} catch (Exception e) {
		}
		return "";
	}

	// 4. CẬP NHẬT HÓA ĐƠN (TRANSACTION QUAN TRỌNG)
	public String updateHoaDon(HoaDon hd, List<Long> listMaDN_Moi) {
		try {
			conn.setAutoCommit(false);

			// B1: Update thông tin bảng HoaDon
			String sqlUpdateHD = "UPDATE HoaDon SET SoTienTT=?, MaPT=?, GhiChu=?, LoaiTT=?, TongTienPP=?, NgayTT=? WHERE MaHoaDon=?";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sqlUpdateHD)) {
				ps.setDouble(1, hd.getTongTien());
				ps.setLong(2, hd.getPhuongThucThanhToan().getMaPT());
				ps.setString(3, hd.getGhiChu());
				ps.setString(4, hd.getLoaiThanhToan());
				ps.setDouble(5, hd.getTongTienPP());
				ps.setTimestamp(6, java.sql.Timestamp.valueOf(hd.getNgayThanhToan()));
				ps.setLong(7, hd.getMaHoaDon());
				ps.executeUpdate();
			}

			// B2: Reset các phiếu điện nước cũ của hóa đơn này về NULL (Trả lại trạng thái
			// chưa thanh toán)
			String sqlResetDN = "UPDATE PhieuDienNuoc SET MaHoaDon = NULL WHERE MaHoaDon = ?";
			try (java.sql.PreparedStatement psReset = conn.prepareStatement(sqlResetDN)) {
				psReset.setLong(1, hd.getMaHoaDon());
				psReset.executeUpdate();
			}

			// B3: Cập nhật danh sách phiếu điện nước mới
			if (!"Tiền Trọ".equals(hd.getLoaiThanhToan()) && listMaDN_Moi != null && !listMaDN_Moi.isEmpty()) {
				String sqlUpdateDN = "UPDATE PhieuDienNuoc SET MaHoaDon = ? WHERE MaDN = ?";
				try (java.sql.PreparedStatement psDN = conn.prepareStatement(sqlUpdateDN)) {
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
			e.printStackTrace();
			return "Lỗi: " + e.getMessage();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception ex) {
			}
		}

	}

	// =================================================================
	// XÓA HÓA ĐƠN (XÓA MỀM + TRẢ LẠI PHIẾU ĐIỆN NƯỚC)
	// =================================================================
	public boolean deleteHoaDon(long maHoaDon) {
		String sqlDeleteHD = "UPDATE HoaDon SET TrangThaiXoa = 1 WHERE MaHoaDon = ?";
		String sqlResetDN = "UPDATE PhieuDienNuoc SET MaHoaDon = NULL WHERE MaHoaDon = ?";

		try {
			conn.setAutoCommit(false); // Bắt đầu Transaction

			// Bước 1: Trả tự do cho các phiếu điện nước (Gỡ MaHoaDon ra)
			try (PreparedStatement psDN = conn.prepareStatement(sqlResetDN)) {
				psDN.setLong(1, maHoaDon);
				psDN.executeUpdate();
			}

			// Bước 2: Xóa mềm hóa đơn
			try (PreparedStatement psHD = conn.prepareStatement(sqlDeleteHD)) {
				psHD.setLong(1, maHoaDon);
				int rowEffect = psHD.executeUpdate();

				if (rowEffect > 0) {
					conn.commit(); // Xác nhận
					return true;
				} else {
					conn.rollback();
					return false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
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

}
