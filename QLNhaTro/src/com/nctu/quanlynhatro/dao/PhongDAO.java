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
import com.nctu.quanlynhatro.model.Phong;

public class PhongDAO {

	private Connection conn;

	public PhongDAO(Connection conn) {
		this.conn = conn;
	}

	public List<Phong> getAll() {
		List<Phong> list = new ArrayList<>();
		String sql = "SELECT * FROM phong WHERE TrangThaiXoa = 0";

		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Phong p = new Phong(rs.getLong("MaPhong"), rs.getInt("SoPhong"), rs.getDouble("Gia"),
						rs.getInt("SLNguoiMax"), rs.getDouble("PhuThu"), rs.getString("TrangThaiPhong"),
						rs.getString("GhiChu"), rs.getInt("BaoTri") == 1);
				NhaTro nt = new NhaTro();
				nt.setMaNT(rs.getInt("MaNT"));
				p.setNhaTro(nt);

				list.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Trả về Map chứa Mã Phòng và Số Phòng dựa vào Mã Nhà Trọ
	public Map<Integer, String> getPhongTrongByMaNT(int maNT) {
		Map<Integer, String> map = new HashMap<>();

		// Chỉ lấy những phòng thuộc Nhà trọ được chọn, chưa bị xóa và đang TRỐNG
		String sql = "SELECT MaPhong, SoPhong FROM phong WHERE MaNT = ? AND TrangThaiXoa = 0 AND TrangThaiPhong = 'Còn Trống'";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maNT);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					map.put(rs.getInt("MaPhong"), rs.getString("SoPhong"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	// Lấy thông tin chi tiết phòng để tính tiền
	public Phong getThongTinPhong(int maPhong) {
		// SQL lấy đúng tên cột trong Database
		String sql = "SELECT MaPhong, SoPhong, Gia, SLNguoiMax, PhuThu FROM Phong WHERE MaPhong = ?";
		Phong p = null;

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, maPhong);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					p = new Phong();
					// Map dữ liệu khớp với Model Phong của bạn
					p.setMaPhong(rs.getLong("MaPhong"));
					p.setSoPhong(rs.getInt("SoPhong"));
					p.setGia(rs.getDouble("Gia")); // Cột Gia -> setGia
					p.setSoNguoiToiDa(rs.getInt("SLNguoiMax")); // Cột SLNguoiMax -> setSoNguoiToiDa
					p.setPhuThu(rs.getDouble("PhuThu")); // Cột PhuThu -> setPhuThu
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	// =================================================================
	// LẤY CHI TIẾT PHÒNG KÈM THÔNG TIN NHÀ TRỌ
	// (Dùng cho Sửa Hợp Đồng để set ComboBox Nhà Trọ)
	// =================================================================
	public Phong getPhongById(long maPhong) {
		// Cần JOIN sang bảng NhaTro để lấy TenNhaTro và MaNT
		String sql = "SELECT p.*, n.TenNT, n.MaNT FROM Phong p " + "JOIN NhaTro n ON p.MaNT = n.MaNT "
				+ "WHERE p.MaPhong = ?";
		Phong p = null;
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maPhong);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					p = new Phong();
					p.setMaPhong(rs.getLong("MaPhong"));
					p.setSoPhong(rs.getInt("SoPhong"));
					p.setGia(rs.getDouble("Gia"));
					p.setSoNguoiToiDa(rs.getInt("SLNguoiMax"));
					p.setPhuThu(rs.getDouble("PhuThu"));

					// Map thông tin Nhà Trọ vào đối tượng Phong
					NhaTro nt = new NhaTro();
					nt.setMaNT(rs.getInt("MaNT")); // Lấy từ bảng NhaTro hoặc Phong đều được
					nt.setTenNT(rs.getString("TenNT")); // Lấy từ bảng NhaTro
					p.setNhaTro(nt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	/* ================= UPDATE TRẠNG THÁI PHÒNG ================= */
	public void updateTrangThaiPhong() {
		String sql = """
				    UPDATE phong
				    SET TrangThaiPhong = CASE
				        WHEN BaoTri = 1 THEN 'Phòng bảo trì'
				        WHEN MaHD IS NULL OR MaHD = '' THEN 'Phòng trống'
				        ELSE 'Đã thuê'
				    END
				""";

		try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* ================= TÌM KIẾM ================= */
	public List<Phong> search(String keyword, boolean daThue, boolean conTrong, boolean baoTri) {

		List<Phong> list = new ArrayList<>();
		StringBuilder sql = new StringBuilder(
				"SELECT * FROM phong WHERE TrangThaiXoa = 0 " + "AND (MaPhong LIKE ? OR SoPhong LIKE ?)");

		List<String> conditions = new ArrayList<>();
		if (daThue) {
			conditions.add("TrangThaiPhong = 'Đã thuê'");
		}
		if (conTrong) {
			conditions.add("TrangThaiPhong = 'Còn trống'");
		}
		if (baoTri) {
			conditions.add("TrangThaiPhong = 'Phòng bảo trì'");
		}

		if (!conditions.isEmpty()) {
			sql.append(" AND (").append(String.join(" OR ", conditions)).append(")");
		}

		try (Connection con = DatabaseConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql.toString())) {

			String key = "%" + keyword + "%";
			ps.setString(1, key);
			ps.setString(2, key);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Phong(rs.getLong("MaPhong"), rs.getInt("SoPhong"), rs.getDouble("Gia"),
						rs.getInt("SLNguoiMax"), rs.getDouble("PhuThu"), rs.getString("TrangThaiPhong"),
						rs.getString("GhiChu"), rs.getInt("BaoTri") == 1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/* ================= BẬT / TẮT BẢO TRÌ ================= */
	public void updateBaoTri(long maPhong, boolean baoTri) {
		String sql = "UPDATE phong SET BaoTri = ? WHERE MaPhong = ?";

		try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, baoTri ? 1 : 0);
			ps.setLong(2, maPhong);
			ps.executeUpdate();

			updateTrangThaiPhong();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* ================= XÓA MỀM ================= */
	public boolean deleteSoft(long maPhong) {
		String sql = "UPDATE phong SET TrangThaiXoa = 1 WHERE MaPhong = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, maPhong);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/* ================= KIỂM TRA TRÙNG PHÒNG ================= */
	public boolean isExistSoPhong(int soPhong, long maNT) throws SQLException {
		String sql = "SELECT 1 FROM phong WHERE SoPhong = ? AND MaNT = ? AND TrangThaiXoa = 0";
		try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, soPhong);
			ps.setLong(2, maNT);
			return ps.executeQuery().next();
		}
	}

	/* ================= THÊM PHÒNG + PHỤ PHÍ (TRANSACTION) ================= */
	public boolean insertPhong(int soPhong, double gia, int slNguoiMax, double phuThu, String trangThai, String ghiChu,
			int maNT, List<Integer> listMaPP) {

		String insertPhong = """
				    INSERT INTO phong
				    (SoPhong, Gia, SLNguoiMax, PhuThu, GhiChu, TrangThaiPhong, MaNT, TrangThaiXoa, BaoTri)
				    VALUES (?, ?, ?, ?, ?, ?, ?, 0, 0)
				""";

		String insertCTPP = "INSERT INTO chitietphuphi (MaPhong, MaPP) VALUES (?, ?)";

		try (Connection con = DatabaseConnection.getConnection()) {
			con.setAutoCommit(false);

			// kiểm tra trùng
			if (isExistSoPhong(soPhong, maNT)) {
				return false;
			}

			int maPhongMoi;
			try (PreparedStatement ps = con.prepareStatement(insertPhong, Statement.RETURN_GENERATED_KEYS)) {

				ps.setInt(1, soPhong);
				ps.setDouble(2, gia);
				ps.setInt(3, slNguoiMax);
				ps.setDouble(4, phuThu);
				ps.setString(5, ghiChu);
				ps.setString(6, trangThai);
				ps.setInt(7, maNT);
				ps.executeUpdate();

				ResultSet rs = ps.getGeneratedKeys();
				rs.next();
				maPhongMoi = rs.getInt(1);
			}

			try (PreparedStatement psCT = con.prepareStatement(insertCTPP)) {
				for (int maPP : listMaPP) {
					psCT.setInt(1, maPhongMoi);
					psCT.setInt(2, maPP);
					psCT.executeUpdate();
				}
			}

			con.commit();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * ========================= KIỂM TRA TRÙNG SỐ PHÒNG (SỬA)
	 * =========================
	 */
	public boolean isSoPhongExistForUpdate(long maPhong, int soPhong, int maNT) throws Exception {

		String sql = "SELECT 1 FROM Phong WHERE SoPhong = ? AND MaNT = ? AND MaPhong <> ?";

		try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, soPhong);
			ps.setInt(2, maNT);
			ps.setLong(3, maPhong);
			return ps.executeQuery().next();
		}
	}

	/*
	 * ========================= 5️⃣ UPDATE PHÒNG + DELETE/INSERT CTPP
	 * =========================
	 */
	public boolean updatePhong(Phong phong, List<Integer> dsMaPP) {

		String sqlUpdatePhong = "UPDATE Phong SET SoPhong=?, Gia=?, SLNguoiMax=?, PhuThu=?, "
				+ "GhiChu=?, TrangThaiPhong=?, MaNT=?, TrangThaiXoa=0 " + "WHERE MaPhong=?";

		String sqlDeleteCTPP = "DELETE FROM chitietphuphi WHERE MaPhong=?";

		String sqlInsertCTPP = "INSERT INTO chitietphuphi(MaPhong, MaPP) VALUES (?, ?)";

		try (Connection con = DatabaseConnection.getConnection()) {
			con.setAutoCommit(false);

			try (PreparedStatement ps = con.prepareStatement(sqlUpdatePhong)) {

				ps.setInt(1, phong.getSoPhong());
				ps.setDouble(2, phong.getGia());
				ps.setInt(3, phong.getSoNguoiToiDa());
				ps.setDouble(4, phong.getPhuThu());
				ps.setString(5, phong.getGhiChu());
				ps.setString(6, phong.getTrangThaiPhong());
				ps.setLong(7, phong.getNhaTro().getMaNT());
				ps.setLong(8, phong.getMaPhong());
				ps.executeUpdate();
			}

			try (PreparedStatement ps = con.prepareStatement(sqlDeleteCTPP)) {
				ps.setLong(1, phong.getMaPhong());
				ps.executeUpdate();
			}

			try (PreparedStatement ps = con.prepareStatement(sqlInsertCTPP)) {
				for (int maPP : dsMaPP) {
					ps.setLong(1, phong.getMaPhong());
					ps.setInt(2, maPP);
					ps.executeUpdate();
				}
			}

			con.commit();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}