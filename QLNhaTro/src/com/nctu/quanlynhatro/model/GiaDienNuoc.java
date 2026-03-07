package com.nctu.quanlynhatro.model;

public class GiaDienNuoc {
	private long maGiaDN;
	private double giaDien;
	private double giaNuoc;
	private boolean trangThaiXoa;

	public GiaDienNuoc() {
	}

	public GiaDienNuoc(long maGiaDN, double giaDien, double giaNuoc) {
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

	public double getGiaDien() {
		return giaDien;
	}

	public void setGiaDien(double giaDien) {
		this.giaDien = giaDien;
	}

	public double getGiaNuoc() {
		return giaNuoc;
	}

	public void setGiaNuoc(double giaNuoc) {
		this.giaNuoc = giaNuoc;
	}

	public boolean isTrangThaiXoa() {
		return trangThaiXoa;
	}

	public void setTrangThaiXoa(boolean trangThaiXoa) {
		this.trangThaiXoa = trangThaiXoa;
	}
}