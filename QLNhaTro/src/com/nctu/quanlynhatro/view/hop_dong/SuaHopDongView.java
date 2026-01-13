package com.nctu.quanlynhatro.view.hop_dong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SuaHopDongView extends JFrame {

    // --- Component TRÁI (Form Hợp Đồng) ---
    private JTextField txtMaKH, txtTenKH; // Read-only, lấy từ bên phải hoặc dữ liệu cũ
    private JTextField txtNgayLap, txtSoThang, txtNgayKetThuc;
    private JComboBox<String> cboNhaTro, cboPhong;
    private JTextField txtSoNguoi, txtGiaThue;
    private JTextArea txtGhiChu;

    // --- Component PHẢI TRÊN (Tìm KH Chính để đổi) ---
    private JTextField txtTimKiem;
    private JButton btnThemKH;
    private JTable tblKhachHang;
    private DefaultTableModel modelKH;

    // --- Component PHẢI DƯỚI (KH Phụ Thuộc) ---
    private JTable tblKHPhuThuoc;
    private DefaultTableModel modelPhuThuoc;

    // --- Component DƯỚI ---
    private JButton btnCapNhat, btnThoat;
    
    private DefaultTableModel mainTableModel;
    private int rowIndex;

    public SuaHopDongView(DefaultTableModel model, int row) {
        this.mainTableModel = model;
        this.rowIndex = row;
        
        setTitle("Cập Nhật Hợp Đồng");
        setSize(1100, 650); // Form lớn
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // =================================================================
        // 1. PANEL TRÁI: FORM NHẬP LIỆU HỢP ĐỒNG
        // =================================================================
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- Hàng 1: Mã KH (Read-only) ---
        gbc.gridx = 0; gbc.gridy = 0;
        pnlLeft.add(new JLabel("Mã Khách Hàng:"), gbc);
        gbc.gridy = 1;
        txtMaKH = new JTextField(); txtMaKH.setEditable(false);
        txtMaKH.setPreferredSize(new Dimension(0, 30));
        // Giả sử Mã KH chưa có trong bảng hiện tại, tạm để trống hoặc lấy từ nguồn khác
        // txtMaKH.setText(getValue(X)); 
        pnlLeft.add(txtMaKH, gbc);

        // --- Hàng 2: Tên KH (Read-only) ---
        gbc.gridy = 2; pnlLeft.add(new JLabel("Tên Khách Hàng:"), gbc);
        gbc.gridy = 3;
        txtTenKH = new JTextField(); txtTenKH.setEditable(false);
        txtTenKH.setPreferredSize(new Dimension(0, 30));
        txtTenKH.setText(getValue(2)); // Cột 2 là Người Thuê
        pnlLeft.add(txtTenKH, gbc);

        // --- Hàng 3: Ngày Lập - Số Tháng - Ngày KT ---
        JPanel pnlTime = new JPanel(new GridLayout(1, 3, 5, 0));
        
        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(new JLabel("Ngày Lập:"), BorderLayout.NORTH);
        txtNgayLap = new JTextField();
        txtNgayLap.setText(getValue(4)); // Cột 4 Ngày BĐ
        p1.add(txtNgayLap, BorderLayout.CENTER);

        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(new JLabel("Số Tháng:"), BorderLayout.NORTH);
        txtSoThang = new JTextField("12"); // Mặc định hoặc tính toán lại
        p2.add(txtSoThang, BorderLayout.CENTER);

        JPanel p3 = new JPanel(new BorderLayout());
        p3.add(new JLabel("Ngày Kết Thúc:"), BorderLayout.NORTH);
        txtNgayKetThuc = new JTextField(); txtNgayKetThuc.setEditable(false);
        txtNgayKetThuc.setText(getValue(5)); // Cột 5 Ngày KT
        p3.add(txtNgayKetThuc, BorderLayout.CENTER);

        pnlTime.add(p1); pnlTime.add(p2); pnlTime.add(p3);
        
        gbc.gridy = 4;
        pnlLeft.add(pnlTime, gbc);

        // --- Hàng 4: Nhà Trọ ---
        gbc.gridy = 5; pnlLeft.add(new JLabel("Chọn Nhà Trọ:"), gbc);
        gbc.gridy = 6;
        cboNhaTro = new JComboBox<>();
        cboNhaTro.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(cboNhaTro, gbc);

        // --- Hàng 5: Phòng ---
        gbc.gridy = 7; pnlLeft.add(new JLabel("Chọn Phòng:"), gbc);
        gbc.gridy = 8;
        cboPhong = new JComboBox<>();
        cboPhong.setPreferredSize(new Dimension(0, 30));
        cboPhong.setSelectedItem(getValue(1)); // Cột 1 là Mã Phòng
        pnlLeft.add(cboPhong, gbc);

        // --- Hàng 6: Số Lượng Người Ở ---
        gbc.gridy = 9; pnlLeft.add(new JLabel("Số Lượng Người Ở:"), gbc);
        gbc.gridy = 10;
        txtSoNguoi = new JTextField("1"); // Giả sử chưa có cột này
        txtSoNguoi.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtSoNguoi, gbc);

        // --- Hàng 7: Giá Thuê ---
        gbc.gridy = 11; pnlLeft.add(new JLabel("Giá Thuê:"), gbc);
        gbc.gridy = 12;
        txtGiaThue = new JTextField();
        txtGiaThue.setPreferredSize(new Dimension(0, 30));
        txtGiaThue.setText(getValue(3)); // Cột 3 Giá Phòng
        pnlLeft.add(txtGiaThue, gbc);

        // --- Hàng 8: Ghi Chú ---
        gbc.gridy = 13; pnlLeft.add(new JLabel("Ghi Chú:"), gbc);
        gbc.gridy = 14;
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnlLeft.add(txtGhiChu, gbc);
        
        // Spacer đẩy lên
        gbc.gridy = 15; gbc.weighty = 1.0; pnlLeft.add(new JLabel(), gbc);


        // =================================================================
        // 2. PANEL PHẢI: TÌM KIẾM & DANH SÁCH PHỤ THUỘC
        // =================================================================
        JPanel pnlRight = new JPanel(new GridLayout(2, 1, 0, 10)); 

        // --- PHẦN TRÊN: TÌM KIẾM KHÁCH HÀNG (Để đổi chủ hợp đồng) ---
        JPanel pnlTopRight = new JPanel(new BorderLayout(5, 5));
        pnlTopRight.setBorder(new TitledBorder("Đổi Khách Hàng Chính (Nếu cần)"));
        
        JPanel pnlSearch = new JPanel(new BorderLayout(5, 0));
        txtTimKiem = new JTextField();
        pnlSearch.add(new JLabel("Tìm kiếm: "), BorderLayout.WEST);
        pnlSearch.add(txtTimKiem, BorderLayout.CENTER);
        btnThemKH = new JButton("Khách Hàng Mới");
        pnlSearch.add(btnThemKH, BorderLayout.EAST);
        
        pnlTopRight.add(pnlSearch, BorderLayout.NORTH);

        String[] colsKH = {"MaKH", "Tên Khách Hàng", "Địa Chỉ", "Giới Tính", "Ngày Sinh"};
        modelKH = new DefaultTableModel(colsKH, 0);
        tblKhachHang = new JTable(modelKH);
        pnlTopRight.add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);

        // --- PHẦN DƯỚI: KHÁCH HÀNG PHỤ THUỘC ---
        JPanel pnlBotRight = new JPanel(new BorderLayout());
        pnlBotRight.setBorder(new TitledBorder("Khách Hàng Phụ Thuộc (Ở chung)"));
        
        String[] colsPT = {"MaKH", "Tên Khách Hàng", "Địa Chỉ", "Giới Tính", "Ngày Sinh"};
        modelPhuThuoc = new DefaultTableModel(colsPT, 0);
        tblKHPhuThuoc = new JTable(modelPhuThuoc);
        pnlBotRight.add(new JScrollPane(tblKHPhuThuoc), BorderLayout.CENTER);

        pnlRight.add(pnlTopRight);
        pnlRight.add(pnlBotRight);


        // =================================================================
        // 3. TỔNG HỢP GIAO DIỆN
        // =================================================================
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlLeft, pnlRight);
        splitPane.setDividerLocation(350); 
        splitPane.setResizeWeight(0.3);
        
        contentPane.add(splitPane, BorderLayout.CENTER);

        // =================================================================
        // 4. PANEL DƯỚI CÙNG: NÚT BẤM
        // =================================================================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnThoat = new JButton("Thoát");
        btnThoat.setPreferredSize(new Dimension(100, 35));
        btnThoat.setFont(new Font("Arial", Font.PLAIN, 13));
        
        btnCapNhat = new JButton("Cập Nhật");
        btnCapNhat.setPreferredSize(new Dimension(100, 35));
        btnCapNhat.setFont(new Font("Arial", Font.PLAIN, 13));

        pnlBottom.add(btnThoat);
        pnlBottom.add(btnCapNhat);
        contentPane.add(pnlBottom, BorderLayout.SOUTH);

        // =================================================================
        // 5. XỬ LÝ SỰ KIỆN
        // =================================================================
        
        // Click bảng khách hàng -> Đổi thông tin KH bên trái
        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblKhachHang.getSelectedRow();
                if (row >= 0) {
                    txtMaKH.setText(tblKhachHang.getValueAt(row, 0).toString());
                    txtTenKH.setText(tblKhachHang.getValueAt(row, 1).toString());
                }
            }
        });

        // Nút Thoát
        btnThoat.addActionListener(e -> this.dispose());

        // Nút Cập Nhật
        btnCapNhat.addActionListener(e -> {
            // Cập nhật lại vào bảng cha
            mainTableModel.setValueAt(cboPhong.getSelectedItem(), rowIndex, 1);
            mainTableModel.setValueAt(txtTenKH.getText(), rowIndex, 2);
            mainTableModel.setValueAt(txtGiaThue.getText(), rowIndex, 3);
            mainTableModel.setValueAt(txtNgayLap.getText(), rowIndex, 4);
            mainTableModel.setValueAt(txtNgayKetThuc.getText(), rowIndex, 5);
            // Các cột khác cập nhật tương tự nếu có
            
            JOptionPane.showMessageDialog(this, "Cập nhật hợp đồng thành công!");
            this.dispose();
        });
        
        // Tính ngày kết thúc khi đổi số tháng
        txtSoThang.addActionListener(e -> tinhNgayKetThuc());
        txtSoThang.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tinhNgayKetThuc();
            }
        });
    }

    private String getValue(int col) {
        Object val = mainTableModel.getValueAt(rowIndex, col);
        return val == null ? "" : val.toString();
    }

    
    private void tinhNgayKetThuc() {
        try {
            int thang = Integer.parseInt(txtSoThang.getText().trim());
            LocalDate date = LocalDate.parse(txtNgayLap.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate end = date.plusMonths(thang);
            txtNgayKetThuc.setText(end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (Exception e) {}
    }
}