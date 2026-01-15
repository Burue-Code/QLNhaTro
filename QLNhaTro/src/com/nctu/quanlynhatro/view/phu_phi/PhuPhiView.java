package com.nctu.quanlynhatro.view.phu_phi;

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

public class PhuPhiView extends JPanel {

    private JTextField txtTimKiem;
    private JTable tblPhuPhi;
    private DefaultTableModel tableModel;
    
    private JPopupMenu popupMenu;
    private JMenuItem mnuThem, mnuSua, mnuXoa, mnuLamMoi;
    
    private TableRowSorter<DefaultTableModel> rowSorter;

    public PhuPhiView() {
        // Cấu hình Form
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // =================================================================
        // 1. KHU VỰC NORTH: TIÊU ĐỀ + TÌM KIẾM
        // =================================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        // 1.1 Tiêu đề
        JLabel lblTitle = new JLabel("DANH SÁCH PHỤ PHÍ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Panel Tìm Kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        lblTimKiem.setFont(new Font("Arial", Font.PLAIN, 14)); 
        lblTimKiem.setForeground(new Color(0, 51, 102)); 
        
        txtTimKiem = new JTextField(25); 
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        
        pnlSearch.add(lblTimKiem);
        pnlSearch.add(txtTimKiem);

        pnlNorth.add(pnlSearch, BorderLayout.SOUTH);
        
        add(pnlNorth, BorderLayout.NORTH);


        // =================================================================
        // 2. BẢNG DỮ LIỆU
        // =================================================================
        String[] headers = {
            "MaPP", "Tên Phụ Phí", "Giá"
        };
        
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        tblPhuPhi = new JTable(tableModel);
        tblPhuPhi.setRowHeight(30); 
        tblPhuPhi.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // --- SỬA TẠI ĐÂY: Bỏ Font.BOLD, chuyển thành Font.PLAIN ---
        tblPhuPhi.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 14)); 
        tblPhuPhi.getTableHeader().setBackground(new Color(230, 230, 230));
        tblPhuPhi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Chỉnh độ rộng cột
        tblPhuPhi.getColumnModel().getColumn(0).setPreferredWidth(100); 
        tblPhuPhi.getColumnModel().getColumn(1).setPreferredWidth(300); 
        tblPhuPhi.getColumnModel().getColumn(2).setPreferredWidth(150); 

        // Bộ lọc tìm kiếm
        rowSorter = new TableRowSorter<>(tableModel);
        tblPhuPhi.setRowSorter(rowSorter);
        
        // Thêm dữ liệu mẫu
        tableModel.addRow(new Object[]{"PP001", "Wifi", "50,000"});
        tableModel.addRow(new Object[]{"PP002", "Vệ sinh", "20,000"});
        tableModel.addRow(new Object[]{"PP003", "Gửi xe", "100,000"});
        tableModel.addRow(new Object[]{"PP004", "Rác", "15,000"});

        JScrollPane scrollPane = new JScrollPane(tblPhuPhi);
        scrollPane.getViewport().setBackground(Color.WHITE); 
        tblPhuPhi.setFillsViewportHeight(true); 
        
        add(scrollPane, BorderLayout.CENTER);
        
        // --- 3. SỰ KIỆN TÌM KIẾM ---
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = txtTimKiem.getText();
                if (text.trim().length() == 0) rowSorter.setRowFilter(null);
                else rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        // --- 4. CONTEXT MENU & SỰ KIỆN ---
        popupMenu = new JPopupMenu();
        mnuThem = new JMenuItem("Thêm Phụ Phí");
        mnuSua = new JMenuItem("Sửa Phụ Phí");
        mnuXoa = new JMenuItem("Xóa Phụ Phí");
        mnuLamMoi = new JMenuItem("Làm Mới");

        popupMenu.add(mnuThem); 
        popupMenu.add(mnuSua); 
        popupMenu.add(mnuXoa);
        popupMenu.addSeparator();
        popupMenu.add(mnuLamMoi);

        tblPhuPhi.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
            public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
            private void showPopup(MouseEvent e) {
                int row = tblPhuPhi.rowAtPoint(e.getPoint());
                if (row >= 0 && row < tblPhuPhi.getRowCount()) {
                    tblPhuPhi.setRowSelectionInterval(row, row);
                } else {
                    tblPhuPhi.clearSelection();
                }
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        
        // --- XỬ LÝ SỰ KIỆN MENU ---
        mnuThem.addActionListener(e -> {
            ThemPhuPhiView frm = new ThemPhuPhiView(tableModel); // Gọi form Thêm
            frm.setVisible(true);
        });
        
        mnuSua.addActionListener(e -> {
            int viewRow = tblPhuPhi.getSelectedRow();
            if (viewRow >= 0) {
                // Lấy đúng dòng trong model (khi có lọc)
                int modelRow = tblPhuPhi.convertRowIndexToModel(viewRow);
                
                // Gọi form Sửa (Bạn cần tạo class SuaPhuPhiView tương ứng)
                SuaPhuPhiView frm = new SuaPhuPhiView(tableModel, modelRow); 
                frm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phụ phí cần sửa!");
            }
        });
        
        mnuXoa.addActionListener(e -> {
            int viewRow = tblPhuPhi.getSelectedRow();
            if (viewRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa phụ phí này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int modelRow = tblPhuPhi.convertRowIndexToModel(viewRow);
                    tableModel.removeRow(modelRow);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phụ phí cần xóa!");
            }
        });
        
        mnuLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            rowSorter.setRowFilter(null);
            JOptionPane.showMessageDialog(this, "Đã làm mới danh sách!");
        });
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new PhuPhiView().setVisible(true));
    }
}
