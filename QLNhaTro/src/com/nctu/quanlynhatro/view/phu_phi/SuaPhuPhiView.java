package com.nctu.quanlynhatro.view.phu_phi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SuaPhuPhiView extends JFrame {

    private JTextField txtTenPP, txtGia;
    private JButton btnThoat, btnLuu;
    private DefaultTableModel tableModel;
    private int rowIndex;

    public SuaPhuPhiView(DefaultTableModel model, int row) {
        this.tableModel = model;
        this.rowIndex = row;
        
        setTitle("Cập Nhật Phụ Phí");
        // Không setSize cứng, dùng pack() ở cuối
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // =================================================================
        // 1. TẠO CONTAINER CHỨA CẢ FORM VÀ NÚT (Để nút dính sát form)
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

        txtTenPP = new JTextField(20);
        txtTenPP.setText(getValue(1)); // Load Tên cũ
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtTenPP, gbc);

        // Dòng 2: Giá
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Đơn Giá:"), gbc);

        txtGia = new JTextField(20);
        txtGia.setText(getValue(2)); // Load Giá cũ
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(txtGia, gbc);

        // --- B. PANEL NÚT BẤM ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        
        btnThoat = new JButton("Thoát");
        btnThoat.setPreferredSize(new Dimension(100, 35));
        
        btnLuu = new JButton("Lưu Thay Đổi");
        btnLuu.setPreferredSize(new Dimension(120, 35));

        buttonPanel.add(btnThoat);
        buttonPanel.add(btnLuu);

        // --- C. GỘP VÀO CONTAINER ---
        containerPanel.add(formPanel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Đặt container vào giữa
        mainPanel.add(containerPanel, BorderLayout.CENTER);

        // =================================================================
        // QUAN TRỌNG: LỆNH PACK()
        // =================================================================
        pack(); 
        setLocationRelativeTo(null); 

        // =================================================================
        // 2. XỬ LÝ SỰ KIỆN
        // =================================================================
        btnThoat.addActionListener(e -> this.dispose());

        btnLuu.addActionListener(e -> {
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
                
                // Cập nhật lại vào bảng (Cột 1: Tên, Cột 2: Giá)
                tableModel.setValueAt(txtTenPP.getText().trim(), rowIndex, 1);
                tableModel.setValueAt(txtGia.getText().trim(), rowIndex, 2);

                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                this.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá phải là số và không âm!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                txtGia.requestFocus();
            }
        });
    }

    // Helper lấy dữ liệu
    private String getValue(int col) {
        Object val = tableModel.getValueAt(rowIndex, col);
        return val == null ? "" : val.toString();
    }
}