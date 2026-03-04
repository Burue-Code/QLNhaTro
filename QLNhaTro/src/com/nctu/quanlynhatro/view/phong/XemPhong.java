package com.nctu.quanlynhatro.view.phong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class XemPhong extends JDialog {

    // --- LEFT (Read-only form) ---
    private JTextField txtSoPhong, txtGiaPhong, txtSoNguoi, txtPhuThu, txtTrangThai, txtGhiChu;

    // --- RIGHT (Tables) ---
    private JTable tblPhuPhi, tblKhachHang;

    // --- BOTTOM ---
    private JButton btnThoat;

    public XemPhong(Frame owner) {
        super(owner, "Xem Thông Tin Phòng", true);

        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // =========================================================
        // CENTER: 2 COLUMNS (LEFT FORM + RIGHT TABLES)
        // =========================================================
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.add(pnlCenter, BorderLayout.CENTER);

        // -----------------------------
        // LEFT: FORM (like Them/Sua)
        // -----------------------------
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int h = 35;

        // Helper: create a readonly textfield
        txtSoPhong = createReadOnlyField(h);
        txtGiaPhong = createReadOnlyField(h);
        txtSoNguoi = createReadOnlyField(h);
        txtPhuThu = createReadOnlyField(h);
        txtTrangThai = createReadOnlyField(h);
        txtGhiChu = createReadOnlyField(h);

        int y = 0;
        addRow(pnlLeft, gbc, y, "Số Phòng", txtSoPhong);      y += 2;
        addRow(pnlLeft, gbc, y, "Giá Phòng", txtGiaPhong);    y += 2;
        addRow(pnlLeft, gbc, y, "Số Người Ở Trong Quy Định", txtSoNguoi); y += 2;
        addRow(pnlLeft, gbc, y, "Phụ Thu Quá Người", txtPhuThu); y += 2;
        addRow(pnlLeft, gbc, y, "Trạng Thái", txtTrangThai);  y += 2;
        addRow(pnlLeft, gbc, y, "Ghi Chú", txtGhiChu);        y += 2;

        // Spacer to push content up
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weighty = 1.0;
        pnlLeft.add(new JLabel(), gbc);

        // -----------------------------
        // RIGHT: TABLES (Top PhuPhi, Bottom KhachHang)
        // -----------------------------
        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));

        // Phụ Phí panel
        JPanel pnlPhuPhi = new JPanel(new BorderLayout());
        pnlPhuPhi.setBorder(new TitledBorder("Phụ Phí"));
        pnlPhuPhi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        String[] colsPP = {"MaPP", "Tên Phụ Phí", "Giá"};
        DefaultTableModel modelPP = new DefaultTableModel(colsPP, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblPhuPhi = new JTable(modelPP);
        tblPhuPhi.setRowHeight(25);
        tblPhuPhi.setShowGrid(false);
        JScrollPane spPP = new JScrollPane(tblPhuPhi);
        spPP.getViewport().setBackground(Color.WHITE);
        pnlPhuPhi.add(spPP, BorderLayout.CENTER);

        // Khách Hàng panel
        JPanel pnlKhachHang = new JPanel(new BorderLayout());
        pnlKhachHang.setBorder(new TitledBorder("Khách Hàng"));
        pnlKhachHang.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        String[] colsKH = {"MaKH", "Tên Khách Hàng", "Địa Chỉ", "Giới Tính", "Ngày"};
        DefaultTableModel modelKH = new DefaultTableModel(colsKH, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblKhachHang = new JTable(modelKH);
        tblKhachHang.setRowHeight(25);
        tblKhachHang.setShowGrid(false);
        JScrollPane spKH = new JScrollPane(tblKhachHang);
        spKH.getViewport().setBackground(Color.WHITE);
        pnlKhachHang.add(spKH, BorderLayout.CENTER);

        // Add to right side with spacing similar to image
        pnlRight.add(pnlPhuPhi);
        pnlRight.add(Box.createVerticalStrut(10));
        pnlRight.add(pnlKhachHang);

        // Add both columns
        pnlCenter.add(pnlLeft);
        pnlCenter.add(pnlRight);

        // =========================================================
        // BOTTOM: ONLY "Thoát" button (remove Xóa/Sửa)
        // =========================================================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnThoat = new JButton("Thoát");
        btnThoat.setPreferredSize(new Dimension(140, 38));
        btnThoat.setBackground(Color.WHITE);
        pnlBottom.add(btnThoat);

        mainPanel.add(pnlBottom, BorderLayout.SOUTH);

        // Events
        btnThoat.addActionListener(e -> dispose());
    }

    private static JTextField createReadOnlyField(int height) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(0, height));
        tf.setEditable(false);   // chỉ xem, không chỉnh
        tf.setFocusable(false);  // tránh viền focus
        return tf;
    }

    private static void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field) {
        gbc.weighty = 0.0;

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridy = y + 1;
        panel.add(field, gbc);
    }

    // Getters (nếu bạn cần controller sau này)
    public JTextField getTxtSoPhong() { return txtSoPhong; }
    public JTextField getTxtGiaPhong() { return txtGiaPhong; }
    public JTextField getTxtSoNguoi() { return txtSoNguoi; }
    public JTextField getTxtPhuThu() { return txtPhuThu; }
    public JTextField getTxtTrangThai() { return txtTrangThai; }
    public JTextField getTxtGhiChu() { return txtGhiChu; }
    public JTable getTblPhuPhi() { return tblPhuPhi; }
    public JTable getTblKhachHang() { return tblKhachHang; }
    public JButton getBtnThoat() { return btnThoat; }
    
    
    // =============================
    // MAIN: chạy giao diện xem phòng
    // =============================
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            XemPhong dlg = new XemPhong(null);
            dlg.setVisible(true);
        });
    }
}