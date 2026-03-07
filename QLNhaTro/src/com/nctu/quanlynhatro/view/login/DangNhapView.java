package com.nctu.quanlynhatro.view.login;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class DangNhapView extends JFrame {

	public JTextField txtUser;
	public JPasswordField txtPass;
	public JButton btnLogin;
	private JLabel lblQuenMatKhau;

	public DangNhapView() {

		setTitle("Đăng nhập hệ thống");
		setSize(450, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		panel.setBackground(new Color(240, 240, 240));
		setContentPane(panel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 5, 10, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		JLabel lblUser = new JLabel("Tài Khoản:");
		lblUser.setFont(new Font("Arial", Font.BOLD, 14));
		panel.add(lblUser, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		txtUser = new JTextField();
		txtUser.setPreferredSize(new Dimension(200, 30));
		panel.add(txtUser, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0;
		JLabel lblSao1 = new JLabel("*");
		lblSao1.setForeground(Color.RED);
		panel.add(lblSao1, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel lblPass = new JLabel("Mật Khẩu:");
		lblPass.setFont(new Font("Arial", Font.BOLD, 14));
		panel.add(lblPass, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		txtPass = new JPasswordField();
		txtPass.setPreferredSize(new Dimension(200, 30));
		panel.add(txtPass, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		JLabel lblSao2 = new JLabel("*");
		lblSao2.setForeground(Color.RED);
		panel.add(lblSao2, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;

		btnLogin = new JButton("Đăng Nhập");
		btnLogin.setPreferredSize(new Dimension(120, 35));
		btnLogin.setBackground(Color.WHITE);
		btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
		btnLogin.setFocusPainted(false);
		panel.add(btnLogin, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(5, 0, 20, 0);

		lblQuenMatKhau = new JLabel("<html><u>Quên mật khẩu</u></html>");
		lblQuenMatKhau.setForeground(Color.BLUE);
		lblQuenMatKhau.setFont(new Font("Arial", Font.PLAIN, 13));
		lblQuenMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel.add(lblQuenMatKhau, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 0, 0);

		JLabel lblFooter = new JLabel("* : Bắt buộc nhập");
		lblFooter.setForeground(Color.RED);
		lblFooter.setFont(new Font("Arial", Font.ITALIC, 12));
		panel.add(lblFooter, gbc);

		lblQuenMatKhau.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				chuyenSangManHinhQuenMatKhau();
			}
		});
	}

	private void chuyenSangManHinhQuenMatKhau() {
		// TODO: Viết code mở form Quên Mật Khẩu ở đây
		// Ví dụ:
		// QuenMatKhauView qmk = new QuenMatKhauView();
		// qmk.setVisible(true);
		// this.dispose(); // Đóng form đăng nhập hiện tại
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			new DangNhapView().setVisible(true);
		});
	}
}