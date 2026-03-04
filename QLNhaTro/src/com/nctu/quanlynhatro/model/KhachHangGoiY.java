package com.nctu.quanlynhatro.model;

public class KhachHangGoiY {
	private long maHD;
	private long maKH;
	private String tenKH;

	public KhachHangGoiY(long maHD, long maKH, String tenKH) {
		this.maHD = maHD;
		this.maKH = maKH;
		this.tenKH = tenKH;
	}

	public long getMaHD() {
		return maHD;
	}

	public long getMaKH() {
		return maKH;
	}

	public String getTenKH() {
		return tenKH;
	}

	@Override
	public String toString() {
		return tenKH + " - " + maKH + " (HĐ: " + maHD + ")";
	}
}