package com.nctu.quanlynhatro.view.login;
import javax.swing.*;
import java.awt.*;

public class DangNhapView extends JFrame {

    public JTextField txtUser;
    public JPasswordField txtPass;
    public JButton btnLogin;

    public DangNhapView() {
        setTitle("Đăng nhập hệ thống");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(new JLabel("Tài khoản"));
        txtUser = new JTextField();
        panel.add(txtUser);

        panel.add(new JLabel("Mật khẩu"));
        txtPass = new JPasswordField();
        panel.add(txtPass);

        btnLogin = new JButton("Đăng nhập");
        panel.add(new JLabel());
        panel.add(btnLogin);

        add(panel);
    }
}