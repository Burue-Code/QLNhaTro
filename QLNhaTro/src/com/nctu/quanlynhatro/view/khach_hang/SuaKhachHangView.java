//package com.nctu.quanlynhatro.view.khach_hang;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.TitledBorder;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//
//public class SuaKhachHangView extends JFrame {
//
//    private JTextField txtMaKH, txtTenKH, txtCCCD, txtSDT, txtQueQuan;
//    private JComboBox<String> cboGioiTinh;
//    private JButton btnLuu;
//    private DefaultTableModel tableModel;
//    private int rowIndex;
//
//    public SuaKhachHangView(DefaultTableModel model, int row) {
//        this.tableModel = model;
//        this.rowIndex = row;
//        setTitle("Cập Nhật Thông Tin");
//        setSize(700, 350);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
//        setContentPane(mainPanel);
//
//        // --- FORM ---
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        formPanel.setBorder(new TitledBorder("Chỉnh sửa thông tin"));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 10, 5, 10);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        // Dòng 1
//        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
//        formPanel.add(new JLabel("Mã Khách Hàng:"), gbc);
//        txtMaKH = new JTextField(model.getValueAt(row, 0).toString(), 15);
//        txtMaKH.setEditable(false);
//        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(txtMaKH, gbc);
//
//        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
//        formPanel.add(new JLabel("Họ và Tên:"), gbc);
//        txtTenKH = new JTextField(model.getValueAt(row, 1).toString(), 15);
//        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(txtTenKH, gbc);
//
//        // Dòng 2
//        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
//        formPanel.add(new JLabel("CCCD/CMND:"), gbc);
//        txtCCCD = new JTextField(model.getValueAt(row, 2).toString(), 15);
//        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; formPanel.add(txtCCCD, gbc);
//
//        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
//        formPanel.add(new JLabel("Số Điện Thoại:"), gbc);
//        txtSDT = new JTextField(model.getValueAt(row, 3).toString(), 15);
//        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0; formPanel.add(txtSDT, gbc);
//
//        // Dòng 3
//        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
//        formPanel.add(new JLabel("Quê Quán:"), gbc);
//        txtQueQuan = new JTextField(model.getValueAt(row, 4).toString(), 15);
//        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; formPanel.add(txtQueQuan, gbc);
//
//        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
//        formPanel.add(new JLabel("Giới Tính:"), gbc);
//        String[] genders = {"Nam", "Nữ", "Khác"};
//        cboGioiTinh = new JComboBox<>(genders);
//        cboGioiTinh.setSelectedItem(model.getValueAt(row, 5).toString());
//        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0; formPanel.add(cboGioiTinh, gbc);
//
//        mainPanel.add(formPanel, BorderLayout.CENTER);
//
//        // --- NÚT BẤM ---
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        btnLuu = new JButton("Lưu");
//        btnLuu.setPreferredSize(new Dimension(120, 35));
//        
//        btnLuu.addActionListener(e -> {
//            tableModel.setValueAt(txtTenKH.getText(), rowIndex, 1);
//            tableModel.setValueAt(txtCCCD.getText(), rowIndex, 2);
//            tableModel.setValueAt(txtSDT.getText(), rowIndex, 3);
//            tableModel.setValueAt(txtQueQuan.getText(), rowIndex, 4);
//            tableModel.setValueAt(cboGioiTinh.getSelectedItem(), rowIndex, 5);
//            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
//            this.dispose();
//        });
//        
//        buttonPanel.add(btnLuu);
//        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
//    }
//}


