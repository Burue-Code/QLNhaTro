package com.nctu.quanlynhatro.model;

import java.time.LocalDate;
import java.time.YearMonth;

public class PhieuDienNuoc {
    private long maDN;
    private LocalDate thangNam; // DB là kiểu DATE
    private float chiSoDienCu, chiSoDienMoi;
    private float chiSoNuocCu, chiSoNuocMoi;
    private double tienDien, tienNuoc, tongTien;
    private double giaDienTaiThoiDiem, giaNuocTaiThoiDiem;
    private String trangThaiDN; // DB là varchar(50)
    private boolean trangThaiXoa; // DB là bit(1)
    private Phong phong;

    public PhieuDienNuoc() {
        this.phong = new Phong();
    }

    // Constructor đầy đủ khớp với các cột chính trong DB
    public PhieuDienNuoc(long maDN, LocalDate thangNam, float chiSoDienCu, float chiSoDienMoi, 
                        float chiSoNuocCu, float chiSoNuocMoi, double tienDien, double tienNuoc, 
                        double giaDien, double giaNuoc, double tongTien, String trangThaiDN) {
        this.phong = new Phong();
        this.maDN = maDN;
        this.thangNam = thangNam;
        this.chiSoDienCu = chiSoDienCu;
        this.chiSoDienMoi = chiSoDienMoi;
        this.chiSoNuocCu = chiSoNuocCu;
        this.chiSoNuocMoi = chiSoNuocMoi;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.giaDienTaiThoiDiem = giaDien;
        this.giaNuocTaiThoiDiem = giaNuoc;
        this.tongTien = tongTien;
        this.trangThaiDN = trangThaiDN;
    }

    public long getMaDN() {
        return maDN;
    }

    public void setMaDN(long maDN) {
        this.maDN = maDN;
    }

    public LocalDate getThangNam() {
        return thangNam;
    }

    public void setThangNam(LocalDate thangNam) {
        this.thangNam = thangNam;
    }

    public float getChiSoDienCu() {
        return chiSoDienCu;
    }

    public void setChiSoDienCu(float chiSoDienCu) {
        this.chiSoDienCu = chiSoDienCu;
    }

    public float getChiSoDienMoi() {
        return chiSoDienMoi;
    }

    public void setChiSoDienMoi(float chiSoDienMoi) {
        this.chiSoDienMoi = chiSoDienMoi;
    }

    public float getChiSoNuocCu() {
        return chiSoNuocCu;
    }

    public void setChiSoNuocCu(float chiSoNuocCu) {
        this.chiSoNuocCu = chiSoNuocCu;
    }

    public double getChiSoNuocMoi() {
        return chiSoNuocMoi;
    }

    public void setChiSoNuocMoi(float chiSoNuocMoi) {
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
    
    public double getGiaDienTaiThoiDiem() {
        return giaDienTaiThoiDiem;
    }
    
    public void setGiaDienTaiThoiDiem(double giaDienTaiThoiDiem) {
        this.giaDienTaiThoiDiem = giaDienTaiThoiDiem;
    }
    
    public double getGiaNuocTaiThoiDiem() {
        return giaNuocTaiThoiDiem;
    }
    
    public void setGiaNuocTaiThoiDiem(double giaNuocTaiThoiDiem) {
        this.giaNuocTaiThoiDiem = giaNuocTaiThoiDiem;
    }
    
    public String getTrangThaiDN() {
        return trangThaiDN;
    }
    
    public void setTrangThaiDN(String trangThaiDN) {
        this.trangThaiDN = trangThaiDN;
    }
    
    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }
}