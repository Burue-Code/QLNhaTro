package com.nctu.quanlynhatro.view.phuong_thuc_tt;

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

public class PhuongThucThanhToanView extends JPanel {

    private MyTextField txtTimKiem;
    private MyTable tblPhuongThuc;
    private DefaultTableModel tableModel;
    
    private JPopupMenu popupMenu;
    private JMenuItem mnuThem, mnuSua, mnuXoa, mnuLamMoi;
    
    private TableRowSorter<DefaultTableModel> rowSorter;

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
        
//        tableModel = new DefaultTableModel(headers, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false; 
//            }
//        };
//
//        tblPhuongThuc = new JTable(tableModel);
//        tblPhuongThuc.setRowHeight(30); 
//        tblPhuongThuc.setFont(new Font("Arial", Font.PLAIN, 14));
//        
//        // Header Font thường (không đậm)
//        tblPhuongThuc.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 14)); 
//        tblPhuongThuc.getTableHeader().setBackground(new Color(230, 230, 230));
//        tblPhuongThuc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        
//        // Chỉnh độ rộng cột (Mã nhỏ, Tên to)
//        tblPhuongThuc.getColumnModel().getColumn(0).setPreferredWidth(200); 
//        tblPhuongThuc.getColumnModel().getColumn(1).setPreferredWidth(500); 
//
//        // Bộ lọc tìm kiếm
//        rowSorter = new TableRowSorter<>(tableModel);
//        tblPhuongThuc.setRowSorter(rowSorter);
//        
//        // Thêm dữ liệu mẫu
//        tableModel.addRow(new Object[]{"PT01", "Tiền mặt"});
//        tableModel.addRow(new Object[]{"PT02", "Chuyển khoản"});
//        tableModel.addRow(new Object[]{"PT03", "Ví điện tử MoMo"});
//
//        JScrollPane scrollPane = new JScrollPane(tblPhuongThuc);
//        scrollPane.getViewport().setBackground(Color.WHITE); 
//        tblPhuongThuc.setFillsViewportHeight(true); 
//        
//        add(scrollPane, BorderLayout.CENTER);
//        
//        // --- 3. SỰ KIỆN TÌM KIẾM ---
//        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
//            public void insertUpdate(DocumentEvent e) { filter(); }
//            public void removeUpdate(DocumentEvent e) { filter(); }
//            public void changedUpdate(DocumentEvent e) { filter(); }
//            private void filter() {
//                String text = txtTimKiem.getText();
//                if (text.trim().length() == 0) rowSorter.setRowFilter(null);
//                else rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
//            }
//        });
//
//        // --- 4. CONTEXT MENU & SỰ KIỆN ---
//        popupMenu = new JPopupMenu();
//        mnuThem = new JMenuItem("Thêm Phương Thức");
//        mnuSua = new JMenuItem("Sửa Phương Thức");
//        mnuXoa = new JMenuItem("Xóa Phương Thức");
//        mnuLamMoi = new JMenuItem("Làm Mới");
//
//        popupMenu.add(mnuThem); 
//        popupMenu.add(mnuSua); 
//        popupMenu.add(mnuXoa);
//        popupMenu.addSeparator();
//        popupMenu.add(mnuLamMoi);
//
//        tblPhuongThuc.addMouseListener(new MouseAdapter() {
//            public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
//            public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
//            private void showPopup(MouseEvent e) {
//                int row = tblPhuongThuc.rowAtPoint(e.getPoint());
//                if (row >= 0 && row < tblPhuongThuc.getRowCount()) {
//                    tblPhuongThuc.setRowSelectionInterval(row, row);
//                } else {
//                    tblPhuongThuc.clearSelection();
//                }
//                popupMenu.show(e.getComponent(), e.getX(), e.getY());
//            }
//        });
//        
//        // --- XỬ LÝ SỰ KIỆN MENU ---
//        
//        // 1. Thêm
//        mnuThem.addActionListener(e -> {
//            // Gọi form Thêm (Cần tạo class ThemPhuongThucView)
//            ThemPhuongThucThanhToanView frm = new ThemPhuongThucThanhToanView(tableModel);
//            frm.setVisible(true);
//        });
//        
//        // 2. Sửa
//        mnuSua.addActionListener(e -> {
//            int viewRow = tblPhuongThuc.getSelectedRow();
//            if (viewRow >= 0) {
//                int modelRow = tblPhuongThuc.convertRowIndexToModel(viewRow);
//                // Gọi form Sửa (Cần tạo class SuaPhuongThucView)
//                SuaPhuongThucThanhToanView frm = new SuaPhuongThucThanhToanView(tableModel, modelRow);
//                frm.setVisible(true);
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa!");
//            }
//        });
//        
//        // 3. Xóa
//        mnuXoa.addActionListener(e -> {
//            int viewRow = tblPhuongThuc.getSelectedRow();
//            if (viewRow >= 0) {
//                int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//                if (confirm == JOptionPane.YES_OPTION) {
//                    int modelRow = tblPhuongThuc.convertRowIndexToModel(viewRow);
//                    tableModel.removeRow(modelRow);
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
//            }
//        });
//        
//        // 4. Làm mới
//        mnuLamMoi.addActionListener(e -> {
//            txtTimKiem.setText("");
//            rowSorter.setRowFilter(null);
//            JOptionPane.showMessageDialog(this, "Đã làm mới danh sách!");
//        });
    }
}