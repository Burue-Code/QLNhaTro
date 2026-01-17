package com.nctu.quanlynhatro.view.hoa_don;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        String[] headers = {"Mã HĐ", "Phòng", "Ngày Lập", "Tổng Tiền", "Trạng Thái", "Ghi Chú"};
        
        tblHoaDon = new MyTable(headers);
        MyScrollTable scrollTable = new MyScrollTable(tblHoaDon, "");
        
        add(scrollTable, BorderLayout.CENTER);

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
//        mnuThem = new JMenuItem("Lập Hóa Đơn Mới");
//        mnuSua = new JMenuItem("Sửa Hóa Đơn");
//        mnuXoa = new JMenuItem("Xóa Hóa Đơn");
//        mnuLamMoi = new JMenuItem("Làm Mới Danh Sách");
//
//        popupMenu.add(mnuThem);
//        popupMenu.add(mnuSua);
//        popupMenu.add(mnuXoa);
//        popupMenu.addSeparator();
//        popupMenu.add(mnuLamMoi);
//
//        MouseAdapter mouseHandler = new MouseAdapter() {
//            public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
//            public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
//            private void showPopup(MouseEvent e) {
//                int row = tblHoaDon.rowAtPoint(e.getPoint());
//                if (row >= 0 && row < tblHoaDon.getRowCount()) {
//                    tblHoaDon.setRowSelectionInterval(row, row);
//                } else {
//                    tblHoaDon.clearSelection();
//                }
//                popupMenu.show(e.getComponent(), e.getX(), e.getY());
//            }
//        };
//        tblHoaDon.addMouseListener(mouseHandler);
//        scrollPane.addMouseListener(mouseHandler);
//
//        // =================================================================
//        // PHẦN 4: XỬ LÝ CHỨC NĂNG
//        // =================================================================
//
//        // 1. Thêm
//        mnuThem.addActionListener(e -> {
//            ThemHoaDonView frmThem = new ThemHoaDonView(tableModel);
//            frmThem.setVisible(true);
//        });
//
//        // 2. Sửa
//        mnuSua.addActionListener(e -> {
//            int viewRow = tblHoaDon.getSelectedRow();
//            if (viewRow >= 0) {
//                // QUAN TRỌNG: Convert index khi đang lọc
//                int modelRow = tblHoaDon.convertRowIndexToModel(viewRow);
//                SuaHoaDonView frmSua = new SuaHoaDonView(tableModel, modelRow);
//                frmSua.setVisible(true);
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để sửa!");
//            }
//        });
//
//        // 3. Xóa
//        mnuXoa.addActionListener(e -> {
//            int viewRow = tblHoaDon.getSelectedRow();
//            if (viewRow >= 0) {
//                int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa hóa đơn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//                if(confirm == JOptionPane.YES_OPTION) {
//                    // QUAN TRỌNG: Convert index khi đang lọc
//                    int modelRow = tblHoaDon.convertRowIndexToModel(viewRow);
//                    tableModel.removeRow(modelRow);
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để xóa!");
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
