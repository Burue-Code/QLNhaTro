package com.nctu.quanlynhatro.model;

import java.time.LocalDate;
import java.util.List;

public class NhaTro {
    private long maNT;
    private String tenNT;
    private String diaChi;
    private int soLuongPhong;
    private String trangThai;
    private String ghiChu;

    
    private List<Phong> danhSachPhong;
    
    
    public NhaTro() {
 
    }
    
    public NhaTro(long maNT, String tenNT, String diaChi, int soLuongPhong, String trangThai, String ghiChu) {
    	this.maNT = maNT;
    	this.tenNT = tenNT;
    	this.diaChi = diaChi;
    	this.soLuongPhong = soLuongPhong;
    	this.trangThai = trangThai;
    	this.ghiChu = ghiChu;
    }

    public long getMaNT() {
        return maNT;
    }

    public void setMaNT(long maNT) {
        this.maNT = maNT;
    }

    public String getTenNT() {
        return tenNT;
    }

    public void setTenNT(String tenNT) {
        this.tenNT = tenNT;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getSoLuongPhong() {
        return soLuongPhong;
    }

    public void setSoLuongPhong(int soLuongPhong) {
        this.soLuongPhong = soLuongPhong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public List<Phong> getDanhSachPhong() {
        return danhSachPhong;
    }

    public void setDanhSachPhong(List<Phong> danhSachPhong) {
        this.danhSachPhong = danhSachPhong;
    }
}