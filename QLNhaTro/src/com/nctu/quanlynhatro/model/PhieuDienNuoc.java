package com.nctu.quanlynhatro.model;

import java.time.YearMonth;

public class PhieuDienNuoc {
    private long maDN;
    private YearMonth thangNam;
    private double chiSoDienCu;
    private double chiSoDienMoi;
    private double chiSoNuocCu;
    private double chiSoNuocMoi;
    private double tienDien;
    private double tienNuoc;
    private double tongTien;

    public long getMaDN() {
        return maDN;
    }

    public void setMaDN(long maDN) {
        this.maDN = maDN;
    }

    public YearMonth getThangNam() {
        return thangNam;
    }

    public void setThangNam(YearMonth thangNam) {
        this.thangNam = thangNam;
    }

    public double getChiSoDienCu() {
        return chiSoDienCu;
    }

    public void setChiSoDienCu(double chiSoDienCu) {
        this.chiSoDienCu = chiSoDienCu;
    }

    public double getChiSoDienMoi() {
        return chiSoDienMoi;
    }

    public void setChiSoDienMoi(double chiSoDienMoi) {
        this.chiSoDienMoi = chiSoDienMoi;
    }

    public double getChiSoNuocCu() {
        return chiSoNuocCu;
    }

    public void setChiSoNuocCu(double chiSoNuocCu) {
        this.chiSoNuocCu = chiSoNuocCu;
    }

    public double getChiSoNuocMoi() {
        return chiSoNuocMoi;
    }

    public void setChiSoNuocMoi(double chiSoNuocMoi) {
        this.chiSoNuocMoi = chiSoNuocMoi;
    }

    public double getTienDien() {
        return tienDien;
    }

    public void setTienDien(double tienDien) {
        this.tienDien = tienDien;
    }

    public double getTienNuoc() {
        return tienNuoc;
    }

    public void setTienNuoc(double tienNuoc) {
        this.tienNuoc = tienNuoc;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}