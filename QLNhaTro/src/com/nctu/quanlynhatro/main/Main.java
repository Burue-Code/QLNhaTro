package com.nctu.quanlynhatro.main;

import com.nctu.quanlynhatro.view.login.*;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new DangNhapView().setVisible(true);
        });
    }
}
