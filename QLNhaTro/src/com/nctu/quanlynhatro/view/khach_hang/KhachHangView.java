package com.nctu.quanlynhatro.view.khach_hang;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.nctu.quanlynhatro.view.component.MyLabel;
import com.nctu.quanlynhatro.view.component.MyScrollTable;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.component.MyTextField;

public class KhachHangView extends JPanel {

	private MyTable tblKhachHang;
	private MyTextField txtTimKiem;

	public KhachHangView() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		// --- KHU VỰC NORTH: TIÊU ĐỀ + TÌM KIẾM ---
		JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

		MyLabel lblTitle = new MyLabel("HỒ SƠ KHÁCH HÀNG", MyLabel.HEADER, SwingConstants.CENTER);
		pnlNorth.add(lblTitle, BorderLayout.NORTH);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		MyLabel lblTim = new MyLabel("Tìm kiếm: ");
		txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....", 300, 35); // Độ dài chuẩn

		searchPanel.add(lblTim);
		searchPanel.add(txtTimKiem);
		pnlNorth.add(searchPanel, BorderLayout.SOUTH);
		add(pnlNorth, BorderLayout.NORTH);

		// --- BẢNG DỮ LIỆU ---
		String[] headers = { "MaKH", "Tên Khách Hàng", "Địa Chỉ", "Giới Tính", "Ngày Sinh", "Số Điện Thoại", "CCCD" };

		tblKhachHang = new MyTable(headers);
		MyScrollTable scrollTable = new MyScrollTable(tblKhachHang, "");
		add(scrollTable, BorderLayout.CENTER);

	}

	public MyTable getTable() {
		return tblKhachHang;
	}

	public MyTextField getTxtTimKiem() {
		return txtTimKiem;
	}
}