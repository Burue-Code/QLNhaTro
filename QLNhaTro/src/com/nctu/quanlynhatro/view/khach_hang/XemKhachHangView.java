package com.nctu.quanlynhatro.view.khach_hang;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class XemKhachHangView extends JDialog {

	private JTextField txtTenKH;
	private JTextField txtNgaySinh;
	private JTextField txtGioiTinh;
	private JTextField txtSDT;
	private JTextField txtDiaChi;

	private JTextField txtCCCD;
	private JTextField txtGmail;
	private JTextField txtOPHong;
	private JTextField txtThuocHopDong;
	private JTextField txtTenKhachHangChinh;

	private JTable tblKhachHangPhuThuoc;
	private DefaultTableModel modelPhuThuoc;

	private JButton btnThoat;
	private JButton btnSua;
	private JButton btnXoa;

	public XemKhachHangView(Frame owner) {
		super(owner, "Xem Hồ Sơ Khách Hàng", true);

		setSize(920, 520);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		JPanel pnlTop = new JPanel(new GridBagLayout());
		GridBagConstraints gbcTop = new GridBagConstraints();
		gbcTop.fill = GridBagConstraints.BOTH;
		gbcTop.weightx = 1.0;
		gbcTop.weighty = 0;

		JPanel colLeft = buildLeftColumn();
		JPanel colRight = buildRightColumn();

		gbcTop.gridx = 0;
		gbcTop.gridy = 0;
		gbcTop.insets = new Insets(0, 0, 0, 0);
		pnlTop.add(colLeft, gbcTop);

		gbcTop.gridx = 1;
		gbcTop.gridy = 0;
		gbcTop.insets = new Insets(0, 20, 0, 0);
		pnlTop.add(colRight, gbcTop);

		contentPane.add(pnlTop, BorderLayout.NORTH);

		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.setBorder(new TitledBorder("Khách Hàng Phụ Thuộc"));

		String[] headers = { "MaKH", "Tên Khách Hàng", "Địa Chỉ", "Giới Tính", "Ngày Sinh", "Số Điện Thoại" };
		modelPhuThuoc = new DefaultTableModel(headers, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		tblKhachHangPhuThuoc = new JTable(modelPhuThuoc);
		tblKhachHangPhuThuoc.setRowHeight(26);
		tblKhachHangPhuThuoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblKhachHangPhuThuoc.setEnabled(false);

		JScrollPane sp = new JScrollPane(tblKhachHangPhuThuoc);
		pnlCenter.add(sp, BorderLayout.CENTER);

		contentPane.add(pnlCenter, BorderLayout.CENTER);

		JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

		btnSua = new JButton("Sửa Hồ Sơ");
		btnXoa = new JButton("Xóa Khách Hàng");
		btnThoat = new JButton("Thoát");

		Dimension btnSize = new Dimension(130, 35);
		btnSua.setPreferredSize(btnSize);
		btnXoa.setPreferredSize(btnSize);
		btnThoat.setPreferredSize(btnSize);

		pnlBottom.add(btnSua);
		pnlBottom.add(btnXoa);
		pnlBottom.add(btnThoat);

		contentPane.add(pnlBottom, BorderLayout.SOUTH);
	}

	private JPanel buildLeftColumn() {
		JPanel pnl = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = baseGbc();

		gbc.gridy = 0;
		pnl.add(labelAbove("Tên Khách Hàng"), gbc);
		gbc.gridy = 1;
		txtTenKH = viewOnlyField();
		pnl.add(txtTenKH, gbc);

		gbc.gridy = 2;
		gbc.insets = new Insets(16, 0, 0, 0);
		JPanel row2 = new JPanel(new GridLayout(1, 2, 20, 0));
		row2.add(stack("Ngày Sinh", txtNgaySinh = viewOnlyField()));
		row2.add(stack("Giới Tính", txtGioiTinh = viewOnlyField()));
		pnl.add(row2, gbc);

		gbc.gridy = 3;
		gbc.insets = new Insets(16, 0, 0, 0);
		pnl.add(labelAbove("Số Điện Thoại"), gbc);
		gbc.gridy = 4;
		gbc.insets = new Insets(6, 0, 0, 0);
		txtSDT = viewOnlyField();
		pnl.add(txtSDT, gbc);

		gbc.gridy = 5;
		gbc.insets = new Insets(16, 0, 0, 0);
		pnl.add(labelAbove("Địa Chỉ"), gbc);
		gbc.gridy = 6;
		gbc.insets = new Insets(6, 0, 0, 0);
		txtDiaChi = viewOnlyField();
		pnl.add(txtDiaChi, gbc);

		return pnl;
	}

	private JPanel buildRightColumn() {
		JPanel pnl = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = baseGbc();

		gbc.gridy = 0;
		pnl.add(labelAbove("Căn Cước Công Dân"), gbc);
		gbc.gridy = 1;
		txtCCCD = viewOnlyField();
		pnl.add(txtCCCD, gbc);

		gbc.gridy = 2;
		gbc.insets = new Insets(16, 0, 0, 0);
		pnl.add(labelAbove("Gmail"), gbc);
		gbc.gridy = 3;
		gbc.insets = new Insets(6, 0, 0, 0);
		txtGmail = viewOnlyField();
		pnl.add(txtGmail, gbc);

		gbc.gridy = 4;
		gbc.insets = new Insets(16, 0, 0, 0);
		JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
		row.add(stack("Ở Phòng", txtOPHong = viewOnlyField()));
		row.add(stack("Thuộc Hợp Đồng", txtThuocHopDong = viewOnlyField()));
		pnl.add(row, gbc);

		gbc.gridy = 5;
		gbc.insets = new Insets(16, 0, 0, 0);
		pnl.add(labelAbove("Tên Khách Hàng Chính"), gbc);
		gbc.gridy = 6;
		gbc.insets = new Insets(6, 0, 0, 0);
		txtTenKhachHangChinh = viewOnlyField();
		pnl.add(txtTenKhachHangChinh, gbc);

		return pnl;
	}

	private GridBagConstraints baseGbc() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(0, 0, 0, 0);
		return gbc;
	}

	private JLabel labelAbove(String text) {
		JLabel lbl = new JLabel(text);
		lbl.setBorder(new EmptyBorder(0, 2, 4, 0));
		return lbl;
	}

	private JPanel stack(String label, JComponent field) {
		JPanel p = new JPanel(new BorderLayout(0, 6));
		p.add(new JLabel(label), BorderLayout.NORTH);
		p.add(field, BorderLayout.CENTER);
		return p;
	}

	private JTextField viewOnlyField() {
		JTextField tf = new JTextField();
		tf.setPreferredSize(new Dimension(0, 32));
		tf.setEditable(false);
		tf.setFocusable(false);
		tf.setBackground(new Color(240, 240, 240));
		return tf;
	}

	public JTextField getTxtTenKH() {
		return txtTenKH;
	}

	public JTextField getTxtNgaySinh() {
		return txtNgaySinh;
	}

	public JTextField getTxtGioiTinh() {
		return txtGioiTinh;
	}

	public JTextField getTxtSDT() {
		return txtSDT;
	}

	public JTextField getTxtDiaChi() {
		return txtDiaChi;
	}

	public JTextField getTxtCCCD() {
		return txtCCCD;
	}

	public JTextField getTxtGmail() {
		return txtGmail;
	}

	public JTextField getTxtOPHong() {
		return txtOPHong;
	}

	public JTextField getTxtThuocHopDong() {
		return txtThuocHopDong;
	}

	public JTextField getTxtTenKhachHangChinh() {
		return txtTenKhachHangChinh;
	}

	public DefaultTableModel getModelPhuThuoc() {
		return modelPhuThuoc;
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

	public void setTxtTenKH(String text) {
		this.txtTenKH.setText(text);
	}

	public void setTxtNgaySinh(String text) {
		this.txtNgaySinh.setText(text);
	}

	public void setTxtGioiTinh(String text) {
		this.txtGioiTinh.setText(text);
	}

	public void setTxtSDT(String text) {
		this.txtSDT.setText(text);
	}

	public void setTxtDiaChi(String text) {
		this.txtDiaChi.setText(text);
	}

	public void setTxtCCCD(String text) {
		this.txtCCCD.setText(text);
	}

	public void setTxtGmail(String text) {
		this.txtGmail.setText(text);
	}

	public void setTxtOPHong(String text) {
		this.txtOPHong.setText(text);
	}

	public void setTxtThuocHopDong(String text) {
		this.txtThuocHopDong.setText(text);
	}

	public void setTxtTenKhachHangChinh(String text) {
		this.txtTenKhachHangChinh.setText(text);
	}
}