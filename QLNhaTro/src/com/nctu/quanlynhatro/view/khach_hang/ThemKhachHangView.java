//package com.nctu.quanlynhatro.view.khach_hang;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.TitledBorder;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//
//public class ThemKhachHangView extends JFrame {
//
//    private JTextField txtMaKH, txtTenKH, txtCCCD, txtSDT, txtQueQuan;
//    private JComboBox<String> cboGioiTinh;
//    private JButton btnThem;
//    private DefaultTableModel tableModel;
//
//    public ThemKhachHangView(DefaultTableModel model) {
//        this.tableModel = model;
//        setTitle("Thêm Khách Hàng");
//        setSize(700, 350);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
//        setContentPane(mainPanel);
//
//        // --- FORM NHẬP LIỆU ---
//        JPanel formPanel = new JPanel(new GridBagLayout());
//        formPanel.setBorder(new TitledBorder("Thông tin cá nhân"));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 10, 5, 10);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        // Dòng 1: Mã KH & Họ Tên
//        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
//        formPanel.add(new JLabel("Mã Khách Hàng:"), gbc);
//        
//        txtMaKH = new JTextField(15);
//        txtMaKH.setEditable(false);
//        txtMaKH.setText("KH" + (model.getRowCount() + 101));
//        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
//        formPanel.add(txtMaKH, gbc);
//
//        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
//        formPanel.add(new JLabel("Họ và Tên:"), gbc);
//        
//        txtTenKH = new JTextField(15);
//        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
//        formPanel.add(txtTenKH, gbc);
//
//        // Dòng 2: CCCD & SĐT
//        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
//        formPanel.add(new JLabel("CCCD/CMND:"), gbc);
//        
//        txtCCCD = new JTextField(15);
//        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
//        formPanel.add(txtCCCD, gbc);
//
//        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
//        formPanel.add(new JLabel("Số Điện Thoại:"), gbc);
//        
//        txtSDT = new JTextField(15);
//        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
//        formPanel.add(txtSDT, gbc);
//
//        // Dòng 3: Quê Quán & Giới Tính
//        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
//        formPanel.add(new JLabel("Quê Quán:"), gbc);
//        
//        txtQueQuan = new JTextField(15);
//        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
//        formPanel.add(txtQueQuan, gbc);
//
//        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
//        formPanel.add(new JLabel("Giới Tính:"), gbc);
//        
//        String[] genders = {"Nam", "Nữ", "Khác"};
//        cboGioiTinh = new JComboBox<>(genders);
//        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0;
//        formPanel.add(cboGioiTinh, gbc);
//
//        mainPanel.add(formPanel, BorderLayout.CENTER);
//
//        // --- NÚT BẤM ---
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        btnThem = new JButton("Thêm");
//        btnThem.setPreferredSize(new Dimension(120, 35));
//        
//        btnThem.addActionListener(e -> {
//            if(txtTenKH.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Tên không được để trống!");
//                return;
//            }
//            tableModel.addRow(new Object[]{
//                txtMaKH.getText(), txtTenKH.getText(), txtCCCD.getText(),
//                txtSDT.getText(), txtQueQuan.getText(), cboGioiTinh.getSelectedItem()
//            });
//            JOptionPane.showMessageDialog(this, "Thêm thành công!");
//            this.dispose();
//        });
//        
//        buttonPanel.add(btnThem);
//        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
//    }
//}



