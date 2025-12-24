package com.nctu.quanlynhatro.model;

import java.util.List;

public class Phong {
    private long maPhong;
    private int soPhong;
    private double gia;
    private int soNguoiToiDa;
    private double phuThu;
    private String trangThaiPhong;
    private boolean baoTri;
    private String ghiChu;

    private HopDong hopDong;
    private List<PhuPhi> danhSachPhuPhi;

    public long getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(long maPhong) {
        this.maPhong = maPhong;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoNguoiToiDa() {
        return soNguoiToiDa;
    }

    public void setSoNguoiToiDa(int soNguoiToiDa) {
        this.soNguoiToiDa = soNguoiToiDa;
    }

    public double getPhuThu() {
        return phuThu;
    }

    public void setPhuThu(double phuThu) {
        this.phuThu = phuThu;
    }

    public String getTrangThaiPhong() {
        return trangThaiPhong;
    }

    public void setTrangThaiPhong(String trangThaiPhong) {
        this.trangThaiPhong = trangThaiPhong;
    }

    public boolean isBaoTri() {
        return baoTri;
    }

    public void setBaoTri(boolean baoTri) {
        this.baoTri = baoTri;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public HopDong getHopDong() {
        return hopDong;
    }

    public void setHopDong(HopDong hopDong) {
        this.hopDong = hopDong;
    }

    public List<PhuPhi> getDanhSachPhuPhi() {
        return danhSachPhuPhi;
    }

    public void setDanhSachPhuPhi(List<PhuPhi> danhSachPhuPhi) {
        this.danhSachPhuPhi = danhSachPhuPhi;
    }
}