package com.nctu.quanlynhatro.view.hop_dong;

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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class ThemHopDongView extends JDialog {

	// --- Component TRÁI (Form Hợp Đồng) ---
	private JTextField txtMaKH, txtTenKH; // Read-only
	private JTextField txtNgayLap, txtSoThang, txtNgayKetThuc;
	private JComboBox<String> cboNhaTro, cboPhong;
	private JTextField txtSoNguoi, txtGiaThue;
	private JTextArea txtGhiChu;

	// --- Component PHẢI TRÊN (Tìm KH Chính) ---
	private JTextField txtTimKiem;
	private JButton btnThemKH;
	private JTable tblKhachHang;
	private DefaultTableModel modelKH;

	// --- Component PHẢI DƯỚI (KH Phụ Thuộc) ---
	private JTable tblKHPhuThuoc;
	private DefaultTableModel modelPhuThuoc;

	// --- Component DƯỚI ---
	private JButton btnThem, btnThoat;

	private DefaultTableModel mainTableModel;

	public ThemHopDongView(DefaultTableModel model) {
		this.mainTableModel = model;
		setTitle("Lập Hợp Đồng Mới");
		setSize(1100, 650); // Form lớn
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		// =================================================================
		// 1. PANEL TRÁI: FORM NHẬP LIỆU HỢP ĐỒNG
		// =================================================================
		JPanel pnlLeft = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		// --- Hàng 1: Mã KH (Read-only) ---
		gbc.gridx = 0;
		gbc.gridy = 0;
		pnlLeft.add(new JLabel("Mã Khách Hàng:"), gbc);
		gbc.gridy = 1;
		txtMaKH = new JTextField();
		txtMaKH.setEditable(false);
		txtMaKH.setPreferredSize(new Dimension(0, 30));
		pnlLeft.add(txtMaKH, gbc);

		// --- Hàng 2: Tên KH (Read-only) ---
		gbc.gridy = 2;
		pnlLeft.add(new JLabel("Tên Khách Hàng:"), gbc);
		gbc.gridy = 3;
		txtTenKH = new JTextField();
		txtTenKH.setEditable(false);
		txtTenKH.setPreferredSize(new Dimension(0, 30));
		pnlLeft.add(txtTenKH, gbc);

		// --- Hàng 3: Ngày Lập - Số Tháng - Ngày KT ---
		JPanel pnlTime = new JPanel(new GridLayout(1, 3, 5, 0));

		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(new JLabel("Ngày Lập:"), BorderLayout.NORTH);
		txtNgayLap = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		p1.add(txtNgayLap, BorderLayout.CENTER);

		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(new JLabel("Số Tháng:"), BorderLayout.NORTH);
		txtSoThang = new JTextField("");
		p2.add(txtSoThang, BorderLayout.CENTER);

		JPanel p3 = new JPanel(new BorderLayout());
		p3.add(new JLabel("Ngày Kết Thúc:"), BorderLayout.NORTH);
		txtNgayKetThuc = new JTextField();
		txtNgayKetThuc.setEditable(false);
		p3.add(txtNgayKetThuc, BorderLayout.CENTER);

		pnlTime.add(p1);
		pnlTime.add(p2);
		pnlTime.add(p3);

		gbc.gridy = 4;
		pnlLeft.add(pnlTime, gbc);

		// --- Hàng 4: Nhà Trọ ---
		gbc.gridy = 5;
		pnlLeft.add(new JLabel("Chọn Nhà Trọ:"), gbc);
		gbc.gridy = 6;
		cboNhaTro = new JComboBox<>();
		cboNhaTro.setPreferredSize(new Dimension(0, 30));
		pnlLeft.add(cboNhaTro, gbc);

		// --- Hàng 5: Phòng ---
		gbc.gridy = 7;
		pnlLeft.add(new JLabel("Chọn Phòng:"), gbc);
		gbc.gridy = 8;
		cboPhong = new JComboBox<>();
		cboPhong.setPreferredSize(new Dimension(0, 30));
		pnlLeft.add(cboPhong, gbc);

		// --- Hàng 6: Số Lượng Người Ở ---
		gbc.gridy = 9;
		pnlLeft.add(new JLabel("Số Lượng Người Ở:"), gbc);
		gbc.gridy = 10;
		txtSoNguoi = new JTextField("1");
		txtSoNguoi.setPreferredSize(new Dimension(0, 30));
		pnlLeft.add(txtSoNguoi, gbc);

		// --- Hàng 7: Giá Thuê ---
		gbc.gridy = 11;
		pnlLeft.add(new JLabel("Giá Thuê:"), gbc);
		gbc.gridy = 12;
		txtGiaThue = new JTextField();
		txtGiaThue.setPreferredSize(new Dimension(0, 30));
		pnlLeft.add(txtGiaThue, gbc);

		// --- Hàng 8: Ghi Chú ---
		gbc.gridy = 13;
		pnlLeft.add(new JLabel("Ghi Chú:"), gbc);
		gbc.gridy = 14;
		txtGhiChu = new JTextArea(3, 20);
		txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		pnlLeft.add(txtGhiChu, gbc);

		// Spacer đẩy lên
		gbc.gridy = 15;
		gbc.weighty = 1.0;
		pnlLeft.add(new JLabel(), gbc);

		// =================================================================
		// 2. PANEL PHẢI: TÌM KIẾM & DANH SÁCH PHỤ THUỘC
		// =================================================================
		JPanel pnlRight = new JPanel(new GridLayout(2, 1, 0, 10)); // Chia 2 phần trên dưới

		// --- PHẦN TRÊN: TÌM KIẾM KHÁCH HÀNG ---
		JPanel pnlTopRight = new JPanel(new BorderLayout(5, 5));
		pnlTopRight.setBorder(new TitledBorder("Khách Hàng (Người thuê chính)"));

		JPanel pnlSearch = new JPanel(new BorderLayout(5, 0));
		txtTimKiem = new JTextField();
		pnlSearch.add(new JLabel("Tìm kiếm: "), BorderLayout.WEST);
		pnlSearch.add(txtTimKiem, BorderLayout.CENTER);
		btnThemKH = new JButton("Khách Hàng Mới");
		pnlSearch.add(btnThemKH, BorderLayout.EAST);

		pnlTopRight.add(pnlSearch, BorderLayout.NORTH);

		String[] colsKH = { "MaKH", "Tên Khách Hàng", "Địa Chỉ", "Giới Tính", "Ngày Sinh" };
		modelKH = new DefaultTableModel(colsKH, 0);
		tblKhachHang = new JTable(modelKH);
		pnlTopRight.add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);

		// --- PHẦN DƯỚI: KHÁCH HÀNG PHỤ THUỘC ---
		JPanel pnlBotRight = new JPanel(new BorderLayout());
		pnlBotRight.setBorder(new TitledBorder("Khách Hàng Phụ Thuộc (Ở chung)"));

		String[] colsPT = { "MaKH", "Tên Khách Hàng", "Địa Chỉ", "Giới Tính", "Ngày Sinh" };
		modelPhuThuoc = new DefaultTableModel(colsPT, 0);
		tblKHPhuThuoc = new JTable(modelPhuThuoc);
		pnlBotRight.add(new JScrollPane(tblKHPhuThuoc), BorderLayout.CENTER);

		pnlRight.add(pnlTopRight);
		pnlRight.add(pnlBotRight);

		// =================================================================
		// 3. TỔNG HỢP GIAO DIỆN
		// =================================================================
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlLeft, pnlRight);
		splitPane.setDividerLocation(350); // Chiều rộng panel trái
		splitPane.setResizeWeight(0.3);

		contentPane.add(splitPane, BorderLayout.CENTER);

		// =================================================================
		// 4. PANEL DƯỚI CÙNG: NÚT BẤM
		// =================================================================
		JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		btnThoat = new JButton("Thoát");
		btnThoat.setPreferredSize(new Dimension(100, 35));
		btnThoat.setFont(new Font("Arial", Font.PLAIN, 13));

		btnThem = new JButton("Thêm");
		btnThem.setPreferredSize(new Dimension(100, 35));
		btnThem.setFont(new Font("Arial", Font.PLAIN, 13));

		pnlBottom.add(btnThoat);
		pnlBottom.add(btnThem);
		contentPane.add(pnlBottom, BorderLayout.SOUTH);

	}

	// =================================================================
	// CÁC HÀM GETTER COMPONENT (Dành cho Controller gắn sự kiện, đổ dữ liệu)
	// =================================================================
	public JButton getBtnThem() {
		return btnThem;
	}

	public JButton getBtnThoat() {
		return btnThoat;
	}

	public JButton getBtnThemKH() {
		return btnThemKH;
	}

	public JTextField getTxtTimKiem() {
		return txtTimKiem;
	}

	public JTextField getTxtSoThang() {
		return txtSoThang;
	}

	public JTextField getTxtNgayLap() {
		return txtNgayLap;
	}

	public JComboBox<String> getCboNhaTro() {
		return cboNhaTro;
	}

	public JComboBox<String> getCboPhong() {
		return cboPhong;
	}

	public JTable getTblKhachHang() {
		return tblKhachHang;
	}

	public DefaultTableModel getModelKH() {
		return modelKH;
	}

	public JTable getTblKHPhuThuoc() {
		return tblKHPhuThuoc;
	}

	public DefaultTableModel getModelPhuThuoc() {
		return modelPhuThuoc;
	}

	// =================================================================
	// CÁC HÀM GETTER DỮ LIỆU NHẬP LIỆU (Lấy chuỗi text để lưu CSDL)
	// =================================================================
	public String getMaKH() {
		return txtMaKH.getText().trim();
	}

	public String getTenKH() {
		return txtTenKH.getText().trim();
	}

	public String getNgayLap() {
		return txtNgayLap.getText().trim();
	}

	public String getSoThang() {
		return txtSoThang.getText().trim();
	}

	public String getNgayKetThuc() {
		return txtNgayKetThuc.getText().trim();
	}

	public String getSoNguoi() {
		return txtSoNguoi.getText().trim();
	}

	public String getGiaThue() {
		return txtGiaThue.getText().trim();
	}

	public String getGhiChu() {
		return txtGhiChu.getText().trim();
	}

	// =================================================================
	// CÁC HÀM SETTER (Controller dùng để đẩy dữ liệu ngược lên giao diện)
	// =================================================================
	public void setMaKH(String maKH) {
		this.txtMaKH.setText(maKH);
	}

	public void setTenKH(String tenKH) {
		this.txtTenKH.setText(tenKH);
	}

	public void setNgayKetThuc(String ngayKT) {
		this.txtNgayKetThuc.setText(ngayKT);
	}

	public void setGiaThue(String gia) {
		this.txtGiaThue.setText(gia);
	}

	public void setSoNguoi(String soNguoi) {
		this.txtSoNguoi.setText(soNguoi);
	}

	public void setGhiChu(String ghiChu) {
		this.txtGhiChu.setText(ghiChu);
	}

}