package com.nctu.quanlynhatro.dao;

import com.nctu.quanlynhatro.model.PhieuDienNuoc;
import java.sql.*;
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
}
