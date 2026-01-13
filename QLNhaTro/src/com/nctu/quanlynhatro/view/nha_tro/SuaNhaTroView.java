package com.nctu.quanlynhatro.view.nha_tro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SuaNhaTroView extends JFrame {

    private JTextField txtTenNha, txtSoPhong, txtDiaChi;
    private JTextArea txtGhiChu;
    private JComboBox<String> cboTrangThai;
    private JButton btnLuu, btnHuy;
    private DefaultTableModel tableModel;
    private int rowIndex;

    public SuaNhaTroView(DefaultTableModel model, int row) {
        this.tableModel = model;
        this.rowIndex = row;
        setTitle("Cập Nhật Nhà Trọ");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Chỉnh sửa thông tin"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Load dữ liệu: Cột 0 là Mã (bỏ qua không hiển thị), bắt đầu từ Cột 1
        
        // Hàng 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Tên Nhà Trọ:"), gbc);
        txtTenNha = new JTextField(model.getValueAt(row, 1).toString(), 15);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(txtTenNha, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Số Lượng Phòng:"), gbc);
        txtSoPhong = new JTextField(model.getValueAt(row, 2).toString(), 15);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(txtSoPhong, gbc);

        // Hàng 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Địa Chỉ:"), gbc);
        txtDiaChi = new JTextField(model.getValueAt(row, 3).toString(), 15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; formPanel.add(txtDiaChi, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Trạng Thái:"), gbc);
        String[] trangThai = {"Đang hoạt động", "Đang sửa chữa", "Ngừng hoạt động", "Còn phòng", "Hết phòng"};
        cboTrangThai = new JComboBox<>(trangThai);
        cboTrangThai.setSelectedItem(model.getValueAt(row, 4).toString());
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0; formPanel.add(cboTrangThai, gbc);

        // Hàng 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Ghi Chú:"), gbc);
        Object ghiChuVal = model.getValueAt(row, 5);
        txtGhiChu = new JTextArea(ghiChuVal != null ? ghiChuVal.toString() : "", 3, 20);
        txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtGhiChu.setLineWrap(true);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; formPanel.add(txtGhiChu, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(100, 35));
        btnHuy.setFont(new Font("Arial", Font.PLAIN, 13));
        
        btnLuu = new JButton("Cập Nhật");
        btnLuu.setPreferredSize(new Dimension(100, 35));
        btnLuu.setFont(new Font("Arial", Font.PLAIN, 13));

        btnLuu.addActionListener(e -> {
        	
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

            // Cập nhật lại vào bảng (Cột 0 giữ nguyên, cập nhật từ cột 1 -> 5)
            tableModel.setValueAt(txtTenNha.getText(), rowIndex, 1);
            tableModel.setValueAt(txtSoPhong.getText(), rowIndex, 2);
            tableModel.setValueAt(txtDiaChi.getText(), rowIndex, 3);
            tableModel.setValueAt(cboTrangThai.getSelectedItem(), rowIndex, 4);
            tableModel.setValueAt(txtGhiChu.getText(), rowIndex, 5);

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            this.dispose();
        });

        buttonPanel.add(btnHuy);
        buttonPanel.add(btnLuu);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
}


