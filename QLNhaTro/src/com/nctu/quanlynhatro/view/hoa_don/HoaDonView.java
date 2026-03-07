package com.nctu.quanlynhatro.view.hoa_don;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.nctu.quanlynhatro.view.component.MyLabel;
import com.nctu.quanlynhatro.view.component.MyScrollTable;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.component.MyTextField;

public class HoaDonView extends JPanel {

	private MyTable tblHoaDon;
	private DefaultTableModel tableModel;
	private JPopupMenu popupMenu;
	private JMenuItem mnuThem, mnuSua, mnuXoa, mnuLamMoi;

	private MyTextField txtTimKiem;
	private TableRowSorter<DefaultTableModel> rowSorter;

	public HoaDonView() {

		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

		MyLabel lblTitle = new MyLabel("DANH SÁCH HÓA ĐƠN THANH TOÁN", MyLabel.HEADER, SwingConstants.CENTER);
		pnlNorth.add(lblTitle, BorderLayout.NORTH);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		MyLabel lblTim = new MyLabel("Tìm kiếm: ");
		txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....", 300, 35);

		searchPanel.add(lblTim);
		searchPanel.add(txtTimKiem);
		pnlNorth.add(searchPanel, BorderLayout.SOUTH);
		add(pnlNorth, BorderLayout.NORTH);

		String[] headers = { "Mã HĐ", "Ngày Lập", "Tổng Tiền", "Loại Thanh Toán", "Phương Thức Thanh Toán", "Ghi Chú" };

		tblHoaDon = new MyTable(headers);
		MyScrollTable scrollTable = new MyScrollTable(tblHoaDon, "");

		add(scrollTable, BorderLayout.CENTER);

	}

	public MyTable getTable() {
		return tblHoaDon;
	}

	public MyTextField getTxtTimKiem() {
		return txtTimKiem;
	}
}
