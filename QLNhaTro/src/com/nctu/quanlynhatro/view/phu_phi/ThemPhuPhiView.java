package com.nctu.quanlynhatro.view.phu_phi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThemPhuPhiView extends JFrame {

    private JTextField txtTenPP, txtGia;
    private JButton btnThoat, btnXacNhan;
    private DefaultTableModel tableModel;

    public ThemPhuPhiView(DefaultTableModel model) {
        this.tableModel = model;
        
        setTitle("Thêm Phụ Phí Mới");
        // setSize(500, 300); <-- BỎ DÒNG NÀY
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
        formPanel.setBorder(new TitledBorder("Thông tin phụ phí"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 1: Tên Phụ Phí
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Tên Phụ Phí:"), gbc);

        txtTenPP = new JTextField(20); // Số 20 để định hình chiều rộng ban đầu
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtTenPP, gbc);

        // Dòng 2: Giá
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Đơn Giá:"), gbc);

        txtGia = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(txtGia, gbc);

        // --- B. PANEL NÚT BẤM ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        
        btnThoat = new JButton("Thoát");
        btnThoat.setPreferredSize(new Dimension(100, 35));
        
        btnXacNhan = new JButton("Xác Nhận");
        btnXacNhan.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(btnThoat);
        buttonPanel.add(btnXacNhan);

        // --- C. GỘP VÀO CONTAINER ---
        containerPanel.add(formPanel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Đặt container vào giữa
        mainPanel.add(containerPanel, BorderLayout.CENTER);

        // =================================================================
        // QUAN TRỌNG: LỆNH PACK() ĐỂ CO KHUNG VỪA KHÍT
        // =================================================================
        pack(); // Tự động co giãn cửa sổ vừa với nội dung
        setLocationRelativeTo(null); // Ra giữa màn hình (phải gọi sau pack)

        // =================================================================
        // 2. XỬ LÝ SỰ KIỆN
        // =================================================================
        btnThoat.addActionListener(e -> this.dispose());

        btnXacNhan.addActionListener(e -> {
            if (txtTenPP.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên phụ phí!");
                txtTenPP.requestFocus();
                return;
            }
            if (txtGia.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn giá!");
                txtGia.requestFocus();
                return;
            }

            try {
                long gia = Long.parseLong(txtGia.getText().trim().replace(",", ""));
                if (gia < 0) throw new NumberFormatException();
                
                String maMoi = "PP" + String.format("%03d", tableModel.getRowCount() + 1);
                
                tableModel.addRow(new Object[]{
                    maMoi,
                    txtTenPP.getText().trim(),
                    txtGia.getText().trim()
                });

                JOptionPane.showMessageDialog(this, "Thêm phụ phí thành công!");
                this.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá phải là số và không âm!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                txtGia.requestFocus();
            }
        });
    }
}