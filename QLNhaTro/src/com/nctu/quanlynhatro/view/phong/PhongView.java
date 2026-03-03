package com.nctu.quanlynhatro.view.phong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.nctu.quanlynhatro.view.component.*;

public class PhongView extends JPanel {

    private MyTextField txtTimKiem;
    private JCheckBox chkDaThue, chkConTrong, chkBaoTri;
    private MyTable tblPhong;
    public PhongView() {
        // Cấu hình Form
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // =================================================================
        // 1. KHU VỰC NORTH: TIÊU ĐỀ + TÌM KIẾM + LỌC
        // =================================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        // 1.1 Tiêu đề (Đã thêm lại theo yêu cầu)
        MyLabel lblTitle = new MyLabel("DANH SÁCH PHÒNG",MyLabel.HEADER, SwingConstants.CENTER);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Panel chứa Tìm kiếm (Trái) và Checkbox (Phải)
        JPanel pnlControl = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Panel Tìm Kiếm ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        MyLabel lblTim = new MyLabel("Tìm kiếm: ");
        txtTimKiem = new MyTextField("Nhập từ khóa cần tìm.....",300,35); // Độ dài chuẩn
        
        searchPanel.add(lblTim);
        searchPanel.add(txtTimKiem);
        pnlNorth.add(searchPanel, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        // --- Panel Checkbox (Đã bỏ tô đen focus) ---
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        
        chkDaThue = new JCheckBox("Đã thuê"); 
        chkDaThue.setFocusable(false); // Bỏ viền focus
        chkDaThue.setFont(new Font("Arial", Font.PLAIN, 13));

        chkConTrong = new JCheckBox("Còn trống"); 
        chkConTrong.setFocusable(false); 
        chkConTrong.setFont(new Font("Arial", Font.PLAIN, 13));

        chkBaoTri = new JCheckBox("Phòng bảo trì"); 
        chkBaoTri.setFocusable(false); 
        chkBaoTri.setFont(new Font("Arial", Font.PLAIN, 13));
        
        pnlFilter.add(chkDaThue); 
        pnlFilter.add(chkConTrong); 
        pnlFilter.add(chkBaoTri);

        // Add vào pnlControl (Search chiếm ít, Filter chiếm nhiều)
        gbc.weightx = 0.0; gbc.gridx = 0; pnlControl.add(searchPanel, gbc);
        gbc.weightx = 1.0; gbc.gridx = 1; pnlControl.add(pnlFilter, gbc);

        // Add pnlControl vào phía dưới tiêu đề
        pnlNorth.add(pnlControl, BorderLayout.SOUTH);
        
        add(pnlNorth, BorderLayout.NORTH);


        // =================================================================
        // 2. BẢNG DỮ LIỆU
        // =================================================================
        String[] headers = {
            "MaPhong", "Số Phòng", "Giá", "Số Người Ở Tối Đa", 
            "Phụ Thu", "Trạng Thái Phòng", "Ghi Chú" // Cột khớp với form Thêm/Sửa
        };
        
        tblPhong = new MyTable(headers);
        MyScrollTable scrollTable = new MyScrollTable(tblPhong, "");
        
        add(scrollTable, BorderLayout.CENTER);

    }
    
    public MyTable getTable() { return tblPhong; }
    public MyTextField getTxtTimKiem() { return txtTimKiem; }
    public JCheckBox getChkDaThue() { return chkDaThue; }
    public JCheckBox getChkConTrong() { return chkConTrong; }
    public JCheckBox getChkBaoTri() { return chkBaoTri; }
}