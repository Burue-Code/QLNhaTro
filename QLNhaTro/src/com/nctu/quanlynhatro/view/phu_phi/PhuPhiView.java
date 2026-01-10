package com.nctu.quanlynhatro.view.phu_phi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PhuPhiView extends JPanel{
	
	public PhuPhiView() {
		setLayout(new BorderLayout());
        setBackground(new Color(230, 240, 255)); // Màu nhẹ
        JLabel lbl = new JLabel("TRANG CHỦ DASHBOARD", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        add(lbl, BorderLayout.CENTER);
	}
}
