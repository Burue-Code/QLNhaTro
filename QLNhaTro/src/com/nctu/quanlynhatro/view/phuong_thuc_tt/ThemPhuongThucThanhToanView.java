package com.nctu.quanlynhatro.view.phuong_thuc_tt;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThemPhuongThucThanhToanView extends JFrame {

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

        // =================================================================
        // 1. TẠO CONTAINER CHỨA CẢ FORM VÀ NÚT
        // =================================================================
        JPanel containerPanel = new JPanel(new BorderLayout(0, 15)); 

        // --- A. FORM NHẬP LIỆU ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Thông tin phương thức"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 1: Tên Phương Thức (Dời lên vị trí đầu tiên)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Tên Phương Thức:"), gbc);

        txtTenPT = new JTextField();
        txtTenPT.setPreferredSize(new Dimension(250, 30));
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtTenPT, gbc);

        // --- B. PANEL NÚT BẤM ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        
        btnThoat = new JButton("Thoát");
        btnThoat.setPreferredSize(new Dimension(100, 35));
        
        btnThem = new JButton("Thêm");
        btnThem.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(btnThoat);
        buttonPanel.add(btnThem);

        // --- C. GỘP VÀO CONTAINER ---
        containerPanel.add(formPanel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Đặt container vào giữa
        mainPanel.add(containerPanel, BorderLayout.CENTER);

        // =================================================================
        // LỆNH PACK() ĐỂ CO KHUNG VỪA KHÍT
        // =================================================================
        pack(); 
        setLocationRelativeTo(null); 

        // =================================================================
        // 2. XỬ LÝ SỰ KIỆN
        // =================================================================
        btnThoat.addActionListener(e -> this.dispose());

        btnThem.addActionListener(e -> {
            if (txtTenPT.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên phương thức thanh toán!");
                txtTenPT.requestFocus();
                return;
            }

            // Tự động sinh mã PT (Ẩn khỏi giao diện nhưng vẫn thêm vào bảng)
            String autoID = "PT" + String.format("%02d", tableModel.getRowCount() + 1);

            // Thêm vào bảng
            tableModel.addRow(new Object[]{
                autoID,
                txtTenPT.getText().trim()
            });

            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            this.dispose();
        });
    }
}