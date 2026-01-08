package com.nctu.quanlynhatro.view.khach_hang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class KhachHangView extends JFrame {

    // Khai báo các component
    private JTextField txtMaKH, txtTenKH;
    private JTextField txtCCCD, txtSDT;
    private JTextField txtQueQuan;
    private JComboBox<String> cboGioiTinh;

    private JButton btnThem, btnSua, btnXoa, btnThoat;

    // Bảng và Model
    private JTable tblKhachHang;
    private DefaultTableModel tableModel;

    public KhachHangView() {
        // Cấu hình Form
        setTitle("Quản lý Thông Tin Khách Hàng");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng form không tắt App

        // --- PANEL CHÍNH ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // =================================================================
        // PHẦN 1: NORTH - NHẬP LIỆU & NÚT BẤM
        // =================================================================
        JPanel topPanel = new JPanel(new BorderLayout());

        // 1.1 Tiêu đề
        JLabel lblTitle = new JLabel("HỒ SƠ KHÁCH HÀNG / SINH VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Form nhập liệu (GridBagLayout)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Thông tin cá nhân"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Dòng 1: Mã KH & Họ Tên ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Mã Khách Hàng:"), gbc);
        
        txtMaKH = new JTextField(15);
        txtMaKH.setEditable(false); // Mã tự động, không cho sửa
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtMaKH, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Họ và Tên:"), gbc);
        
        txtTenKH = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtTenKH, gbc);

        // --- Dòng 2: CCCD & SĐT ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("CCCD/CMND:"), gbc);
        
        txtCCCD = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(txtCCCD, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Số Điện Thoại:"), gbc);
        
        txtSDT = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(txtSDT, gbc);

        // --- Dòng 3: Quê Quán & Giới Tính ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Quê Quán:"), gbc);
        
        txtQueQuan = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        formPanel.add(txtQueQuan, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Giới Tính:"), gbc);
        
        String[] genders = {"Nam", "Nữ", "Khác"};
        cboGioiTinh = new JComboBox<>(genders);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0;
        formPanel.add(cboGioiTinh, gbc);

        topPanel.add(formPanel, BorderLayout.CENTER);

        // 1.3 Panel Nút Bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnThoat = new JButton("Thoát");

        Dimension btnSize = new Dimension(100, 35);
        btnThem.setPreferredSize(btnSize);
        btnSua.setPreferredSize(btnSize);
        btnXoa.setPreferredSize(btnSize);
        btnThoat.setPreferredSize(btnSize);

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnThoat);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // =================================================================
        // PHẦN 2: CENTER - DANH SÁCH KHÁCH HÀNG (TRỐNG)
        // =================================================================
        String[] headers = {"Mã KH", "Họ Tên", "CCCD", "SĐT", "Quê Quán", "Giới Tính"};
        
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblKhachHang = new JTable(tableModel);
        tblKhachHang.setRowHeight(25);
        tblKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
        scrollPane.setBorder(new TitledBorder("Danh sách khách hàng"));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // =================================================================
        // PHẦN 3: XỬ LÝ SỰ KIỆN
        // =================================================================
        
        // 3.1 Click bảng -> Đổ dữ liệu lên Form
        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblKhachHang.getSelectedRow();
                if (row >= 0) {
                    txtMaKH.setText(getValue(row, 0));
                    txtTenKH.setText(getValue(row, 1));
                    txtCCCD.setText(getValue(row, 2));
                    txtSDT.setText(getValue(row, 3));
                    txtQueQuan.setText(getValue(row, 4));
                    cboGioiTinh.setSelectedItem(getValue(row, 5));
                }
            }
        });

        // 3.2 Nút Thêm
        btnThem.addActionListener(e -> {
            // Giả lập mã KH tăng tự động
            String maMoi = "KH" + (tableModel.getRowCount() + 100); 
            tableModel.addRow(new Object[]{
                maMoi,
                txtTenKH.getText(),
                txtCCCD.getText(),
                txtSDT.getText(),
                txtQueQuan.getText(),
                cboGioiTinh.getSelectedItem()
            });
            resetForm();
        });

        // 3.3 Nút Sửa
        btnSua.addActionListener(e -> {
            int row = tblKhachHang.getSelectedRow();
            if (row >= 0) {
                tableModel.setValueAt(txtTenKH.getText(), row, 1);
                tableModel.setValueAt(txtCCCD.getText(), row, 2);
                tableModel.setValueAt(txtSDT.getText(), row, 3);
                tableModel.setValueAt(txtQueQuan.getText(), row, 4);
                tableModel.setValueAt(cboGioiTinh.getSelectedItem(), row, 5);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
            }
        });

        // 3.4 Nút Xóa
        btnXoa.addActionListener(e -> {
            int row = tblKhachHang.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(row);
                    resetForm();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
            }
        });

        // 3.5 Nút Thoát
        btnThoat.addActionListener(e -> this.dispose());
    }

    // Helper: Lấy giá trị chuỗi an toàn
    private String getValue(int row, int col) {
        Object val = tableModel.getValueAt(row, col);
        return val == null ? "" : val.toString();
    }

    // Helper: Reset form
    private void resetForm() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtCCCD.setText("");
        txtSDT.setText("");
        txtQueQuan.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtTenKH.requestFocus();
    }

    // Main để chạy thử
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new KhachHangView().setVisible(true));
    }
}