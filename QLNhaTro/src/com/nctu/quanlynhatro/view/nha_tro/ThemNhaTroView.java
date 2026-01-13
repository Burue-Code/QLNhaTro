package com.nctu.quanlynhatro.view.nha_tro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThemNhaTroView extends JFrame {

    private JTextField txtTenNha, txtSoPhong, txtDiaChi;
    private JTextArea txtGhiChu;
    private JComboBox<String> cboTrangThai;
    private JButton btnThem, btnHuy;
    private DefaultTableModel tableModel;

    public ThemNhaTroView(DefaultTableModel model) {
        this.tableModel = model;
        setTitle("Thêm Nhà Trọ Mới");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Form nhập liệu
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Thông tin nhà trọ"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hàng 0: Tên & Số Phòng
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Tên Nhà Trọ:"), gbc);
        txtTenNha = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(txtTenNha, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Số Lượng Phòng:"), gbc);
        txtSoPhong = new JTextField(15);
        txtSoPhong.setToolTipText("Chỉ nhập số");
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(txtSoPhong, gbc);

        // Hàng 1: Địa Chỉ & Trạng Thái
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Địa Chỉ:"), gbc);
        txtDiaChi = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; formPanel.add(txtDiaChi, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Trạng Thái:"), gbc);
        String[] trangThai = {"Đang sửa chữa","Còn phòng", "Hết phòng"};
        cboTrangThai = new JComboBox<>(trangThai);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0; formPanel.add(cboTrangThai, gbc);

        // Hàng 2: Ghi Chú
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Ghi Chú:"), gbc);
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtGhiChu.setLineWrap(true);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; formPanel.add(txtGhiChu, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(100, 35));
        btnHuy.setFont(new Font("Arial", Font.PLAIN, 13));
        
        btnThem = new JButton("Thêm");
        btnThem.setPreferredSize(new Dimension(100, 35));
        btnThem.setFont(new Font("Arial", Font.PLAIN, 13));

        btnHuy.addActionListener(e -> this.dispose());
        btnThem.addActionListener(e -> {
            if(txtTenNha.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập tên nhà trọ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(txtSoPhong.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập số phòng nhà trọ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(txtDiaChi.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập địa chỉ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                int sp = Integer.parseInt(txtSoPhong.getText().trim());
                if(sp <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số phòng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- KHẮC PHỤC LỖI DỒN CỘT TẠI ĐÂY ---
            // Thêm phần tử đầu tiên là "" (hoặc null) để giữ chỗ cho cột MaNT
            tableModel.addRow(new Object[]{
                "", // <--- GIỮ CHỖ CHO MÃ (Sau này DB tự sinh ID thì mình load lại bảng sau)
                txtTenNha.getText(),
                txtSoPhong.getText(),
                txtDiaChi.getText(),
                cboTrangThai.getSelectedItem(),
                txtGhiChu.getText()
            });
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            this.dispose();
            
        });

        buttonPanel.add(btnHuy);
        buttonPanel.add(btnThem);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
}