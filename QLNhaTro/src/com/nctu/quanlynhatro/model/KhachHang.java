package com.nctu.quanlynhatro.model;

import java.time.LocalDate;

public class KhachHang {
    private long maKH;
    private String tenKH;
    private String diaChi;
    private boolean gioiTinh;
    private LocalDate ngaySinh;
    private String sdt;
    private String gmail;
    private String cccd;
    private long khachHangChinh;
    private Phong phong;
    
    
    public KhachHang() {
    	this.phong = new Phong();
    }
    
    public KhachHang(long maKH, String tenKH, String diaChi, boolean gioiTinh, LocalDate ngaySinh, String std, String gmail, String cccd, long maKHC) {
    	this.phong = new Phong();
    	this.maKH = maKH;
    	this.tenKH = tenKH;
    	this.diaChi = diaChi;
    	this.gioiTinh = gioiTinh;
    	this.ngaySinh = ngaySinh;
    	this.sdt = std;
    	this.gmail = gmail;
    	this.cccd = cccd;
    	this.khachHangChinh = maKHC;
    }

    public long getMaKH() {
        return maKH;
    }

    public void setMaKH(long maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public boolean getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }
    
    public long getKhachHangChinh() {
        return khachHangChinh;
    }

    public void setKhachHangChinh(long khachHangChinh) {
        this.khachHangChinh = khachHangChinh;
    }
    
    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }

}
