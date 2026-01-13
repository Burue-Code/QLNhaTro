package com.nctu.quanlynhatro.view.component;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyButton extends JButton {
    // Màu mặc định
    private Color color = new Color(30, 136, 229); // Xanh dương
    private Color colorOver = new Color(21, 101, 192); // Xanh đậm (khi di chuột)
    private Color colorClick = new Color(13, 71, 161); // Xanh rất đậm (khi bấm)
    private Color borderColor = new Color(30, 136, 229);
    private int radius = 10; // Bo góc

    public MyButton(String text) {
        super(text);
        settingButton();
        setPreferredSize(new Dimension(50, 35));
    }
    public MyButton(String text, int width, int height) {
        super(text);
        settingButton();
        setPreferredSize(new Dimension(width, height));
    }
    
    private void settingButton() {
    	// Xóa style mặc định
        setContentAreaFilled(false);
        setFocusPainted(false); // Xóa viền focus
        setBorderPainted(false);
        
        // Style chữ
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hình bàn tay

        // Sự kiện chuột để đổi màu
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                setBackground(colorOver);
            }
            @Override
            public void mouseExited(MouseEvent me) {
                setBackground(color);
            }
            @Override
            public void mousePressed(MouseEvent me) {
                setBackground(colorClick);
            }
            @Override
            public void mouseReleased(MouseEvent me) {
                setBackground(colorOver);
            }
        });
    }

    // Ghi đè hàm vẽ để bo góc
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        // Vẽ hình chữ nhật bo góc
        g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g);
    }
    
    // Hàm set màu nhanh nếu muốn nút màu đỏ/cam...
    public void setButtonColor(Color color) {
        this.color = color;
        this.setBackground(color);
    }
}