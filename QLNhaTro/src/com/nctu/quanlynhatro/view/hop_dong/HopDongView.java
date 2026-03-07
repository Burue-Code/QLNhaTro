package com.nctu.quanlynhatro.view.hop_dong;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.nctu.quanlynhatro.controller.HopDongController;
import com.nctu.quanlynhatro.view.component.MyLabel;
import com.nctu.quanlynhatro.view.component.MyScrollTable;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.component.MyTextField;

public class HopDongView extends JPanel {

	private MyTable tblHopDong;
	private DefaultTableModel tableModel;
	private JPopupMenu popupMenu;
	private JMenuItem mnuThem, mnuSua, mnuXoa, mnuLamMoi;

	private MyTextField txtTimKiem;
	private TableRowSorter<DefaultTableModel> rowSorter;

	public HopDongView() {

		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

		MyLabel lblTitle = new MyLabel("DANH SÁCH HỢP ĐỒNG THUÊ PHÒNG TRỌ", MyLabel.HEADER, SwingConstants.CENTER);
		pnlNorth.add(lblTitle, BorderLayout.NORTH);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		MyLabel lblTim = new MyLabel("Tìm kiếm: ");
		txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....", 300, 35);

		searchPanel.add(lblTim);
		searchPanel.add(txtTimKiem);
		pnlNorth.add(searchPanel, BorderLayout.SOUTH);
		add(pnlNorth, BorderLayout.NORTH);

		String[] headers = { "MaHD", "Tên Khách Hàng", "Ngày Lập Hợp Đồng", "Ngày Kết Thúc Hợp Đồng", "Giá Thuê",
				"Số Người Ở", "Trạng Thái Hợp Đồng", "Ghi Chú" };

		tblHopDong = new MyTable(headers);
		MyScrollTable scrollTable = new MyScrollTable(tblHopDong, "");
		add(scrollTable, BorderLayout.CENTER);

		new HopDongController(this);

	}

	public MyTable getTable() {
		return tblHopDong;
	}

	public MyTextField getTxtTimKiem() {
		return txtTimKiem;
	}
}