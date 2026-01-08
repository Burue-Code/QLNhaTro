package com.nctu.quanlynhatro.view.phong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PhongView extends JFrame {

    // Khai báo các component nhập liệu
    private JTextField txtMaPhong, txtTenPhong;
    private JTextField txtGiaPhong, txtDienTich;
    private JComboBox<String> cboLoaiPhong, cboTrangThai;
    private JTextArea txtMoTa;

    // Các nút chức năng
    private JButton btnThem, btnSua, btnXoa, btnThoat;

    // Bảng và Model dữ liệu
    private JTable tblPhong;
    private DefaultTableModel tableModel;

    public PhongView() {
        // Cấu hình Form chính
        setTitle("Quản lý Danh Sách Phòng Trọ");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng form này không tắt cả App

        // --- PANEL CHÍNH ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // =================================================================
        // PHẦN 1: NORTH - FORM NHẬP LIỆU & NÚT BẤM
        // =================================================================
        JPanel topPanel = new JPanel(new BorderLayout());

        // 1.1 Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ THÔNG TIN PHÒNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Form nhập liệu (GridBagLayout)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Thông tin chi tiết phòng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Padding cho thoáng
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Dòng 1: Mã Phòng & Tên Phòng ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Mã Phòng:"), gbc);
        
        txtMaPhong = new JTextField(15);
        txtMaPhong.setEditable(false); // Mã tự động
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtMaPhong, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Tên Phòng:"), gbc);
        
        txtTenPhong = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtTenPhong, gbc);

        // --- Dòng 2: Loại Phòng & Giá Phòng ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Loại Phòng:"), gbc);
        
        String[] loaiPhong = {"Phòng Thường", "Phòng VIP", "Phòng Có Gác", "Phòng Máy Lạnh"};
        cboLoaiPhong = new JComboBox<>(loaiPhong);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(cboLoaiPhong, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Giá Phòng:"), gbc);
        
        txtGiaPhong = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(txtGiaPhong, gbc);

        // --- Dòng 3: Diện Tích & Trạng Thái ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Diện Tích (m2):"), gbc);
        
        txtDienTich = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        formPanel.add(txtDienTich, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Trạng Thái:"), gbc);
        
        String[] trangThai = {"Trống", "Đang thuê", "Đang sửa chữa"};
        cboTrangThai = new JComboBox<>(trangThai);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0;
        formPanel.add(cboTrangThai, gbc);

        // --- Dòng 4: Mô Tả (TextArea) ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Mô Tả Thêm:"), gbc);
        
        txtMoTa = new JTextArea(3, 15);
        txtMoTa.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa); // Thêm scroll cho mô tả
        
        gbc.gridx = 1; gbc.gridy = 3; 
        gbc.gridwidth = 3; // Chiếm hết 3 ô còn lại
        gbc.weightx = 1.0;
        formPanel.add(scrollMoTa, gbc);

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
        // PHẦN 2: CENTER - DANH SÁCH PHÒNG (TRỐNG)
        // =================================================================
        String[] headers = {
            "Mã Phòng", "Tên Phòng", "Loại Phòng", "Giá Thuê", "Diện Tích", "Trạng Thái", "Mô Tả"
        };
        
        // Model không có dữ liệu ban đầu
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPhong = new JTable(tableModel);
        tblPhong.setRowHeight(25);
        tblPhong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Điều chỉnh độ rộng cột Mô tả cho dễ nhìn
        tblPhong.getColumnModel().getColumn(6).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(tblPhong);
        scrollPane.setBorder(new TitledBorder("Danh sách phòng hiện có"));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // =================================================================
        // PHẦN 3: XỬ LÝ SỰ KIỆN
        // =================================================================
        
        // 3.1 Click bảng -> Đổ dữ liệu lên Form
        tblPhong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPhong.getSelectedRow();
                if (row >= 0) {
                    txtMaPhong.setText(getValue(row, 0));
                    txtTenPhong.setText(getValue(row, 1));
                    cboLoaiPhong.setSelectedItem(getValue(row, 2));
                    txtGiaPhong.setText(getValue(row, 3));
                    txtDienTich.setText(getValue(row, 4));
                    cboTrangThai.setSelectedItem(getValue(row, 5));
                    txtMoTa.setText(getValue(row, 6));
                }
            }
        });

        // 3.2 Nút Thêm (Demo)
        btnThem.addActionListener(e -> {
            String maMoi = "P" + (100 + tableModel.getRowCount() + 1);
            tableModel.addRow(new Object[]{
                maMoi,
                txtTenPhong.getText(),
                cboLoaiPhong.getSelectedItem(),
                txtGiaPhong.getText(),
                txtDienTich.getText(),
                cboTrangThai.getSelectedItem(),
                txtMoTa.getText()
            });
            resetForm();
        });

        // 3.3 Nút Sửa
        btnSua.addActionListener(e -> {
            int row = tblPhong.getSelectedRow();
            if (row >= 0) {
                tableModel.setValueAt(txtTenPhong.getText(), row, 1);
                tableModel.setValueAt(cboLoaiPhong.getSelectedItem(), row, 2);
                tableModel.setValueAt(txtGiaPhong.getText(), row, 3);
                tableModel.setValueAt(txtDienTich.getText(), row, 4);
                tableModel.setValueAt(cboTrangThai.getSelectedItem(), row, 5);
                tableModel.setValueAt(txtMoTa.getText(), row, 6);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần sửa!");
            }
        });

        // 3.4 Nút Xóa
        btnXoa.addActionListener(e -> {
            int row = tblPhong.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa phòng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(row);
                    resetForm();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần xóa!");
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
        txtMaPhong.setText("");
        txtTenPhong.setText("");
        txtGiaPhong.setText("");
        txtDienTich.setText("");
        txtMoTa.setText("");
        cboLoaiPhong.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        txtTenPhong.requestFocus();
    }

    // Main Test
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new PhongView().setVisible(true));
    }
}