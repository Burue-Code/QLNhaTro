package com.nctu.quanlynhatro.view.dien_nuoc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.nctu.quanlynhatro.controller.DienNuocController;
import com.nctu.quanlynhatro.view.component.MyLabel;
import com.nctu.quanlynhatro.view.component.MyScrollTable;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.component.MyTextField;

public class DienNuocView extends JPanel {

	private MyTable tblDanhSach;
	private MyTextField txtTimKiem;

	public DienNuocView() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel topPanel = new JPanel(new BorderLayout(10, 10));

		MyLabel lblTitle = new MyLabel("QUẢN LÝ THU PHÍ ĐIỆN NƯỚC", MyLabel.HEADER, SwingConstants.CENTER);
		topPanel.add(lblTitle, BorderLayout.NORTH);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		MyLabel lblTim = new MyLabel("Tìm kiếm: ");
		txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....", 300, 35);

		searchPanel.add(lblTim);
		searchPanel.add(txtTimKiem);
		topPanel.add(searchPanel, BorderLayout.SOUTH);
		add(topPanel, BorderLayout.NORTH);

		String[] headers = { "Mã DN", "Số Phòng", "Thời Gian", "Giá Điện", "Giá Nước", "Tổng Tiền", "Trạng Thái" };

		tblDanhSach = new MyTable(headers);
		MyScrollTable scrollPane = new MyScrollTable(tblDanhSach, "Danh Sách Hóa Đơn");
		add(scrollPane, BorderLayout.CENTER);

		new DienNuocController(this);
	}

	public MyTable getTable() {
		return tblDanhSach;
	}

	public MyTextField getTxtTimKiem() {
		return txtTimKiem;
	}

}