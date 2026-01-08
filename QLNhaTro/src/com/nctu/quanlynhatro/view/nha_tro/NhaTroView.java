package com.nctu.quanlynhatro.view.nha_tro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NhaTroView extends JFrame {

    // Khai báo các component nhập liệu
    private JTextField txtMaNha, txtTenNha;
    private JTextField txtDiaChi, txtSDT;
    private JTextField txtSoPhong, txtNguoiQuanLy;

    // Các nút chức năng
    private JButton btnThem, btnSua, btnXoa, btnThoat;

    // Bảng và Model dữ liệu
    private JTable tblNhaTro;
    private DefaultTableModel tableModel;

    public NhaTroView() {
        // Cấu hình Form chính
        setTitle("Quản lý Danh Sách Nhà Trọ");
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
        JLabel lblTitle = new JLabel("QUẢN LÝ THÔNG TIN NHÀ TRỌ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Form nhập liệu (GridBagLayout)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Thông tin nhà trọ"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Padding cho thoáng
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Dòng 1: Mã Nhà & Tên Nhà ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Mã Nhà Trọ:"), gbc);
        
        txtMaNha = new JTextField(15);
        txtMaNha.setEditable(false); // Mã tự động, không cho sửa
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtMaNha, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Tên Nhà Trọ:"), gbc);
        
        txtTenNha = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtTenNha, gbc);

        // --- Dòng 2: Địa Chỉ & SĐT ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Địa Chỉ:"), gbc);
        
        txtDiaChi = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(txtDiaChi, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Số Điện Thoại:"), gbc);
        
        txtSDT = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(txtSDT, gbc);

        // --- Dòng 3: Số Lượng Phòng & Người Quản Lý ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Số Lượng Phòng:"), gbc);
        
        txtSoPhong = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        formPanel.add(txtSoPhong, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Người Quản Lý:"), gbc);
        
        txtNguoiQuanLy = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0;
        formPanel.add(txtNguoiQuanLy, gbc);

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
        // PHẦN 2: CENTER - DANH SÁCH NHÀ TRỌ (TRỐNG)
        // =================================================================
        String[] headers = {
            "Mã Nhà", "Tên Nhà Trọ", "Địa Chỉ", "SĐT Liên Hệ", "Số Phòng", "Quản Lý"
        };
        
        // Model không có dữ liệu ban đầu
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblNhaTro = new JTable(tableModel);
        tblNhaTro.setRowHeight(25);
        tblNhaTro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Điều chỉnh độ rộng cột Địa chỉ cho dễ nhìn
        tblNhaTro.getColumnModel().getColumn(2).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(tblNhaTro);
        scrollPane.setBorder(new TitledBorder("Danh sách nhà trọ hệ thống"));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // =================================================================
        // PHẦN 3: XỬ LÝ SỰ KIỆN
        // =================================================================
        
        // 3.1 Click bảng -> Đổ dữ liệu lên Form
        tblNhaTro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblNhaTro.getSelectedRow();
                if (row >= 0) {
                    txtMaNha.setText(getValue(row, 0));
                    txtTenNha.setText(getValue(row, 1));
                    txtDiaChi.setText(getValue(row, 2));
                    txtSDT.setText(getValue(row, 3));
                    txtSoPhong.setText(getValue(row, 4));
                    txtNguoiQuanLy.setText(getValue(row, 5));
                }
            }
        });

        // 3.2 Nút Thêm (Demo)
        btnThem.addActionListener(e -> {
            // Giả lập tạo mã nhà trọ tự động
            String maMoi = "NT" + (tableModel.getRowCount() + 1);
            tableModel.addRow(new Object[]{
                maMoi,
                txtTenNha.getText(),
                txtDiaChi.getText(),
                txtSDT.getText(),
                txtSoPhong.getText(),
                txtNguoiQuanLy.getText()
            });
            resetForm();
        });

        // 3.3 Nút Sửa
        btnSua.addActionListener(e -> {
            int row = tblNhaTro.getSelectedRow();
            if (row >= 0) {
                tableModel.setValueAt(txtTenNha.getText(), row, 1);
                tableModel.setValueAt(txtDiaChi.getText(), row, 2);
                tableModel.setValueAt(txtSDT.getText(), row, 3);
                tableModel.setValueAt(txtSoPhong.getText(), row, 4);
                tableModel.setValueAt(txtNguoiQuanLy.getText(), row, 5);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà trọ cần sửa!");
            }
        });

        // 3.4 Nút Xóa
        btnXoa.addActionListener(e -> {
            int row = tblNhaTro.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                        "Bạn chắc chắn muốn xóa nhà trọ này?\nTất cả phòng thuộc nhà này cũng nên được xem xét.", 
                        "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(row);
                    resetForm();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà trọ cần xóa!");
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
        txtMaNha.setText("");
        txtTenNha.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtSoPhong.setText("");
        txtNguoiQuanLy.setText("");
        txtTenNha.requestFocus();
    }

    // Main Test
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new NhaTroView().setVisible(true));
    }
}