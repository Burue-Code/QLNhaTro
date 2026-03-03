package com.nctu.quanlynhatro.view.hoa_don;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import com.nctu.quanlynhatro.view.component.*;

public class HoaDonView extends JPanel {

    private MyTable tblHoaDon;
    private DefaultTableModel tableModel;
    private JPopupMenu popupMenu;
    private JMenuItem mnuThem, mnuSua, mnuXoa, mnuLamMoi;
    
    // --- Biến cho tìm kiếm ---
    private MyTextField txtTimKiem;
    private TableRowSorter<DefaultTableModel> rowSorter;
    
    

    public HoaDonView() {
        // Cấu hình Form
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // =================================================================
        // PHẦN 1: KHU VỰC NORTH (TIÊU ĐỀ + TÌM KIẾM)
        // =================================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        // 1.1 Tiêu đề
        MyLabel lblTitle = new MyLabel("DANH SÁCH HÓA ĐƠN THANH TOÁN", MyLabel.HEADER, SwingConstants.CENTER);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        MyLabel lblTim = new MyLabel("Tìm kiếm: ");
        txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....",300,35); // Độ dài chuẩn
        
        searchPanel.add(lblTim);
        searchPanel.add(txtTimKiem);
        pnlNorth.add(searchPanel, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        // =================================================================
        // PHẦN 2: BẢNG DỮ LIỆU
        // =================================================================
        String[] headers = {"Mã HĐ", "Ngày Lập", "Tổng Tiền", "Loại Thanh Toán","Phương Thức Thanh Toán", "Ghi Chú"};
        
        tblHoaDon = new MyTable(headers);
        MyScrollTable scrollTable = new MyScrollTable(tblHoaDon, "");
        
        add(scrollTable, BorderLayout.CENTER);

    }
    
    public MyTable getTable() { return tblHoaDon; }
    public MyTextField getTxtTimKiem() { return txtTimKiem; }
}
