package com.nctu.quanlynhatro.view.gia_dien_nuoc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog; // Sửa từ JPanel thành JDialog
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class GiaDienNuocView extends JDialog { // Kế thừa JDialog
	private JTextField txtGiaDienCu, txtGiaNuocCu;
	private JTextField txtGiaDienMoi, txtGiaNuocMoi;
	private JButton btnLuu, btnHuy;

	public GiaDienNuocView(Frame owner) {
		super(owner, "Cập nhật Giá Điện Nước", true); // Set title và modal = true
		setSize(400, 350);
		setLocationRelativeTo(owner); // Căn giữa so với form cha
		setLayout(new BorderLayout(10, 10));

		JPanel pnlCenter = new JPanel(new GridLayout(5, 2, 10, 15));
		pnlCenter.setBorder(new EmptyBorder(20, 20, 20, 20));

		// Giá hiện tại (Read-only)
		pnlCenter.add(new JLabel("Giá Điện hiện tại:"));
		txtGiaDienCu = createTextField(false);
		pnlCenter.add(txtGiaDienCu);

		pnlCenter.add(new JLabel("Giá Nước hiện tại:"));
		txtGiaNuocCu = createTextField(false);
		pnlCenter.add(txtGiaNuocCu);

		// Separator (Dòng kẻ ngang)
		pnlCenter.add(new JSeparator());
		pnlCenter.add(new JSeparator());

		// Giá mới
		pnlCenter.add(new JLabel("Giá Điện MỚI:"));
		txtGiaDienMoi = createTextField(true);
		pnlCenter.add(txtGiaDienMoi);

		pnlCenter.add(new JLabel("Giá Nước MỚI:"));
		txtGiaNuocMoi = createTextField(true);
		pnlCenter.add(txtGiaNuocMoi);

		add(pnlCenter, BorderLayout.CENTER);

		// Buttons
		JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnHuy = new JButton("Hủy");
		btnLuu = new JButton("Lưu Thay Đổi");
		btnLuu.setBackground(new Color(0, 153, 76));
		btnLuu.setForeground(Color.WHITE);

		pnlBtn.add(btnHuy);
		pnlBtn.add(btnLuu);
		add(pnlBtn, BorderLayout.SOUTH);
	}

	private JTextField createTextField(boolean editable) {
		JTextField txt = new JTextField();
		txt.setEditable(editable);
		if (!editable) {
			txt.setBackground(new Color(230, 230, 230));
			txt.setFont(new Font("Arial", Font.BOLD, 12));
			txt.setForeground(Color.BLUE);
		}
		return txt;
	}

	// Getters
	public JTextField getTxtGiaDienCu() {
		return txtGiaDienCu;
	}

	public JTextField getTxtGiaNuocCu() {
		return txtGiaNuocCu;
	}

	public JTextField getTxtGiaDienMoi() {
		return txtGiaDienMoi;
	}

	public JTextField getTxtGiaNuocMoi() {
		return txtGiaNuocMoi;
	}

	public JButton getBtnLuu() {
		return btnLuu;
	}

	public JButton getBtnHuy() {
		return btnHuy;
	}
}