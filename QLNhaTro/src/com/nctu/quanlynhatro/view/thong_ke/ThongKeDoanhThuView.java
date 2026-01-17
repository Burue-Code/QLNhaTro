package com.nctu.quanlynhatro.view.thong_ke;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.nctu.quanlynhatro.view.component.*;

public class ThongKeDoanhThuView extends JPanel {

    private MyComboBox cboNam;
    private MyButton btnThongKe, btnXuatExcel;
    private MyTable tblDoanhThu;
    private DefaultTableModel tableModel;
    private MyLabel lblTongDoanhThu;

    public ThongKeDoanhThuView() {
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // =================================================================
        // 1. KHU VỰC NORTH: TIÊU ĐỀ + BỘ LỌC
        // =================================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        // 1.1 Tiêu đề
        MyLabel lblTitle = new MyLabel("THỐNG KÊ DOANH THU NHÀ TRỌ", MyLabel.HEADER, SwingConstants.CENTER);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Panel Bộ Lọc (Chọn Năm)
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        
        MyLabel lblNam = new MyLabel("Chọn Năm:");
       
        List<String> dsNam = new ArrayList<String>();
        // Load năm: từ 2020 đến năm hiện tại
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= 2020; i--) {
        	dsNam.add(String.valueOf(i));
        }
		cboNam = new MyComboBox(dsNam.toArray(new String[0]));
        // --- SỬA LỖI TẠI ĐÂY: Khởi tạo nút Thống Kê ---
        btnThongKe = new MyButton("Xem Thống Kê",150,35);
        btnThongKe.setButtonColor(new Color(0, 102, 204));
        btnXuatExcel = new MyButton("Xuất Excel",130,35);
        btnXuatExcel.setButtonColor(new Color(34, 139, 34));

        pnlFilter.add(lblNam);
        pnlFilter.add(cboNam);
        pnlFilter.add(btnThongKe); // Add vào panel sau khi đã new
        pnlFilter.add(btnXuatExcel);

        pnlNorth.add(pnlFilter, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);


        // =================================================================
        // 2. BẢNG DỮ LIỆU (CENTER)
        // =================================================================
        String[] headers = {
            "Tháng", "Số HĐ Đã Thu", "Tiền Phòng", "Tiền Điện Nước", "Phụ Phí", "Tổng Doanh Thu"
        };
        
        tblDoanhThu = new MyTable(headers);
        MyScrollTable scrollTable = new MyScrollTable(tblDoanhThu, "");
        
        add(scrollTable, BorderLayout.CENTER);
        
        
//        tableModel = new DefaultTableModel(headers, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false; 
//            }
//        };
//
//        tblDoanhThu = new JTable(tableModel);
//        tblDoanhThu.setRowHeight(30);
//        tblDoanhThu.setFont(new Font("Arial", Font.PLAIN, 14));
//        
//        // Header font thường
//        tblDoanhThu.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 14));
//        tblDoanhThu.getTableHeader().setBackground(new Color(230, 230, 230));
//        tblDoanhThu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//        JScrollPane scrollPane = new JScrollPane(tblDoanhThu);
//        scrollPane.getViewport().setBackground(Color.WHITE);
//        tblDoanhThu.setFillsViewportHeight(true);
//        
//        add(scrollPane, BorderLayout.CENTER);


        // =================================================================
        // 3. KHU VỰC SOUTH: TỔNG KẾT
        // =================================================================
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnlSouth.setBackground(new Color(245, 245, 245));
        pnlSouth.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        MyLabel lblTextTong = new MyLabel("TỔNG DOANH THU CẢ NĂM:", MyLabel.HEADER, SwingConstants.RIGHT);
        
        lblTongDoanhThu = new MyLabel("0 VNĐ",MyLabel.HEADER, SwingConstants.RIGHT);
        lblTongDoanhThu.setForeground(Color.RED);

        pnlSouth.add(lblTextTong);
        pnlSouth.add(lblTongDoanhThu);

        add(pnlSouth, BorderLayout.SOUTH);

        // =================================================================
        // 4. SỰ KIỆN
        // =================================================================
        
        // Load dữ liệu mẫu khi mở
//        loadDummyData();
//
//        // Thêm sự kiện cho nút Thống Kê
//        btnThongKe.addActionListener(e -> {
//            int nam = (int) cboNam.getSelectedItem();
//            JOptionPane.showMessageDialog(this, "Đang tải dữ liệu thống kê năm " + nam + "...");
//            loadDummyData(); // Reload lại dữ liệu demo
//        });
//
//        btnXuatExcel.addActionListener(e -> {
//            JOptionPane.showMessageDialog(this, "Chức năng xuất báo cáo Excel đang phát triển!");
//        });
//    }
//
//    // Hàm tạo dữ liệu giả để test giao diện
//    private void loadDummyData() {
//        tableModel.setRowCount(0);
//        DecimalFormat df = new DecimalFormat("#,###");
//        long tongNam = 0;
//
//        // Giả lập 12 tháng
//        for (int i = 1; i <= 12; i++) {
//            long tienPhong = 10000000 + (i * 500000);
//            long dienNuoc = 3000000 + (i * 100000);
//            long phuPhi = 500000;
//            long tongThang = tienPhong + dienNuoc + phuPhi;
//            
//            tongNam += tongThang;
//
//            tableModel.addRow(new Object[]{
//                "Tháng " + i,
//                "10", // Số HĐ
//                df.format(tienPhong),
//                df.format(dienNuoc),
//                df.format(phuPhi),
//                df.format(tongThang)
//            });
//        }
//        
//        // Cập nhật tổng
//        lblTongDoanhThu.setText(df.format(tongNam) + " VNĐ");
//    }

    }
}
