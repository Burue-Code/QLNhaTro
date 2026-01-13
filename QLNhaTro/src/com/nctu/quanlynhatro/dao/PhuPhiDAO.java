package com.nctu.quanlynhatro.dao;
import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.model.PhuPhi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhuPhiDAO {

    public List<PhuPhi> getAll() {
        List<PhuPhi> list = new ArrayList<>();
        String sql = "SELECT MaPP, TenPP, Gia FROM phuphi WHERE TrangThaiXoa = 0";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new PhuPhi(
                        rs.getLong("MaPP"),
                        rs.getString("TenPP"),
                        rs.getDouble("Gia")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