package com.nctu.quanlynhatro.view.khach_hang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThemKhachHangView extends JFrame {

    // --- Component bên TRÁI (Form nhập liệu) ---
    private JTextField txtTenKH, txtDiaChi, txtSDT;
    private JTextField txtCCCD, txtEmail;
    private JTextField txtNgaySinh; // Tạm dùng text, sau này có thể dùng JDateChooser
    private JRadioButton rdoNam, rdoNu;
    private ButtonGroup btnGroupGioiTinh;
    
    // Hai ô này Read-only, dữ liệu lấy từ bảng bên phải
    private JTextField txtTenKHChinh, txtMaKHChinh; 

    // --- Component bên PHẢI (Danh sách chọn KH chính) ---
    private JTextField txtTimKiem;
    private JTable tblKhachHang;
    private DefaultTableModel tableModelDS; // Model cho bảng tìm kiếm bên phải

    // --- Component bên DƯỚI ---
    private JButton btnThem, btnHuy;
    
    private DefaultTableModel mainTableModel; // Model của form cha để thêm dữ liệu về

    public ThemKhachHangView(DefaultTableModel model) {
        this.mainTableModel = model;
        setTitle("Thêm Khách Hàng / Sinh Viên");
        setSize(1000, 550); // Form này cần rộng hơn để chứa 2 cột
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel chính dùng BorderLayout
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // =================================================================
        // 1. PANEL TRÁI: FORM NHẬP LIỆU
        // =================================================================
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        // pnlLeft.setBorder(new TitledBorder("Thông tin chi tiết"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- Hàng 1: Tên Khách Hàng ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        pnlLeft.add(new JLabel("Tên Khách Hàng:"), gbc);
        gbc.gridy = 1; 
        txtTenKH = new JTextField(20);
        txtTenKH.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtTenKH, gbc);

        // --- Hàng 2: Địa Chỉ ---
        gbc.gridy = 2; 
        pnlLeft.add(new JLabel("Địa Chỉ:"), gbc);
        gbc.gridy = 3; 
        txtDiaChi = new JTextField(20);
        txtDiaChi.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtDiaChi, gbc);

        // --- Hàng 3: Ngày Sinh & SĐT (Chia đôi) ---
        // Panel con để chứa 2 ô này trên cùng 1 hàng layout
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

        // --- Hàng 4: Giới Tính ---
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

        // --- Hàng 5: CCCD ---
        gbc.gridy = 7;
        pnlLeft.add(new JLabel("Số CCCD:"), gbc);
        gbc.gridy = 8;
        txtCCCD = new JTextField(20);
        txtCCCD.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtCCCD, gbc);

        // --- Hàng 6: Gmail ---
        gbc.gridy = 9;
        pnlLeft.add(new JLabel("Gmail:"), gbc);
        gbc.gridy = 10;
        txtEmail = new JTextField(20);
        txtEmail.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtEmail, gbc);

        // --- Hàng 7: Tên KH Chính (Read only) ---
        gbc.gridy = 11;
        pnlLeft.add(new JLabel("Tên Khách Hàng Chính:"), gbc);
        gbc.gridy = 12;
        txtTenKHChinh = new JTextField(20);
        txtTenKHChinh.setPreferredSize(new Dimension(0, 30));
        txtTenKHChinh.setEditable(false); // Không cho sửa, chọn từ bảng bên phải
        txtTenKHChinh.setBackground(new Color(240, 240, 240));
        pnlLeft.add(txtTenKHChinh, gbc);

        // --- Hàng 8: Mã KH Chính (Read only) ---
        gbc.gridy = 13;
        pnlLeft.add(new JLabel("Mã Khách Hàng Chính:"), gbc);
        gbc.gridy = 14;
        txtMaKHChinh = new JTextField(20);
        txtMaKHChinh.setPreferredSize(new Dimension(0, 30));
        txtMaKHChinh.setEditable(false);
        txtMaKHChinh.setBackground(new Color(240, 240, 240));
        pnlLeft.add(txtMaKHChinh, gbc);
        
        // Đẩy các thành phần lên trên cùng
        gbc.gridy = 15;
        gbc.weighty = 1.0;
        pnlLeft.add(new JLabel(), gbc); 


        // =================================================================
        // 2. PANEL PHẢI: DANH SÁCH TÌM KIẾM
        // =================================================================
        JPanel pnlRight = new JPanel(new BorderLayout(5, 5));
        // pnlRight.setBorder(new TitledBorder("Chọn Khách Hàng Chính"));

        // Ô tìm kiếm
        JPanel pnlSearch = new JPanel(new BorderLayout());
        pnlSearch.add(new JLabel("Tìm kiếm (Tên/Mã): "), BorderLayout.NORTH);
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

        // Xử lý sự kiện click bảng bên phải -> Điền vào ô bên trái
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
        // 3. TỔNG HỢP: DÙNG SPLIT PANE
        // =================================================================
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlLeft, pnlRight);
        splitPane.setDividerLocation(400); // Chiều rộng panel trái
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

        // --- SỰ KIỆN NÚT ---
        btnHuy.addActionListener(e -> this.dispose());

        btnThem.addActionListener(e -> {
            // Validate đơn giản
        	
        	// Bắt lỗi nếu có ô thông tin bị để trống
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
                return;
            }
            
            try {
                // Đổi tên biến thành giaThue cho đỡ nhầm
                int SDT = Integer.parseInt(txtSDT.getText().trim());
                if(SDT <= 0 || SDT > 10) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá thuê phải là số và lớn hơn 0!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Logic Thêm (Giả lập thêm vào Model cha)
            // Cột: MaKH, TenKH, CCCD, SDT, QueQuan, GioiTinh
            // Lưu ý: Cần điều chỉnh lại Model cha nếu muốn lưu đủ các trường Email, MaKHChinh

            String gioiTinh = rdoNam.isSelected() ? "Nam" : "Nữ";
            
            mainTableModel.addRow(new Object[]{ 
                txtTenKH.getText(), 
                txtCCCD.getText(), 
                txtSDT.getText(), 
                txtDiaChi.getText(), 
                gioiTinh
            });

            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            this.dispose();
        });
    }
}