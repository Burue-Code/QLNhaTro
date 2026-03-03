package com.nctu.quanlynhatro.model;
import java.util.List;

public class GiaDienNuoc {
    private long maGiaDN;
    private String giaDien;
    private String giaNuoc;

    
    private List<GiaDienNuoc> danhSachGiaDienNuoc;
    
    
    public GiaDienNuoc() {
 
    }
    
    public GiaDienNuoc(long maGiaDN, String giaDien, String giaNuoc) {
    	this.maGiaDN = maGiaDN;
    	this.giaDien = giaDien;
    	this.giaNuoc = giaNuoc;
    }

    public long getMaGiaDN() {
        return maGiaDN;
    }

    public void setMaGiaDN(long maGiaDN) {
        this.maGiaDN = maGiaDN;
    }

    public String getGiaDien() {
        return giaDien;
    }

    public void setGiaDien(String giaDien) {
        this.giaDien = giaDien;
    }

    public String getGiaNuoc() {
        return giaNuoc;
    }

    public void setGiaNuoc(String giaNuoc) {
        this.giaNuoc = giaNuoc;
    }


    public List<GiaDienNuoc> getDanhSachGiaDN() {
        return danhSachGiaDienNuoc;
    }

    public void setDanhSachGiaDN(List<GiaDienNuoc> danhSachGiaDienNuoc) {
        this.danhSachGiaDienNuoc = danhSachGiaDienNuoc;
    }
}