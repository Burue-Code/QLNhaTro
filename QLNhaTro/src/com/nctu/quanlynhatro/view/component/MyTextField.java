package com.nctu.quanlynhatro.view.component;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MyTextField extends JTextField {
    private String hint;

    public MyTextField(String hint) {
    	this(hint, 200, 35);
    }
    public MyTextField(String hint,int width, int height) {
    	this.hint = hint;

        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);

        // Border + Padding
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Vẽ hint nếu rỗng
        if (getText().length() == 0) {
            int h = getHeight();
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Tính toán vị trí vẽ
            java.awt.Insets ins = getInsets();
            java.awt.FontMetrics fm = g.getFontMetrics();
            
            g.setColor(Color.GRAY);
            g.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
        }
    }
}
