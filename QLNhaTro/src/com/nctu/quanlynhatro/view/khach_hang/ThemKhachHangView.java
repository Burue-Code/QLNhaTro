package com.nctu.quanlynhatro.view.khach_hang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.nctu.quanlynhatro.view.component.MyScrollTable;
import com.nctu.quanlynhatro.view.component.MyTable;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThemKhachHangView extends JDialog {

    // --- Component bên TRÁI (Form nhập liệu) ---
    private JTextField txtTenKH, txtDiaChi, txtSDT;
    private JTextField txtCCCD, txtEmail;
    private JTextField txtNgaySinh; 
    private JRadioButton rdoNam, rdoNu;
    private ButtonGroup btnGroupGioiTinh;
    
    // Hai ô này Read-only, dữ liệu lấy từ bảng bên phải
    private JTextField txtTenKHChinh, txtMaKHChinh; 

    // --- Component bên PHẢI (Danh sách chọn KH chính) ---
    private JTextField txtTimKiem;
    private MyTable tblKhachHang;
    private DefaultTableModel tableModelDS;

    // --- Component bên DƯỚI ---
    private JButton btnThem, btnHuy;
    
    private DefaultTableModel mainTableModel; 

    public ThemKhachHangView(DefaultTableModel model) {
        this.mainTableModel = model;
        setTitle("Thêm Khách Hàng / Sinh Viên");
        setSize(1000, 580); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // =================================================================
        // 1. PANEL TRÁI: FORM NHẬP LIỆU
        // =================================================================
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Dòng 1: Tên Khách Hàng
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        pnlLeft.add(new JLabel("Tên Khách Hàng:"), gbc);
        gbc.gridy = 1; 
        txtTenKH = new JTextField(20);
        txtTenKH.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtTenKH, gbc);

        // Dòng 2: Địa Chỉ
        gbc.gridy = 2; 
        pnlLeft.add(new JLabel("Địa Chỉ:"), gbc);
        gbc.gridy = 3; 
        txtDiaChi = new JTextField(20);
        txtDiaChi.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtDiaChi, gbc);

        // Dòng 3: Ngày Sinh & SĐT 
        JPanel pnlRow3 = new JPanel(new GridLayout(1, 2, 10, 0));
        
        JPanel pnlNgaySinh = new JPanel(new BorderLayout());
        pnlNgaySinh.add(new JLabel("Ngày Sinh:"), BorderLayout.NORTH);
        txtNgaySinh = new JTextField();
        txtNgaySinh.setPreferredSize(new Dimension(0, 30));
        pnlNgaySinh.add(txtNgaySinh, BorderLayout.CENTER);
        
        JPanel pnlSDT = new JPanel(new BorderLayout());
        pnlSDT.add(new JLabel("Số Điện Thoại:"), BorderLayout.NORTH);
        txtSDT = new JTextField();
        txtSDT.setPreferredSize(new Dimension(0, 30));
        pnlSDT.add(txtSDT, BorderLayout.CENTER);

        pnlRow3.add(pnlNgaySinh);
        pnlRow3.add(pnlSDT);

        gbc.gridy = 4;
        pnlLeft.add(pnlRow3, gbc);

        // Dòng 4: Giới Tính
        gbc.gridy = 5;
        pnlLeft.add(new JLabel("Giới Tính:"), gbc);
        
        JPanel pnlGioiTinh = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rdoNam = new JRadioButton("Nam");
        rdoNu = new JRadioButton("Nữ");
        rdoNam.setSelected(true);
        
        btnGroupGioiTinh = new ButtonGroup();
        btnGroupGioiTinh.add(rdoNam);
        btnGroupGioiTinh.add(rdoNu);
        
        pnlGioiTinh.add(rdoNam);
        pnlGioiTinh.add(rdoNu);
        
        gbc.gridy = 6;
        pnlLeft.add(pnlGioiTinh, gbc);

        // Dòng 5: CCCD
        gbc.gridy = 7;
        pnlLeft.add(new JLabel("Số CCCD:"), gbc);
        gbc.gridy = 8;
        txtCCCD = new JTextField(20);
        txtCCCD.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtCCCD, gbc);

        // Dòng 6: Gmail
        gbc.gridy = 9;
        pnlLeft.add(new JLabel("Gmail:"), gbc);
        gbc.gridy = 10;
        txtEmail = new JTextField(20);
        txtEmail.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtEmail, gbc);

        // Dòng 7: Tên KH Chính 
        gbc.gridy = 11;
        pnlLeft.add(new JLabel("Tên Khách Hàng Chính:"), gbc);
        gbc.gridy = 12;
        txtTenKHChinh = new JTextField(20);
        txtTenKHChinh.setPreferredSize(new Dimension(0, 30));
        txtTenKHChinh.setEditable(false); 
        txtTenKHChinh.setBackground(new Color(240, 240, 240));
        pnlLeft.add(txtTenKHChinh, gbc);

        // Dòng 8: Mã KH Chính 
        gbc.gridy = 13;
        pnlLeft.add(new JLabel("Mã Khách Hàng Chính:"), gbc);
        gbc.gridy = 14;
        txtMaKHChinh = new JTextField(20);
        txtMaKHChinh.setPreferredSize(new Dimension(0, 30));
        txtMaKHChinh.setEditable(false);
        txtMaKHChinh.setBackground(new Color(240, 240, 240));
        pnlLeft.add(txtMaKHChinh, gbc);
        
        // Đẩy lên
        gbc.gridy = 15;
        gbc.weighty = 1.0;
        pnlLeft.add(new JLabel(), gbc); 

        // =================================================================
        // 2. PANEL PHẢI: DANH SÁCH TÌM KIẾM
        // =================================================================
        JPanel pnlRight = new JPanel(new BorderLayout(5, 5));

        JPanel pnlSearch = new JPanel(new BorderLayout());
        pnlSearch.add(new JLabel("Tìm kiếm (Tên/Mã): "), BorderLayout.NORTH);
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(0, 30));
        pnlSearch.add(txtTimKiem, BorderLayout.CENTER);
        
        pnlRight.add(pnlSearch, BorderLayout.NORTH);

        String[] headers = {"MaKH", "Tên Khách Hàng", "Địa Chỉ","Giới Tính","Ngày Sinh", "SĐT"};
        tblKhachHang = new MyTable(headers);
        MyScrollTable scrollTable = new MyScrollTable(tblKhachHang, "");
        
        pnlRight.add(scrollTable, BorderLayout.CENTER);

        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblKhachHang.getSelectedRow();
                if (row >= 0) {
                    String ma = tblKhachHang.getValueAt(row, 0).toString();
                    String ten = tblKhachHang.getValueAt(row, 1).toString();
                    
                    txtMaKHChinh.setText(ma);
                    txtTenKHChinh.setText(ten);
                }
            }
        });

        // =================================================================
        // 3. TỔNG HỢP (SPLIT PANE)
        // =================================================================
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlLeft, pnlRight);
        splitPane.setDividerLocation(400); 
        splitPane.setResizeWeight(0.4);
        
        contentPane.add(splitPane, BorderLayout.CENTER);

        // =================================================================
        // 4. PANEL DƯỚI: NÚT BẤM
        // =================================================================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        
        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(100, 35));
        btnHuy.setFont(new Font("Arial", Font.PLAIN, 13));
        
        btnThem = new JButton("Thêm");
        btnThem.setPreferredSize(new Dimension(100, 35));
        btnThem.setFont(new Font("Arial", Font.PLAIN, 13));

        pnlBottom.add(btnHuy);
        pnlBottom.add(btnThem);
        
        contentPane.add(pnlBottom, BorderLayout.SOUTH);
    }

    // =================================================================
    // CÁC HÀM GETTER
    // =================================================================

    public String getTenKhachHang() { return txtTenKH.getText().trim(); }
    public String getDiaChi() { return txtDiaChi.getText().trim(); }
    public String getSoDienThoai() { return txtSDT.getText().trim(); }
    public String getNgaySinh() { return txtNgaySinh.getText().trim(); }
    public String getGioiTinh() { return rdoNam.isSelected() ? "Nam" : "Nữ"; }
    public String getCCCD() { return txtCCCD.getText().trim(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getMaKhachHangChinh() { return txtMaKHChinh.getText().trim(); }
    public String getTenKhachHangChinh() { return txtTenKHChinh.getText().trim(); }
    
    public JTextField getTxtTimKiem() { return txtTimKiem; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnHuy() { return btnHuy; }
    public MyTable getTblKhachHang() { return tblKhachHang; }
    public DefaultTableModel getTableModelDS() { return tableModelDS; }
    
    public void setTenKhachHang(String ten) { this.txtTenKH.setText(ten); }
    public void setDiaChi(String diaChi) { this.txtDiaChi.setText(diaChi); }
    public void setSoDienThoai(String sdt) { this.txtSDT.setText(sdt); }
    public void setNgaySinh(String ngaySinh) { this.txtNgaySinh.setText(ngaySinh); }
    public void setCCCD(String cccd) { this.txtCCCD.setText(cccd); }
    public void setEmail(String email) { this.txtEmail.setText(email); }
    public void setMaKhachHangChinh(String maKH) { this.txtMaKHChinh.setText(maKH); }
    public void setTenKhachHangChinh(String tenKH) { this.txtTenKHChinh.setText(tenKH); }
    
    public void setGioiTinh(boolean isNu) {
        if (isNu) {
            this.rdoNu.setSelected(true);
        } else {
            this.rdoNam.setSelected(true);
        }
    }
}