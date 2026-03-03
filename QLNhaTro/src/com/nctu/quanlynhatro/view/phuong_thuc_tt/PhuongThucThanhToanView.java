package com.nctu.quanlynhatro.view.phuong_thuc_tt;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.nctu.quanlynhatro.view.component.*;

public class PhuongThucThanhToanView extends JPanel {

    private MyTextField txtTimKiem;
    private MyTable tblPhuongThuc;

    public PhuongThucThanhToanView() {
        // Cấu hình Form
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // =================================================================
        // 1. KHU VỰC NORTH: TIÊU ĐỀ + TÌM KIẾM
        // =================================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        // 1.1 Tiêu đề
        MyLabel lblTitle = new MyLabel("DANH SÁCH PHƯƠNG THỨC THANH TOÁN",MyLabel.HEADER, SwingConstants.CENTER);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Panel Tìm Kiếm (Style chuẩn của bạn)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        MyLabel lblTim = new MyLabel("Tìm kiếm: ");
        txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....",300,35); // Độ dài chuẩn
        
        searchPanel.add(lblTim);
        searchPanel.add(txtTimKiem);
        pnlNorth.add(searchPanel, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        // =================================================================
        // 2. BẢNG DỮ LIỆU (Giống ảnh)
        // =================================================================
        String[] headers = {
            "Mã Phương Thức Thanh Toán", "Tên Phương Thức Thanh Toán"
        };
        
        tblPhuongThuc = new MyTable(headers);
        MyScrollTable scrollTable = new MyScrollTable(tblPhuongThuc, "");
        
        add(scrollTable, BorderLayout.CENTER);

    }
    
    public MyTable getTable() { return tblPhuongThuc; }
    public MyTextField getTxtTimKiem() { return txtTimKiem; }
}