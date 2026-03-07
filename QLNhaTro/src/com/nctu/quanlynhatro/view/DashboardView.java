package com.nctu.quanlynhatro.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class DashboardView extends JFrame {

	private JPanel cardsContainer;
	private CardLayout cardLayout;

	private JMenuItem mniNhaTro, mniPhong, mniKhachHang;
	private JMenuItem mniHopDong, mniHoaDon, mniDienNuoc;
	private JMenuItem mniThongKeDoanhThu;
	private JMenuItem mniPhuPhi, mniPhuongThucTT, mniGiaDienNuoc;

	public DashboardView() {
		setTitle("Trang chủ - Quản lý Nhà Trọ");
		setSize(1000, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setupMenu();
		setupPanels();
	}

	private void setupMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu dm = new JMenu("Danh mục");
		mniNhaTro = new JMenuItem("Nhà trọ");
		mniPhong = new JMenuItem("Phòng");
		mniKhachHang = new JMenuItem("Khách hàng");

		dm.add(mniNhaTro);
		dm.add(mniPhong);
		dm.add(mniKhachHang);

		JMenu nv = new JMenu("Nghiệp vụ");
		mniHopDong = new JMenuItem("Hợp đồng");
		mniHoaDon = new JMenuItem("Hóa đơn");
		mniDienNuoc = new JMenuItem("Điện - Nước");

		nv.add(mniHopDong);
		nv.add(mniHoaDon);
		nv.add(mniDienNuoc);

		JMenu tk = new JMenu("Thống kê");
		mniThongKeDoanhThu = new JMenuItem("Doanh thu");
		tk.add(mniThongKeDoanhThu);

		JMenu cd = new JMenu("Cài Đặt");
		mniPhuPhi = new JMenuItem("Cấu hình Phụ Phí");
		mniPhuongThucTT = new JMenuItem("Cấu hình Phương Thức Thanh Toán");
		mniGiaDienNuoc = new JMenuItem("Cấu hình Giá Điện Nước");
		cd.add(mniPhuPhi);
		cd.add(mniPhuongThucTT);
		cd.add(mniGiaDienNuoc);
		menuBar.add(dm);
		menuBar.add(nv);
		menuBar.add(tk);
		menuBar.add(cd);

		setJMenuBar(menuBar);
	}

	private void setupPanels() {
		cardLayout = new CardLayout();
		cardsContainer = new JPanel(cardLayout);

		add(cardsContainer, BorderLayout.CENTER);
	}

	public void addMenuListener(ActionListener listener) {
		mniNhaTro.addActionListener(listener);
		mniPhong.addActionListener(listener);
		mniKhachHang.addActionListener(listener);

		mniHopDong.addActionListener(listener);
		mniHoaDon.addActionListener(listener);
		mniDienNuoc.addActionListener(listener);

		mniThongKeDoanhThu.addActionListener(listener);

		mniPhuPhi.addActionListener(listener);
		mniPhuongThucTT.addActionListener(listener);
		mniGiaDienNuoc.addActionListener(listener);
	}

	public void addCard(String name, JPanel panel) {
		cardsContainer.add(panel, name);
	}

	public void showCard(String key) {
		cardLayout.show(cardsContainer, key);
	}

	public JMenuItem getMniNhaTro() {
		return mniNhaTro;
	}

	public JMenuItem getMniPhong() {
		return mniPhong;
	}

	public JMenuItem getMniKhachHang() {
		return mniKhachHang;
	}

	public JMenuItem getMniHopDong() {
		return mniHopDong;
	}

	public JMenuItem getMniHoaDon() {
		return mniHoaDon;
	}

	public JMenuItem getMniDienNuoc() {
		return mniDienNuoc;
	}

	public JMenuItem getMniThongKeDoanhThu() {
		return mniThongKeDoanhThu;
	}

	public JMenuItem getMniPhuPhi() {
		return mniPhuPhi;
	}

	public JMenuItem getMniPhuongThucTT() {
		return mniPhuongThucTT;
	}

	public JMenuItem getMniGiaDienNuoc() {
		return mniGiaDienNuoc;
	}
}
