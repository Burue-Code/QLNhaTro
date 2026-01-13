package com.nctu.quanlynhatro.view.component;

import javax.swing.*;
import java.awt.*;

public class MyScrollTable extends JScrollPane {

    public MyScrollTable(JTable table) {
        super(table);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        getViewport().setBackground(Color.WHITE);
    }
}