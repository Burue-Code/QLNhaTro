package com.nctu.quanlynhatro.model;

public class PhuPhi {
    private long maPP;
    private String tenPP;
    private double gia;

    public PhuPhi(long maPP, String tenPP, double gia) {
    	this.maPP = maPP;
    	this.tenPP = tenPP;
    	this.gia = gia;
    }
    public long getMaPP() {
        return maPP;
    }

    public void setMaPP(long maPP) {
        this.maPP = maPP;
    }

    public String getTenPP() {
        return tenPP;
    }

    public void setTenPP(String tenPP) {
        this.tenPP = tenPP;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }
}
