package com.nctu.quanlynhatro.view.phuong_thuc_tt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class SuaPhuongThucThanhToanView extends JFrame {

	private JTextField txtTenPT;
	private JButton btnThoat, btnLuu;
	private DefaultTableModel tableModel;
	private int rowIndex;

	public SuaPhuongThucThanhToanView(DefaultTableModel model, int row) {
		this.tableModel = model;
		this.rowIndex = row;

		setTitle("Cập Nhật Phương Thức");
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
		txtTenPT.setText(getValue(1));

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		formPanel.add(txtTenPT, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

		btnThoat = new JButton("Thoát");
		btnThoat.setPreferredSize(new Dimension(100, 35));

		btnLuu = new JButton("Lưu Thay Đổi");
		btnLuu.setPreferredSize(new Dimension(120, 35));

		buttonPanel.add(btnThoat);
		buttonPanel.add(btnLuu);

		containerPanel.add(formPanel, BorderLayout.CENTER);
		containerPanel.add(buttonPanel, BorderLayout.SOUTH);

		mainPanel.add(containerPanel, BorderLayout.CENTER);

		pack();
		setLocationRelativeTo(null);

		btnThoat.addActionListener(e -> this.dispose());

		btnLuu.addActionListener(e -> {
			if (txtTenPT.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập tên phương thức thanh toán!");
				txtTenPT.requestFocus();
				return;
			}

			tableModel.setValueAt(txtTenPT.getText().trim(), rowIndex, 1);

			JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
			this.dispose();
		});
	}

	private String getValue(int col) {
		Object val = tableModel.getValueAt(rowIndex, col);
		return val == null ? "" : val.toString();
	}
}