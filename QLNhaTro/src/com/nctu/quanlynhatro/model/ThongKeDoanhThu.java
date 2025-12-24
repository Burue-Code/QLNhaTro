package com.nctu.quanlynhatro.model;

import java.time.LocalDate;
import java.util.List;

public class ThongKeDoanhThu {

    public double thongKeTheoThang(int thang, int nam, List<HoaDon> hoaDons) {
        return hoaDons.stream()
                .filter(hd -> hd.getNgayThanhToan().getMonthValue() == thang
                           && hd.getNgayThanhToan().getYear() == nam)
                .mapToDouble(HoaDon::getTongTien)
                .sum();
    }

    public double thongKeTheoKhoangNgay(LocalDate tuNgay, LocalDate denNgay, List<HoaDon> hoaDons) {
        return hoaDons.stream()
                .filter(hd -> !hd.getNgayThanhToan().toLocalDate().isBefore(tuNgay)
                           && !hd.getNgayThanhToan().toLocalDate().isAfter(denNgay))
                .mapToDouble(HoaDon::getTongTien)
                .sum();
    }
}