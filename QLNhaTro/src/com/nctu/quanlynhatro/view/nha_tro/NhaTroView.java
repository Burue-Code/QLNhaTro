package com.nctu.quanlynhatro.view.nha_tro;

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

public class NhaTroView extends JPanel {

    private MyTable tblNhaTro;
    private DefaultTableModel tableModel;
    private JPopupMenu popupMenu;
    private JMenuItem mnuThem, mnuSua, mnuXoa, mnuLamMoi;
    
    private MyTextField txtTimKiem;
    
    // 1. Khai báo biến bộ lọc
    private TableRowSorter<DefaultTableModel> rowSorter;

    public NhaTroView() {
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- KHU VỰC NORTH (TIÊU ĐỀ + TÌM KIẾM) ---
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        MyLabel lblTitle = new MyLabel("DANH SÁCH NHÀ TRỌ", MyLabel.HEADER, SwingConstants.CENTER);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        MyLabel lblSearch = new MyLabel("Tìm kiếm:");
        
        txtTimKiem = new MyTextField("Nhập từ khóa cần tìm");
        
        pnlTimKiem.add(lblSearch);
        pnlTimKiem.add(txtTimKiem);
        
        pnlNorth.add(pnlTimKiem, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU ---
        String[] headers = {"MaNT", "Tên Nhà Trọ", "Số Lượng Phòng", "Địa Chỉ", "Trạng Thái", "Ghi Chú"};
        
        tblNhaTro = new MyTable(headers);
        MyScrollTable scrollTable = new MyScrollTable(tblNhaTro, "");
        
        add(scrollTable, BorderLayout.CENTER);
        
//        // 2. Cài đặt bộ lọc cho bảng
//        rowSorter = new TableRowSorter<>(tableModel);
//        tblNhaTro.setRowSorter(rowSorter);
//        
//        tblNhaTro.getColumnModel().getColumn(0).setPreferredWidth(50); 
//        tblNhaTro.getColumnModel().getColumn(1).setPreferredWidth(150); 
//        
//        JScrollPane scrollPane = new JScrollPane(tblNhaTro);
//        scrollPane.setBorder(new TitledBorder("Danh sách chi tiết"));
//        tblNhaTro.setFillsViewportHeight(true); 
//        
        

//        // --- XỬ LÝ TÌM KIẾM (Gõ đến đâu lọc đến đó) ---
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
//                    // (?i) để tìm không phân biệt hoa thường
//                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
//                }
//            }
//        });
//
//        // --- MENU CHUỘT PHẢI ---
//        popupMenu = new JPopupMenu();
//        mnuThem = new JMenuItem("Thêm Nhà Trọ Mới");
//        mnuSua = new JMenuItem("Sửa Thông Tin");
//        mnuXoa = new JMenuItem("Xóa Nhà Trọ");
//        mnuLamMoi = new JMenuItem("Làm Mới");
//
//        popupMenu.add(mnuThem); popupMenu.add(mnuSua); popupMenu.add(mnuXoa);
//        popupMenu.addSeparator(); popupMenu.add(mnuLamMoi);
//
//        MouseAdapter mouseHandler = new MouseAdapter() {
//            public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
//            public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
//            private void showPopup(MouseEvent e) {
//                int row = tblNhaTro.rowAtPoint(e.getPoint());
//                if (row >= 0 && row < tblNhaTro.getRowCount()) {
//                    tblNhaTro.setRowSelectionInterval(row, row);
//                } else {
//                    tblNhaTro.clearSelection();
//                }
//                popupMenu.show(e.getComponent(), e.getX(), e.getY());
//            }
//        };
//        tblNhaTro.addMouseListener(mouseHandler);
//        scrollPane.addMouseListener(mouseHandler);
//
//        // --- SỰ KIỆN CHỨC NĂNG ---
//        mnuThem.addActionListener(e -> {
//            ThemNhaTroView frm = new ThemNhaTroView(tableModel);
//            frm.setVisible(true);
//        });
//
//        mnuSua.addActionListener(e -> {
//            int viewRow = tblNhaTro.getSelectedRow();
//            if (viewRow >= 0) {
//                // 3. QUAN TRỌNG: Chuyển đổi chỉ số dòng khi bảng đang được lọc/sắp xếp
//                int modelRow = tblNhaTro.convertRowIndexToModel(viewRow);
//                
//                SuaNhaTroView frm = new SuaNhaTroView(tableModel, modelRow);
//                frm.setVisible(true);
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà trọ cần sửa!");
//            }
//        });
//
//        mnuXoa.addActionListener(e -> {
//            int viewRow = tblNhaTro.getSelectedRow();
//            if (viewRow >= 0) {
//                int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn xóa dòng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//                if(confirm == JOptionPane.YES_OPTION) {
//                    // 3. QUAN TRỌNG: Chuyển đổi chỉ số dòng
//                    int modelRow = tblNhaTro.convertRowIndexToModel(viewRow);
//                    tableModel.removeRow(modelRow);
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà trọ cần xóa!");
//            }
//        });
//        
//        mnuLamMoi.addActionListener(e -> {
//            txtTimKiem.setText(""); // Xóa tìm kiếm để hiện lại toàn bộ
//            JOptionPane.showMessageDialog(this, "Đã làm mới!");
//        });
    }
}