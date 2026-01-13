package com.nctu.quanlynhatro.view.phong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SuaPhongView extends JFrame {

    // --- Component BÊN TRÁI (Form Nhập) ---
    private JComboBox<String> cboNhaTro, cboTrangThai;
    private JTextField txtSoPhong, txtGiaPhong, txtSoNguoi, txtPhuThu, txtGhiChu; // Đổi thành TextField

    // --- Component BÊN PHẢI (Phụ Phí) ---
    private JComboBox<String> cboPhuPhi;
    private JButton btnThemPhuPhi;
    private JTable tblPhuPhi;
    private DefaultTableModel modelPhuPhi;

    // --- Component BÊN DƯỚI ---
    private JButton btnThoat, btnLuu;

    private DefaultTableModel mainTableModel;
    private int rowIndex;

    public SuaPhongView(DefaultTableModel model, int row) {
        this.mainTableModel = model;
        this.rowIndex = row;
        
        setTitle("Cập Nhật Thông Tin Phòng");
        setSize(950, 600); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // =================================================================
        // PHẦN GIỮA: CHIA 2 CỘT
        // =================================================================
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0)); 

        // -------------------------------------------------------------
        // 1. PANEL TRÁI: FORM NHẬP LIỆU
        // -------------------------------------------------------------
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        int inputHeight = 40; 
        gbc.insets = new Insets(5, 0, 5, 20); 

        // --- Dòng 1: Nhà Trọ ---
        gbc.gridx = 0; gbc.gridy = 0;
        pnlLeft.add(new JLabel("Nhà Trọ"), gbc);
        
        gbc.gridy = 1;
        cboNhaTro = new JComboBox<>(new String[]{"Nhà Trọ Hạnh Phúc", "Nhà Trọ A"});
        cboNhaTro.setPreferredSize(new Dimension(0, inputHeight));
        pnlLeft.add(cboNhaTro, gbc);

        // --- Dòng 2: Số Phòng ---
        gbc.gridy = 2;
        pnlLeft.add(new JLabel("Số Phòng"), gbc);
        
        gbc.gridy = 3;
        txtSoPhong = new JTextField();
        txtSoPhong.setPreferredSize(new Dimension(0, inputHeight));
        txtSoPhong.setText(getValue(1)); 
        pnlLeft.add(txtSoPhong, gbc);

        // --- Dòng 3: Giá Phòng ---
        gbc.gridy = 4;
        pnlLeft.add(new JLabel("Giá Phòng"), gbc);
        
        gbc.gridy = 5;
        txtGiaPhong = new JTextField();
        txtGiaPhong.setPreferredSize(new Dimension(0, inputHeight));
        txtGiaPhong.setText(getValue(3)); // Cột 3 là Giá
        pnlLeft.add(txtGiaPhong, gbc);

        // --- Dòng 4: Số Người Ở ---
        gbc.gridy = 6;
        pnlLeft.add(new JLabel("Số Người Ở Trong Quy Định"), gbc);
        
        gbc.gridy = 7;
        txtSoNguoi = new JTextField();
        txtSoNguoi.setPreferredSize(new Dimension(0, inputHeight));
        txtSoNguoi.setText(getValue(4)); // Giả sử cột 4
        pnlLeft.add(txtSoNguoi, gbc);

        // --- Dòng 5: Phụ Thu ---
        gbc.gridy = 8;
        pnlLeft.add(new JLabel("Phụ Thu Quá Người"), gbc);
        
        gbc.gridy = 9;
        txtPhuThu = new JTextField();
        txtPhuThu.setPreferredSize(new Dimension(0, inputHeight));
        txtPhuThu.setText(getValue(5)); // Giả sử cột 5
        pnlLeft.add(txtPhuThu, gbc);

        // --- Dòng 6: Trạng Thái ---
        gbc.gridy = 10;
        pnlLeft.add(new JLabel("Trạng Thái"), gbc);
        
        gbc.gridy = 11;
        cboTrangThai = new JComboBox<>(new String[]{"Phòng Trống", "Đã Thuê", "Bảo Trì"});
        cboTrangThai.setPreferredSize(new Dimension(0, inputHeight));
        cboTrangThai.setSelectedItem(getValue(5)); 
        pnlLeft.add(cboTrangThai, gbc);

        // --- Dòng 7: Ghi Chú (ĐÃ SỬA THÀNH TEXTFIELD) ---
        gbc.gridy = 12;
        pnlLeft.add(new JLabel("Ghi Chú"), gbc);
        
        gbc.gridy = 13;
        txtGhiChu = new JTextField(); // Dùng TextField 1 dòng
        txtGhiChu.setPreferredSize(new Dimension(0, inputHeight));
        txtGhiChu.setText(getValue(6)); // Load Ghi chú cũ
        pnlLeft.add(txtGhiChu, gbc);

        // Spacer
        gbc.gridy = 14; gbc.weighty = 1.0; 
        pnlLeft.add(new JLabel(), gbc);


        // -------------------------------------------------------------
        // 2. PANEL PHẢI: BẢNG PHỤ PHÍ
        // -------------------------------------------------------------
        JPanel pnlRight = new JPanel(new BorderLayout(0, 10));
        pnlRight.setBorder(new TitledBorder("Phụ Phí"));

        JPanel pnlAddPP = new JPanel(new BorderLayout(10, 0));
        cboPhuPhi = new JComboBox<>(new String[]{"                 ", "Wifi", "Vệ sinh"});
        cboPhuPhi.setPreferredSize(new Dimension(0, 35));
        
        btnThemPhuPhi = new JButton("Thêm");
        btnThemPhuPhi.setPreferredSize(new Dimension(80, 35));
        btnThemPhuPhi.setBackground(Color.WHITE);
        
        pnlAddPP.add(cboPhuPhi, BorderLayout.CENTER);
        pnlAddPP.add(btnThemPhuPhi, BorderLayout.EAST);
        pnlRight.add(pnlAddPP, BorderLayout.NORTH);

        String[] colsPP = {"MaPP", "Tên Phụ Phí", "Giá"};
        modelPhuPhi = new DefaultTableModel(colsPP, 0);
        tblPhuPhi = new JTable(modelPhuPhi);
        tblPhuPhi.setRowHeight(25);
        tblPhuPhi.setShowGrid(false);
        
        pnlRight.add(new JScrollPane(tblPhuPhi), BorderLayout.CENTER);

        // Add vào giữa
        pnlCenter.add(pnlLeft);
        pnlCenter.add(pnlRight);
        mainPanel.add(pnlCenter, BorderLayout.CENTER);


        // =================================================================
        // PHẦN DƯỚI: NÚT BẤM
        // =================================================================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        
        btnThoat = new JButton("Thoát");
        btnThoat.setPreferredSize(new Dimension(100, 35));
        btnThoat.setBackground(Color.WHITE);
        
        btnLuu = new JButton("Lưu Thay Đổi");
        btnLuu.setPreferredSize(new Dimension(120, 35));
        btnLuu.setBackground(Color.WHITE);

        pnlBottom.add(btnThoat);
        pnlBottom.add(btnLuu);
        
        mainPanel.add(pnlBottom, BorderLayout.SOUTH);

        // =================================================================
        // SỰ KIỆN
        // =================================================================
        btnThoat.addActionListener(e -> this.dispose());

        btnLuu.addActionListener(e -> {
            if (txtSoPhong.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số phòng!");
                return;
            }
            
            // Cập nhật lại vào bảng cha (PhongView)
            // {MaPhong, SoPhong, Loai, Gia, DT, TrangThai, GhiChu}
            mainTableModel.setValueAt(txtSoPhong.getText(), rowIndex, 1);
            // mainTableModel.setValueAt(cboLoai.getSelectedItem(), rowIndex, 2); // Nếu có combo loại
            mainTableModel.setValueAt(txtGiaPhong.getText(), rowIndex, 3);
            // mainTableModel.setValueAt(txtDienTich.getText(), rowIndex, 4);
            mainTableModel.setValueAt(cboTrangThai.getSelectedItem(), rowIndex, 5);
            mainTableModel.setValueAt(txtGhiChu.getText(), rowIndex, 6);
            
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            this.dispose();
        });
        
        btnThemPhuPhi.addActionListener(e -> {
            String sel = (String) cboPhuPhi.getSelectedItem();
            if(sel != null && !sel.trim().isEmpty()) {
                modelPhuPhi.addRow(new Object[]{"PP01", sel, "50,000"});
            }
        });
    }

    private String getValue(int col) {
        if(col < 0 || col >= mainTableModel.getColumnCount()) return "";
        Object val = mainTableModel.getValueAt(rowIndex, col);
        return val == null ? "" : val.toString();
    }
}