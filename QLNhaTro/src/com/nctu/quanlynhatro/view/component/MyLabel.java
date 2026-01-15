package com.nctu.quanlynhatro.view.component;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class MyLabel extends JLabel {
    
    // Định nghĩa các kiểu chữ
    public static final int HEADER = 1; // Chữ to tiêu đề
    public static final int NORMAL = 2; // Chữ thường
    public static final int ITALIC = 3; // Chữ nghiêng

    public MyLabel(String text, int type, int alignment) {
        super(text);
        setHorizontalAlignment(alignment);
        switch (type) {
            case HEADER:
                setFont(new Font("SansSerif", Font.BOLD, 24));
                setForeground(new Color(0, 0, 254)); // Đen đậm
                break;
            case NORMAL:
                setFont(new Font("SansSerif", Font.PLAIN, 14));
                setForeground(new Color(50, 50, 50)); // Xám đậm
                break;
            case ITALIC:
                setFont(new Font("SansSerif", Font.ITALIC, 12));
                setForeground(Color.GRAY);
                break;
            default:
                setFont(new Font("SansSerif", Font.PLAIN, 14));
        }
    }
    
    // Constructor mặc định là Normal
    public MyLabel(String text) {
        this(text, NORMAL, LEFT);
    }
}