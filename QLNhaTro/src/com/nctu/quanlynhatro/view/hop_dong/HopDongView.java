package com.nctu.quanlynhatro.view.hop_dong;

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

public class HopDongView extends JPanel {

    private MyTable tblHopDong;
    private DefaultTableModel tableModel;
    private JPopupMenu popupMenu;
    private JMenuItem mnuThem, mnuSua, mnuXoa, mnuLamMoi;
    
    // --- Biến cho tìm kiếm ---
    private MyTextField txtTimKiem;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public HopDongView() {
        // Cấu hình Form
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // =================================================================
        // PHẦN 1: KHU VỰC NORTH (TIÊU ĐỀ + TÌM KIẾM)
        // =================================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        // 1.1 Tiêu đề
        MyLabel lblTitle = new MyLabel("DANH SÁCH HỢP ĐỒNG THUÊ PHÒNG TRỌ", MyLabel.HEADER, SwingConstants.CENTER);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Panel Tìm Kiếm (Mới bổ sung)
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        MyLabel lblSearch = new MyLabel("Tìm kiếm:");
        
        txtTimKiem = new MyTextField("Nhập từ khóa cần tìm");
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        
        pnlSearch.add(lblSearch);
        pnlSearch.add(txtTimKiem);
        
        pnlNorth.add(pnlSearch, BorderLayout.SOUTH);
        
        add(pnlNorth, BorderLayout.NORTH);


        // =================================================================
        // PHẦN 2: BẢNG DỮ LIỆU
        // =================================================================
        String[] headers = {
            "MaHD", "Tên Khách Hàng", "Ngày Lập Hợp Đồng", "Ngày Kết Thúc Hợp Đồng", "Giá Thuê", "Trạng Thái Hợp Đồng", "Ghi Chú"
        };
        
        tblHopDong = new MyTable(headers);
        MyScrollTable scrollTable = new MyScrollTable(tblHopDong, "");
        add(scrollTable, BorderLayout.CENTER);
        
//        // --- CẤU HÌNH BỘ LỌC TÌM KIẾM ---
//        rowSorter = new TableRowSorter<>(tableModel);
//        tblHopDong.setRowSorter(rowSorter);
//        
//        // Thêm dữ liệu mẫu
//        tableModel.addRow(new Object[]{"HD001", "Nguyễn Văn A", "01/01/2023", "01/01/2024", "2,500,000", "Hiệu lực", ""});
//        tableModel.addRow(new Object[]{"HD002", "Trần Thị B", "15/05/2023", "15/05/2024", "3,000,000", "Sắp hết hạn", "Gia hạn thêm"});
//        
//        
//
//        // --- XỬ LÝ SỰ KIỆN TÌM KIẾM ---
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
//                    rowSorter.setRowFilter(null);
//                } else {
//                    // (?i) tìm không phân biệt hoa thường
//                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
//                }
//            }
//        });
//
//
//        // =================================================================
//        // PHẦN 3: CONTEXT MENU
//        // =================================================================
//        popupMenu = new JPopupMenu();
//        mnuThem = new JMenuItem("Thêm Hợp Đồng Mới");
//        mnuSua = new JMenuItem("Sửa Hợp Đồng");
//        mnuXoa = new JMenuItem("Hủy Hợp Đồng");
//        mnuLamMoi = new JMenuItem("Làm Mới Danh Sách");
//
//        popupMenu.add(mnuThem); popupMenu.add(mnuSua); popupMenu.add(mnuXoa);
//        popupMenu.addSeparator(); popupMenu.add(mnuLamMoi);
//
//        MouseAdapter mouseHandler = new MouseAdapter() {
//            public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
//            public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
//            private void showPopup(MouseEvent e) {
//                int row = tblHopDong.rowAtPoint(e.getPoint());
//                if (row >= 0 && row < tblHopDong.getRowCount()) {
//                    tblHopDong.setRowSelectionInterval(row, row);
//                } else {
//                    tblHopDong.clearSelection();
//                }
//                popupMenu.show(e.getComponent(), e.getX(), e.getY());
//            }
//        };
//        tblHopDong.addMouseListener(mouseHandler);
//        scrollPane.addMouseListener(mouseHandler);
//
//        // =================================================================
//        // PHẦN 4: XỬ LÝ CHỨC NĂNG
//        // =================================================================
//
//        // 1. Thêm
//        mnuThem.addActionListener(e -> {
//            ThemHopDongView frmThem = new ThemHopDongView(tableModel);
//            frmThem.setVisible(true);
//        });
//
//        // 2. Sửa
//        mnuSua.addActionListener(e -> {
//            int viewRow = tblHopDong.getSelectedRow();
//            if (viewRow >= 0) {
//                // QUAN TRỌNG: Convert index khi đang lọc
//                int modelRow = tblHopDong.convertRowIndexToModel(viewRow);
//                SuaHopDongView frmSua = new SuaHopDongView(tableModel, modelRow);
//                frmSua.setVisible(true);
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hợp đồng để sửa!");
//            }
//        });
//
//        // 3. Xóa
//        mnuXoa.addActionListener(e -> {
//            int viewRow = tblHopDong.getSelectedRow();
//            if (viewRow >= 0) {
//                int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa hợp đồng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//                if(confirm == JOptionPane.YES_OPTION) {
//                    // QUAN TRỌNG: Convert index khi đang lọc
//                    int modelRow = tblHopDong.convertRowIndexToModel(viewRow);
//                    tableModel.removeRow(modelRow);
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hợp đồng để xóa!");
//            }
//        });
//
//        // 4. Làm mới
//        mnuLamMoi.addActionListener(e -> {
//            txtTimKiem.setText(""); // Xóa ô tìm kiếm
//            JOptionPane.showMessageDialog(this, "Đã làm mới danh sách!");
//        });
    }
}