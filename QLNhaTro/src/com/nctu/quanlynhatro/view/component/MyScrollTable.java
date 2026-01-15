package com.nctu.quanlynhatro.view.component;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;

public class MyScrollTable extends JScrollPane {

    public MyScrollTable(JTable table, String text) {
        super(table);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        getViewport().setBackground(Color.WHITE);
        setBorder(new TitledBorder(text));
    }
}