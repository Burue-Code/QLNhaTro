package com.nctu.quanlynhatro.view;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JFrame {

    public DashboardView() {
        setTitle("Trang chủ - Quản lý Nhà Trọ");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        JMenu dm = new JMenu("Danh mục");
        dm.add(new JMenuItem("Nhà trọ"));
        dm.add(new JMenuItem("Phòng"));
        dm.add(new JMenuItem("Khách hàng"));

        JMenu nv = new JMenu("Nghiệp vụ");
        nv.add(new JMenuItem("Hợp đồng"));
        nv.add(new JMenuItem("Hóa đơn"));
        nv.add(new JMenuItem("Điện - Nước"));

        JMenu tk = new JMenu("Thống kê");
        tk.add(new JMenuItem("Doanh thu"));

        menuBar.add(dm);
        menuBar.add(nv);
        menuBar.add(tk);

        setJMenuBar(menuBar);

        JLabel lbl = new JLabel("HỆ THỐNG QUẢN LÝ NHÀ TRỌ", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 26));
        add(lbl, BorderLayout.CENTER);
    }
}