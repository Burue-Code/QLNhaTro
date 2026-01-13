package com.nctu.quanlynhatro.view.Login;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class LayLaiMKView extends JFrame {

    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhauMoi, txtXacNhan;
    private JButton btnLuu;

    public LayLaiMKView() {
        // Cấu hình Form
        setTitle("Lấy Lại Mật Khẩu");
        setSize(450, 320); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Panel chính
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        setContentPane(mainPanel);

        // --- TẠO PANEL CON CÓ VIỀN TIÊU ĐỀ ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 240));
        
        Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lineBorder, "Tạo mật khẩu mới");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            titledBorder,
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- HÀNG 1: TÀI KHOẢN ---
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel lblTaiKhoan = new JLabel("Tài Khoản:");
        lblTaiKhoan.setFont(new Font("Arial", Font.BOLD, 13));
        lblTaiKhoan.setHorizontalAlignment(SwingConstants.RIGHT); 
        formPanel.add(lblTaiKhoan, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        txtTaiKhoan = new JTextField();
        txtTaiKhoan.setPreferredSize(new Dimension(200, 30));
        txtTaiKhoan.setFont(new Font("Arial", Font.PLAIN, 13));
        formPanel.add(txtTaiKhoan, gbc);

        // --- HÀNG 2: MẬT KHẨU MỚI ---
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblPass = new JLabel("Mật Khẩu Mới:");
        lblPass.setFont(new Font("Arial", Font.BOLD, 13));
        lblPass.setHorizontalAlignment(SwingConstants.RIGHT);
        formPanel.add(lblPass, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        txtMatKhauMoi = new JPasswordField();
        txtMatKhauMoi.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtMatKhauMoi, gbc);

        // --- HÀNG 3: XÁC NHẬN ---
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel lblConfirm = new JLabel("Xác Nhận:");
        lblConfirm.setFont(new Font("Arial", Font.BOLD, 13));
        lblConfirm.setHorizontalAlignment(SwingConstants.RIGHT);
        formPanel.add(lblConfirm, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        txtXacNhan = new JPasswordField();
        txtXacNhan.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtXacNhan, gbc);

        // --- HÀNG 4: NÚT LƯU ---
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2; 
        gbc.insets = new Insets(20, 0, 0, 0); 

        btnLuu = new JButton("Lưu Mật Khẩu"); // Sửa lại tên nút cho đúng
        btnLuu.setPreferredSize(new Dimension(150, 35));
        btnLuu.setBackground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 13));
        btnLuu.setFocusPainted(false);
        
        // ĐÃ XÓA DÒNG GÂY LỖI: panel.add(btnLuu, gbc); 
        
        // Panel chứa nút để căn giữa
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlButton.setOpaque(false);
        pnlButton.add(btnLuu);
        
        formPanel.add(pnlButton, gbc);

        // Thêm formPanel vào giữa MainPanel
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0; gbcMain.gridy = 0;
        gbcMain.weightx = 1.0;
        gbcMain.weighty = 1.0;
        gbcMain.fill = GridBagConstraints.BOTH;
        mainPanel.add(formPanel, gbcMain);

        // --- SỰ KIỆN ---
        btnLuu.addActionListener(e -> xuLyLuuMatKhau());
    }

    private void xuLyLuuMatKhau() {
        String taiKhoan = txtTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhauMoi.getPassword());
        String xacNhan = new String(txtXacNhan.getPassword());

        if (taiKhoan.isEmpty() || matKhau.isEmpty() || xacNhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!matKhau.equals(xacNhan)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
        this.dispose(); 
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new LayLaiMKView().setVisible(true));
    }
}