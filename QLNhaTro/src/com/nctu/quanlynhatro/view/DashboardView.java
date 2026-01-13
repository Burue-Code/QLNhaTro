package com.nctu.quanlynhatro.view;

import com.nctu.quanlynhatro.view.dien_nuoc.DienNuocView; 
import com.nctu.quanlynhatro.view.hoa_dong.HoaDonView;
import com.nctu.quanlynhatro.view.hop_dong.HopDongView;
import com.nctu.quanlynhatro.view.khach_hang.KhachHangView;
import com.nctu.quanlynhatro.view.nha_tro.NhaTroView;
import com.nctu.quanlynhatro.view.phong.PhongView;

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

        JMenuBar menuBar = new JMenuBar();

        // --- MENU DANH MỤC ---
        JMenu dm = new JMenu("Danh mục");
        
     // 2. Tách menu item "Nhà Trọ" ra biến riêng để xử lý
        JMenuItem mnuNhaTro = new JMenuItem("Nhà Trọ");
        
        // 3. Thêm sự kiện click
        mnuNhaTro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở form Nhà Trọ
            	NhaTroView viewDN = new NhaTroView();
                viewDN.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form con, không tắt Dashboard
                viewDN.setVisible(true);
            }
        });
        
        dm.add(mnuNhaTro); // Thêm vào menu cha
        
        
     // 2. Tách menu item "Phòng" ra biến riêng để xử lý
        JMenuItem mnuPhong = new JMenuItem("Phòng");
        
        // 3. Thêm sự kiện click
        mnuPhong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở form Phòng
            	PhongView viewDN = new PhongView();
                viewDN.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form con, không tắt Dashboard
                viewDN.setVisible(true);
            }
        });
        
        dm.add(mnuPhong); // Thêm vào menu cha
        
        
     // 2. Tách menu item "Khách Hàng" ra biến riêng để xử lý
        JMenuItem mnuKhachHang = new JMenuItem("Khách Hàng");
        
        // 3. Thêm sự kiện click
        mnuKhachHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở form Khách Hàng
            	KhachHangView viewDN = new KhachHangView();
                viewDN.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form con, không tắt Dashboard
                viewDN.setVisible(true);
            }
        });
        
        dm.add(mnuKhachHang); // Thêm vào menu cha

        
        // --- MENU NGHIỆP VỤ ---
        JMenu nv = new JMenu("Nghiệp vụ");
        
     // 2. Tách menu item "Hợp Đồng" ra biến riêng để xử lý
        JMenuItem mnuDopDong = new JMenuItem("Hợp Đồng");
        
        // 3. Thêm sự kiện click
        mnuDopDong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở form Hợp Đồng
            	HopDongView viewDN = new HopDongView();
                viewDN.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form con, không tắt Dashboard
                viewDN.setVisible(true);
            }
        });
        
        nv.add(mnuDopDong); // Thêm vào menu cha
        
        
     // 2. Tách menu item "Hóa Đơn" ra biến riêng để xử lý
        JMenuItem mnuHoaDon = new JMenuItem("Hóa Đơn");
        
        // 3. Thêm sự kiện click
        mnuHoaDon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở form Hóa Đơn
            	HoaDonView viewDN = new HoaDonView();
                viewDN.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form con, không tắt Dashboard
                viewDN.setVisible(true);
            }
        });
        
        nv.add(mnuHoaDon); // Thêm vào menu cha

        
        // 2. Tách menu item "Điện - Nước" ra biến riêng để xử lý
        JMenuItem mnuDienNuoc = new JMenuItem("Điện - Nước");
        
        // 3. Thêm sự kiện click
        mnuDienNuoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở form Điện Nước
                DienNuocView viewDN = new DienNuocView();
                viewDN.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form con, không tắt Dashboard
                viewDN.setVisible(true);
            }
        });
        
        nv.add(mnuDienNuoc); // Thêm vào menu cha
        

        // --- MENU THỐNG KÊ ---
        JMenu tk = new JMenu("Thống kê");
        
        tk.add(new JMenuItem("Doanh thu"));

        // Add các menu vào thanh bar
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
    
    // Hàm main để chạy thử Dashboard
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DashboardView().setVisible(true);
        });
    }
}

