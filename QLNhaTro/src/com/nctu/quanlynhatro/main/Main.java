package com.nctu.quanlynhatro.main;

import com.nctu.quanlynhatro.view.*;
import com.nctu.quanlynhatro.controller.*;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
//    	SwingUtilities.invokeLater(() -> {
//            // 1. Tạo View
//             DashboardView view = new DashboardView();
//            
//            // 2. Tạo Controller và đưa View vào để quản lý
//            MainController controller = new MainController(view);
//            
//            // 3. Hiển thị
//            controller.showMainView();
//        });
    	new DemoView().setVisible(true);
    	
    }
}
