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
        else if (source == view.getMniDienNuoc()) {
            view.showCard("VIEW_DIENNUOC"); 
       }
        else if (source == view.getMniGiaDienNuoc()) {
            view.showCard("VIEW_GIADIENNUOC"); 
       }
        else if (source == view.getMniHoaDon()) {
            view.showCard("VIEW_HOADON"); 
       }
        else if (source == view.getMniHopDong()) {
            view.showCard("VIEW_HOPDONG"); 
       }
        else if (source == view.getMniKhachHang()) {
            view.showCard("VIEW_KHACHHANG"); 
       }
        else if (source == view.getMniNhaTro()) {
            view.showCard("VIEW_NHATRO"); 
       }
        else if (source == view.getMniPhong()) {
            view.showCard("VIEW_PHONG"); 
       }
        else if (source == view.getMniPhuongThucTT()) {
            view.showCard("VIEW_PHUONGTHUCTHANHTOAN"); 
       }
        
    }
}