package com.nctu.quanlynhatro.view.khach_hang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.nctu.quanlynhatro.view.component.*;

public class KhachHangView extends JPanel {

    private MyTable tblKhachHang;
    private DefaultTableModel tableModel;
    private JPopupMenu popupMenu;
    private JMenuItem mnuThem, mnuSua, mnuXoa, mnuLamMoi;
    private MyTextField txtTimKiem;
    
    // --- 1. KHAI BÁO BIẾN CHO BỘ LỌC TÌM KIẾM ---
    private TableRowSorter<DefaultTableModel> rowSorter;

    public KhachHangView() {
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- KHU VỰC NORTH: TIÊU ĐỀ + TÌM KIẾM ---
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        MyLabel lblTitle = new MyLabel("HỒ SƠ KHÁCH HÀNG", MyLabel.HEADER, SwingConstants.CENTER);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        MyLabel lblTimKiem = new MyLabel("Tìm kiếm:");
        
        txtTimKiem = new MyTextField("Nhập từ khóa");
        
        pnlSearch.add(lblTimKiem);
        pnlSearch.add(txtTimKiem);
        
        pnlNorth.add(pnlSearch, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);


        // --- BẢNG DỮ LIỆU ---
        String[] headers = {"MaKH", "Tên Khách Hàng", "Địa Chỉ", "Giới Tính", "Ngày Sinh", "Số Điện Thoại"};
        
        tblKhachHang = new MyTable(headers);
        MyScrollTable scrollTable = new MyScrollTable(tblKhachHang, "");
        add(scrollTable, BorderLayout.CENTER);
//        // --- 2. CẤU HÌNH BỘ LỌC CHO BẢNG ---
//        rowSorter = new TableRowSorter<>(tableModel);
//        tblKhachHang.setRowSorter(rowSorter);
//
//        // Thêm dữ liệu mẫu
//        tableModel.addRow(new Object[]{"KH001", "Nguyễn Văn A", "Cần Thơ", "Nam", "01/01/2000", "0909123456"});
//        tableModel.addRow(new Object[]{"KH002", "Trần Thị B", "Hậu Giang", "Nữ", "05/05/2001", "0918111222"});
//        tableModel.addRow(new Object[]{"KH003", "Lê Văn C", "Vĩnh Long", "Nam", "10/10/1999", "0933444555"});
//        
//        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
//        scrollPane.setBorder(new TitledBorder("Danh sách khách hàng"));
//        tblKhachHang.setFillsViewportHeight(true);
//        
//
//        // --- 3. XỬ LÝ SỰ KIỆN TÌM KIẾM ---
//        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) { filter(); }
//            @Override
//            public void removeUpdate(DocumentEvent e) { filter(); }
//            @Override
//            public void changedUpdate(DocumentEvent e) { filter(); }
//            
//            private void filter() {
//                String text = txtTimKiem.getText();
//                if (text.trim().length() == 0) {
//                    rowSorter.setRowFilter(null); // Không lọc nếu rỗng
//                } else {
//                    // (?i) để không phân biệt hoa thường
//                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
//                }
//            }
//        });
//
//        // --- CONTEXT MENU ---
//        popupMenu = new JPopupMenu();
//        mnuThem = new JMenuItem("Thêm Khách Hàng");
//        mnuSua = new JMenuItem("Sửa Thông Tin");
//        mnuXoa = new JMenuItem("Xóa Khách Hàng");
//        mnuLamMoi = new JMenuItem("Làm Mới Danh Sách");
//
//        popupMenu.add(mnuThem); popupMenu.add(mnuSua); popupMenu.add(mnuXoa);
//        popupMenu.addSeparator(); popupMenu.add(mnuLamMoi);
//
//        MouseAdapter mouseHandler = new MouseAdapter() {
//            public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
//            public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
//            private void showPopup(MouseEvent e) {
//                int row = tblKhachHang.rowAtPoint(e.getPoint());
//                if (row >= 0 && row < tblKhachHang.getRowCount()) {
//                    tblKhachHang.setRowSelectionInterval(row, row);
//                } else {
//                    tblKhachHang.clearSelection();
//                }
//                popupMenu.show(e.getComponent(), e.getX(), e.getY());
//            }
//        };
//        tblKhachHang.addMouseListener(mouseHandler);
//        scrollPane.addMouseListener(mouseHandler);
//
//        // --- XỬ LÝ CHỨC NĂNG ---
//        mnuThem.addActionListener(e -> {
//            ThemKhachHangView frmThem = new ThemKhachHangView(tableModel);
//            frmThem.setVisible(true);
//        });
//
//        mnuSua.addActionListener(e -> {
//            int row = tblKhachHang.getSelectedRow();
//            if (row >= 0) {
//                // LƯU Ý: Khi dùng Sorter, index dòng trên bảng (View) khác với trong Model
//                // Cần convertRowIndexToModel để lấy đúng dòng dữ liệu gốc
//                int modelRow = tblKhachHang.convertRowIndexToModel(row);
//                SuaKhachHangView frmSua = new SuaKhachHangView(tableModel, modelRow);
//                frmSua.setVisible(true);
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
//            }
//        });
//
//        mnuXoa.addActionListener(e -> {
//            int row = tblKhachHang.getSelectedRow();
//            if (row >= 0) {
//                int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//                if(confirm == JOptionPane.YES_OPTION) {
//                    // Convert index khi xóa
//                    int modelRow = tblKhachHang.convertRowIndexToModel(row);
//                    tableModel.removeRow(modelRow);
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
//            }
//        });
//        
//        mnuLamMoi.addActionListener(e -> {
//            txtTimKiem.setText(""); // Xóa ô tìm kiếm
//            JOptionPane.showMessageDialog(this, "Đã làm mới!");
//        });
   }
}