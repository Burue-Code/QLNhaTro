package com.nctu.quanlynhatro.view.hoa_dong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoaDonView extends JFrame {

    // Khai báo các component
    private JTextField txtMaHD, txtMaPhong;
    private JTextField txtNgayLap, txtTongTien;
    private JComboBox<String> cboTrangThai; // Dùng ComboBox cho trạng thái (Đã thu/Chưa thu)
    private JTextArea txtGhiChu; // Dùng TextArea cho ghi chú
    
    private JButton btnThem, btnSua, btnXoa, btnThoat;
    
    // Khai báo bảng và model
    private JTable tblHoaDon;
    private DefaultTableModel tableModel;

    public HoaDonView() {
        // Cấu hình Form
        setTitle("Quản lý Hóa Đơn");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form này, không tắt cả App

        // --- PANEL CHÍNH ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // =================================================================
        // PHẦN 1: NORTH - NHẬP LIỆU & NÚT BẤM
        // =================================================================
        JPanel topPanel = new JPanel(new BorderLayout());

        // 1.1 Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ HÓA ĐƠN THANH TOÁN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Form nhập liệu (GridBagLayout)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Thông tin hóa đơn"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa các ô
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Dòng 1: Mã HĐ & Phòng ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Mã Hóa Đơn:"), gbc);
        
        txtMaHD = new JTextField(15);
        txtMaHD.setEditable(false); // Mã tự tăng nên không cho sửa
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtMaHD, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Mã Phòng:"), gbc);
        
        txtMaPhong = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtMaPhong, gbc);

        // --- Dòng 2: Ngày Lập & Tổng Tiền ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Ngày Lập:"), gbc);
        
        txtNgayLap = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(txtNgayLap, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Tổng Tiền:"), gbc);
        
        txtTongTien = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(txtTongTien, gbc);

        // --- Dòng 3: Trạng Thái & Ghi Chú ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Trạng Thái:"), gbc);
        
        String[] trangThai = {"Chưa thanh toán", "Đã thanh toán"};
        cboTrangThai = new JComboBox<>(trangThai);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        formPanel.add(cboTrangThai, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Ghi Chú:"), gbc);
        
        txtGhiChu = new JTextArea(2, 15); // TextArea cho ghi chú dài
        txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0;
        formPanel.add(txtGhiChu, gbc);

        topPanel.add(formPanel, BorderLayout.CENTER);

        // 1.3 Panel Nút Bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnThoat = new JButton("Thoát");
        
        // Đặt kích thước nút
        Dimension btnSize = new Dimension(100, 35);
        btnThem.setPreferredSize(btnSize);
        btnSua.setPreferredSize(btnSize);
        btnXoa.setPreferredSize(btnSize);
        btnThoat.setPreferredSize(btnSize);
        
        // Thêm icon (nếu muốn, ở đây để text cho đơn giản)
        // btnThem.setIcon(new ImageIcon("path/to/icon.png"));

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnThoat);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // =================================================================
        // PHẦN 2: CENTER - DANH SÁCH HÓA ĐƠN (TRỐNG)
        // =================================================================
        // Tiêu đề cột
        String[] headers = {"Mã HĐ", "Phòng", "Ngày Lập", "Tổng Tiền", "Trạng Thái", "Ghi Chú"};
        
        // Model không có dữ liệu ban đầu (rowCount = 0)
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên bảng
            }
        };

        tblHoaDon = new JTable(tableModel);
        tblHoaDon.setRowHeight(25);
        tblHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tblHoaDon);
        scrollPane.setBorder(new TitledBorder("Danh sách hóa đơn"));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // =================================================================
        // PHẦN 3: XỬ LÝ SỰ KIỆN
        // =================================================================
        
        // 3.1 Click vào bảng -> Đổ dữ liệu lên Form
        tblHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblHoaDon.getSelectedRow();
                if (selectedRow >= 0) {
                    txtMaHD.setText(getValue(selectedRow, 0));
                    txtMaPhong.setText(getValue(selectedRow, 1));
                    txtNgayLap.setText(getValue(selectedRow, 2));
                    txtTongTien.setText(getValue(selectedRow, 3));
                    cboTrangThai.setSelectedItem(getValue(selectedRow, 4));
                    txtGhiChu.setText(getValue(selectedRow, 5));
                }
            }
        });

        // 3.2 Sự kiện nút Thêm (Giả lập thêm vào bảng)
        btnThem.addActionListener(e -> {
            // Demo thêm dòng để test
            String maMoi = "HD" + (tableModel.getRowCount() + 1);
            tableModel.addRow(new Object[]{
                maMoi, 
                txtMaPhong.getText(), 
                txtNgayLap.getText(),
                txtTongTien.getText(),
                cboTrangThai.getSelectedItem(),
                txtGhiChu.getText()
            });
            resetForm();
        });

        // 3.3 Sự kiện nút Xóa
        btnXoa.addActionListener(e -> {
            int selectedRow = tblHoaDon.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa!");
            } else {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow);
                    resetForm();
                }
            }
        });
        
        // 3.4 Sự kiện nút Sửa (Cập nhật dòng đang chọn)
        btnSua.addActionListener(e -> {
             int selectedRow = tblHoaDon.getSelectedRow();
             if (selectedRow >= 0) {
                 tableModel.setValueAt(txtMaPhong.getText(), selectedRow, 1);
                 tableModel.setValueAt(txtNgayLap.getText(), selectedRow, 2);
                 tableModel.setValueAt(txtTongTien.getText(), selectedRow, 3);
                 tableModel.setValueAt(cboTrangThai.getSelectedItem(), selectedRow, 4);
                 tableModel.setValueAt(txtGhiChu.getText(), selectedRow, 5);
                 JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
             } else {
                 JOptionPane.showMessageDialog(this, "Chọn dòng để sửa!");
             }
        });

        // 3.5 Sự kiện nút Thoát
        btnThoat.addActionListener(e -> this.dispose());
    }

    // Hàm phụ lấy giá trị từ bảng an toàn
    private String getValue(int row, int col) {
        return tableModel.getValueAt(row, col) == null ? "" : tableModel.getValueAt(row, col).toString();
    }
    
    // Hàm làm mới form
    private void resetForm() {
        txtMaHD.setText("");
        txtMaPhong.setText("");
        txtNgayLap.setText("");
        txtTongTien.setText("");
        txtGhiChu.setText("");
        cboTrangThai.setSelectedIndex(0);
    }

    // Hàm Main để chạy thử riêng form này
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new HoaDonView().setVisible(true);
        });
    }
}