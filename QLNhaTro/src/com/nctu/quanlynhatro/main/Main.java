package com.nctu.quanlynhatro.main;

import com.nctu.quanlynhatro.view.*;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new DashboardView().setVisible(true);
        });
    }
}
