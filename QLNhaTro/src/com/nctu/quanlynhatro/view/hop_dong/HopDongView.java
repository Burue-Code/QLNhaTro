package com.nctu.quanlynhatro.view.hop_dong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HopDongView extends JFrame {

    // Khai báo các component nhập liệu
    private JTextField txtMaHD, txtMaPhong, txtTenNguoiThue;
    private JTextField txtGiaPhong, txtTienCoc;
    private JTextField txtNgayBatDau, txtNgayKetThuc;
    private JComboBox<String> cboTrangThai;

    // Các nút chức năng
    private JButton btnThem, btnSua, btnXoa, btnThoat;

    // Bảng và dữ liệu
    private JTable tblHopDong;
    private DefaultTableModel tableModel;

    public HopDongView() {
        // Cấu hình Form chính
        setTitle("Quản lý Hợp Đồng Thuê Phòng");
        setSize(900, 650); // Form này nhiều trường nên để rộng hơn chút
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng form này không tắt cả app

        // --- PANEL CHÍNH ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // =================================================================
        // PHẦN 1: NORTH - FORM NHẬP LIỆU & NÚT BẤM
        // =================================================================
        JPanel topPanel = new JPanel(new BorderLayout());

        // 1.1 Tiêu đề
        JLabel lblTitle = new JLabel("HỢP ĐỒNG THUÊ NHÀ TRỌ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Form nhập liệu (GridBagLayout - 4 dòng, 4 cột)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Thông tin hợp đồng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Padding rộng hơn chút cho thoáng
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Dòng 1: Mã HĐ & Mã Phòng ---
        // Cột 0, 1
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Mã Hợp Đồng:"), gbc);
        txtMaHD = new JTextField(15); 
        txtMaHD.setEditable(false); // Không cho sửa mã
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(txtMaHD, gbc);

        // Cột 2, 3
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; formPanel.add(new JLabel("Mã Phòng:"), gbc);
        txtMaPhong = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(txtMaPhong, gbc);

        // --- Dòng 2: Người Thuê & Giá Phòng ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; formPanel.add(new JLabel("Người Thuê:"), gbc);
        txtTenNguoiThue = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; formPanel.add(txtTenNguoiThue, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0; formPanel.add(new JLabel("Giá Phòng:"), gbc);
        txtGiaPhong = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0; formPanel.add(txtGiaPhong, gbc);

        // --- Dòng 3: Ngày Bắt Đầu & Ngày Kết Thúc ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; formPanel.add(new JLabel("Ngày Bắt Đầu:"), gbc);
        txtNgayBatDau = new JTextField(15); // Có thể thay bằng JDateChooser nếu có thư viện
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; formPanel.add(txtNgayBatDau, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0; formPanel.add(new JLabel("Ngày Kết Thúc:"), gbc);
        txtNgayKetThuc = new JTextField(15);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0; formPanel.add(txtNgayKetThuc, gbc);

        // --- Dòng 4: Tiền Cọc & Trạng Thái ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; formPanel.add(new JLabel("Tiền Cọc:"), gbc);
        txtTienCoc = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0; formPanel.add(txtTienCoc, gbc);

        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0; formPanel.add(new JLabel("Trạng Thái:"), gbc);
        String[] status = {"Đang hiệu lực", "Đã thanh lý", "Hết hạn"};
        cboTrangThai = new JComboBox<>(status);
        gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 1.0; formPanel.add(cboTrangThai, gbc);

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
        // PHẦN 2: CENTER - DANH SÁCH (TRỐNG)
        // =================================================================
        String[] headers = {
            "Mã HĐ", "Mã Phòng", "Người Thuê", "Giá Phòng", 
            "Ngày BĐ", "Ngày KT", "Tiền Cọc", "Trạng Thái"
        };
        
        // Model không có dữ liệu ban đầu
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblHopDong = new JTable(tableModel);
        tblHopDong.setRowHeight(25);
        tblHopDong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tblHopDong);
        scrollPane.setBorder(new TitledBorder("Danh sách hợp đồng"));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // =================================================================
        // PHẦN 3: XỬ LÝ SỰ KIỆN
        // =================================================================
        
        // 3.1 Click bảng -> Đổ dữ liệu lên Form
        tblHopDong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblHopDong.getSelectedRow();
                if (row >= 0) {
                    txtMaHD.setText(getValue(row, 0));
                    txtMaPhong.setText(getValue(row, 1));
                    txtTenNguoiThue.setText(getValue(row, 2));
                    txtGiaPhong.setText(getValue(row, 3));
                    txtNgayBatDau.setText(getValue(row, 4));
                    txtNgayKetThuc.setText(getValue(row, 5));
                    txtTienCoc.setText(getValue(row, 6));
                    cboTrangThai.setSelectedItem(getValue(row, 7));
                }
            }
        });

        // 3.2 Nút Thêm (Demo thêm dòng)
        btnThem.addActionListener(e -> {
            String maMoi = "HD" + System.currentTimeMillis() % 1000; // Tạo mã ngẫu nhiên
            tableModel.addRow(new Object[]{
                maMoi,
                txtMaPhong.getText(),
                txtTenNguoiThue.getText(),
                txtGiaPhong.getText(),
                txtNgayBatDau.getText(),
                txtNgayKetThuc.getText(),
                txtTienCoc.getText(),
                cboTrangThai.getSelectedItem()
            });
            resetForm();
        });

        // 3.3 Nút Sửa
        btnSua.addActionListener(e -> {
            int row = tblHopDong.getSelectedRow();
            if (row >= 0) {
                tableModel.setValueAt(txtMaPhong.getText(), row, 1);
                tableModel.setValueAt(txtTenNguoiThue.getText(), row, 2);
                tableModel.setValueAt(txtGiaPhong.getText(), row, 3);
                tableModel.setValueAt(txtNgayBatDau.getText(), row, 4);
                tableModel.setValueAt(txtNgayKetThuc.getText(), row, 5);
                tableModel.setValueAt(txtTienCoc.getText(), row, 6);
                tableModel.setValueAt(cboTrangThai.getSelectedItem(), row, 7);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng để sửa!");
            }
        });

        // 3.4 Nút Xóa
        btnXoa.addActionListener(e -> {
            int row = tblHopDong.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(row);
                    resetForm();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng để xóa!");
            }
        });

        // 3.5 Nút Thoát
        btnThoat.addActionListener(e -> this.dispose());
    }

    // Helper: Lấy giá trị chuỗi từ bảng
    private String getValue(int row, int col) {
        Object val = tableModel.getValueAt(row, col);
        return val == null ? "" : val.toString();
    }

    // Helper: Xóa trắng form
    private void resetForm() {
        txtMaHD.setText("");
        txtMaPhong.setText("");
        txtTenNguoiThue.setText("");
        txtGiaPhong.setText("");
        txtNgayBatDau.setText("");
        txtNgayKetThuc.setText("");
        txtTienCoc.setText("");
        cboTrangThai.setSelectedIndex(0);
    }

    // Main để test
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new HopDongView().setVisible(true));
    }
}