package com.nctu.quanlynhatro.view.hoa_don;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class XemHoaDonView extends JDialog {

	private JTextField txtTenKH, txtNgayThanhToan;
	private JTextField txtMaHopDong, txtNhaTro, txtPhong;
	private JTextField txtGiaThue, txtGhiChu;

	private JTextField txtHoaDonDienNuoc;
	private JTextField txtPhuongThucThanhToan;
	private JTextField txtLoaiThanhToan;

	private JTable tblPhuPhi, tblDienNuoc;
	private DefaultTableModel modelPhuPhi, modelDienNuoc;

	private JTextField txtTongTienDN, txtTongTienPhuPhi, txtTongThanhToan;
	private JButton btnDong;
	private JButton btnSua;
	private JButton btnXoa;

	public XemHoaDonView() {

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0));

		JPanel pnlLeftForm = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		addLabel(pnlLeftForm, "Tên Khách Hàng:", 0, 0);
		addLabel(pnlLeftForm, "Ngày Thanh Toán:", 1, 0);

		txtTenKH = createTextField();
		addComponent(pnlLeftForm, txtTenKH, 0, 1);

		txtNgayThanhToan = createTextField();
		txtNgayThanhToan.setText("");
		addComponent(pnlLeftForm, txtNgayThanhToan, 1, 1);

		addLabel(pnlLeftForm, "Mã Hợp Đồng:", 0, 2);
		addLabel(pnlLeftForm, "Hóa Đơn Điện Nước:", 1, 2);

		txtMaHopDong = createTextField();
		addComponent(pnlLeftForm, txtMaHopDong, 0, 3);

		txtHoaDonDienNuoc = createTextField();
		addComponent(pnlLeftForm, txtHoaDonDienNuoc, 1, 3);

		addLabel(pnlLeftForm, "Nhà Trọ:", 0, 4);
		addLabel(pnlLeftForm, "Giá Thuê:", 1, 4);

		txtNhaTro = createTextField();
		addComponent(pnlLeftForm, txtNhaTro, 0, 5);

		txtGiaThue = createTextField();
		addComponent(pnlLeftForm, txtGiaThue, 1, 5);

		addLabel(pnlLeftForm, "Phòng:", 0, 6);
		addLabel(pnlLeftForm, "Ghi Chú:", 1, 6);

		txtPhong = createTextField();
		addComponent(pnlLeftForm, txtPhong, 0, 7);

		txtGhiChu = createTextField();
		addComponent(pnlLeftForm, txtGhiChu, 1, 7);

		addLabel(pnlLeftForm, "Phương Thức Thanh Toán:", 0, 8);
		addLabel(pnlLeftForm, "Loại Thanh Toán:", 1, 8);

		txtPhuongThucThanhToan = createTextField();
		addComponent(pnlLeftForm, txtPhuongThucThanhToan, 0, 9);

		txtLoaiThanhToan = createTextField();
		addComponent(pnlLeftForm, txtLoaiThanhToan, 1, 9);

		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.weighty = 1.0;
		pnlLeftForm.add(new JLabel(), gbc);

		JPanel pnlRightTables = new JPanel(new GridLayout(2, 1, 0, 10));

		JPanel pnlPhuPhi = new JPanel(new BorderLayout());
		pnlPhuPhi.setBorder(new TitledBorder("Phụ Phí"));
		String[] colsPhuPhi = { "MaPP", "Tên Phụ Phí", "Giá" };
		modelPhuPhi = new DefaultTableModel(colsPhuPhi, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblPhuPhi = new JTable(modelPhuPhi);
		tblPhuPhi.setRowHeight(25);
		tblPhuPhi.setPreferredScrollableViewportSize(new Dimension(450, 100));
		pnlPhuPhi.add(new JScrollPane(tblPhuPhi), BorderLayout.CENTER);

		JPanel pnlDienNuoc = new JPanel(new BorderLayout());
		pnlDienNuoc.setBorder(new TitledBorder("Hóa Đơn Điện Nước"));
		String[] colsDN = { "MaDN", "Thời Gian", "Giá" };
		modelDienNuoc = new DefaultTableModel(colsDN, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblDienNuoc = new JTable(modelDienNuoc);
		tblDienNuoc.setRowHeight(25);
		tblDienNuoc.setPreferredScrollableViewportSize(new Dimension(450, 100));
		pnlDienNuoc.add(new JScrollPane(tblDienNuoc), BorderLayout.CENTER);

		pnlRightTables.add(pnlPhuPhi);
		pnlRightTables.add(pnlDienNuoc);

		pnlCenter.add(pnlLeftForm);
		pnlCenter.add(pnlRightTables);
		contentPane.add(pnlCenter, BorderLayout.CENTER);

		JPanel pnlFooter = new JPanel(new GridLayout(1, 2, 20, 0));
		pnlFooter.setBorder(new EmptyBorder(10, 0, 0, 0));

		JPanel pnlTotalContainer = new JPanel(new BorderLayout());
		pnlTotalContainer.setBorder(new TitledBorder("Tổng Tiền"));

		JPanel pnlTotalFields = new JPanel(new GridLayout(3, 1, 5, 5));
		txtTongTienDN = createTotalField();
		txtTongTienPhuPhi = createTotalField();
		txtTongThanhToan = createTotalField();

		pnlTotalFields.add(createLabeledPanel("Tổng Tiền Điện Nước:", txtTongTienDN));
		pnlTotalFields.add(createLabeledPanel("Tổng Tiền Phụ Phí:", txtTongTienPhuPhi));
		pnlTotalFields.add(createLabeledPanel("Tổng Tiền Thanh Toán:", txtTongThanhToan));

		pnlTotalContainer.add(pnlTotalFields, BorderLayout.CENTER);
		pnlFooter.add(pnlTotalContainer);

		JPanel pnlButtonsContainer = new JPanel(new BorderLayout());
		JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		btnSua = new JButton("Sửa Hóa Đơn");
		btnXoa = new JButton("Xóa Hóa Đơn");
		btnDong = new JButton("Đóng");

		Dimension btnSize = new Dimension(120, 40);
		btnSua.setPreferredSize(btnSize);
		btnXoa.setPreferredSize(btnSize);
		btnDong.setPreferredSize(btnSize);

		pnlButtons.add(btnSua);
		pnlButtons.add(btnXoa);
		pnlButtons.add(btnDong);

		pnlButtonsContainer.add(pnlButtons, BorderLayout.SOUTH);
		pnlFooter.add(pnlButtonsContainer);

		contentPane.add(pnlFooter, BorderLayout.SOUTH);

		setViewOnly();

		pack();
	}

	private void setViewOnly() {
		JTextField[] fields = { txtTenKH, txtNgayThanhToan, txtMaHopDong, txtNhaTro, txtPhong, txtGiaThue, txtGhiChu,
				txtHoaDonDienNuoc, txtPhuongThucThanhToan, txtLoaiThanhToan, txtTongTienDN, txtTongTienPhuPhi,
				txtTongThanhToan };
		for (JTextField f : fields) {
			f.setEditable(false);
			f.setBackground(new Color(240, 240, 240));
		}

		tblPhuPhi.setEnabled(false);
		tblDienNuoc.setEnabled(false);
	}

	private void addLabel(JPanel panel, String text, int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 0, 5);
		panel.add(new JLabel(text), gbc);
	}

	private void addComponent(JPanel panel, JComponent comp, int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(0, 5, 10, 5);
		panel.add(comp, gbc);
	}

	private JTextField createTextField() {
		JTextField txt = new JTextField();
		txt.setPreferredSize(new Dimension(0, 30));
		return txt;
	}

	private JTextField createTotalField() {
		JTextField txt = new JTextField("0");
		txt.setEditable(false);
		txt.setBackground(new Color(230, 230, 230));
		txt.setForeground(Color.RED);
		txt.setHorizontalAlignment(JTextField.RIGHT);
		txt.setFont(new Font("Arial", Font.BOLD, 14));
		txt.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		return txt;
	}

	private JPanel createLabeledPanel(String label, JTextField txt) {
		JPanel p = new JPanel(new BorderLayout(5, 0));
		JLabel lbl = new JLabel(label);
		lbl.setPreferredSize(new Dimension(150, 0));
		p.add(lbl, BorderLayout.WEST);
		p.add(txt, BorderLayout.CENTER);
		return p;
	}

	public JTextField getTxtTenKH() {
		return txtTenKH;
	}

	public JTextField getTxtNgayThanhToan() {
		return txtNgayThanhToan;
	}

	public JTextField getTxtMaHopDong() {
		return txtMaHopDong;
	}

	public JTextField getTxtNhaTro() {
		return txtNhaTro;
	}

	public JTextField getTxtPhong() {
		return txtPhong;
	}

	public JTextField getTxtGiaThue() {
		return txtGiaThue;
	}

	public JTextField getTxtGhiChu() {
		return txtGhiChu;
	}

	public JTextField getTxtHoaDonDienNuoc() {
		return txtHoaDonDienNuoc;
	}

	public JTextField getTxtPhuongThucThanhToan() {
		return txtPhuongThucThanhToan;
	}

	public JTextField getTxtLoaiThanhToan() {
		return txtLoaiThanhToan;
	}

	public JTextField getTxtTongTienDN() {
		return txtTongTienDN;
	}

	public JTextField getTxtTongTienPhuPhi() {
		return txtTongTienPhuPhi;
	}

	public JTextField getTxtTongThanhToan() {
		return txtTongThanhToan;
	}

	public JTable getTblPhuPhi() {
		return tblPhuPhi;
	}

	public JTable getTblDienNuoc() {
		return tblDienNuoc;
	}

	public DefaultTableModel getModelPhuPhi() {
		return modelPhuPhi;
	}

	public DefaultTableModel getModelDienNuoc() {
		return modelDienNuoc;
	}

	public JButton getBtnDong() {
		return btnDong;
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

	public void setTxtNgayThanhToan(String text) {
		this.txtNgayThanhToan.setText(text);
	}

	public void setTxtMaHopDong(String text) {
		this.txtMaHopDong.setText(text);
	}

	public void setTxtNhaTro(String text) {
		this.txtNhaTro.setText(text);
	}

	public void setTxtPhong(String text) {
		this.txtPhong.setText(text);
	}

	public void setTxtGiaThue(String text) {
		this.txtGiaThue.setText(text);
	}

	public void setTxtGhiChu(String text) {
		this.txtGhiChu.setText(text);
	}

	public void setTxtHoaDonDienNuoc(String text) {
		this.txtHoaDonDienNuoc.setText(text);
	}

	public void setTxtPhuongThucThanhToan(String text) {
		this.txtPhuongThucThanhToan.setText(text);
	}

	public void setTxtLoaiThanhToan(String text) {
		this.txtLoaiThanhToan.setText(text);
	}

	public void setTxtTongTienDN(String text) {
		this.txtTongTienDN.setText(text);
	}

	public void setTxtTongTienPhuPhi(String text) {
		this.txtTongTienPhuPhi.setText(text);
	}

	public void setTxtTongThanhToan(String text) {
		this.txtTongThanhToan.setText(text);
	}
}