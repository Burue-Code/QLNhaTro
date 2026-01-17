package com.nctu.quanlynhatro.view.dien_nuoc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.nctu.quanlynhatro.controller.DienNuocController;
import com.nctu.quanlynhatro.view.component.*;

public class DienNuocView extends JPanel {

    private MyTable tblDanhSach;
    private MyTextField txtTimKiem; 
    public DienNuocView() {
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // =================================================================
        // PHẦN 1: HEADER (TIÊU ĐỀ + TÌM KIẾM)
        // =================================================================
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // 1.1 Tiêu đề
        MyLabel lblTitle = new MyLabel("QUẢN LÝ THU PHÍ ĐIỆN NƯỚC",MyLabel.HEADER, SwingConstants.CENTER);
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Khu vực tìm kiếm (Căn trái)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        MyLabel lblTim = new MyLabel("Tìm kiếm: ");
        txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....",300,35); // Độ dài chuẩn
        
        searchPanel.add(lblTim);
        searchPanel.add(txtTimKiem);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // =================================================================
        // PHẦN 2: BẢNG DỮ LIỆU
        // =================================================================
        String[] headers = {
            "Mã DN", "Số Phòng", "Thời Gian", 
            "Giá Điện", "Giá Nước", "Tổng Tiền", "Trạng Thái"
        };
        
        tblDanhSach = new MyTable(headers);
        MyScrollTable scrollPane = new MyScrollTable(tblDanhSach, "Danh Sách Hóa Đơn");
        add(scrollPane, BorderLayout.CENTER);
        
        new DienNuocController(this);
    }
        	
    public MyTable getTable() { return tblDanhSach; }
    public MyTextField getTxtTimKiem() { return txtTimKiem; }

}