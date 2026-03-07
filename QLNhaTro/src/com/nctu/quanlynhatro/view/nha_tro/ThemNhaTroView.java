package com.nctu.quanlynhatro.view.nha_tro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.nctu.quanlynhatro.view.component.MyButton;
import com.nctu.quanlynhatro.view.component.MyComboBox;
import com.nctu.quanlynhatro.view.component.MyLabel;
import com.nctu.quanlynhatro.view.component.MyTextField;

public class ThemNhaTroView extends JDialog {

	private MyTextField txtTenNha, txtSoPhong, txtDiaChi;
	private JTextArea txtGhiChu;
	private MyComboBox cboTrangThai;
	private MyButton btnThem, btnHuy;
	private DefaultTableModel tableModel;

	public ThemNhaTroView(DefaultTableModel model) {
		this.tableModel = model;
		setTitle("Thêm Nhà Trọ Mới");
		setSize(700, 450);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(mainPanel);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(new TitledBorder("Thông tin nhà trọ"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		formPanel.add(new MyLabel("Tên Nhà Trọ:"), gbc);
		txtTenNha = new MyTextField("", 200, 35);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		formPanel.add(txtTenNha, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0;
		formPanel.add(new MyLabel("Số Lượng Phòng:"), gbc);
		txtSoPhong = new MyTextField("", 100, 35);
		txtSoPhong.setToolTipText("Chỉ nhập số");
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		formPanel.add(txtSoPhong, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		formPanel.add(new MyLabel("Địa Chỉ:"), gbc);
		txtDiaChi = new MyTextField("", 200, 35);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		formPanel.add(txtDiaChi, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 0;
		formPanel.add(new MyLabel("Trạng Thái:"), gbc);
		String[] trangThai = { "Còn phòng", "Đang sửa chữa", "Hết phòng" };
		cboTrangThai = new MyComboBox(trangThai, 100, 35);
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		formPanel.add(cboTrangThai, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Ghi Chú:"), gbc);
		txtGhiChu = new JTextArea(3, 20);
		txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		txtGhiChu.setLineWrap(true);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		formPanel.add(txtGhiChu, gbc);

		mainPanel.add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnHuy = new MyButton("Hủy");
		btnHuy.setButtonColor(Color.red);

		btnThem = new MyButton("Thêm");
		btnThem.setButtonColor(Color.BLUE);

		buttonPanel.add(btnHuy);
		buttonPanel.add(btnThem);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
	}

	public MyTextField getTxtTenNha() {
		return txtTenNha;
	}

	public MyTextField getTxtDiaChi() {
		return txtDiaChi;
	}

	public MyTextField getTxtSoPhong() {
		return txtSoPhong;
	}

	public JTextArea getTxtGhiChu() {
		return txtGhiChu;
	}

	public MyComboBox getCbTrangThai() {
		return cboTrangThai;
	}

	public MyButton getBtnHuy() {
		return btnHuy;
	}

	public MyButton getBtnThem() {
		return btnThem;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

}