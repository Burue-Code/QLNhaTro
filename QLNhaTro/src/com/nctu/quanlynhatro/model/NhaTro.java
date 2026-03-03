package com.nctu.quanlynhatro.model;

import java.util.List;

public class NhaTro {
    private long maNT;
    private String tenNT;
    private String diaChi;
    private int slPhong;
    private String trangThaiNT;
    private String ghiChu;

    
    private List<Phong> danhSachPhong;
    
    
    public NhaTro() {
 
    }
    
    public NhaTro(long maNT, String tenNT, String diaChi, int slPhong, String trangThaiNT, String ghiChu) {
    	this.maNT = maNT;
    	this.tenNT = tenNT;
    	this.diaChi = diaChi;
    	this.slPhong = slPhong;
    	this.trangThaiNT = trangThaiNT;
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

    public int getSLPhong() {
        return slPhong;
    }

    public void setSLPhong(int slPhong) {
        this.slPhong = slPhong;
    }

    public String getTrangThaiNT() {
        return trangThaiNT;
    }

    public void setTrangThaiNT(String trangThaiNT) {
        this.trangThaiNT = trangThaiNT;
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