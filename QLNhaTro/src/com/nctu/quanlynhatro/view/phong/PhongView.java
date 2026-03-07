package com.nctu.quanlynhatro.view.phong;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.nctu.quanlynhatro.view.component.MyLabel;
import com.nctu.quanlynhatro.view.component.MyScrollTable;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.component.MyTextField;

public class PhongView extends JPanel {

	private MyTextField txtTimKiem;
	private JCheckBox chkDaThue, chkConTrong, chkBaoTri;
	private MyTable tblPhong;

	public PhongView() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

		MyLabel lblTitle = new MyLabel("DANH SÁCH PHÒNG", MyLabel.HEADER, SwingConstants.CENTER);
		pnlNorth.add(lblTitle, BorderLayout.NORTH);

		JPanel pnlControl = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		MyLabel lblTim = new MyLabel("Tìm kiếm: ");
		txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....", 300, 35);

		searchPanel.add(lblTim);
		searchPanel.add(txtTimKiem);
		pnlNorth.add(searchPanel, BorderLayout.SOUTH);
		add(pnlNorth, BorderLayout.NORTH);

		JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));

		chkDaThue = new JCheckBox("Đã thuê");
		chkDaThue.setFocusable(false);
		chkDaThue.setFont(new Font("Arial", Font.PLAIN, 13));

		chkConTrong = new JCheckBox("Còn trống");
		chkConTrong.setFocusable(false);
		chkConTrong.setFont(new Font("Arial", Font.PLAIN, 13));

		chkBaoTri = new JCheckBox("Phòng bảo trì");
		chkBaoTri.setFocusable(false);
		chkBaoTri.setFont(new Font("Arial", Font.PLAIN, 13));

		pnlFilter.add(chkDaThue);
		pnlFilter.add(chkConTrong);
		pnlFilter.add(chkBaoTri);

		gbc.weightx = 0.0;
		gbc.gridx = 0;
		pnlControl.add(searchPanel, gbc);
		gbc.weightx = 1.0;
		gbc.gridx = 1;
		pnlControl.add(pnlFilter, gbc);

		pnlNorth.add(pnlControl, BorderLayout.SOUTH);

		add(pnlNorth, BorderLayout.NORTH);

		String[] headers = { "MaPhong", "Số Phòng", "Giá", "Số Người Ở Tối Đa", "Phụ Thu", "Trạng Thái Phòng",
				"Ghi Chú" };

		tblPhong = new MyTable(headers);
		MyScrollTable scrollTable = new MyScrollTable(tblPhong, "");

		add(scrollTable, BorderLayout.CENTER);

	}

	public MyTable getTable() {
		return tblPhong;
	}

	public MyTextField getTxtTimKiem() {
		return txtTimKiem;
	}

	public JCheckBox getChkDaThue() {
		return chkDaThue;
	}

	public JCheckBox getChkConTrong() {
		return chkConTrong;
	}

	public JCheckBox getChkBaoTri() {
		return chkBaoTri;
	}
}