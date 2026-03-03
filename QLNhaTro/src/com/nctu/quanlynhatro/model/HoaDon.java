package com.nctu.quanlynhatro.model;

import java.time.LocalDateTime;

public class HoaDon {
    private long maHoaDon;
    private LocalDateTime ngayThanhToan;
    private double tongTien;
    private double tongTienPP;
    private String loaiThanhToan;
    private String ghiChu;
    
    private HopDong hopDong;
    private PhieuDienNuoc phieuDienNuoc;
    private PhuongThucThanhToan phuongThucThanhToan;
    
    public HoaDon() {
    	this.hopDong = new HopDong();
    	this.phuongThucThanhToan = new PhuongThucThanhToan();
    }
    
    public HoaDon(long maHoaDon, LocalDateTime ngayTT, double tongTien, double tongTienPP, String loaiTT, String ghiChu) {
    	this.hopDong = new HopDong();
    	this.phuongThucThanhToan = new PhuongThucThanhToan();
    	this.maHoaDon = maHoaDon;
    	this.ngayThanhToan = ngayTT;
    	this.tongTien = tongTien;
    	this.tongTienPP = tongTienPP;
    	this.loaiThanhToan = loaiTT;
    	this.ghiChu = ghiChu;
    }

    public long getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(long maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public LocalDateTime getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
    
    public double getTongTienPP() {
        return tongTienPP;
    }

    public void setTongTienPP(double tongTienPP) {
        this.tongTienPP = tongTienPP;
    }

    public String getLoaiThanhToan() {
        return loaiThanhToan;
    }

    public void setLoaiThanhToan(String loaiThanhToan) {
        this.loaiThanhToan = loaiThanhToan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public PhieuDienNuoc getPhieuDienNuoc() {
        return phieuDienNuoc;
    }

    public void setPhieuDienNuoc(PhieuDienNuoc phieuDienNuoc) {
        this.phieuDienNuoc = phieuDienNuoc;
    }

    public PhuongThucThanhToan getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(PhuongThucThanhToan phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }
    
    public HopDong getHopDong() {
        return hopDong;
    }

    public void setHopDong(HopDong hopDong) {
        this.hopDong = hopDong;
    }
}