package com.nctu.quanlynhatro.view.component;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyPopupMenu {

    private JPopupMenu popup;

    public MyPopupMenu(JComponent target) {
        popup = new JPopupMenu();
        attachTo(target);
    }

    /* ================= GẮN CHUỘT PHẢI ================= */
    private void attachTo(JComponent target) {
        MouseAdapter mouseHandler = new MouseAdapter() {
            private void show(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) show(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) show(e);
            }
        };

        target.addMouseListener(mouseHandler);
    }

    /* ================= API MỞ RỘNG ================= */
    public JMenuItem addItem(String text) {
        JMenuItem item = new JMenuItem(text);
        popup.add(item);
        return item;
    }

    public JMenuItem addItem(String text, Icon icon) {
        JMenuItem item = new JMenuItem(text, icon);
        popup.add(item);
        return item;
    }

    public void addSeparator() {
        popup.addSeparator();
    }

    public JPopupMenu getPopup() {
        return popup;
    }
}
