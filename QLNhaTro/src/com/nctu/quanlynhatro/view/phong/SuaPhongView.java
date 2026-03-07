package com.nctu.quanlynhatro.view.phong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class SuaPhongView extends JDialog {

	private JComboBox<String> cboNhaTro, cboTrangThai;
	private JTextField txtSoPhong, txtGiaPhong, txtSoNguoi, txtPhuThu, txtGhiChu;

	private JComboBox<String> cboPhuPhi;
	private JButton btnThemPhuPhi;
	private JTable tblPhuPhi;
	private DefaultTableModel modelPhuPhi;

	private JButton btnThoat, btnLuu;

	private DefaultTableModel mainTableModel;
	private int rowIndex;

	public SuaPhongView(DefaultTableModel model, int row) {
		this.mainTableModel = model;
		this.rowIndex = row;

		setTitle("Cập Nhật Thông Tin Phòng");
		setSize(950, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(mainPanel);

		JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0));

		JPanel pnlLeft = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		int inputHeight = 40;
		gbc.insets = new Insets(5, 0, 5, 20);

		gbc.gridx = 0;
		gbc.gridy = 0;
		pnlLeft.add(new JLabel("Nhà Trọ"), gbc);

		gbc.gridy = 1;
		cboNhaTro = new JComboBox<>(new String[] { "Nhà Trọ Hạnh Phúc", "Nhà Trọ A" });
		cboNhaTro.setPreferredSize(new Dimension(0, inputHeight));
		pnlLeft.add(cboNhaTro, gbc);

		gbc.gridy = 2;
		pnlLeft.add(new JLabel("Số Phòng"), gbc);

		gbc.gridy = 3;
		txtSoPhong = new JTextField();
		txtSoPhong.setPreferredSize(new Dimension(0, inputHeight));
		txtSoPhong.setText(getValue(1));
		pnlLeft.add(txtSoPhong, gbc);

		gbc.gridy = 4;
		pnlLeft.add(new JLabel("Giá Phòng"), gbc);

		gbc.gridy = 5;
		txtGiaPhong = new JTextField();
		txtGiaPhong.setPreferredSize(new Dimension(0, inputHeight));
		txtGiaPhong.setText(getValue(3));
		pnlLeft.add(txtGiaPhong, gbc);

		gbc.gridy = 6;
		pnlLeft.add(new JLabel("Số Người Ở Trong Quy Định"), gbc);

		gbc.gridy = 7;
		txtSoNguoi = new JTextField();
		txtSoNguoi.setPreferredSize(new Dimension(0, inputHeight));
		txtSoNguoi.setText(getValue(4));
		pnlLeft.add(txtSoNguoi, gbc);

		gbc.gridy = 8;
		pnlLeft.add(new JLabel("Phụ Thu Quá Người"), gbc);

		gbc.gridy = 9;
		txtPhuThu = new JTextField();
		txtPhuThu.setPreferredSize(new Dimension(0, inputHeight));
		txtPhuThu.setText(getValue(5));
		pnlLeft.add(txtPhuThu, gbc);

		gbc.gridy = 10;
		pnlLeft.add(new JLabel("Trạng Thái"), gbc);

		gbc.gridy = 11;
		cboTrangThai = new JComboBox<>(new String[] { "Phòng Trống", "Đã Thuê", "Bảo Trì" });
		cboTrangThai.setPreferredSize(new Dimension(0, inputHeight));
		cboTrangThai.setSelectedItem(getValue(5));
		pnlLeft.add(cboTrangThai, gbc);

		gbc.gridy = 12;
		pnlLeft.add(new JLabel("Ghi Chú"), gbc);

		gbc.gridy = 13;
		txtGhiChu = new JTextField();
		txtGhiChu.setPreferredSize(new Dimension(0, inputHeight));
		txtGhiChu.setText(getValue(6));
		pnlLeft.add(txtGhiChu, gbc);

		gbc.gridy = 14;
		gbc.weighty = 1.0;
		pnlLeft.add(new JLabel(), gbc);

		JPanel pnlRight = new JPanel(new BorderLayout(0, 10));
		pnlRight.setBorder(new TitledBorder("Phụ Phí"));

		JPanel pnlAddPP = new JPanel(new BorderLayout(10, 0));
		cboPhuPhi = new JComboBox<>(new String[] { "                 ", "Wifi", "Vệ sinh" });
		cboPhuPhi.setPreferredSize(new Dimension(0, 35));

		btnThemPhuPhi = new JButton("Thêm");
		btnThemPhuPhi.setPreferredSize(new Dimension(80, 35));
		btnThemPhuPhi.setBackground(Color.WHITE);

		pnlAddPP.add(cboPhuPhi, BorderLayout.CENTER);
		pnlAddPP.add(btnThemPhuPhi, BorderLayout.EAST);
		pnlRight.add(pnlAddPP, BorderLayout.NORTH);

		String[] colsPP = { "MaPP", "Tên Phụ Phí", "Giá" };
		modelPhuPhi = new DefaultTableModel(colsPP, 0);
		tblPhuPhi = new JTable(modelPhuPhi);
		tblPhuPhi.setRowHeight(25);
		tblPhuPhi.setShowGrid(false);

		pnlRight.add(new JScrollPane(tblPhuPhi), BorderLayout.CENTER);

		pnlCenter.add(pnlLeft);
		pnlCenter.add(pnlRight);
		mainPanel.add(pnlCenter, BorderLayout.CENTER);

		JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));

		btnThoat = new JButton("Thoát");
		btnThoat.setPreferredSize(new Dimension(100, 35));
		btnThoat.setBackground(Color.WHITE);

		btnLuu = new JButton("Lưu Thay Đổi");
		btnLuu.setPreferredSize(new Dimension(120, 35));
		btnLuu.setBackground(Color.WHITE);

		pnlBottom.add(btnThoat);
		pnlBottom.add(btnLuu);

		mainPanel.add(pnlBottom, BorderLayout.SOUTH);

		btnThoat.addActionListener(e -> this.dispose());

		btnLuu.addActionListener(e -> {
			if (txtSoPhong.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập số phòng!");
				return;
			}

			mainTableModel.setValueAt(txtSoPhong.getText(), rowIndex, 1);
			mainTableModel.setValueAt(txtGiaPhong.getText(), rowIndex, 3);
			mainTableModel.setValueAt(cboTrangThai.getSelectedItem(), rowIndex, 5);
			mainTableModel.setValueAt(txtGhiChu.getText(), rowIndex, 6);

			JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
			this.dispose();
		});

		btnThemPhuPhi.addActionListener(e -> {
			String sel = (String) cboPhuPhi.getSelectedItem();
			if (sel != null && !sel.trim().isEmpty()) {
				modelPhuPhi.addRow(new Object[] { "PP01", sel, "50,000" });
			}
		});
	}

	private String getValue(int col) {
		if (col < 0 || col >= mainTableModel.getColumnCount()) {
			return "";
		}
		Object val = mainTableModel.getValueAt(rowIndex, col);
		return val == null ? "" : val.toString();
	}

	public JTable getTblPhuPhi() {
		return tblPhuPhi;
	}

	public JTextField getTxtSoPhong() {
		return txtSoPhong;
	}

	public JTextField getTxtGiaPhong() {
		return txtGiaPhong;
	}

	public JTextField getTxtSoNguoi() {
		return txtSoNguoi;
	}

	public JTextField getTxtPhuThu() {
		return txtPhuThu;
	}

	public JTextField getTxtGhiChu() {
		return txtGhiChu;
	}

	public JComboBox<String> getCboNhaTro() {
		return cboNhaTro;
	}

	public JComboBox<String> getCboTrangThai() {
		return cboTrangThai;
	}

	public JComboBox<String> getCboPhuPhi() {
		return cboPhuPhi;
	}

	public JButton getBtnThemPhuPhi() {
		return btnThemPhuPhi;
	}

	public JButton getBtnThoat() {
		return btnThoat;
	}

	public JButton getBtnLuu() {
		return btnLuu;
	}
}