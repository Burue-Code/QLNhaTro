package com.nctu.quanlynhatro.view.dien_nuoc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DienNuocView extends JFrame {

    // Khai báo các component
    private JTextField txtMaDN, txtThangNam;
    private JTextField txtDienCu, txtDienMoi;
    private JTextField txtNuocCu, txtNuocMoi;
    private JTextField txtThanhTien;
    // BỔ SUNG: Khai báo thêm nút btnXoa
    private JButton btnLamMoi, btnTinhTien, btnXoa, btnThoat;
    
    // Khai báo bảng và model
    private JTable tblDanhSach;
    private DefaultTableModel tableModel;

    public DienNuocView() {
        setTitle("Quản lý Phiếu Điện Nước");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // --- PANEL CHÍNH ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // =================================================================
        // PHẦN 1: NORTH - NHẬP LIỆU & NÚT BẤM
        // =================================================================
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("PHIẾU TÍNH TIỀN ĐIỆN, NƯỚC", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Thông tin phiếu"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 1: Mã số
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Mã số:"), gbc);
        txtMaDN = new JTextField(15); 
        txtMaDN.setEditable(false); 
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtMaDN, gbc);

        // Dòng 2: Tháng/Năm
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Tháng/Năm:"), gbc);
        txtThangNam = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(txtThangNam, gbc);

        // Dòng 3: Điện
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Điện cũ:"), gbc);
        txtDienCu = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        formPanel.add(txtDienCu, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Điện mới:"), gbc);
        txtDienMoi = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0;
        formPanel.add(txtDienMoi, gbc);

        // Dòng 4: Nước
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Nước cũ:"), gbc);
        txtNuocCu = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        formPanel.add(txtNuocCu, gbc);

        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Nước mới:"), gbc);
        txtNuocMoi = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 1.0;
        formPanel.add(txtNuocMoi, gbc);

        // Dòng 5: Thành tiền
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Thành tiền:"), gbc);
        txtThanhTien = new JTextField(15); 
        txtThanhTien.setEditable(false);
        txtThanhTien.setFont(new Font("Arial", Font.BOLD, 14));
        txtThanhTien.setForeground(Color.RED);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        formPanel.add(txtThanhTien, gbc);

        topPanel.add(formPanel, BorderLayout.CENTER);

        // --- BỔ SUNG NÚT XÓA VÀO PANEL ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // Khởi tạo các nút (Lưu ý: Tên biến cũ nhưng Label mới theo yêu cầu của bạn)
        btnLamMoi = new JButton("Thêm");
        btnTinhTien = new JButton("Sửa");
        btnXoa = new JButton("Xóa"); // Nút mới
        btnThoat = new JButton("Thoát");
        
        Dimension btnSize = new Dimension(110, 35);
        btnLamMoi.setPreferredSize(btnSize);
        btnTinhTien.setPreferredSize(btnSize);
        btnXoa.setPreferredSize(btnSize); // Set kích thước cho nút Xóa
        btnThoat.setPreferredSize(btnSize);
        
        // Thêm vào Panel theo thứ tự: Thêm -> Sửa -> Xóa -> Thoát
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnTinhTien);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnThoat);
        
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);


        // =================================================================
        // PHẦN 2: CENTER - DANH SÁCH (TRỐNG)
        // =================================================================
        String[] columnHeaders = {"Mã ĐN", "Tháng/Năm", "Điện Cũ", "Điện Mới", "Nước Cũ", "Nước Mới", "Thành Tiền"};
        
        tableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDanhSach = new JTable(tableModel);
        tblDanhSach.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tblDanhSach);
        scrollPane.setBorder(new TitledBorder("Danh sách hóa đơn"));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // =================================================================
        // PHẦN 3: SỰ KIỆN
        // =================================================================
        
        tblDanhSach.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblDanhSach.getSelectedRow();
                if (selectedRow >= 0) {
                    txtMaDN.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtThangNam.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtDienCu.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtDienMoi.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtNuocCu.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    txtNuocMoi.setText(tableModel.getValueAt(selectedRow, 5).toString());
                    txtThanhTien.setText(tableModel.getValueAt(selectedRow, 6).toString());
                }
            }
        });

        // Sự kiện nút Xóa (Xóa dòng trên bảng để test)
        btnXoa.addActionListener(e -> {
            int selectedRow = tblDanhSach.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
            } else {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow); // Xóa khỏi bảng giao diện
                    // Sau này sẽ thêm code xóa khỏi CSDL ở đây
                    resetForm(); // Xóa trắng form nhập
                }
            }
        });
        
        // Sự kiện nút Thêm (Tạm thời để demo thêm dòng vào bảng)
        btnLamMoi.addActionListener(e -> {
             // Demo thêm dòng giả để test nút xóa
             tableModel.addRow(new Object[]{"Demo", "10/2025", "100", "200", "50", "60", "500.000"});
        });

        // Nút Thoát
        btnThoat.addActionListener(e -> System.exit(0));
    }

    // Hàm phụ để xóa trắng form
    private void resetForm() {
        txtMaDN.setText("");
        txtThangNam.setText("");
        txtDienCu.setText("");
        txtDienMoi.setText("");
        txtNuocCu.setText("");
        txtNuocMoi.setText("");
        txtThanhTien.setText("");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> {
            new DienNuocView().setVisible(true);
        });
    }
}