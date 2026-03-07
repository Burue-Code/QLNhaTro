package com.nctu.quanlynhatro.view.phuong_thuc_tt;

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

public class ThemPhuongThucThanhToanView extends JDialog {

	private JTextField txtTenPT;
	private JButton btnThoat, btnThem;
	private DefaultTableModel tableModel;

	public ThemPhuongThucThanhToanView(DefaultTableModel model) {
		this.tableModel = model;

		setTitle("Thêm Phương Thức Mới");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(mainPanel);

		JPanel containerPanel = new JPanel(new BorderLayout(0, 15));

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(new TitledBorder("Thông tin phương thức"));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Tên Phương Thức:"), gbc);

		txtTenPT = new JTextField();
		txtTenPT.setPreferredSize(new Dimension(250, 30));

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		formPanel.add(txtTenPT, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

		btnThoat = new JButton("Thoát");
		btnThoat.setPreferredSize(new Dimension(100, 35));

		btnThem = new JButton("Thêm");
		btnThem.setPreferredSize(new Dimension(100, 35));

		buttonPanel.add(btnThoat);
		buttonPanel.add(btnThem);

		containerPanel.add(formPanel, BorderLayout.CENTER);
		containerPanel.add(buttonPanel, BorderLayout.SOUTH);

		mainPanel.add(containerPanel, BorderLayout.CENTER);

		pack();
		setLocationRelativeTo(null);

	}

	public JTextField getTxtTenPT() {
		return txtTenPT;
	}

	public JButton getBtnHuy() {
		return btnThoat;
	}

	public JButton getBtnThem() {
		return btnThem;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

}