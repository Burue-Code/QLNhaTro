package com.nctu.quanlynhatro.dao;

import java.sql.*;
import java.util.*;

import com.nctu.quanlynhatro.model.NhaTro;


public class NhaTroDAO {
	private Connection conn;

    public NhaTroDAO(Connection conn) {
        this.conn = conn;
    }
    
    // lấy dữ liệu nhà trọ load vào bản ở quản lý nhà trọ
    public List<NhaTro> getAll() {
        List<NhaTro> list = new ArrayList<>();
        String sql = "SELECT * FROM NhaTro WHERE TrangThaiXoa = 'False'";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            	list.add( new NhaTro(
            			rs.getLong("MaNT"),
            			rs.getString("TenNT"),
            			rs.getString("DiaChi"),
                        rs.getInt("SLPhong"),
                        rs.getNString("TrangThaiNT"),
                        rs.getString("GhiChu")
                    ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

	
    public Map<Integer, String> getNhaTroConPhong() {
        Map<Integer, String> map = new HashMap<>();

        String sql = """
            SELECT nt.MaNT, nt.TenNT
            FROM nhatro nt
            WHERE nt.TrangThaiXoa = 0 """;
//			AND (
//			      SELECT COUNT(*)
//			      FROM phong p
//			      WHERE p.MaNT = nt.MaNT AND p.TrangThaiXoa = 0
//				) < nt.SLPhong
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getInt("MaNT"), rs.getString("TenNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    
    
    /* ================= XÓA MỀM ================= */
    public boolean deleteSoft(int maNT) {
        String sql = "UPDATE nhatro SET TrangThaiXoa = 1 WHERE MaNT = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maNT);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /* ================= KIỂM TRA TRÙNG TÊN NHÀ TRỌ ================= */
    public boolean isTenNhaTroExist(String tenNT) {
        String sql = "SELECT 1 FROM nhatro WHERE TenNT = ? AND TrangThaiXoa = 0";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenNT);
            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /* ================= KIỂM TRA TRÙNG TÊN (SỬA) ================= */
    public boolean isTenNhaTroExistForUpdate(int maNT, String tenNT) {

        String sql = """
            SELECT 1 
            FROM nhatro 
            WHERE TenNT = ? AND MaNT <> ? AND TrangThaiXoa = 0
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenNT);
            ps.setInt(2, maNT);
            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /* ================= THÊM NHÀ TRỌ ================= */
    public boolean insert(String tenNT, int slPhong, String diaChi, String ghiChu, String trangThai) {

        String sql = """
            INSERT INTO nhatro
            (TenNT, SLPhong, DiaChi, GhiChu, TrangThaiNT, TrangThaiXoa)
            VALUES (?, ?, ?, ?, ?, 0)
        """;

        if (isTenNhaTroExist(tenNT)) {
            return false;
        }
        try ( PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){

                ps.setString(1, tenNT);
                ps.setInt(2, slPhong);
                ps.setString(3, diaChi);
                ps.setString(4, ghiChu);
                ps.setString(5, trangThai);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
            

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }   
    }
    
    /* ================= CẬP NHẬT NHÀ TRỌ ================= */
    public boolean update(NhaTro nt) {

        String sql = """
            UPDATE nhatro
            SET TenNT = ?, SLPhong = ?, DiaChi = ?, GhiChu = ?, TrangThaiNT = ?
            WHERE MaNT = ?
        """;

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
