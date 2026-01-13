package com.nctu.quanlynhatro.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.nctu.quanlynhatro.view.phu_phi.*;
import com.nctu.quanlynhatro.view.thong_ke.*;
import com.nctu.quanlynhatro.view.dien_nuoc.*;
import com.nctu.quanlynhatro.view.hop_dong.*;
import com.nctu.quanlynhatro.view.khach_hang.*;
import com.nctu.quanlynhatro.view.nha_tro.*;
import com.nctu.quanlynhatro.view.phong.*;
import com.nctu.quanlynhatro.view.phuong_thuc_tt.*;
import com.nctu.quanlynhatro.view.gia_dien_nuoc.*;
import com.nctu.quanlynhatro.view.gia_diennuoc.*;
import com.nctu.quanlynhatro.view.gia_diennuoc.GiaDienNuocView;
import com.nctu.quanlynhatro.view.hoa_don.*;

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
        cardsContainer.add(new NhaTroView(), "VIEW_NHATRO");
        cardsContainer.add(new PhuPhiView(), "VIEW_PHUPHI");
        cardsContainer.add(new HopDongView(), "VIEW_HOPDONG");
        cardsContainer.add(new HoaDonView(), "VIEW_HOADON");
        cardsContainer.add(new KhachHangView(), "VIEW_KHACHHANG");
        cardsContainer.add(new PhongView(), "VIEW_PHONG");
        cardsContainer.add(new PhuongThucThanhToanView(), "VIEW_PHUONGTHUCTHANHTOAN");
        cardsContainer.add(new DienNuocView(), "VIEW_DIENNUOC");
        cardsContainer.add(new GiaDienNuocView(), "VIEW_GIADIENNUOC");
        cardsContainer.add(new ThongKeDoanhThuView(), "VIEW_THONGKEDOANHTHU");
        

        // Nội dung chính
        JLabel lbl = new JLabel("HỆ THỐNG QUẢN LÝ NHÀ TRỌ", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 26));
        lbl.setForeground(Color.BLUE);
        add(lbl, BorderLayout.CENTER);
    }
    
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

