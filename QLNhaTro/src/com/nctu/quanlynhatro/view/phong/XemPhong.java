package com.nctu.quanlynhatro.view.phong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class XemPhong extends JDialog {

	private JTextField txtSoPhong, txtGiaPhong, txtSoNguoi, txtPhuThu, txtTrangThai, txtGhiChu;

	private JTable tblPhuPhi, tblKhachHang;

	private JButton btnThoat;
	private JButton btnSua, btnXoa;

	public XemPhong(Frame owner) {
		super(owner, "Xem Thông Tin Phòng", true);

		setSize(950, 550);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(mainPanel);

		JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0));
		mainPanel.add(pnlCenter, BorderLayout.CENTER);

		JPanel pnlLeft = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		int h = 35;

		txtSoPhong = createReadOnlyField(h);
		txtGiaPhong = createReadOnlyField(h);
		txtSoNguoi = createReadOnlyField(h);
		txtPhuThu = createReadOnlyField(h);
		txtTrangThai = createReadOnlyField(h);
		txtGhiChu = createReadOnlyField(h);

		int y = 0;
		addRow(pnlLeft, gbc, y, "Số Phòng", txtSoPhong);
		y += 2;
		addRow(pnlLeft, gbc, y, "Giá Phòng", txtGiaPhong);
		y += 2;
		addRow(pnlLeft, gbc, y, "Số Người Ở Trong Quy Định", txtSoNguoi);
		y += 2;
		addRow(pnlLeft, gbc, y, "Phụ Thu Quá Người", txtPhuThu);
		y += 2;
		addRow(pnlLeft, gbc, y, "Trạng Thái", txtTrangThai);
		y += 2;
		addRow(pnlLeft, gbc, y, "Ghi Chú", txtGhiChu);
		y += 2;

		gbc.gridx = 0;
		gbc.gridy = y;
		gbc.weighty = 1.0;
		pnlLeft.add(new JLabel(), gbc);

		JPanel pnlRight = new JPanel();
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));

		JPanel pnlPhuPhi = new JPanel(new BorderLayout());
		pnlPhuPhi.setBorder(new TitledBorder("Phụ Phí"));
		pnlPhuPhi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

		String[] colsPP = { "MaPP", "Tên Phụ Phí", "Giá" };
		DefaultTableModel modelPP = new DefaultTableModel(colsPP, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblPhuPhi = new JTable(modelPP);
		tblPhuPhi.setRowHeight(25);
		tblPhuPhi.setShowGrid(false);
		JScrollPane spPP = new JScrollPane(tblPhuPhi);
		spPP.getViewport().setBackground(Color.WHITE);
		pnlPhuPhi.add(spPP, BorderLayout.CENTER);

		JPanel pnlKhachHang = new JPanel(new BorderLayout());
		pnlKhachHang.setBorder(new TitledBorder("Khách Hàng"));
		pnlKhachHang.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

		String[] colsKH = { "MaKH", "Tên Khách Hàng", "Địa Chỉ", "Giới Tính", "Ngày" };
		DefaultTableModel modelKH = new DefaultTableModel(colsKH, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblKhachHang = new JTable(modelKH);
		tblKhachHang.setRowHeight(25);
		tblKhachHang.setShowGrid(false);
		JScrollPane spKH = new JScrollPane(tblKhachHang);
		spKH.getViewport().setBackground(Color.WHITE);
		pnlKhachHang.add(spKH, BorderLayout.CENTER);

		pnlRight.add(pnlPhuPhi);
		pnlRight.add(Box.createVerticalStrut(10));
		pnlRight.add(pnlKhachHang);

		pnlCenter.add(pnlLeft);
		pnlCenter.add(pnlRight);

		JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		btnThoat = new JButton("Thoát");
		btnThoat.setPreferredSize(new Dimension(140, 38));
		btnThoat.setBackground(Color.WHITE);
		pnlBottom.add(btnThoat);

		mainPanel.add(pnlBottom, BorderLayout.SOUTH);

		btnSua = new JButton("Sửa Thông Tin");
		btnXoa = new JButton("Xóa Phòng");

		// Định dạng nút
		btnSua.setPreferredSize(new Dimension(130, 38));
		btnXoa.setPreferredSize(new Dimension(130, 38));

		btnXoa.setBackground(new Color(255, 235, 235));
		btnSua.setBackground(new Color(235, 245, 255));

		pnlBottom.add(btnXoa);
		pnlBottom.add(btnSua);

	}

	private static JTextField createReadOnlyField(int height) {
		JTextField tf = new JTextField();
		tf.setPreferredSize(new Dimension(0, height));
		tf.setEditable(false);
		tf.setFocusable(false);
		return tf;
	}

	private static void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field) {
		gbc.weighty = 0.0;

		gbc.gridx = 0;
		gbc.gridy = y;
		panel.add(new JLabel(label), gbc);

		gbc.gridy = y + 1;
		panel.add(field, gbc);
	}

	public JTextField getTxtSoPhong() {
		return txtSoPhong;
	}

	public JTextField getTxtGiaPhong() {
		return txtGiaPhong;
	}

	public JTextField getTxtSoNguoi() {
		return txtSoNguoi;
	}

	public JTextField getTxtPhuThu() {
		return txtPhuThu;
	}

	public JTextField getTxtTrangThai() {
		return txtTrangThai;
	}

	public JTextField getTxtGhiChu() {
		return txtGhiChu;
	}

	public JTable getTblPhuPhi() {
		return tblPhuPhi;
	}

	public JTable getTblKhachHang() {
		return tblKhachHang;
	}

	public JButton getBtnThoat() {
		return btnThoat;
	}

	public JButton getBtnSua() {
		return btnSua;
	}

	public JButton getBtnXoa() {
		return btnXoa;
	}

}