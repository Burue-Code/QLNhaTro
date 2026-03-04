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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.nctu.quanlynhatro.model.KhachHangGoiY;

public class ThemHoaDonView extends JDialog {

	// --- Component KHU VỰC TRÁI ---
	private JTextField txtTenKH, txtNgayThanhToan;
	private JTextField txtMaHopDong;
	// Thay đổi: Nhà trọ & Phòng thành ComboBox
	private JTextField txtNhaTro, txtPhong; // Đổi từ ComboBox về JTextField
	private JPopupMenu popupMenu;
	private JList<KhachHangGoiY> listGoiY;
	private DefaultListModel<KhachHangGoiY> listModel;

	private JTextField txtGiaThue, txtGhiChu;

	private JComboBox<String> cboChonDienNuoc;
	private JButton btnCongDN, btnThemPhieuMoi;
	private JComboBox<String> cboPhuongThuc, cboLoaiThanhToan;

	// --- Component KHU VỰC PHẢI ---
	private JTable tblPhuPhi, tblDienNuoc;
	private DefaultTableModel modelPhuPhi, modelDienNuoc;

	// --- Component KHU VỰC DƯỚI (Footer) ---
	private JTextField txtTongTienDN, txtTongTienPhuPhi, txtTongThanhToan;
	private JButton btnHuy, btnXacNhan, btnIn;

	private DefaultTableModel mainTableModel;

	public ThemHoaDonView(DefaultTableModel model) {
		this.mainTableModel = model;
		setTitle("Lập Hóa Đơn Thanh Toán");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		// =================================================================
		// 1. PHẦN GIỮA: CHIA 2 CỘT
		// =================================================================
		JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0));

		// --- CỘT TRÁI: FORM NHẬP LIỆU ---
		JPanel pnlLeftForm = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		// Hàng 1
		addLabel(pnlLeftForm, "Tên Khách Hàng:", 0, 0);
		addLabel(pnlLeftForm, "Ngày Thanh Toán:", 1, 0);

		txtTenKH = createTextField();
		addComponent(pnlLeftForm, txtTenKH, 0, 1);

		txtNgayThanhToan = createTextField();
		txtNgayThanhToan.setEditable(false);
		txtNgayThanhToan.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		addComponent(pnlLeftForm, txtNgayThanhToan, 1, 1);

		// Hàng 2: Mã Hợp Đồng & Điện Nước
		addLabel(pnlLeftForm, "Mã Hợp Đồng:", 0, 2);
		addLabel(pnlLeftForm, "Hóa Đơn Điện Nước:", 1, 2);

		txtMaHopDong = createTextField();
		txtMaHopDong.setEditable(false);
		addComponent(pnlLeftForm, txtMaHopDong, 0, 3);

		JPanel pnlDienNuocOption = new JPanel(new BorderLayout(5, 0));
		cboChonDienNuoc = new JComboBox<>(new String[] { "-- Chọn phiếu --" });
		btnCongDN = new JButton("+");
		btnThemPhieuMoi = new JButton("Mới");

		pnlDienNuocOption.add(cboChonDienNuoc, BorderLayout.CENTER);
		JPanel pnlBtnDN = new JPanel(new GridLayout(1, 2, 2, 0));
		pnlBtnDN.add(btnCongDN);
		pnlBtnDN.add(btnThemPhieuMoi);
		pnlDienNuocOption.add(pnlBtnDN, BorderLayout.EAST);
		pnlDienNuocOption.setPreferredSize(new Dimension(0, 30));

		addComponent(pnlLeftForm, pnlDienNuocOption, 1, 3);

		// Hàng 3: Nhà Trọ & Giá Thuê
		addLabel(pnlLeftForm, "Nhà Trọ:", 0, 4);
		addLabel(pnlLeftForm, "Giá Thuê:", 1, 4);

		txtNhaTro = createTextField();
		txtNhaTro.setEditable(false);
		addComponent(pnlLeftForm, txtNhaTro, 0, 5);

		txtGiaThue = createTextField();
		txtGiaThue.setEditable(false);
		addComponent(pnlLeftForm, txtGiaThue, 1, 5);

		// Hàng 4: Phòng & Ghi Chú
		addLabel(pnlLeftForm, "Phòng:", 0, 6);
		addLabel(pnlLeftForm, "Ghi Chú:", 1, 6);

		txtPhong = createTextField();
		txtPhong.setEditable(false);
		addComponent(pnlLeftForm, txtPhong, 0, 7);

		txtGhiChu = createTextField();
		addComponent(pnlLeftForm, txtGhiChu, 1, 7);

