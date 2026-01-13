package com.nctu.quanlynhatro.view.component;

import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class MyComboBox extends JComboBox<String> {

    public MyComboBox(String[] items, int width, int height) {
        super(items);
        initStyle();
        setPreferredSize(new Dimension(width, height));
    }
    public MyComboBox(String[] items) {
        super(items);
        initStyle();
        setPreferredSize(new Dimension(150, 35));
    }
    public MyComboBox() {
        super();
        initStyle();
    }

    private void initStyle() {
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        // Bỏ focus khi click
        setFocusable(false);
    }
}