package com.nctu.quanlynhatro.dao;
import com.nctu.quanlynhatro.dao.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class NhaTroDAO {

    public Map<Integer, String> getNhaTroConCho() {
        Map<Integer, String> map = new HashMap<>();

        String sql = """
            SELECT nt.MaNT, nt.TenNT
            FROM nhatro nt
            WHERE nt.TrangThaiXoa = 0
            AND (
                SELECT COUNT(*)
                FROM phong p
                WHERE p.MaNT = nt.MaNT AND p.TrangThaiXoa = 0
            ) < nt.SLPhong
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getInt("MaNT"), rs.getString("TenNT"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
