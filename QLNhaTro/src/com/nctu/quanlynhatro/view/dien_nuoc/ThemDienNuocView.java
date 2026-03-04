package com.nctu.quanlynhatro.view.dien_nuoc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class ThemDienNuocView extends JDialog {

	// Các thành phần giao diện
	private JTextField txtMaDN;
	private JComboBox<String> cboNhaTro, cboSoPhong;
	private JComboBox<String> cboThang;

	private JTextField txtDienCu, txtDienMoi;
	private JTextField txtNuocCu, txtNuocMoi;

	private JTextField txtGiaDien, txtGiaNuoc;
	private JTextField txtTienDien, txtTienNuoc;
	private JTextField txtTongTien;

	private JButton btnThem;
	private DefaultTableModel tableModel;

	// Cấu hình đơn giá mặc định
	private final long DON_GIA_DIEN_MAC_DINH = 3500;
	private final long DON_GIA_NUOC_MAC_DINH = 10000;

	private DecimalFormat df = new DecimalFormat("#,###");

	public ThemDienNuocView(DefaultTableModel model) {
		this.tableModel = model;
		setTitle("Thêm Phiếu Điện Nước Mới");
		setSize(800, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(mainPanel);

		// --- FORM NHẬP LIỆU (GRID BAG LAYOUT) ---
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(new TitledBorder("Nhập chỉ số điện nước"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// --- HÀNG 1: Nhà trọ (Trái) - Phòng (Phải) ---
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(new JLabel("Nhà trọ:"), gbc);
		cboNhaTro = new JComboBox<>();
		// loadDataNhaTro(); // Để trống chờ DB
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		formPanel.add(cboNhaTro, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Phòng:"), gbc);
		cboSoPhong = new JComboBox<>();
		// loadDataPhong(); // Để trống chờ DB
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		formPanel.add(cboSoPhong, gbc);

		// --- HÀNG 2: Giá điện (Trái) - Giá nước (Phải) ---
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(new JLabel("Đơn giá Điện:"), gbc);
		txtGiaDien = new JTextField(df.format(DON_GIA_DIEN_MAC_DINH));
		txtGiaDien.setEditable(false);
		gbc.gridx = 1;
		gbc.gridy = 1;
		formPanel.add(txtGiaDien, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		formPanel.add(new JLabel("Đơn giá Nước:"), gbc);
		txtGiaNuoc = new JTextField(df.format(DON_GIA_NUOC_MAC_DINH));
		txtGiaNuoc.setEditable(false);
		gbc.gridx = 3;
		gbc.gridy = 1;
		formPanel.add(txtGiaNuoc, gbc);

		// --- HÀNG 3: Số điện cũ (Trái) - Số nước cũ (Phải) ---
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(new JLabel("Số điện cũ:"), gbc);
		txtDienCu = new JTextField("0");
		txtDienCu.setEditable(false);
		gbc.gridx = 1;
		gbc.gridy = 2;
		formPanel.add(txtDienCu, gbc);

		gbc.gridx = 2;
		gbc.gridy = 2;
		formPanel.add(new JLabel("Số nước cũ:"), gbc);
		txtNuocCu = new JTextField("0");
		txtNuocCu.setEditable(false);

		gbc.gridx = 3;
		gbc.gridy = 2;
		formPanel.add(txtNuocCu, gbc);

		// --- HÀNG 4: Số điện mới (Trái) - Số nước mới (Phải) ---
		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(new JLabel("Số điện mới:"), gbc);
		txtDienMoi = new JTextField("0");
		gbc.gridx = 1;
		gbc.gridy = 3;
		formPanel.add(txtDienMoi, gbc);

		gbc.gridx = 2;
		gbc.gridy = 3;
		formPanel.add(new JLabel("Số nước mới:"), gbc);
		txtNuocMoi = new JTextField("0");
		gbc.gridx = 3;
		gbc.gridy = 3;
		formPanel.add(txtNuocMoi, gbc);

		// --- HÀNG 5: Tiền điện (Trái) - Tiền nước (Phải) ---
		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(new JLabel("Thành tiền Điện:"), gbc);
		txtTienDien = new JTextField("0");
		txtTienDien.setEditable(false);
		txtTienDien.setForeground(Color.BLUE);
		gbc.gridx = 1;
		gbc.gridy = 4;
		formPanel.add(txtTienDien, gbc);

		gbc.gridx = 2;
		gbc.gridy = 4;
		formPanel.add(new JLabel("Thành tiền Nước:"), gbc);
		txtTienNuoc = new JTextField("0");
		txtTienNuoc.setEditable(false);
		txtTienNuoc.setForeground(Color.BLUE);
		gbc.gridx = 3;
		gbc.gridy = 4;
		formPanel.add(txtTienNuoc, gbc);

		// --- HÀNG 6: Tháng thu (Combobox) - Tổng tiền (Phải) ---
		gbc.gridx = 0;
		gbc.gridy = 5;
		formPanel.add(new JLabel("Tháng thu:"), gbc);

		cboThang = new JComboBox<>();
//        loadDataThang(); // Hàm load tháng 1-12 (không có năm)
		gbc.gridx = 1;
		gbc.gridy = 5;
		formPanel.add(cboThang, gbc);

		gbc.gridx = 2;
		gbc.gridy = 5;
		formPanel.add(new JLabel("TỔNG CỘNG:"), gbc);
		txtTongTien = new JTextField("0");
		txtTongTien.setEditable(false);
		txtTongTien.setForeground(Color.RED);
		txtTongTien.setFont(new Font("Arial", Font.BOLD, 18));
		gbc.gridx = 3;
		gbc.gridy = 5;
		formPanel.add(txtTongTien, gbc);

		mainPanel.add(formPanel, BorderLayout.CENTER);

		// --- NÚT BẤM ---
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnThem = new JButton("Thêm");
		btnThem.setPreferredSize(new Dimension(150, 40));
		btnThem.setFont(new Font("Arial", Font.PLAIN, 14));

		buttonPanel.add(btnThem);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
	}

	// ... (Phần code giao diện giữ nguyên) ...

	// ==============================================
	// GETTER CHO CONTROLLER TRUY CẬP
	// ==============================================
	public JComboBox<String> getCboNhaTro() {
		return cboNhaTro;
	}

	public JComboBox<String> getCboSoPhong() {
		return cboSoPhong;
	}

	public JComboBox<String> getCboThang() {
		return cboThang;
	}

	public JTextField getTxtDienCu() {
		return txtDienCu;
	}

	public JTextField getTxtDienMoi() {
		return txtDienMoi;
	}

	public JTextField getTxtNuocCu() {
		return txtNuocCu;
	}

	public JTextField getTxtNuocMoi() {
		return txtNuocMoi;
	}

	public JTextField getTxtGiaDien() {
		return txtGiaDien;
	}

	public JTextField getTxtGiaNuoc() {
		return txtGiaNuoc;
	}

	public JTextField getTxtTienDien() {
		return txtTienDien;
	}

	public JTextField getTxtTienNuoc() {
		return txtTienNuoc;
	}

	public JTextField getTxtTongTien() {
		return txtTongTien;
	}

	public JButton getBtnThem() {
		return btnThem;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}
}