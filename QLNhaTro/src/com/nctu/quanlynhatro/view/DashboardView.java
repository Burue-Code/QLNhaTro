package com.nctu.quanlynhatro.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import com.nctu.quanlynhatro.view.phu_phi.*;
import com.nctu.quanlynhatro.view.thong_ke.*;

public class DashboardView extends JFrame {
	
	// 1. Khai báo các thành phần giao diện ở cấp độ Class (để Controller gọi được)
    private JPanel cardsContainer;
    private CardLayout cardLayout;

    // Khai báo các Menu Item cần bắt sự kiện
    private JMenuItem mniNhaTro, mniPhong, mniKhachHang;
    private JMenuItem mniHopDong, mniHoaDon, mniDienNuoc;
    private JMenuItem mniThongKeDoanhThu;
    private JMenuItem mniPhuPhi,mniPhuongThucTT,mniGiaDienNuoc;

    public DashboardView() {
        setTitle("Trang chủ - Quản lý Nhà Trọ");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setupMenu();
        setupPanels();
    }
    
    private void setupMenu() {
    	JMenuBar menuBar = new JMenuBar();

        // --- Menu Danh Mục ---
        JMenu dm = new JMenu("Danh mục");
        mniNhaTro = new JMenuItem("Nhà trọ");
        mniPhong = new JMenuItem("Phòng");
        mniKhachHang = new JMenuItem("Khách hàng");
        
        dm.add(mniNhaTro);
        dm.add(mniPhong);
        dm.add(mniKhachHang);

        // --- Menu Nghiệp Vụ ---
        JMenu nv = new JMenu("Nghiệp vụ");
        mniHopDong = new JMenuItem("Hợp đồng");
        mniHoaDon = new JMenuItem("Hóa đơn");
        mniDienNuoc = new JMenuItem("Điện - Nước");
        
        nv.add(mniHopDong);
        nv.add(mniHoaDon);
        nv.add(mniDienNuoc);

        // --- Menu Thống Kê ---
        JMenu tk = new JMenu("Thống kê");
        mniThongKeDoanhThu = new JMenuItem("Doanh thu");
        tk.add(mniThongKeDoanhThu);
        
        // --- Menu Cài Đặt ---
        JMenu cd = new JMenu("Cài Đặt");
        mniPhuPhi = new JMenuItem("Cấu hình Phụ Phí");
        mniPhuongThucTT = new JMenuItem("Cấu hình Phương Thức Thanh Toán");
        mniGiaDienNuoc = new JMenuItem("Cấu hình Giá Điện Nước");
        cd.add(mniPhuPhi);
        cd.add(mniPhuongThucTT);
        cd.add(mniGiaDienNuoc);
        // Add các Menu chính vào Bar
        menuBar.add(dm);
        menuBar.add(nv);
        menuBar.add(tk);
        menuBar.add(cd);

        setJMenuBar(menuBar);
    }
    
    private void setupPanels() {
    	cardLayout = new CardLayout();
        cardsContainer = new JPanel(cardLayout);

        // Thêm các view con vào và đặt TÊN ĐỊNH DANH (KEY)
        // Ví dụ: PhuPhiView ứng với menu Điện - Nước
        cardsContainer.add(new PhuPhiView(), "VIEW_PHUPHI"); 
        
        // Bạn cần tạo thêm các Panel khác và add vào đây
        cardsContainer.add(new ThongKeDoanhThuView(), "VIEW_THONGKEDOANHTHU");
        

        add(cardsContainer);
    }

    // --- Các phương thức public để Controller gọi ---

    // 1. Phương thức để Controller gán sự kiện click
    public void addMenuListener(ActionListener listener) {
    	mniNhaTro.addActionListener(listener);
        mniPhong.addActionListener(listener);
        mniKhachHang.addActionListener(listener);
        
        mniHopDong.addActionListener(listener);
        mniHoaDon.addActionListener(listener);
        mniDienNuoc.addActionListener(listener);
        
        mniThongKeDoanhThu.addActionListener(listener);
        
        mniPhuPhi.addActionListener(listener);
        mniPhuongThucTT.addActionListener(listener);
        mniGiaDienNuoc.addActionListener(listener);
    }

    // 2. Phương thức để Controller yêu cầu chuyển trang
    public void showCard(String key) {
        cardLayout.show(cardsContainer, key);
    }

    // Getter để nhận biết nút nào được nhấn
    public JMenuItem getMniNhaTro() { return mniNhaTro; }
    public JMenuItem getMniPhong() { return mniPhong; }
    public JMenuItem getMniKhachHang() { return mniKhachHang; }
    
    public JMenuItem getMniHopDong() { return mniHopDong; }
    public JMenuItem getMniHoaDon() { return mniHoaDon; }
    public JMenuItem getMniDienNuoc() { return mniDienNuoc; }
    
    public JMenuItem getMniThongKeDoanhThu() { return mniThongKeDoanhThu; }
    
    public JMenuItem getMniPhuPhi() { return mniPhuPhi; }
    public JMenuItem getMniPhuongThucTT() { return mniPhuongThucTT; }
    public JMenuItem getMniGiaDienNuoc() { return mniGiaDienNuoc; }
    
}