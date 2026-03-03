package com.nctu.quanlynhatro.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

		String insertHD = """
				    INSERT INTO HopDong(MaKH, MaPhong, NgayLapHD, NgayKT, GiaThue, SoNguoiO, GhiChu, TrangThaiHD, TrangThaiXoa)
				    VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)
				""";

		String updateKHPhu = "UPDATE KhachHang SET MaKHChinh = ?, MaPhong = ? WHERE MaKH = ?";
		String updateKHChinh = "UPDATE KhachHang SET MaPhong = ? WHERE MaKH = ?";

		// 🔥 SỬA 1: Cập nhật luôn trạng thái phòng thành 'Đã thuê'
		String updatePhong = "UPDATE Phong SET MaHD = ?, TrangThaiPhong = 'Đã thuê' WHERE MaPhong = ?";

		try {
			conn.setAutoCommit(false); // 🔥 Bắt đầu Transaction

			// 1. Kiểm tra hợp đồng tồn tại
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

			// 2. Thêm Hợp Đồng mới
			// 🔥 LƯU Ý: Phải có Statement.RETURN_GENERATED_KEYS để lấy ID tự tăng
			try (PreparedStatement psInsert = conn.prepareStatement(insertHD, Statement.RETURN_GENERATED_KEYS)) {
				psInsert.setLong(1, maKH);
				psInsert.setInt(2, maPhong);

				// 🔥 SỬA 2: Kiểm tra Null cho ngày tháng để tránh lỗi crash app
				if (ngayBD != null) {
					psInsert.setDate(3, java.sql.Date.valueOf(ngayBD));
				} else {
					psInsert.setNull(3, java.sql.Types.DATE);
				}

				if (ngayKT != null) {
					psInsert.setDate(4, java.sql.Date.valueOf(ngayKT));
				} else {
					psInsert.setNull(4, java.sql.Types.DATE);
				}

				psInsert.setDouble(5, giaThue);
				psInsert.setInt(6, soNguoiO);
				psInsert.setString(7, ghiChu);
				psInsert.setString(8, trangThai);

				psInsert.executeUpdate();

				// Lấy ID (Mã Hợp Đồng) vừa tự động sinh ra
				try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						maHDMoi = generatedKeys.getInt(1);
					} else {
						// Nếu database không để MaHD là AUTO_INCREMENT thì sẽ vào đây
						throw new SQLException(
								"Thêm hợp đồng thất bại, không lấy được ID. Kiểm tra lại DB cột MaHD đã Auto Increment chưa.");
					}
				}
			}

			// 3. Cập nhật danh sách Khách hàng phụ
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

			// 4. Cập nhật Phòng cho Khách hàng chính
			try (PreparedStatement psUpdateKHChinh = conn.prepareStatement(updateKHChinh)) {
				psUpdateKHChinh.setInt(1, maPhong);
				psUpdateKHChinh.setLong(2, maKH);
				psUpdateKHChinh.executeUpdate();
			}

			// 5. Cập nhật Mã Hợp Đồng & Trạng thái vào bảng Phòng
			try (PreparedStatement psUpdatePhong = conn.prepareStatement(updatePhong)) {
				psUpdatePhong.setInt(1, maHDMoi);
				psUpdatePhong.setInt(2, maPhong);
				psUpdatePhong.executeUpdate();
			}

			// 6. Commit nếu tất cả đều lọt qua an toàn
			conn.commit();
			return "SUCCESS";

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				conn.rollback(); // 🔥 Quay xe nếu có lỗi
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			return "Lỗi hệ thống: " + ex.getMessage();

		} finally {
			try {
				conn.setAutoCommit(true); // Trả lại trạng thái mặc định
			} catch (SQLException closeEx) {
				closeEx.printStackTrace();
			}
		}
	}

	// =================================================================
	// 1. LẤY CHI TIẾT HỢP ĐỒNG (KÈM THÔNG TIN PHÒNG & KHÁCH)
	// =================================================================
	public HopDong getHopDongById(long maHD) {
		HopDong hd = null;
		String sql = "SELECT * FROM HopDong WHERE MaHD = ?";

		try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maHD);
			try (java.sql.ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					hd = new HopDong();
					hd.setMaHD(rs.getLong("MaHD"));

					java.sql.Date d1 = rs.getDate("NgayLapHD");
					if (d1 != null) {
						hd.setNgayLap(d1.toLocalDate());
					}

					java.sql.Date d2 = rs.getDate("NgayKT");
					if (d2 != null) {
						hd.setNgayKetThuc(d2.toLocalDate());
					}

					hd.setGiaThue(rs.getDouble("GiaThue"));
					hd.setSoNguoiO(rs.getInt("SoNguoiO"));
					hd.setGhiChu(rs.getString("GhiChu"));
					hd.setTrangThai(rs.getString("TrangThaiHD"));

					// --- 1. LẤY THÔNG TIN PHÒNG ---
					int maPhong = rs.getInt("MaPhong");
					PhongDAO phongDao = new PhongDAO(conn);
					// Hàm này trả về 1 đối tượng Phong
					hd.setPhong(phongDao.getPhongById(maPhong));

					// --- 2. LẤY THÔNG TIN KHÁCH HÀNG CHÍNH (QUAN TRỌNG) ---
					long maKH = rs.getLong("MaKH");
					KhachHangDAO khDao = new KhachHangDAO(conn);

					// [SỬA LỖI]: Gọi hàm getKhachHangById (trả về 1 người)
					// KHÔNG ĐƯỢC gọi getKhachHangByPhong (vì nó trả về List -> gây lỗi Cast)
					KhachHang khChinh = khDao.getKhachHangById(maKH);
					hd.setTenKH(khChinh != null ? khChinh.getTenKH() : "Không xác định");

					// --- 3. LẤY DANH SÁCH NGƯỜI Ở CÙNG ---
					// Hàm này trả về List, gán vào biến List là đúng
					java.util.List<KhachHang> listKH = khDao.getKhachHangByPhong(maPhong);
					hd.setDanhSachKhachHang(listKH);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hd;
	}

	// =================================================================
	// CẬP NHẬT HỢP ĐỒNG (TRANSACTION)
	// =================================================================
	public String updateHopDong(long maHD, long maKHChinh, int maPhongMoi, int maPhongCu, LocalDate ngayBD,
			LocalDate ngayKT, double giaThue, int soNguoi, String ghiChu, java.util.List<Long> listMaKHPhu) {
		try {
			conn.setAutoCommit(false); // Bắt đầu Transaction

			// 1. Cập nhật bảng HopDong
			String sqlUpdateHD = "UPDATE HopDong SET MaKH=?, MaPhong=?, NgayLapHD=?, NgayKT=?, GiaThue=?, SoNguoiO=?, GhiChu=? WHERE MaHD=?";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sqlUpdateHD)) {
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

			// 2. Xử lý ĐỔI PHÒNG (Nếu người dùng chọn phòng khác)
			if (maPhongMoi != maPhongCu) {
				// Phòng cũ: Trả về trạng thái TRỐNG, Xóa MaHD
				String sqlOld = "UPDATE Phong SET TrangThaiPhong='Trống', MaHD=NULL WHERE MaPhong=?";
				try (java.sql.PreparedStatement psOld = conn.prepareStatement(sqlOld)) {
					psOld.setInt(1, maPhongCu);
					psOld.executeUpdate();
				}

				// Phòng mới: Set ĐÃ THUÊ, Gán MaHD
				String sqlNew = "UPDATE Phong SET TrangThaiPhong='Đã thuê', MaHD=? WHERE MaPhong=?";
				try (java.sql.PreparedStatement psNew = conn.prepareStatement(sqlNew)) {
					psNew.setLong(1, maHD);
					psNew.setInt(2, maPhongMoi);
					psNew.executeUpdate();
				}

				// Cập nhật lại MaPhong cho TOÀN BỘ khách hàng cũ (để họ sang phòng mới)
				// Lưu ý: Logic C# của bạn có đoạn reset rồi update lại, ở đây mình gộp cho gọn
				String sqlMoveKH = "UPDATE KhachHang SET MaPhong=? WHERE MaPhong=?";
				try (java.sql.PreparedStatement psMove = conn.prepareStatement(sqlMoveKH)) {
					psMove.setInt(1, maPhongMoi);
					psMove.setInt(2, maPhongCu);
					psMove.executeUpdate();
				}
			}

			// 3. Cập nhật danh sách người ở ghép (KhachHangPhu)
			// Logic: Reset tất cả người ở phòng này về KHÔNG PHỤ THUỘC trước (như code C#
			// của bạn)

			// Bước 3.1: Reset khách phụ cũ (Những người đang phụ thuộc vào chủ hộ này)
			String sqlReset = "UPDATE KhachHang SET MaKHChinh=NULL WHERE MaKHChinh=?";
			try (java.sql.PreparedStatement psReset = conn.prepareStatement(sqlReset)) {
				psReset.setLong(1, maKHChinh);
				psReset.executeUpdate();
			}

			// Bước 3.2: Gán khách phụ mới từ danh sách trên giao diện
			if (listMaKHPhu != null && !listMaKHPhu.isEmpty()) {
				String sqlUpdatePhu = "UPDATE KhachHang SET MaKHChinh=?, MaPhong=? WHERE MaKH=?";
				try (java.sql.PreparedStatement psPhu = conn.prepareStatement(sqlUpdatePhu)) {
					for (Long maPhu : listMaKHPhu) {
						psPhu.setLong(1, maKHChinh); // Phụ thuộc vào ông chủ mới
						psPhu.setInt(2, maPhongMoi); // Ở phòng mới
						psPhu.setLong(3, maPhu);
						psPhu.executeUpdate();
					}
				}
			}

			// Bước 3.3: Đảm bảo ông chủ hộ cũng ở đúng phòng và không phụ thuộc ai
			String sqlUpdateMain = "UPDATE KhachHang SET MaPhong=?, MaKHChinh=NULL WHERE MaKH=?";
			try (java.sql.PreparedStatement psMain = conn.prepareStatement(sqlUpdateMain)) {
				psMain.setInt(1, maPhongMoi);
				psMain.setLong(2, maKHChinh);
				psMain.executeUpdate();
			}

			// Bước 3.4: Đảm bảo Phòng mới trỏ đúng Hợp đồng (Phòng bị)
			String sqlUpdatePhongHD = "UPDATE Phong SET MaHD=? WHERE MaPhong=?";
			try (java.sql.PreparedStatement psP = conn.prepareStatement(sqlUpdatePhongHD)) {
				psP.setLong(1, maHD);
				psP.setInt(2, maPhongMoi);
				psP.executeUpdate();
			}

			conn.commit();
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
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

	// =================================================================
	// XÓA HỢP ĐỒNG (XÓA MỀM + TRẢ PHÒNG + TRẢ KHÁCH)
	// =================================================================
	public boolean deleteHopDong(long maHD) {
		try {
			conn.setAutoCommit(false); // Bắt đầu Transaction

			// 1. Lấy MaPhong và MaKH từ Hợp đồng sắp xóa để xử lý trả phòng/khách
			int maPhong = 0;
			// (Bạn có thể select ra trước nếu cần, hoặc dùng lệnh Update trực tiếp bên
			// dưới)

			// 2. Cập nhật trạng thái Hợp Đồng -> Đã xóa (TrangThaiXoa = 1)
			String sqlDeleteHD = "UPDATE HopDong SET TrangThaiXoa = 1, TrangThaiHD = 'Đã hủy' WHERE MaHD = ?";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sqlDeleteHD)) {
				ps.setLong(1, maHD);
				ps.executeUpdate();
			}

			// 3. Trả Phòng: Set Phong về trạng thái 'Còn trống', MaHD = NULL
			// (Chỉ update những phòng đang gắn với hợp đồng này)
			String sqlUpdatePhong = "UPDATE Phong SET MaHD = NULL, TrangThaiPhong = 'Còn trống' WHERE MaHD = ?";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sqlUpdatePhong)) {
				ps.setLong(1, maHD);
				ps.executeUpdate();
			}

			// 4. Trả Khách: Những khách hàng thuộc Hợp đồng này sẽ bị xóa liên kết phòng
			// (Dựa vào việc join hoặc subquery tìm khách đang ở phòng của hợp đồng này)
			// Cách đơn giản nhất: Update KhachHang set MaPhong = NULL where MaPhong =
			// (Phòng của HĐ này)
			// Để an toàn, ta dùng query lồng:
			String sqlUpdateKhach = """
					UPDATE KhachHang
					SET MaPhong = NULL, MaKHChinh = NULL
					WHERE MaPhong IN (SELECT MaPhong FROM Phong WHERE MaHD = ?)
					""";
			// Lưu ý: Do bước 3 đã set MaHD=NULL, nên bước 4 này có thể không tìm thấy nếu
			// chạy sau bước 3.
			// => GIẢI PHÁP: Nên Select MaPhong ra trước bước 2.

			// --- SỬA LẠI LOGIC CHUẨN ---

			// B1: Lấy MaPhong của hợp đồng
			String sqlGetPhong = "SELECT MaPhong FROM HopDong WHERE MaHD = ?";
			try (java.sql.PreparedStatement psGet = conn.prepareStatement(sqlGetPhong)) {
				psGet.setLong(1, maHD);
				java.sql.ResultSet rs = psGet.executeQuery();
				if (rs.next()) {
					maPhong = rs.getInt("MaPhong");
				}
			}

			// B2: Reset Khách Hàng (Dựa theo MaPhong vừa lấy)
			if (maPhong > 0) {
				String sqlResetKH = "UPDATE KhachHang SET MaPhong = NULL, MaKHChinh = NULL WHERE MaPhong = ?";
				try (java.sql.PreparedStatement psKH = conn.prepareStatement(sqlResetKH)) {
					psKH.setInt(1, maPhong);
					psKH.executeUpdate();
				}
			}

			// B3: Reset Phòng
			String sqlResetPhong = "UPDATE Phong SET MaHD = NULL, TrangThaiPhong = 'Còn trống' WHERE MaHD = ?";
			try (java.sql.PreparedStatement psP = conn.prepareStatement(sqlResetPhong)) {
				psP.setLong(1, maHD);
				psP.executeUpdate();
			}

			// B4: Xóa Hợp đồng
			try (java.sql.PreparedStatement psHD = conn.prepareStatement(sqlDeleteHD)) {
				psHD.setLong(1, maHD);
				psHD.executeUpdate();
			}

			conn.commit(); // Xác nhận thành công
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
			} // Quay lui nếu lỗi
			return false;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception ex) {
			}
		}
	}
}