//		Khởi tạo Popup Menu cho gợi ý
		popupMenu = new JPopupMenu();
		listModel = new DefaultListModel<>();
		listGoiY = new JList<>(listModel);
		popupMenu.add(new JScrollPane(listGoiY));
		popupMenu.setFocusable(false); // Quan trọng để không mất focus khỏi txtTenKH

		// Hàng 5
		addLabel(pnlLeftForm, "Phương Thức Thanh Toán:", 0, 8);
		addLabel(pnlLeftForm, "Loại Thanh Toán:", 1, 8);
		cboPhuongThuc = new JComboBox<>();
		cboPhuongThuc.setPreferredSize(new Dimension(0, 30));
		addComponent(pnlLeftForm, cboPhuongThuc, 0, 9);

		cboLoaiThanhToan = new JComboBox<>(new String[] { "Tất Cả", "Một Phần" });
		cboLoaiThanhToan.setPreferredSize(new Dimension(0, 30));
		addComponent(pnlLeftForm, cboLoaiThanhToan, 1, 9);

		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.weighty = 1.0;
		pnlLeftForm.add(new JLabel(), gbc);

		// --- CỘT PHẢI: BẢNG DỮ LIỆU ---
		JPanel pnlRightTables = new JPanel(new GridLayout(2, 1, 0, 10));

		// Bảng Phụ Phí
		JPanel pnlPhuPhi = new JPanel(new BorderLayout());
		pnlPhuPhi.setBorder(new TitledBorder("Phụ Phí"));
		String[] colsPhuPhi = { "MaPP", "Tên Phụ Phí", "Giá" };
		modelPhuPhi = new DefaultTableModel(colsPhuPhi, 0);
		tblPhuPhi = new JTable(modelPhuPhi);
		tblPhuPhi.setRowHeight(25);
		tblPhuPhi.setPreferredScrollableViewportSize(new Dimension(450, 100));
		pnlPhuPhi.add(new JScrollPane(tblPhuPhi), BorderLayout.CENTER);

		// Bảng Điện Nước
		JPanel pnlDienNuoc = new JPanel(new BorderLayout());
		pnlDienNuoc.setBorder(new TitledBorder("Hóa Đơn Điện Nước"));
		String[] colsDN = { "MaDN", "Thời Gian", "Giá" };
		modelDienNuoc = new DefaultTableModel(colsDN, 0);
		tblDienNuoc = new JTable(modelDienNuoc);
		tblDienNuoc.setRowHeight(25);
		tblDienNuoc.setPreferredScrollableViewportSize(new Dimension(450, 100));
		pnlDienNuoc.add(new JScrollPane(tblDienNuoc), BorderLayout.CENTER);

		pnlRightTables.add(pnlPhuPhi);
		pnlRightTables.add(pnlDienNuoc);

		pnlCenter.add(pnlLeftForm);
		pnlCenter.add(pnlRightTables);
		contentPane.add(pnlCenter, BorderLayout.CENTER);

		// =================================================================
		// 2. PHẦN DƯỚI: FOOTER
		// =================================================================
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

		btnHuy = new JButton("Hủy");
		btnHuy.setPreferredSize(new Dimension(100, 40));

		btnXacNhan = new JButton("Xác Nhận");
		btnXacNhan.setPreferredSize(new Dimension(100, 40));

		btnIn = new JButton("In");
		btnIn.setPreferredSize(new Dimension(100, 40));

		pnlButtons.add(btnHuy);
		pnlButtons.add(btnXacNhan);
		pnlButtons.add(btnIn);

		pnlButtonsContainer.add(pnlButtons, BorderLayout.SOUTH);
		pnlFooter.add(pnlButtonsContainer);

		contentPane.add(pnlFooter, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(null);
	}

	// --- Helper Functions ---
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

	// =================================================================
	// CÁC HÀM GETTER (Để Controller lấy dữ liệu từ giao diện)
	// =================================================================

	public JTextField getTxtTenKH() {
		return txtTenKH;
	}

	public JTextField getTxtNgayThanhToan() {
		return txtNgayThanhToan;
	}

	public JTextField getTxtMaHopDong() {
		return txtMaHopDong;
	}

	// Getter mới cho ComboBox
	public JTextField getTxtNhaTro() {
		return txtNhaTro;
	}

	public JTextField getTxtPhong() {
		return txtPhong;
	}

	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public JList<KhachHangGoiY> getListGoiY() {
		return listGoiY;
	}

	public DefaultListModel<KhachHangGoiY> getListModel() {
		return listModel;
	}

	public JTextField getTxtGiaThue() {
		return txtGiaThue;
	}

	public JTextField getTxtGhiChu() {
		return txtGhiChu;
	}

	public JComboBox<String> getCboChonDienNuoc() {
		return cboChonDienNuoc;
	}

	public JComboBox<String> getCboPhuongThuc() {
		return cboPhuongThuc;
	}

	public JComboBox<String> getCboLoaiThanhToan() {
		return cboLoaiThanhToan;
	}

	public JButton getBtnCongDN() {
		return btnCongDN;
	}

	public JButton getBtnThemPhieuMoi() {
		return btnThemPhieuMoi;
	}

	public JButton getBtnHuy() {
		return btnHuy;
	}

	public JButton getBtnXacNhan() {
		return btnXacNhan;
	}

	public JButton getBtnIn() {
		return btnIn;
	}

	public DefaultTableModel getModelPhuPhi() {
		return modelPhuPhi;
	}

	public DefaultTableModel getModelDienNuoc() {
		return modelDienNuoc;
	}

	public JTable getTblPhuPhi() {
		return tblPhuPhi;
	}

	public JTable getTblDienNuoc() {
		return tblDienNuoc;
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

	// =================================================================
	// CÁC HÀM SETTER (Để Controller đổ dữ liệu lên giao diện)
	// =================================================================

	public void setTenKH(String tenKH) {
		this.txtTenKH.setText(tenKH);
	}

	public void setMaHopDong(String maHD) {
		this.txtMaHopDong.setText(maHD);
	}

	public void setGiaThue(String giaThue) {
		this.txtGiaThue.setText(giaThue);
	}

	public void setTongTienDN(String tongTien) {
		this.txtTongTienDN.setText(tongTien);
	}

	public void setTongTienPhuPhi(String tongTien) {
		this.txtTongTienPhuPhi.setText(tongTien);
	}

	public void setTongThanhToan(String tongTien) {
		this.txtTongThanhToan.setText(tongTien);
	}
}