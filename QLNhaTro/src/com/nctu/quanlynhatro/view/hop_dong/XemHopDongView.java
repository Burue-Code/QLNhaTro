package com.nctu.quanlynhatro.view.hop_dong;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class XemHopDongView extends JDialog {

	// ====== TextFields (chỉ giao diện, không cần dữ liệu) ======
	private JTextField txtMaHopDong;
	private JTextField txtTenKhachHang;
	private JTextField txtNgayBatDau;
	private JTextField txtNgayKetThuc;

	private JTextField txtNhaTro;
	private JTextField txtPhong;
	private JTextField txtSoLuongNguoiO;

	private JTextField txtGiaThue;
	private JTextField txtTrangThaiHopDong;
	private JTextField txtGhiChu;

	// ====== Table ======
	private JTable tblKhachHangPhuThuoc;
	private DefaultTableModel modelPhuThuoc;

	// ====== Buttons ======
	private JButton btnThoat;
	private JButton btnSua;
	private JButton btnXoa;

	public XemHopDongView() {
		setTitle("Xem Hợp Đồng");
		setSize(1200, 650);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		JPanel pnlTop = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		txtMaHopDong = createFieldReadonly();
		txtTenKhachHang = createFieldReadonly();
		txtNgayBatDau = createFieldReadonly();
		txtNgayKetThuc = createFieldReadonly();
		txtNhaTro = createFieldReadonly();
		txtPhong = createFieldReadonly();
		txtSoLuongNguoiO = createFieldReadonly();
		txtGiaThue = createFieldReadonly();
		txtTrangThaiHopDong = createFieldReadonly();
		txtGhiChu = createFieldReadonly();

		// ---- Row 0: MaHD | NhaTro | GiaThue ----
		addLabeledField(pnlTop, gbc, 0, 0, "Mã Hợp Đồng", txtMaHopDong);
		addLabeledField(pnlTop, gbc, 1, 0, "Nhà Trọ", txtNhaTro);
		addLabeledField(pnlTop, gbc, 2, 0, "Giá Thuê", txtGiaThue);

		// ---- Row 1: TenKH | Phong | TrangThai ----
		addLabeledField(pnlTop, gbc, 0, 1, "Tên Khách Hàng", txtTenKhachHang);
		addLabeledField(pnlTop, gbc, 1, 1, "Phòng", txtPhong);
		addLabeledField(pnlTop, gbc, 2, 1, "Trạng Thái Hợp Đồng", txtTrangThaiHopDong);

		// ---- Row 2: (NgayBatDau + NgayKetThuc) | SoLuongNguoiO | GhiChu ----
		JPanel pnlDates = new JPanel(new GridBagLayout());
		GridBagConstraints d = new GridBagConstraints();
		d.insets = new Insets(0, 0, 0, 8);
		d.fill = GridBagConstraints.HORIZONTAL;
		d.weightx = 1.0;

		JPanel startBox = new JPanel(new BorderLayout(0, 6));
		startBox.add(new JLabel("Ngày Bắt Đầu"), BorderLayout.NORTH);
		startBox.add(txtNgayBatDau, BorderLayout.CENTER);

		JPanel endBox = new JPanel(new BorderLayout(0, 6));
		endBox.add(new JLabel("Ngày Kết Thúc"), BorderLayout.NORTH);
		endBox.add(txtNgayKetThuc, BorderLayout.CENTER);

		d.gridx = 0;
		d.gridy = 0;
		d.weightx = 1.0;
		pnlDates.add(startBox, d);

		d.gridx = 1;
		d.insets = new Insets(0, 8, 0, 0);
		pnlDates.add(endBox, d);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 1.0;
		pnlTop.add(pnlDates, gbc);

		addLabeledField(pnlTop, gbc, 1, 2, "Số Lượng Người Ở", txtSoLuongNguoiO);
		addLabeledField(pnlTop, gbc, 2, 2, "Ghi Chú", txtGhiChu);

		contentPane.add(pnlTop, BorderLayout.NORTH);

		// =========================
		// CENTER: TABLE "Khách Hàng Phụ Thuộc"
		// =========================
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.setBorder(new TitledBorder("Khách Hàng Phụ Thuộc"));

		String[] colsPT = { "MaKH", "Tên Khách Hàng", "Địa Chỉ", "Giới Tính", "Ngày Sinh", "Số Điện Thoại" };
		modelPhuThuoc = new DefaultTableModel(colsPT, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // không cho sửa cell
			}
		};

		tblKhachHangPhuThuoc = new JTable(modelPhuThuoc);
		tblKhachHangPhuThuoc.setEnabled(false); // không cho tương tác (chọn/sửa)
		tblKhachHangPhuThuoc.setRowSelectionAllowed(false);
		tblKhachHangPhuThuoc.setCellSelectionEnabled(false);
		tblKhachHangPhuThuoc.setFocusable(false);

		pnlCenter.add(new JScrollPane(tblKhachHangPhuThuoc), BorderLayout.CENTER);
		contentPane.add(pnlCenter, BorderLayout.CENTER);

		JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

		btnSua = new JButton("Sửa Hợp Đồng");
		btnXoa = new JButton("Xóa Hợp Đồng");
		btnThoat = new JButton("Thoát");

		Dimension btnSize = new Dimension(140, 35);
		btnSua.setPreferredSize(btnSize);
		btnXoa.setPreferredSize(btnSize);
		btnThoat.setPreferredSize(btnSize);

		// Add theo thứ tự từ trái qua phải
		pnlBottom.add(btnSua);
		pnlBottom.add(btnXoa);
		pnlBottom.add(btnThoat);

		contentPane.add(pnlBottom, BorderLayout.SOUTH);

	}

	private JTextField createFieldReadonly() {
		JTextField tf = new JTextField();
		tf.setPreferredSize(new Dimension(0, 34));
		tf.setEditable(false); // không cho gõ
		tf.setFocusable(false); // không focus bằng tab/click
		tf.setBackground(UIManager.getColor("TextField.background")); // giữ màu giống form
		return tf;
	}

	private void addLabeledField(JPanel parent, GridBagConstraints gbc, int col, int row, String label,
			JComponent field) {
		JPanel box = new JPanel(new BorderLayout(0, 6));
		box.add(new JLabel(label), BorderLayout.NORTH);
		box.add(field, BorderLayout.CENTER);

		gbc.gridx = col;
		gbc.gridy = row;
		gbc.gridwidth = 1;
		gbc.weightx = 1.0;
		parent.add(box, gbc);
	}

	// ====== Getters ======

	public JTextField getTxtMaHopDong() {
		return txtMaHopDong;
	}

	public JTextField getTxtTenKhachHang() {
		return txtTenKhachHang;
	}

	public JTextField getTxtNgayBatDau() {
		return txtNgayBatDau;
	}

	public JTextField getTxtNgayKetThuc() {
		return txtNgayKetThuc;
	}

	public JTextField getTxtNhaTro() {
		return txtNhaTro;
	}

	public JTextField getTxtPhong() {
		return txtPhong;
	}

	public JTextField getTxtSoLuongNguoiO() {
		return txtSoLuongNguoiO;
	}

	public JTextField getTxtGiaThue() {
		return txtGiaThue;
	}

	public JTextField getTxtTrangThaiHopDong() {
		return txtTrangThaiHopDong;
	}

	public JTextField getTxtGhiChu() {
		return txtGhiChu;
	}

	public JTable getTblKhachHangPhuThuoc() {
		return tblKhachHangPhuThuoc;
	}

	public DefaultTableModel getModelPhuThuoc() {
		return modelPhuThuoc;
	}

	public JButton getBtnThoat() {
		return btnThoat;
	}

	// [MỚI THÊM] Getter cho 2 nút Sửa và Xóa
	public JButton getBtnSua() {
		return btnSua;
	}

	public JButton getBtnXoa() {
		return btnXoa;
	}

	// ====== Setters (Cho các trường dữ liệu) ======

	public void setTxtMaHopDong(String maHopDong) {
		this.txtMaHopDong.setText(maHopDong);
	}

	public void setTxtTenKhachHang(String tenKhachHang) {
		this.txtTenKhachHang.setText(tenKhachHang);
	}

	public void setTxtNgayBatDau(String ngayBatDau) {
		this.txtNgayBatDau.setText(ngayBatDau);
	}

	public void setTxtNgayKetThuc(String ngayKetThuc) {
		this.txtNgayKetThuc.setText(ngayKetThuc);
	}

	public void setTxtNhaTro(String nhaTro) {
		this.txtNhaTro.setText(nhaTro);
	}

	public void setTxtPhong(String phong) {
		this.txtPhong.setText(phong);
	}

	public void setTxtSoLuongNguoiO(String soLuong) {
		this.txtSoLuongNguoiO.setText(soLuong);
	}

	public void setTxtGiaThue(String giaThue) {
		this.txtGiaThue.setText(giaThue);
	}

	public void setTxtTrangThaiHopDong(String trangThai) {
		this.txtTrangThaiHopDong.setText(trangThai);
	}

	public void setTxtGhiChu(String ghiChu) {
		this.txtGhiChu.setText(ghiChu);
	}
}