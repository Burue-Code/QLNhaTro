package com.nctu.quanlynhatro.view.hop_dong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ThemHopDongView extends JFrame {

    // --- Component TRÁI (Form Hợp Đồng) ---
    private JTextField txtMaKH, txtTenKH; // Read-only
    private JTextField txtNgayLap, txtSoThang, txtNgayKetThuc;
    private JComboBox<String> cboNhaTro, cboPhong;
    private JTextField txtSoNguoi, txtGiaThue;
    private JTextArea txtGhiChu;

    // --- Component PHẢI TRÊN (Tìm KH Chính) ---
    private JTextField txtTimKiem;
    private JButton btnThemKH;
    private JTable tblKhachHang;
    private DefaultTableModel modelKH;

    // --- Component PHẢI DƯỚI (KH Phụ Thuộc) ---
    private JTable tblKHPhuThuoc;
    private DefaultTableModel modelPhuThuoc;

    // --- Component DƯỚI ---
    private JButton btnThem, btnThoat;
    
    private DefaultTableModel mainTableModel;

    public ThemHopDongView(DefaultTableModel model) {
        this.mainTableModel = model;
        setTitle("Lập Hợp Đồng Mới");
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
        pnlLeft.add(txtMaKH, gbc);

        // --- Hàng 2: Tên KH (Read-only) ---
        gbc.gridy = 2; pnlLeft.add(new JLabel("Tên Khách Hàng:"), gbc);
        gbc.gridy = 3;
        txtTenKH = new JTextField(); txtTenKH.setEditable(false);
        txtTenKH.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtTenKH, gbc);

        // --- Hàng 3: Ngày Lập - Số Tháng - Ngày KT ---
        JPanel pnlTime = new JPanel(new GridLayout(1, 3, 5, 0));
        
        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(new JLabel("Ngày Lập:"), BorderLayout.NORTH);
        txtNgayLap = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        p1.add(txtNgayLap, BorderLayout.CENTER);

        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(new JLabel("Số Tháng:"), BorderLayout.NORTH);
        txtSoThang = new JTextField("");
        p2.add(txtSoThang, BorderLayout.CENTER);

        JPanel p3 = new JPanel(new BorderLayout());
        p3.add(new JLabel("Ngày Kết Thúc:"), BorderLayout.NORTH);
        txtNgayKetThuc = new JTextField(); txtNgayKetThuc.setEditable(false);
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
        pnlLeft.add(cboPhong, gbc);

        // --- Hàng 6: Số Lượng Người Ở ---
        gbc.gridy = 9; pnlLeft.add(new JLabel("Số Lượng Người Ở:"), gbc);
        gbc.gridy = 10;
        txtSoNguoi = new JTextField("1");
        txtSoNguoi.setPreferredSize(new Dimension(0, 30));
        pnlLeft.add(txtSoNguoi, gbc);

        // --- Hàng 7: Giá Thuê ---
        gbc.gridy = 11; pnlLeft.add(new JLabel("Giá Thuê:"), gbc);
        gbc.gridy = 12;
        txtGiaThue = new JTextField();
        txtGiaThue.setPreferredSize(new Dimension(0, 30));
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
        JPanel pnlRight = new JPanel(new GridLayout(2, 1, 0, 10)); // Chia 2 phần trên dưới

        // --- PHẦN TRÊN: TÌM KIẾM KHÁCH HÀNG ---
        JPanel pnlTopRight = new JPanel(new BorderLayout(5, 5));
        pnlTopRight.setBorder(new TitledBorder("Khách Hàng (Người thuê chính)"));
        
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
        splitPane.setDividerLocation(350); // Chiều rộng panel trái
        splitPane.setResizeWeight(0.3);
        
        contentPane.add(splitPane, BorderLayout.CENTER);

        // =================================================================
        // 4. PANEL DƯỚI CÙNG: NÚT BẤM
        // =================================================================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnThoat = new JButton("Thoát");
        btnThoat.setPreferredSize(new Dimension(100, 35));
        btnThoat.setFont(new Font("Arial", Font.PLAIN, 13));
        
        btnThem = new JButton("Thêm");
        btnThem.setPreferredSize(new Dimension(100, 35));
        btnThem.setFont(new Font("Arial", Font.PLAIN, 13));
        
        pnlBottom.add(btnThoat);
        pnlBottom.add(btnThem);
        contentPane.add(pnlBottom, BorderLayout.SOUTH);

        // =================================================================
        // 5. XỬ LÝ SỰ KIỆN
        // =================================================================
        
        // Click bảng khách hàng -> Điền vào ô bên trái
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

        // Nút Xác Nhận
        btnThem.addActionListener(e -> {
            if(txtMaKH.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng thuê!");
                return;
            }
            
            try {
                // Đổi tên biến thành giaThue cho đỡ nhầm
                int giaThue = Integer.parseInt(txtGiaThue.getText().trim());
                if(giaThue <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá thuê phải là số và lớn hơn 0!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                int soNguoi = Integer.parseInt(txtSoNguoi.getText().trim());
                
                // Nếu <= 0 HOẶC > 2 thì báo lỗi
                if(soNguoi <= 0 || soNguoi > 2) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số người ở chỉ được từ 1 đến 2 người!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                txtSoNguoi.requestFocus();
                return;
            }
            
            // Thêm vào bảng cha
            mainTableModel.addRow(new Object[]{
                cboPhong.getSelectedItem(),
                txtTenKH.getText(),
                txtGiaThue.getText(),
                txtNgayLap.getText(),
                txtNgayKetThuc.getText(),
                "0", // Cọc
                "Hiệu lực"
            });
            JOptionPane.showMessageDialog(this, "Lập hợp đồng thành công!");
            this.dispose();
        });
        
        // --- XỬ LÝ TỰ ĐỘNG TÍNH NGÀY KẾT THÚC KHI GÕ ---
        // Sử dụng DocumentListener thay vì ActionListener/FocusListener
        txtSoThang.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                tinhNgayKetThuc();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                tinhNgayKetThuc();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                tinhNgayKetThuc();
            }
        });
        
        // Tính toán lần đầu khi mở form (để khớp với số tháng mặc định "12")
        tinhNgayKetThuc();
    }

    
    private void tinhNgayKetThuc() {
        try {
            // Kiểm tra rỗng trước khi parse
            String textThang = txtSoThang.getText().trim();
            if (textThang.isEmpty()) {
                txtNgayKetThuc.setText(""); // Nếu xóa hết thì ô kết quả rỗng
                return;
            }
            
            int thang = Integer.parseInt(textThang);
            LocalDate date = LocalDate.parse(txtNgayLap.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate end = date.plusMonths(thang);
            txtNgayKetThuc.setText(end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (Exception e) {
            // Nếu nhập chữ hoặc lỗi định dạng thì không làm gì (hoặc có thể set rỗng)
            // txtNgayKetThuc.setText(""); 
        }
    }
}