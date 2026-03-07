package com.nctu.quanlynhatro.view.phu_phi;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.nctu.quanlynhatro.view.component.MyLabel;
import com.nctu.quanlynhatro.view.component.MyScrollTable;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.component.MyTextField;

public class PhuPhiView extends JPanel {

	private MyTextField txtTimKiem;
	private MyTable tblPhuPhi;

	public PhuPhiView() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

		MyLabel lblTitle = new MyLabel("DANH SÁCH PHỤ PHÍ", MyLabel.HEADER, SwingConstants.CENTER);
		pnlNorth.add(lblTitle, BorderLayout.NORTH);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		MyLabel lblTim = new MyLabel("Tìm kiếm: ");
		txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....", 300, 35);

		searchPanel.add(lblTim);
		searchPanel.add(txtTimKiem);
		pnlNorth.add(searchPanel, BorderLayout.SOUTH);
		add(pnlNorth, BorderLayout.NORTH);

		String[] headers = { "MaPP", "Tên Phụ Phí", "Giá" };

		tblPhuPhi = new MyTable(headers);
		MyScrollTable scrollTable = new MyScrollTable(tblPhuPhi, "");

		add(scrollTable, BorderLayout.CENTER);

	}

	public MyTable getTable() {
		return tblPhuPhi;
	}

	public MyTextField getTxtTimKiem() {
		return txtTimKiem;
	}
}
