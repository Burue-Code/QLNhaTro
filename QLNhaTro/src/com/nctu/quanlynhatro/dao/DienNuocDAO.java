package com.nctu.quanlynhatro.dao;

import com.nctu.quanlynhatro.model.PhieuDienNuoc;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DienNuocDAO {
	private Connection conn;

    public DienNuocDAO(Connection conn) {
        this.conn = conn;
    }

    public List<PhieuDienNuoc> getAll() {
        List<PhieuDienNuoc> list = new ArrayList<>();
        String sql = "SELECT dn.*, p.SoPhong " +
                "FROM phieudiennuoc dn " +
                "INNER JOIN phong p ON dn.MaPhong = p.MaPhong " +
                "WHERE dn.TrangThaiXoa = 0";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            	PhieuDienNuoc pdn = new PhieuDienNuoc(
                        rs.getLong("MaDN"),
                        rs.getDate("ThangNam").toLocalDate(),
                        rs.getFloat("ChiSoDienCu"),
                        rs.getFloat("ChiSoDienMoi"),
                        rs.getFloat("ChiSoNuocCu"),
                        rs.getFloat("ChiSoNuocMoi"),
                        rs.getDouble("TienDien"),
                        rs.getDouble("TienNuoc"),
                        rs.getDouble("GiaDienTaiThoiDiem"),
                        rs.getDouble("GiaNuocTaiThoiDiem"),
                        rs.getDouble("TongTien"),
                        rs.getString("TrangThaiDN")
                    );
            	
            	pdn.getPhong().setSoPhong(rs.getInt("SoPhong"));
            	pdn.getPhong().setMaPhong(rs.getLong("MaPhong"));
            	list.add(pdn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public float[] getChiSoCu(int maPhong) {

        String sql = """
            SELECT ChiSoDienMoi, ChiSoNuocMoi
            FROM phieudiennuoc
            WHERE MaPhong = ?
            ORDER BY ThangNam DESC
            LIMIT 1
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maPhong);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new float[]{
                    rs.getFloat("ChiSoDienMoi"),
                    rs.getFloat("ChiSoNuocMoi")
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new float[]{0, 0};
    }
    
    public boolean isExists(Date thangNam, int maPhong) {

        String sql = "SELECT 1 FROM phieudiennuoc WHERE ThangNam = ? AND MaPhong = ? AND TrangThaiXoa = 0";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(thangNam.getTime()));
            ps.setInt(2, maPhong);
            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean insert(PhieuDienNuoc p) {

        String sql = """
            INSERT INTO phieudiennuoc
            (ThangNam, ChiSoDienCu, ChiSoDienMoi, ChiSoNuocCu, ChiSoNuocMoi,
             TienDien, TienNuoc, GiaDienTaiThoiDiem, GiaNuocTaiThoiDiem,
             TongTien, TrangThaiDN, MaPhong, TrangThaiXoa, MaGiaDN)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?)
        """;

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(p.getThangNam().getMonthValue()));
            ps.setFloat(2, p.getChiSoDienCu());
            ps.setFloat(3, p.getChiSoDienMoi());
            ps.setFloat(4, p.getChiSoNuocCu());
            ps.setFloat(5, p.getChiSoNuocMoi());
            ps.setDouble(6, p.getTienDien());
            ps.setDouble(7, p.getTienNuoc());
            ps.setDouble(8, p.getGiaDienTaiThoiDiem());
            ps.setDouble(9, p.getGiaNuocTaiThoiDiem());
            ps.setDouble(10, p.getTongTien());
            ps.setString(11, "Chưa thanh toán");
//            ps.setInt(12, p.getMaPhong());
//            ps.setInt(13, p.getMaGiaDN());

            ps.executeUpdate();
            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
//    public PhieuDienNuoc getById(long maDN) {
//        String sql = "SELECT * FROM phieudiennuoc WHERE TrangThaiXoa = 0 AND MaDN = ?";
//        PhieuDienNuoc pdn = null;
//
//        try (Connection con = DatabaseConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setLong(1, maDN);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                pdn = new PhieuDienNuoc();
//                pdn.setMaDN(maDN);
//                pdn.setMaPhong(rs.getLong("MaPhong"));
//                pdn.setThangNam(rs.getDate("ThangNam").toLocalDate());
//
//                pdn.setChiSoDienCu(rs.getFloat("ChiSoDienCu"));
//                pdn.setChiSoDienMoi(rs.getFloat("ChiSoDienMoi"));
//                pdn.setChiSoNuocCu(rs.getFloat("ChiSoNuocCu"));
//                pdn.setChiSoNuocMoi(rs.getFloat("ChiSoNuocMoi"));
//
//                pdn.setGiaDienTaiThoiDiem(rs.getBigDecimal("GiaDienTaiThoiDiem"));
//                pdn.setGiaNuocTaiThoiDiem(rs.getBigDecimal("GiaNuocTaiThoiDiem"));
//
//                pdn.setTienDien(rs.getBigDecimal("TienDien"));
//                pdn.setTienNuoc(rs.getBigDecimal("TienNuoc"));
//                pdn.setTongTien(rs.getBigDecimal("TongTien"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return pdn;
//    }
//    
//    public boolean update(PhieuDienNuoc p) {
//        String sql = """
//            UPDATE phieudiennuoc SET
//                ThangNam = ?,
//                ChiSoDienCu = ?,
//                ChiSoDienMoi = ?,
//                ChiSoNuocCu = ?,
//                ChiSoNuocMoi = ?,
//                TienDien = ?,
//                TienNuoc = ?,
//                GiaDienTaiThoiDiem = ?,
//                GiaNuocTaiThoiDiem = ?,
//                TongTien = ?
//            WHERE MaDN = ?
//        """;
//
//        try (Connection con = DatabaseConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setDate(1, java.sql.Date.valueOf(p.getThangNam()));
//            ps.setFloat(2, p.getChiSoDienCu());
//            ps.setFloat(3, p.getChiSoDienMoi());
//            ps.setFloat(4, p.getChiSoNuocCu());
//            ps.setFloat(5, p.getChiSoNuocMoi());
//            ps.setBigDecimal(6, p.getTienDien());
//            ps.setBigDecimal(7, p.getTienNuoc());
//            ps.setBigDecimal(8, p.getGiaDienTaiThoiDiem());
//            ps.setBigDecimal(9, p.getGiaNuocTaiThoiDiem());
//            ps.setBigDecimal(10, p.getTongTien());
//            ps.setLong(11, p.getMaDN());
//
//            return ps.executeUpdate() > 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public Set<String> getAllThangNam() {
//        Set<String> set = new HashSet<>();
//        String sql = "SELECT DISTINCT DATE_FORMAT(ThangNam, '%Y/%m') FROM phieudiennuoc WHERE TrangThaiXoa = 0";
//
//        try (Connection con = DatabaseConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                set.add(rs.getString(1));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return set;
//    }
}