package com.nctu.quanlynhatro.view.khach_hang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SuaKhachHangView extends JFrame {

    // --- Component bên TRÁI (Form nhập liệu) ---
    private JTextField txtMaKH, txtTenKH, txtDiaChi, txtSDT;
    private JTextField txtCCCD, txtEmail;
    private JTextField txtNgaySinh; 
    private JRadioButton rdoNam, rdoNu;
    private ButtonGroup btnGroupGioiTinh;
    
    // Hai ô này Read-only, dữ liệu lấy từ bảng bên phải hoặc dữ liệu cũ
    private JTextField txtTenKHChinh, txtMaKHChinh; 

    // --- Component bên PHẢI (Danh sách chọn KH chính) ---
    private JTextField txtTimKiem;
    private JTable tblKhachHang;
    private DefaultTableModel tableModelDS; 

    // --- Component bên DƯỚI ---
    private JButton btnXacNhan, btnHuy;
    
    private DefaultTableModel mainTableModel;
    private int rowIndex; // Dòng đang được sửa

    public SuaKhachHangView(DefaultTableModel model, int row) {
        this.mainTableModel = model;
        this.rowIndex = row;
        
        setTitle("Cập Nhật Thông Tin Khách Hàng");
        setSize(1000, 550); 
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

        // --- Hàng 0: Mã Khách Hàng (Read only) ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        pnlLeft.add(new JLabel("Mã Khách Hàng:"), gbc);
        gbc.gridy = 1; 
        txtMaKH = new JTextField(20);
        txtMaKH.setPreferredSize(new Dimension(0, 30));
        txtMaKH.setEditable(false); // Không cho sửa mã
        // Lấy dữ liệu cũ (Giả sử Mã KH ở cột 0)
        txtMaKH.setText(getValue(0)); 
        pnlLeft.add(txtMaKH, gbc);

        // --- Hàng 1: Tên Khách Hàng ---
        gbc.gridy = 2; 
        pnlLeft.add(new JLabel("Tên Khách Hàng:"), gbc);
        gbc.gridy = 3; 
        txtTenKH = new JTextField(20);
        txtTenKH.setPreferredSize(new Dimension(0, 30));
        txtTenKH.setText(getValue(1)); // Cột 1 là Tên
        pnlLeft.add(txtTenKH, gbc);

        // --- Hàng 2: Địa Chỉ ---
        gbc.gridy = 4; 
        pnlLeft.add(new JLabel("Địa Chỉ:"), gbc);
        gbc.gridy = 5; 
        txtDiaChi = new JTextField(20);
        txtDiaChi.setPreferredSize(new Dimension(0, 30));
        txtDiaChi.setText(getValue(4)); // Cột 4 là Địa chỉ (Ví dụ)
        pnlLeft.add(txtDiaChi, gbc);

        // --- Hàng 3: Ngày Sinh & SĐT ---
        JPanel pnlRow3 = new JPanel(new GridLayout(1, 2, 10, 0));
        
        JPanel pnlNgaySinh = new JPanel(new BorderLayout());
        pnlNgaySinh.add(new JLabel("Ngày Sinh (dd/MM/yyyy):"), BorderLayout.NORTH);
        txtNgaySinh = new JTextField();
        txtNgaySinh.setPreferredSize(new Dimension(0, 30));
        // Giả sử chưa có cột ngày sinh trong model cũ, để trống hoặc thêm sau
        // txtNgaySinh.setText(getValue(X)); 
        pnlNgaySinh.add(txtNgaySinh, BorderLayout.CENTER);
        
        JPanel pnlSDT = new JPanel(new BorderLayout());
        pnlSDT.add(new JLabel("Số Điện Thoại:"), BorderLayout.NORTH);
        txtSDT = new JTextField();
        txtSDT.setPreferredSize(new Dimension(0, 30));
        txtSDT.setText(getValue(3)); // Cột 3 là SĐT
        pnlSDT.add(txtSDT, BorderLayout.CENTER);

        pnlRow3.add(pnlNgaySinh);
        pnlRow3.add(pnlSDT);

        gbc.gridy = 6;
        pnlLeft.add(pnlRow3, gbc);

        // --- Hàng 4: Giới Tính ---
        gbc.gridy = 7;
        pnlLeft.add(new JLabel("Giới Tính:"), gbc);
        
        JPanel pnlGioiTinh = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rdoNam = new JRadioButton("Nam");
        rdoNu = new JRadioButton("Nữ");
        
        // Set giới tính cũ
        String gender = getValue(5); // Cột 5 là giới tính
        if("Nam".equalsIgnoreCase(gender)) rdoNam.setSelected(true);
        else rdoNu.setSelected(true);
        
        btnGroupGioiTinh = new ButtonGroup();
        btnGroupGioiTinh.add(rdoNam);
        btnGroupGioiTinh.add(rdoNu);
        
        pnlGioiTinh.add(rdoNam);
        pnlGioiTinh.add(rdoNu);
        
        gbc.gridy = 8;
        pnlLeft.add(pnlGioiTinh, gbc);

        // --- Hàng 5: CCCD ---
        gbc.gridy = 9;
        pnlLeft.add(new JLabel("Căn Cước Công Dân:"), gbc);
        gbc.gridy = 10;
        txtCCCD = new JTextField(20);
        txtCCCD.setPreferredSize(new Dimension(0, 30));
        txtCCCD.setText(getValue(2)); // Cột 2 là CCCD
        pnlLeft.add(txtCCCD, gbc);

        // --- Hàng 6: Email ---
        gbc.gridy = 11;
        pnlLeft.add(new JLabel("Email (Gmail):"), gbc);
        gbc.gridy = 12;
        txtEmail = new JTextField(20);
        txtEmail.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtEmail, gbc);

        // --- Hàng 7: Tên KH Chính ---
        gbc.gridy = 13;
        pnlLeft.add(new JLabel("Tên Khách Hàng Chính:"), gbc);
        gbc.gridy = 14;
        txtTenKHChinh = new JTextField(20);
        txtTenKHChinh.setPreferredSize(new Dimension(0, 30));
        txtTenKHChinh.setEditable(false); 
        txtTenKHChinh.setBackground(new Color(240, 240, 240));
        pnlLeft.add(txtTenKHChinh, gbc);

        // --- Hàng 8: Mã KH Chính ---
        gbc.gridy = 15;
        pnlLeft.add(new JLabel("Mã Khách Hàng Chính:"), gbc);
        gbc.gridy = 16;
        txtMaKHChinh = new JTextField(20);
        txtMaKHChinh.setPreferredSize(new Dimension(0, 30));
        txtMaKHChinh.setEditable(false);
        txtMaKHChinh.setBackground(new Color(240, 240, 240));
        pnlLeft.add(txtMaKHChinh, gbc);
        
        gbc.gridy = 17;
        gbc.weighty = 1.0;
        pnlLeft.add(new JLabel(), gbc); 


        // =================================================================
        // 2. PANEL PHẢI: DANH SÁCH TÌM KIẾM (Để đổi KH chính)
        // =================================================================
        JPanel pnlRight = new JPanel(new BorderLayout(5, 5));

        // Ô tìm kiếm
        JPanel pnlSearch = new JPanel(new BorderLayout());
        pnlSearch.add(new JLabel("Tìm kiếm KH chính khác: "), BorderLayout.NORTH);
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(0, 30));
        pnlSearch.add(txtTimKiem, BorderLayout.CENTER);
        
        pnlRight.add(pnlSearch, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] headers = {"MaKH", "Tên Khách Hàng", "Địa Chỉ", "SĐT"};
        tableModelDS = new DefaultTableModel(headers, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tblKhachHang = new JTable(tableModelDS);
        tblKhachHang.setRowHeight(25);
        tblKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        

        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
        pnlRight.add(scrollPane, BorderLayout.CENTER);

        // Click bảng phải -> Đổi người bảo lãnh
        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblKhachHang.getSelectedRow();
                if (row >= 0) {
                    String ma = tblKhachHang.getValueAt(row, 0).toString();
                    String ten = tblKhachHang.getValueAt(row, 1).toString();
                    
                    // Không cho chọn chính mình làm người bảo lãnh
                    if(ma.equals(txtMaKH.getText())) {
                        JOptionPane.showMessageDialog(SuaKhachHangView.this, "Không thể chọn chính mình làm người bảo lãnh!");
                        return;
                    }

                    txtMaKHChinh.setText(ma);
                    txtTenKHChinh.setText(ten);
                }
            }
        });

        // =================================================================
        // 3. TỔNG HỢP: SPLIT PANE
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
        
        btnXacNhan = new JButton("Cập Nhật"); // Đổi tên nút thành Cập Nhật
        btnXacNhan.setPreferredSize(new Dimension(100, 35));

        pnlBottom.add(btnHuy);
        pnlBottom.add(btnXacNhan);
        
        contentPane.add(pnlBottom, BorderLayout.SOUTH);

        // --- SỰ KIỆN NÚT ---
        btnHuy.addActionListener(e -> this.dispose());

        btnXacNhan.addActionListener(e -> {
        	if (txtTenKH.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!");
                return;
            }
            
            try {
                // Đổi tên biến thành giaThue cho đỡ nhầm
                int CCCD = Integer.parseInt(txtCCCD.getText().trim());
                if(CCCD <= 0 || CCCD > 12) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "CCCD phải lớn hơn 0 và đủ 12 số!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                txtCCCD.requestFocus(); // Đưa trỏ chuột về ô này
                return;
            }
            
            try {
                // Đổi tên biến thành giaThue cho đỡ nhầm
                int SDT = Integer.parseInt(txtSDT.getText().trim());
                if(SDT <= 0 || SDT > 10) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá thuê phải là số và lớn hơn 0!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                txtSDT.requestFocus(); // Đưa trỏ chuột về ô này
                return;
            }

            
            // Cập nhật lại vào bảng chính (Model cha)
            // Lưu ý: Index cột phải khớp với KhachHangView.java
            mainTableModel.setValueAt(txtTenKH.getText(), rowIndex, 1);
            mainTableModel.setValueAt(txtCCCD.getText(), rowIndex, 2);
            mainTableModel.setValueAt(txtSDT.getText(), rowIndex, 3);
            mainTableModel.setValueAt(txtDiaChi.getText(), rowIndex, 4);

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            this.dispose();
        });
    }

    private String getValue(int col) {
        Object val = mainTableModel.getValueAt(rowIndex, col);
        return val == null ? "" : val.toString();
    }
}