package com.nctu.quanlynhatro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nctu.quanlynhatro.model.HoaDon;
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

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                HoaDon hd = new HoaDon(
                    rs.getLong("MaHoaDon"),
                    rs.getTimestamp("NgayTT").toLocalDateTime(),
                    rs.getDouble("SoTienTT"),
                    rs.getDouble("TongTienPP"),
                    rs.getString("LoaiTT"),
                    rs.getString("GhiChu")
                );

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

}
