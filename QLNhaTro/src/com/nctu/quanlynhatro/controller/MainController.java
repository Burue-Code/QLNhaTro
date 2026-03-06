package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;

import com.nctu.quanlynhatro.view.DashboardView;
import com.nctu.quanlynhatro.view.dien_nuoc.DienNuocView;
import com.nctu.quanlynhatro.view.gia_dien_nuoc.GiaDienNuocView;
import com.nctu.quanlynhatro.view.hoa_don.HoaDonView;
import com.nctu.quanlynhatro.view.hop_dong.HopDongView;
import com.nctu.quanlynhatro.view.khach_hang.KhachHangView;
import com.nctu.quanlynhatro.view.nha_tro.NhaTroView;
import com.nctu.quanlynhatro.view.phong.PhongView;
import com.nctu.quanlynhatro.view.phu_phi.PhuPhiView;
import com.nctu.quanlynhatro.view.phuong_thuc_tt.PhuongThucThanhToanView;
import com.nctu.quanlynhatro.view.thong_ke.ThongKeDoanhThuView;

public class MainController implements ActionListener {

	private DashboardView view;
	private Map<JMenuItem, String> menuMap = new HashMap<>();

	public MainController(DashboardView view) {
		this.view = view;
		this.view.addMenuListener(this);
		initCards();
		initMenuMap();
	}

	public void showMainView() {
		view.setVisible(true);
	}

	private void initCards() {

		ThongKeDoanhThuView thongKeDoanhThuView = new ThongKeDoanhThuView();
		new ThongKeDoanhThuController(thongKeDoanhThuView);

		NhaTroView nhaTroView = new NhaTroView();
		new NhaTroController(nhaTroView);

		PhuPhiView phuPhiView = new PhuPhiView();
		new PhuPhiController(phuPhiView);

		HopDongView hopDongView = new HopDongView();
		new HopDongController(hopDongView);

		HoaDonView hoaDonView = new HoaDonView();
		new HoaDonController(hoaDonView);

		KhachHangView khachHangView = new KhachHangView();
		new KhachHangController(khachHangView);

		PhongView phongView = new PhongView();
		new PhongController(phongView);

		PhuongThucThanhToanView phuongThucThanhToanView = new PhuongThucThanhToanView();
		new PhuongThucTTController(phuongThucThanhToanView);

		DienNuocView dienNuocView = new DienNuocView();
		new DienNuocController(dienNuocView);

		view.addCard("VIEW_THONGKEDOANHTHU", thongKeDoanhThuView);
		view.addCard("VIEW_NHATRO", nhaTroView);
		view.addCard("VIEW_PHUPHI", phuPhiView);
		view.addCard("VIEW_HOPDONG", hopDongView);
		view.addCard("VIEW_HOADON", hoaDonView);
		view.addCard("VIEW_KHACHHANG", khachHangView);
		view.addCard("VIEW_PHONG", phongView);
		view.addCard("VIEW_PHUONGTHUCTHANHTOAN", phuongThucThanhToanView);
		view.addCard("VIEW_DIENNUOC", dienNuocView);

	}

	private void initMenuMap() {
		menuMap.put(view.getMniThongKeDoanhThu(), "VIEW_THONGKEDOANHTHU");
		menuMap.put(view.getMniPhuPhi(), "VIEW_PHUPHI");
		menuMap.put(view.getMniDienNuoc(), "VIEW_DIENNUOC");
		menuMap.put(view.getMniGiaDienNuoc(), "VIEW_GIADIENNUOC");
		menuMap.put(view.getMniHoaDon(), "VIEW_HOADON");
		menuMap.put(view.getMniHopDong(), "VIEW_HOPDONG");
		menuMap.put(view.getMniKhachHang(), "VIEW_KHACHHANG");
		menuMap.put(view.getMniNhaTro(), "VIEW_NHATRO");
		menuMap.put(view.getMniPhong(), "VIEW_PHONG");
		menuMap.put(view.getMniPhuongThucTT(), "VIEW_PHUONGTHUCTHANHTOAN");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == view.getMniGiaDienNuoc()) {
			openGiaDienNuocDialog();
			return;
		}

		String card = menuMap.get(source);
		if (card != null) {
			view.showCard(card);
		}
	}

	private void openGiaDienNuocDialog() {
		GiaDienNuocView dialog = new GiaDienNuocView(view);
		new GiaDienNuocController(dialog);
		dialog.setVisible(true);
	}
}