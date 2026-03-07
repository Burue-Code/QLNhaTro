package com.nctu.quanlynhatro.view.phu_phi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class ThemPhuPhiView extends JDialog {

	private JTextField txtTenPP, txtGia;
	private JButton btnThoat, btnXacNhan;
	private DefaultTableModel tableModel;

	public ThemPhuPhiView(DefaultTableModel model) {
		this.tableModel = model;

		setTitle("Thêm Phụ Phí Mới");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(mainPanel);

		JPanel containerPanel = new JPanel(new BorderLayout(0, 15));

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(new TitledBorder("Thông tin phụ phí"));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Tên Phụ Phí:"), gbc);

		txtTenPP = new JTextField(20);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		formPanel.add(txtTenPP, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Đơn Giá:"), gbc);

		txtGia = new JTextField(20);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		formPanel.add(txtGia, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

		btnThoat = new JButton("Thoát");
		btnThoat.setPreferredSize(new Dimension(100, 35));

		btnXacNhan = new JButton("Xác Nhận");
		btnXacNhan.setPreferredSize(new Dimension(100, 35));

		buttonPanel.add(btnThoat);
		buttonPanel.add(btnXacNhan);

		containerPanel.add(formPanel, BorderLayout.CENTER);
		containerPanel.add(buttonPanel, BorderLayout.SOUTH);

		mainPanel.add(containerPanel, BorderLayout.CENTER);

		pack();
		setLocationRelativeTo(null);

	}

	public JTextField getTxtTenPP() {
		return txtTenPP;
	}

	public JTextField getTxtGia() {
		return txtGia;
	}

	public JButton getBtnThoat() {
		return btnThoat;
	}

	public JButton getBtnXatNhan() {
		return btnXacNhan;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

}