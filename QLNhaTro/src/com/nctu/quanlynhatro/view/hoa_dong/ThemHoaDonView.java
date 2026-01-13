package com.nctu.quanlynhatro.view.hoa_dong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ThemHoaDonView extends JFrame {

    // --- Component KHU VỰC TRÁI ---
    private JTextField txtTenKH, txtNgayThanhToan;
    private JTextField txtMaHopDong, txtNhaTro, txtPhong;
    private JTextField txtGiaThue, txtGhiChu;
    
    private JComboBox<String> cboChonDienNuoc;
    private JButton btnCongDN, btnThemPhieuMoi;
    private JComboBox<String> cboPhuongThuc, cboLoaiThanhToan;

    // --- Component KHU VỰC PHẢI ---
    private JTable tblPhuPhi, tblDienNuoc;
    private DefaultTableModel modelPhuPhi, modelDienNuoc;

    // --- Component KHU VỰC DƯỚI (Footer) ---
    private JTextField txtTongTienDN, txtTongTienPhuPhi, txtTongThanhToan;
    private JButton btnHuy, btnXacNhan, btnIn;

    private DefaultTableModel mainTableModel;

    public ThemHoaDonView(DefaultTableModel model) {
        this.mainTableModel = model;
        setTitle("Lập Hóa Đơn Thanh Toán");
        // setSize(1100, 700); <-- Dùng pack() ở cuối
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // =================================================================
        // 1. PHẦN GIỮA: CHIA 2 CỘT
        // =================================================================
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0)); 

        // --- CỘT TRÁI: FORM NHẬP LIỆU ---
        JPanel pnlLeftForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Hàng 1
        addLabel(pnlLeftForm, "Tên Khách Hàng:", 0, 0);
        addLabel(pnlLeftForm, "Ngày Thanh Toán:", 1, 0);
        
        txtTenKH = createTextField();
        txtTenKH.setEditable(false); // Tự động load từ Hợp đồng
        addComponent(pnlLeftForm, txtTenKH, 0, 1);
        
        txtNgayThanhToan = createTextField();
        txtNgayThanhToan.setEditable(false);
        txtNgayThanhToan.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        addComponent(pnlLeftForm, txtNgayThanhToan, 1, 1);

        // Hàng 2: Mã Hợp Đồng (Đã bỏ nút ...) & Điện Nước
        addLabel(pnlLeftForm, "Mã Hợp Đồng:", 0, 2);
        addLabel(pnlLeftForm, "Hóa Đơn Điện Nước:", 1, 2);

        // Ô Mã Hợp Đồng: Chỉ là Textbox Read-only
        txtMaHopDong = createTextField();
        txtMaHopDong.setEditable(false); 
        addComponent(pnlLeftForm, txtMaHopDong, 0, 3);

        // Panel Điện nước
        JPanel pnlDienNuocOption = new JPanel(new BorderLayout(5, 0));
        cboChonDienNuoc = new JComboBox<>(new String[]{"-- Chọn phiếu --"});
        btnCongDN = new JButton("+");
        btnThemPhieuMoi = new JButton("Mới");
        
        pnlDienNuocOption.add(cboChonDienNuoc, BorderLayout.CENTER);
        JPanel pnlBtnDN = new JPanel(new GridLayout(1, 2, 2, 0));
        pnlBtnDN.add(btnCongDN);
        pnlBtnDN.add(btnThemPhieuMoi);
        pnlDienNuocOption.add(pnlBtnDN, BorderLayout.EAST);
        pnlDienNuocOption.setPreferredSize(new Dimension(0, 30));
        
        addComponent(pnlLeftForm, pnlDienNuocOption, 1, 3);

        // Hàng 3
        addLabel(pnlLeftForm, "Nhà Trọ:", 0, 4);
        addLabel(pnlLeftForm, "Giá Thuê:", 1, 4);

        txtNhaTro = createTextField(); 
        txtNhaTro.setEditable(false); // Tự động load
        addComponent(pnlLeftForm, txtNhaTro, 0, 5);

        txtGiaThue = createTextField();
        txtGiaThue.setEditable(false); // Tự động load
        addComponent(pnlLeftForm, txtGiaThue, 1, 5);

        // Hàng 4
        addLabel(pnlLeftForm, "Phòng:", 0, 6);
        addLabel(pnlLeftForm, "Ghi Chú:", 1, 6);

        txtPhong = createTextField(); 
        txtPhong.setEditable(false); // Tự động load
        addComponent(pnlLeftForm, txtPhong, 0, 7);

        txtGhiChu = createTextField();
        addComponent(pnlLeftForm, txtGhiChu, 1, 7);

        // Hàng 5
        addLabel(pnlLeftForm, "Phương Thức Thanh Toán:", 0, 8);
        addLabel(pnlLeftForm, "Loại Thanh Toán:", 1, 8);

        cboPhuongThuc = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "MoMo"});
        cboPhuongThuc.setPreferredSize(new Dimension(0, 30));
        addComponent(pnlLeftForm, cboPhuongThuc, 0, 9);

        cboLoaiThanhToan = new JComboBox<>(new String[]{"Tất Cả", "Một Phần"});
        cboLoaiThanhToan.setPreferredSize(new Dimension(0, 30));
        addComponent(pnlLeftForm, cboLoaiThanhToan, 1, 9);
        
        gbc.gridx = 0; gbc.gridy = 10; gbc.weighty = 1.0;
        pnlLeftForm.add(new JLabel(), gbc);


        // --- CỘT PHẢI: BẢNG DỮ LIỆU ---
        JPanel pnlRightTables = new JPanel(new GridLayout(2, 1, 0, 10)); 

        // Bảng Phụ Phí
        JPanel pnlPhuPhi = new JPanel(new BorderLayout());
        pnlPhuPhi.setBorder(new TitledBorder("Phụ Phí"));
        String[] colsPhuPhi = {"MaPP", "Tên Phụ Phí", "Giá"};
        modelPhuPhi = new DefaultTableModel(colsPhuPhi, 0);
        tblPhuPhi = new JTable(modelPhuPhi);
        tblPhuPhi.setRowHeight(25);
        tblPhuPhi.setPreferredScrollableViewportSize(new Dimension(450, 100)); 
        // Không add dữ liệu demo
        pnlPhuPhi.add(new JScrollPane(tblPhuPhi), BorderLayout.CENTER);

        // Bảng Điện Nước
        JPanel pnlDienNuoc = new JPanel(new BorderLayout());
        pnlDienNuoc.setBorder(new TitledBorder("Hóa Đơn Điện Nước"));
        String[] colsDN = {"MaDN", "Thời Gian", "Giá"};
        modelDienNuoc = new DefaultTableModel(colsDN, 0);
        tblDienNuoc = new JTable(modelDienNuoc);
        tblDienNuoc.setRowHeight(25);
        tblDienNuoc.setPreferredScrollableViewportSize(new Dimension(450, 100));
        pnlDienNuoc.add(new JScrollPane(tblDienNuoc), BorderLayout.CENTER);

        pnlRightTables.add(pnlPhuPhi);
        pnlRightTables.add(pnlDienNuoc);

        // Add vào Center
        pnlCenter.add(pnlLeftForm);
        pnlCenter.add(pnlRightTables);
        contentPane.add(pnlCenter, BorderLayout.CENTER);


        // =================================================================
        // 2. PHẦN DƯỚI: FOOTER (TỔNG TIỀN + NÚT BẤM)
        // =================================================================
        JPanel pnlFooter = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlFooter.setBorder(new EmptyBorder(10, 0, 0, 0)); 

        // --- CỘT TRÁI FOOTER: TỔNG TIỀN ---
        JPanel pnlTotalContainer = new JPanel(new BorderLayout());
        pnlTotalContainer.setBorder(new TitledBorder("Tổng Tiền"));
        
        JPanel pnlTotalFields = new JPanel(new GridLayout(3, 1, 5, 5));
        txtTongTienDN = createTotalField();
        txtTongTienPhuPhi = createTotalField();
        txtTongThanhToan = createTotalField();

        pnlTotalFields.add(createLabeledPanel("Tổng Tiền Điện Nước:", txtTongTienDN));
        pnlTotalFields.add(createLabeledPanel("Tổng Tiền Phụ Phí:", txtTongTienPhuPhi));
        pnlTotalFields.add(createLabeledPanel("Tổng Tiền Thanh Toán:", txtTongThanhToan));
        
        pnlTotalContainer.add(pnlTotalFields, BorderLayout.CENTER);
        pnlFooter.add(pnlTotalContainer);

        // --- CỘT PHẢI FOOTER: NÚT BẤM ---
        JPanel pnlButtonsContainer = new JPanel(new BorderLayout());
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(100, 40));
        
        btnXacNhan = new JButton("Xác Nhận");
        btnXacNhan.setPreferredSize(new Dimension(100, 40));
        
        btnIn = new JButton("In");
        btnIn.setPreferredSize(new Dimension(100, 40));

        pnlButtons.add(btnHuy);
        pnlButtons.add(btnXacNhan);
        pnlButtons.add(btnIn);
        
        pnlButtonsContainer.add(pnlButtons, BorderLayout.SOUTH); 
        pnlFooter.add(pnlButtonsContainer);

        contentPane.add(pnlFooter, BorderLayout.SOUTH);

        pack(); // Tự động co giãn
        setLocationRelativeTo(null); 

        // --- SỰ KIỆN ---
        
        btnHuy.addActionListener(e -> this.dispose());
        
        btnXacNhan.addActionListener(e -> {
            if (txtMaHopDong.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng!");
                return;
            }
            mainTableModel.addRow(new Object[]{
                txtPhong.getText(), 
                txtNgayThanhToan.getText(),
                txtTongThanhToan.getText(),
                "Đã thanh toán",
                txtGhiChu.getText()
            });
            JOptionPane.showMessageDialog(this, "Lập hóa đơn thành công!");
            this.dispose();
        });
    }

    // --- Helper Functions ---
    private void addLabel(JPanel panel, String text, int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x; gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 0, 5);
        panel.add(new JLabel(text), gbc);
    }

    private void addComponent(JPanel panel, JComponent comp, int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x; gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 5, 10, 5);
        panel.add(comp, gbc);
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(0, 30));
        return txt;
    }

    private JTextField createTotalField() {
        JTextField txt = new JTextField("0");
        txt.setEditable(false);
        txt.setBackground(new Color(230, 230, 230)); 
        txt.setForeground(Color.RED); // Chữ màu ĐỎ
        txt.setHorizontalAlignment(JTextField.RIGHT);
        txt.setFont(new Font("Arial", Font.BOLD, 14));
        txt.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return txt;
    }

    private JPanel createLabeledPanel(String label, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout(5, 0));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(150, 0)); 
        p.add(lbl, BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }
}