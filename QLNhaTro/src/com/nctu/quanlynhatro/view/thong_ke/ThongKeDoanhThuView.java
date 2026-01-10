package com.nctu.quanlynhatro.view.thong_ke;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ThongKeDoanhThuView extends JPanel{
	public ThongKeDoanhThuView() {
        setLayout(new FlowLayout());
        setBackground(new Color(255, 230, 230)); // Màu nhẹ khác
        add(new JLabel("Quản lý danh sách sinh viên"));
        add(new JTextField(20));
        add(new JButton("Tìm kiếm"));
    }
}
