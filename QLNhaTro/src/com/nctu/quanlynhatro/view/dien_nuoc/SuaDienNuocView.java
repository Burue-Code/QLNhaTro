package com.nctu.quanlynhatro.view.dien_nuoc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;

public class SuaDienNuocView extends JFrame {

    private JTextField txtMaDN;
    private JComboBox<String> cboNhaTro, cboSoPhong;
    private JComboBox<String> cboThang; // Đã đổi thành ComboBox
    
    private JTextField txtDienCu, txtDienMoi;
    private JTextField txtNuocCu, txtNuocMoi;
    private JTextField txtTongTien;
    private JComboBox<String> cboTrangThai;
    private JButton btnLuu;
    private DefaultTableModel tableModel;
    private int rowIndex;

    private DecimalFormat df = new DecimalFormat("#,###");

    public SuaDienNuocView(DefaultTableModel model, int row) {
        this.tableModel = model;
        this.rowIndex = row;
        
        setTitle("Cập Nhật Phiếu Điện Nước");
        setSize(800, 500); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // --- FORM NHẬP LIỆU ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Thông tin hóa đơn"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Dòng 1: Mã ĐN & Nhà Trọ ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Mã Phiếu:"), gbc);
        
        // Lấy dữ liệu từ bảng đổ vào
        txtMaDN = new JTextField(model.getValueAt(row, 0).toString(), 15);
        txtMaDN.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(txtMaDN, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Nhà Trọ:"), gbc);
        cboNhaTro = new JComboBox<>();
        // loadDataNhaTro(); // Để trống
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(cboNhaTro, gbc);

        // --- Dòng 2: Số Phòng & Trạng Thái ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Phòng:"), gbc);
        cboSoPhong = new JComboBox<>();
        // loadDataPhong(); // Để trống
        // Giả sử lấy mã phòng từ bảng và add tạm vào để hiển thị
        String currentRoom = model.getValueAt(row, 1).toString();
        cboSoPhong.addItem(currentRoom); 
        cboSoPhong.setSelectedItem(currentRoom);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; formPanel.add(cboSoPhong, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Trạng Thái:"), gbc);
        String[] status = {"Chưa đóng", "Đã đóng", "Nợ"};
        cboTrangThai = new JComboBox<>(status);
        cboTrangThai.setSelectedItem(model.getValueAt(row, 6).toString());
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0; formPanel.add(cboTrangThai, gbc);

        // --- Dòng 3: Tiền Điện Nước Tháng ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Tháng thu:"), gbc);
        
        cboThang = new JComboBox<>();
        loadDataThang(); // Load Tháng 1 -> 12
        
        // Logic chọn lại tháng cũ từ bảng
        String oldMonthStr = model.getValueAt(row, 2).toString(); // VD: "Tháng 5"
        cboThang.setSelectedItem(oldMonthStr);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; 
        gbc.gridwidth = 3; 
        formPanel.add(cboThang, gbc);
        gbc.gridwidth = 1; 

        // --- Dòng 4: Chỉ số Điện ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Số Điện Cũ:"), gbc);
        txtDienCu = new JTextField("0", 15);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0; formPanel.add(txtDienCu, gbc);

        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Số Điện Mới:"), gbc);
        txtDienMoi = new JTextField("0", 15);
        gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 1.0; formPanel.add(txtDienMoi, gbc);

        // --- Dòng 5: Chỉ số Nước ---
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Số Nước Cũ:"), gbc);
        txtNuocCu = new JTextField("0", 15);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0; formPanel.add(txtNuocCu, gbc);

        gbc.gridx = 2; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Số Nước Mới:"), gbc);
        txtNuocMoi = new JTextField("0", 15);
        gbc.gridx = 3; gbc.gridy = 4; gbc.weightx = 1.0; formPanel.add(txtNuocMoi, gbc);

        // --- Dòng 6: Tổng Tiền ---
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        formPanel.add(new JLabel("TỔNG CỘNG:"), gbc);
        
        txtTongTien = new JTextField(model.getValueAt(row, 5).toString(), 15);
        txtTongTien.setEditable(false);
        txtTongTien.setForeground(Color.RED);
        txtTongTien.setFont(new Font("Arial", Font.BOLD, 18));
        txtTongTien.setHorizontalAlignment(JTextField.CENTER);
        
        gbc.gridx = 1; gbc.gridy = 5; 
        gbc.gridwidth = 3; 
        formPanel.add(txtTongTien, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- XỬ LÝ TỰ ĐỘNG TÍNH TOÁN ---
        DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { tinhTienTuDong(); }
            public void removeUpdate(DocumentEvent e) { tinhTienTuDong(); }
            public void changedUpdate(DocumentEvent e) { tinhTienTuDong(); }
        };
        
        txtDienCu.getDocument().addDocumentListener(docListener);
        txtDienMoi.getDocument().addDocumentListener(docListener);
        txtNuocCu.getDocument().addDocumentListener(docListener);
        txtNuocMoi.getDocument().addDocumentListener(docListener);

        // --- NÚT BẤM ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnLuu = new JButton("Lưu Thay Đổi");
        btnLuu.setPreferredSize(new Dimension(150, 40));
        btnLuu.setFont(new Font("Arial", Font.PLAIN, 14));

        btnLuu.addActionListener(e -> {
            String maPhong = cboSoPhong.getSelectedItem().toString();
            String thangThu = cboThang.getSelectedItem().toString(); // Lấy từ ComboBox
            
            long dCu = parseLongSafe(txtDienCu.getText());
            long dMoi = parseLongSafe(txtDienMoi.getText());
            long nCu = parseLongSafe(txtNuocCu.getText());
            long nMoi = parseLongSafe(txtNuocMoi.getText());
            
            // Tính lại tiền
            long tienDien = Math.max(0, dMoi - dCu) * 3500;
            long tienNuoc = Math.max(0, nMoi - nCu) * 10000;
            
            // Nếu người dùng không nhập gì (vẫn là 0), ta giữ nguyên tổng tiền cũ
            // Nếu có nhập, ta cập nhật tiền mới
            String tongTienMoi = txtTongTien.getText();

            // Cập nhật lại vào bảng
            tableModel.setValueAt(maPhong, rowIndex, 1);
            tableModel.setValueAt(thangThu, rowIndex, 2);
            tableModel.setValueAt(df.format(tienDien), rowIndex, 3);
            tableModel.setValueAt(df.format(tienNuoc), rowIndex, 4);
            tableModel.setValueAt(tongTienMoi, rowIndex, 5);
            tableModel.setValueAt(cboTrangThai.getSelectedItem(), rowIndex, 6);

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            this.dispose();
        });

        buttonPanel.add(btnLuu);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadDataThang() {
        cboThang.removeAllItems();
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem("Tháng " + i);
        }
    }

    private void tinhTienTuDong() {
        try {
            long dCu = parseLongSafe(txtDienCu.getText());
            long dMoi = parseLongSafe(txtDienMoi.getText());
            long nCu = parseLongSafe(txtNuocCu.getText());
            long nMoi = parseLongSafe(txtNuocMoi.getText());

            // Nếu tất cả là 0 (mới mở form lên), không tính lại để giữ nguyên số tiền cũ
            if (dCu == 0 && dMoi == 0 && nCu == 0 && nMoi == 0) return;

            long DON_GIA_DIEN = 3500;
            long DON_GIA_NUOC = 10000;

            long tienDien = (dMoi - dCu) * DON_GIA_DIEN;
            long tienNuoc = (nMoi - nCu) * DON_GIA_NUOC;
            
            if(tienDien < 0) tienDien = 0;
            if(tienNuoc < 0) tienNuoc = 0;

            long tong = tienDien + tienNuoc;
            txtTongTien.setText(df.format(tong));

        } catch (Exception ex) {}
    }
    
    private long parseLongSafe(String s) {
        try {
            return Long.parseLong(s.trim().isEmpty() ? "0" : s.trim().replace(",", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}