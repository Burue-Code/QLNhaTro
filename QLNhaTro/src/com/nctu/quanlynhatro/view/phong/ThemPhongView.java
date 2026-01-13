package com.nctu.quanlynhatro.view.phong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThemPhongView extends JFrame {

    // --- Component BÊN TRÁI ---
    private JComboBox<String> cboNhaTro, cboTrangThai;
    private JTextField txtSoPhong, txtGiaPhong, txtSoNguoi, txtPhuThu, txtGhiChu;

    // --- Component BÊN PHẢI ---
    private JComboBox<String> cboPhuPhi;
    private JButton btnThemPhuPhi;
    private JTable tblPhuPhi;
    private DefaultTableModel modelPhuPhi;

    // --- Component BÊN DƯỚI ---
    private JButton btnThoat, btnXacNhan;

    private DefaultTableModel mainTableModel;

    public ThemPhongView(DefaultTableModel model) {
        this.mainTableModel = model;
        
        setTitle("Thêm Phòng Mới");
        setSize(950, 550); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // =================================================================
        // PHẦN GIỮA: CHIA 2 CỘT (TRÁI & PHẢI)
        // =================================================================
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0)); 

        // -------------------------------------------------------------
        // 1. PANEL TRÁI: FORM NHẬP LIỆU (GridBagLayout chuẩn)
        // -------------------------------------------------------------
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        // pnlLeft.setBorder(new TitledBorder("Thông tin phòng")); // Nếu thích có viền
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- Nhà Trọ ---
        gbc.gridx = 0; gbc.gridy = 0;
        pnlLeft.add(new JLabel("Nhà Trọ"), gbc);
        
        gbc.gridy = 1;
        cboNhaTro = new JComboBox<>(new String[]{"Nhà Trọ Hạnh Phúc", "Nhà Trọ Sinh Viên A"});
        cboNhaTro.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(cboNhaTro, gbc);

        // --- Số Phòng ---
        gbc.gridy = 2;
        pnlLeft.add(new JLabel("Số Phòng"), gbc);
        
        gbc.gridy = 3;
        txtSoPhong = new JTextField();
        txtSoPhong.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtSoPhong, gbc);

        // --- Giá Phòng ---
        gbc.gridy = 4;
        pnlLeft.add(new JLabel("Giá Phòng"), gbc);
        
        gbc.gridy = 5;
        txtGiaPhong = new JTextField();
        txtGiaPhong.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtGiaPhong, gbc);

        // --- Số Người Ở ---
        gbc.gridy = 6;
        pnlLeft.add(new JLabel("Số Người Ở Trong Quy Định"), gbc);
        
        gbc.gridy = 7;
        txtSoNguoi = new JTextField();
        txtSoNguoi.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtSoNguoi, gbc);

        // --- Phụ Thu ---
        gbc.gridy = 8;
        pnlLeft.add(new JLabel("Phụ Thu Quá Người"), gbc);
        
        gbc.gridy = 9;
        txtPhuThu = new JTextField();
        txtPhuThu.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtPhuThu, gbc);

        // --- Trạng Thái (Phòng Trống) ---
        gbc.gridy = 10;
        pnlLeft.add(new JLabel("Phòng Trống"), gbc);
        
        gbc.gridy = 11;
        cboTrangThai = new JComboBox<>(new String[]{"Phòng Trống", "Đã Thuê", "Bảo Trì"});
        cboTrangThai.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(cboTrangThai, gbc);

        // --- Ghi Chú ---
        gbc.gridy = 12;
        pnlLeft.add(new JLabel("Ghi Chú"), gbc);
        
        gbc.gridy = 13;
        txtGhiChu = new JTextField(); // Dùng Textfield 1 dòng như ảnh
        txtGhiChu.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtGhiChu, gbc);

        // Spacer đẩy lên trên
        gbc.gridy = 14; gbc.weighty = 1.0; 
        pnlLeft.add(new JLabel(), gbc);


        // -------------------------------------------------------------
        // 2. PANEL PHẢI: BẢNG PHỤ PHÍ
        // -------------------------------------------------------------
        JPanel pnlRight = new JPanel(new BorderLayout(0, 10));
        pnlRight.setBorder(new TitledBorder("Phụ Phí"));

        // Phần trên: Combo + Button Thêm
        JPanel pnlAddPP = new JPanel(new BorderLayout(10, 0));
        
        cboPhuPhi = new JComboBox<>(new String[]{"Wifi", "Vệ sinh"});
        cboPhuPhi.setPreferredSize(new Dimension(0, 35));
        
        btnThemPhuPhi = new JButton("Thêm");
        btnThemPhuPhi.setPreferredSize(new Dimension(80, 35));
        btnThemPhuPhi.setBackground(Color.WHITE);
        
        pnlAddPP.add(cboPhuPhi, BorderLayout.CENTER);
        pnlAddPP.add(btnThemPhuPhi, BorderLayout.EAST);
        
        pnlRight.add(pnlAddPP, BorderLayout.NORTH);

        // Phần giữa: Bảng
        String[] colsPP = {"MaPP", "Tên Phụ Phí", "Giá"};
        modelPhuPhi = new DefaultTableModel(colsPP, 0);
        tblPhuPhi = new JTable(modelPhuPhi);
        tblPhuPhi.setRowHeight(25);
        
        // Tạo ScrollPane cho bảng
        JScrollPane scrollPP = new JScrollPane(tblPhuPhi);
        scrollPP.getViewport().setBackground(Color.WHITE); // Nền trắng cho bảng
        
        pnlRight.add(scrollPP, BorderLayout.CENTER);

        // Add 2 panel vào giữa
        pnlCenter.add(pnlLeft);
        pnlCenter.add(pnlRight);
        mainPanel.add(pnlCenter, BorderLayout.CENTER);


        // =================================================================
        // PHẦN DƯỚI: NÚT BẤM (CĂN PHẢI)
        // =================================================================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        
        btnThoat = new JButton("Thoát");
        btnThoat.setPreferredSize(new Dimension(100, 35));
        btnThoat.setBackground(Color.WHITE);
        
        btnXacNhan = new JButton("Xác Nhận");
        btnXacNhan.setPreferredSize(new Dimension(100, 35));
        btnXacNhan.setBackground(Color.WHITE);

        pnlBottom.add(btnThoat);
        pnlBottom.add(btnXacNhan);
        
        mainPanel.add(pnlBottom, BorderLayout.SOUTH);

        // =================================================================
        // SỰ KIỆN
        // =================================================================
        btnThoat.addActionListener(e -> this.dispose());

        btnXacNhan.addActionListener(e -> {
            if (txtSoPhong.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số phòng!");
                return;
            }
            // Thêm vào bảng cha
            mainTableModel.addRow(new Object[]{
                "P" + System.currentTimeMillis()%1000, 
                txtSoPhong.getText(),
                txtGiaPhong.getText(),
                txtSoNguoi.getText(),
                txtPhuThu.getText(),
                cboTrangThai.getSelectedItem(),
                txtGhiChu.getText()
            });
            JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
            this.dispose();
        });
        
        btnThemPhuPhi.addActionListener(e -> {
            String sel = (String) cboPhuPhi.getSelectedItem();
            if(sel != null && !sel.trim().isEmpty()) {
                modelPhuPhi.addRow(new Object[]{"PP01", sel, "50,000"});
            }
        });
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new ThemPhongView(new DefaultTableModel()).setVisible(true));
    }
}