package com.nctu.quanlynhatro.model;

import java.time.LocalDate;
import java.util.List;

public class HopDong {
    private long maHD;
    private LocalDate ngayLap;
    private LocalDate ngayKetThuc;
    private double giaThue;
    private int soNguoiO;
    private String trangThaiHD;
    private String ghiChu;
    private String tenKH;

    private Phong phong;
    private List<KhachHang> danhSachKhachHang;
    private List<HoaDon> danhSachHoaDon;
    
    public HopDong() {
    	
    }

    public HopDong(long maHD,String tenKH, LocalDate ngayLap, LocalDate ngayKT, double giaThue, int soNguoiO, String trangThaiHD, String ghiChu) {
    	this.maHD = maHD;
    	this.tenKH = tenKH;
    	this.ngayLap = ngayLap;
    	this.ngayKetThuc = ngayKT;
    	this.giaThue = giaThue;
    	this.soNguoiO = soNguoiO;
    	this.trangThaiHD = trangThaiHD;
    	this.ghiChu = ghiChu;
    }
    
    public long getMaHD() {
        return maHD;
    }

    public void setMaHD(long maHD) {
        this.maHD = maHD;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public double getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(double giaThue) {
        this.giaThue = giaThue;
    }

    public int getSoNguoiO() {
        return soNguoiO;
    }

    public void setSoNguoiO(int soNguoiO) {
        this.soNguoiO = soNguoiO;
    }

    public String getTrangThai() {
        return trangThaiHD;
    }

    public void setTrangThai(String trangThaiHD) {
        this.trangThaiHD = trangThaiHD;
    }
    
    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getTenKH() {
        return tenKH;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }

    public List<KhachHang> getDanhSachKhachHang() {
        return danhSachKhachHang;
    }

    public void setDanhSachKhachHang(List<KhachHang> danhSachKhachHang) {
        this.danhSachKhachHang = danhSachKhachHang;
    }

    public List<HoaDon> getDanhSachHoaDon() {
        return danhSachHoaDon;
    }

    public void setDanhSachHoaDon(List<HoaDon> danhSachHoaDon) {
        this.danhSachHoaDon = danhSachHoaDon;
    }
}
