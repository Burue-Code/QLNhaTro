package com.nctu.quanlynhatro.model;

public class PhuongThucThanhToan {
    private long maPT;
    private String tenPT;
    
    public PhuongThucThanhToan() {
    	
    }
    
    public PhuongThucThanhToan(long maPT, String tenPT) {
    	this.maPT = maPT;
    	this.tenPT = tenPT;
    }

    public long getMaPT() {
        return maPT;
    }

    public void setMaPT(long maPT) {
        this.maPT = maPT;
    }

    public String getTenPT() {
        return tenPT;
    }

    public void setTenPT(String tenPT) {
        this.tenPT = tenPT;
    }
}