package com.nctu.quanlynhatro.view.nha_tro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.nctu.quanlynhatro.view.component.*;

public class NhaTroView extends JPanel {

    private MyTable tblNhaTro;
    private MyTextField txtTimKiem;
    
    // 1. Khai báo biến bộ lọc

    public NhaTroView() {
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- KHU VỰC NORTH (TIÊU ĐỀ + TÌM KIẾM) ---
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        MyLabel lblTitle = new MyLabel("DANH SÁCH NHÀ TRỌ", MyLabel.HEADER, SwingConstants.CENTER);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        MyLabel lblTim = new MyLabel("Tìm kiếm: ");
        txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....",300,35); // Độ dài chuẩn
        
        searchPanel.add(lblTim);
        searchPanel.add(txtTimKiem);
        pnlNorth.add(searchPanel, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);
        // --- BẢNG DỮ LIỆU ---
        String[] headers = {"MaNT", "Tên Nhà Trọ", "Số Lượng Phòng", "Địa Chỉ", "Trạng Thái", "Ghi Chú"};
        
        tblNhaTro = new MyTable(headers);
        MyScrollTable scrollTable = new MyScrollTable(tblNhaTro, "");
        
        add(scrollTable, BorderLayout.CENTER);
        
    }
    
    public MyTable getTable() { return tblNhaTro; }
    public MyTextField getTxtTimKiem() { return txtTimKiem; }
}