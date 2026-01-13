package com.nctu.quanlynhatro.view.Login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DangKyView extends JFrame {

    // Khai báo các thành phần
    private JTextField txtTenChuTK, txtTaiKhoan, txtSDT, txtCCCD, txtGmail;
    private JPasswordField txtMatKhau, txtXacNhanMK;
    private JComboBox<String> cboVaiTro;
    private JButton btnHuy, btnDangKy;

    public DangKyView() {
        // Cấu hình Form
        setTitle("Đăng Ký Tài Khoản");
        setSize(500, 500); // Kích thước lớn hơn form đăng nhập
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Panel chính
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240)); 
        setContentPane(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); // Khoảng cách dọc lớn hơn chút cho thoáng
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- HÀNG 1: TÊN CHỦ TÀI KHOẢN ---
        addRow(panel, gbc, 0, "Tên Chủ Tài Khoản:", true);
        txtTenChuTK = new JTextField();
        addInput(panel, gbc, 0, txtTenChuTK);

        // --- HÀNG 2: TÀI KHOẢN ---
        addRow(panel, gbc, 1, "Tài Khoản:", true);
        txtTaiKhoan = new JTextField();
        addInput(panel, gbc, 1, txtTaiKhoan);

        // --- HÀNG 3: MẬT KHẨU ---
        addRow(panel, gbc, 2, "Mật Khẩu:", true);
        txtMatKhau = new JPasswordField();
        addInput(panel, gbc, 2, txtMatKhau);

        // --- HÀNG 4: XÁC NHẬN MẬT KHẨU ---
        addRow(panel, gbc, 3, "Xác Nhận:", true);
        txtXacNhanMK = new JPasswordField();
        addInput(panel, gbc, 3, txtXacNhanMK);

        // --- HÀNG 5: SỐ ĐIỆN THOẠI ---
        addRow(panel, gbc, 4, "Số Điện Thoại:", true);
        txtSDT = new JTextField();
        addInput(panel, gbc, 4, txtSDT);

        // --- HÀNG 6: CCCD (Không bắt buộc) ---
        addRow(panel, gbc, 5, "CCCD:", false);
        txtCCCD = new JTextField();
        addInput(panel, gbc, 5, txtCCCD);

        // --- HÀNG 7: GMAIL (Không bắt buộc) ---
        addRow(panel, gbc, 6, "Gmail:", false);
        txtGmail = new JTextField();
        addInput(panel, gbc, 6, txtGmail);

        // --- HÀNG 8: VAI TRÒ ---
        addRow(panel, gbc, 7, "Vai Trò:", false);
        String[] roles = {"Người Thuê", "Quản Lý"};
        cboVaiTro = new JComboBox<>(roles);
        cboVaiTro.setBackground(Color.WHITE);
        addInput(panel, gbc, 7, cboVaiTro);

        // --- HÀNG 9: FOOTER (Label bắt buộc + Nút bấm) ---
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 0, 0, 0); // Cách trên 20px

        // Panel Footer chứa 2 phần: Trái (Label) - Phải (Buttons)
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setOpaque(false); // Trong suốt để lấy màu nền cha

        // Label Chú thích
        JLabel lblNote = new JLabel("* : Bắt buộc nhập");
        lblNote.setForeground(Color.RED);
        lblNote.setFont(new Font("Arial", Font.ITALIC, 12));
        pnlFooter.add(lblNote, BorderLayout.WEST);

        // Panel chứa 2 nút
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);

        btnHuy = new JButton("Hủy");
        styleButton(btnHuy);
        
        btnDangKy = new JButton("Đăng Ký");
        styleButton(btnDangKy);

        pnlButtons.add(btnHuy);
        pnlButtons.add(btnDangKy);
        
        pnlFooter.add(pnlButtons, BorderLayout.EAST);

        panel.add(pnlFooter, gbc);
        
        // --- SỰ KIỆN ---
        btnHuy.addActionListener(e -> {
            // Quay lại màn hình đăng nhập hoặc thoát
            this.dispose();
            new DangNhapView().setVisible(true); // Giả sử đã có DangNhapView
            
            if (txtTenChuTK.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên chủ tài khoảng!");
                return;
            }
            
            if (txtTaiKhoan.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!");
                return;
            }
            
            
        });
        
        btnDangKy.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đang xử lý đăng ký...");
        });
    }

    // --- Helper 1: Thêm Label tiêu đề ---
    private void addRow(JPanel p, GridBagConstraints gbc, int row, String text, boolean required) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setHorizontalAlignment(SwingConstants.RIGHT); // Căn phải text
        p.add(lbl, gbc);

        // Nếu bắt buộc -> Thêm dấu sao ở cột 2 (Sẽ xử lý ở hàm addInput hoặc thêm riêng)
        // Nhưng để dễ layout, ta thêm dấu sao ở cột 3
        if(required) {
            GridBagConstraints gbcStar = (GridBagConstraints) gbc.clone();
            gbcStar.gridx = 2; 
            gbcStar.anchor = GridBagConstraints.WEST;
            JLabel lblStar = new JLabel("*");
            lblStar.setForeground(Color.RED);
            p.add(lblStar, gbcStar);
        }
    }

    // --- Helper 2: Thêm ô nhập liệu ---
    private void addInput(JPanel p, GridBagConstraints gbc, int row, JComponent comp) {
        gbc.gridx = 1; gbc.gridy = row;
        gbc.weightx = 1.0;
        comp.setPreferredSize(new Dimension(200, 30));
        comp.setFont(new Font("Arial", Font.PLAIN, 14));
        p.add(comp, gbc);
    }
    
    // --- Helper 3: Style nút bấm ---
    private void styleButton(JButton btn) {
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setBackground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new DangKyView().setVisible(true));
    }
}