package com.nctu.quanlynhatro.view.phu_phi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import com.nctu.quanlynhatro.view.component.*;

public class PhuPhiView extends JPanel {

    private MyTextField txtTimKiem;
    private MyTable tblPhuPhi;

    public PhuPhiView() {
        // Cấu hình Form
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // =================================================================
        // 1. KHU VỰC NORTH: TIÊU ĐỀ + TÌM KIẾM
        // =================================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        // 1.1 Tiêu đề
        MyLabel lblTitle = new MyLabel("DANH SÁCH PHỤ PHÍ", MyLabel.HEADER, SwingConstants.CENTER); 
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Panel Tìm Kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        MyLabel lblTim = new MyLabel("Tìm kiếm: ");
        txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....",300,35); // Độ dài chuẩn
        
        searchPanel.add(lblTim);
        searchPanel.add(txtTimKiem);
        pnlNorth.add(searchPanel, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);


        // =================================================================
        // 2. BẢNG DỮ LIỆU
        // =================================================================
        String[] headers = {
            "MaPP", "Tên Phụ Phí", "Giá"
        };
        
        tblPhuPhi = new MyTable(headers);
        MyScrollTable scrollTable = new MyScrollTable(tblPhuPhi, "");
        
        add(scrollTable, BorderLayout.CENTER);

    }
    
    public MyTable getTable() { return tblPhuPhi; }
    public MyTextField getTxtTimKiem() { return txtTimKiem; }
}
