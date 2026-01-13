package com.nctu.quanlynhatro.view.dien_nuoc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class ThemDienNuocView extends JFrame {

    // Các thành phần giao diện
    private JTextField txtMaDN; 
    private JComboBox<String> cboNhaTro, cboSoPhong;
    private JComboBox<String> cboThang; 
    
    private JTextField txtDienCu, txtDienMoi;
    private JTextField txtNuocCu, txtNuocMoi;
    
    private JTextField txtGiaDien, txtGiaNuoc;     
    private JTextField txtTienDien, txtTienNuoc;   
    private JTextField txtTongTien;
    
    private JButton btnThem;
    private DefaultTableModel tableModel;

    // Cấu hình đơn giá mặc định
    private final long DON_GIA_DIEN_MAC_DINH = 3500;
    private final long DON_GIA_NUOC_MAC_DINH = 10000;
    
    private DecimalFormat df = new DecimalFormat("#,###");

    public ThemDienNuocView(DefaultTableModel model) {
        this.tableModel = model;
        setTitle("Thêm Phiếu Điện Nước Mới");
        setSize(800, 500); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // --- FORM NHẬP LIỆU (GRID BAG LAYOUT) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Nhập chỉ số điện nước"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;


        // --- HÀNG 1: Nhà trọ (Trái) - Phòng (Phải) ---
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Nhà trọ:"), gbc);
        cboNhaTro = new JComboBox<>(); 
        // loadDataNhaTro(); // Để trống chờ DB
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(cboNhaTro, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; formPanel.add(new JLabel("Phòng:"), gbc);
        cboSoPhong = new JComboBox<>(); 
        // loadDataPhong(); // Để trống chờ DB
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(cboSoPhong, gbc);

        // --- HÀNG 2: Giá điện (Trái) - Giá nước (Phải) ---
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Đơn giá Điện:"), gbc);
        txtGiaDien = new JTextField(df.format(DON_GIA_DIEN_MAC_DINH));
        txtGiaDien.setEditable(false); 
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtGiaDien, gbc);

        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Đơn giá Nước:"), gbc);
        txtGiaNuoc = new JTextField(df.format(DON_GIA_NUOC_MAC_DINH));
        txtGiaNuoc.setEditable(false);
        gbc.gridx = 3; gbc.gridy = 1; formPanel.add(txtGiaNuoc, gbc);

        // --- HÀNG 3: Số điện cũ (Trái) - Số nước cũ (Phải) ---
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Số điện cũ:"), gbc);
        txtDienCu = new JTextField("0");
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(txtDienCu, gbc);

        gbc.gridx = 2; gbc.gridy = 2; formPanel.add(new JLabel("Số nước cũ:"), gbc);
        txtNuocCu = new JTextField("0");
        gbc.gridx = 3; gbc.gridy = 2; formPanel.add(txtNuocCu, gbc);

        // --- HÀNG 4: Số điện mới (Trái) - Số nước mới (Phải) ---
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Số điện mới:"), gbc);
        txtDienMoi = new JTextField("0");
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(txtDienMoi, gbc);

        gbc.gridx = 2; gbc.gridy = 3; formPanel.add(new JLabel("Số nước mới:"), gbc);
        txtNuocMoi = new JTextField("0");
        gbc.gridx = 3; gbc.gridy = 3; formPanel.add(txtNuocMoi, gbc);

        // --- HÀNG 5: Tiền điện (Trái) - Tiền nước (Phải) ---
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Thành tiền Điện:"), gbc);
        txtTienDien = new JTextField("0");
        txtTienDien.setEditable(false);
        txtTienDien.setForeground(Color.BLUE);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(txtTienDien, gbc);

        gbc.gridx = 2; gbc.gridy = 4; formPanel.add(new JLabel("Thành tiền Nước:"), gbc);
        txtTienNuoc = new JTextField("0");
        txtTienNuoc.setEditable(false);
        txtTienNuoc.setForeground(Color.BLUE);
        gbc.gridx = 3; gbc.gridy = 4; formPanel.add(txtTienNuoc, gbc);

        // --- HÀNG 6: Tháng thu (Combobox) - Tổng tiền (Phải) ---
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Tháng thu:"), gbc);
        
        cboThang = new JComboBox<>();
        loadDataThang(); // Hàm load tháng 1-12 (không có năm)
        gbc.gridx = 1; gbc.gridy = 5; formPanel.add(cboThang, gbc);

        gbc.gridx = 2; gbc.gridy = 5; formPanel.add(new JLabel("TỔNG CỘNG:"), gbc);
        txtTongTien = new JTextField("0");
        txtTongTien.setEditable(false);
        txtTongTien.setForeground(Color.RED);
        txtTongTien.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 3; gbc.gridy = 5; formPanel.add(txtTongTien, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- SỰ KIỆN TỰ ĐỘNG TÍNH TOÁN ---
        DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { tinhToan(); }
            public void removeUpdate(DocumentEvent e) { tinhToan(); }
            public void changedUpdate(DocumentEvent e) { tinhToan(); }
        };
        txtDienCu.getDocument().addDocumentListener(docListener);
        txtDienMoi.getDocument().addDocumentListener(docListener);
        txtNuocCu.getDocument().addDocumentListener(docListener);
        txtNuocMoi.getDocument().addDocumentListener(docListener);

        // --- NÚT BẤM ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnThem = new JButton("Thêm");
        btnThem.setPreferredSize(new Dimension(150, 40));
        btnThem.setFont(new Font("Arial", Font.PLAIN, 14));

        btnThem.addActionListener(e -> {
            // Tính lại lần cuối
            tinhToan(); 
            
            // Lưu vào bảng 
            tableModel.addRow(new Object[]{
                txtMaDN.getText(), 
                cboSoPhong.getSelectedItem(),
                cboThang.getSelectedItem(), // Lấy từ ComboBox
                txtTienDien.getText(), 
                txtTienNuoc.getText(), 
                txtTongTien.getText(),
                "Chưa đóng"
            });
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            this.dispose();
        });

        buttonPanel.add(btnThem);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Load dữ liệu tháng 1 -> 12 (CHỈ CÓ THÁNG)
    private void loadDataThang() {
        cboThang.removeAllItems(); // Xóa cũ nếu có
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem("Tháng " + i);
        }
        // Chọn sẵn tháng hiện tại
        int currentMonth = LocalDate.now().getMonthValue();
        cboThang.setSelectedIndex(currentMonth - 1);
    }

    private void tinhToan() {
        try {
            long dCu = parseLong(txtDienCu.getText());
            long dMoi = parseLong(txtDienMoi.getText());
            long nCu = parseLong(txtNuocCu.getText());
            long nMoi = parseLong(txtNuocMoi.getText());

            long soDien = Math.max(0, dMoi - dCu);
            long soNuoc = Math.max(0, nMoi - nCu);

            long tienDien = soDien * DON_GIA_DIEN_MAC_DINH;
            long tienNuoc = soNuoc * DON_GIA_NUOC_MAC_DINH;
            long tong = tienDien + tienNuoc;

            txtTienDien.setText(df.format(tienDien));
            txtTienNuoc.setText(df.format(tienNuoc));
            txtTongTien.setText(df.format(tong));

        } catch (Exception e) {}
    }

    private long parseLong(String s) {
        try {
            return Long.parseLong(s.trim().replace(",", "").replace(".", ""));
        } catch (Exception e) { return 0; }
    }
}