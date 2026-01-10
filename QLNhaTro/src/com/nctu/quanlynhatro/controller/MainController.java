package com.nctu.quanlynhatro.controller;
import com.nctu.quanlynhatro.view.DashboardView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class MainController implements ActionListener {
    
    private DashboardView view;

    public MainController(DashboardView view) {
        this.view = view;
        
        // Gán controller này (this) làm người lắng nghe sự kiện cho view
        this.view.addMenuListener(this);
    }

    public void showMainView() {
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Kiểm tra xem người dùng nhấn vào đâu
        Object source = e.getSource();
        
        if (source == view.getMniPhuPhi()) {
            view.showCard("VIEW_PHUPHI");
        } 
        else if (source == view.getMniThongKeDoanhThu()) {
             view.showCard("VIEW_THONGKEDOANHTHU"); 
        }
    }
}