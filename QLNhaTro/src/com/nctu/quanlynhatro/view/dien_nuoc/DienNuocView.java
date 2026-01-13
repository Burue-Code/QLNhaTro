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

public class DienNuocView extends JFrame {

    private JTable tblDanhSach;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    
    private JTextField txtTimKiem; 
    
    private JPopupMenu popupMenu;
    private JMenuItem mnuThem, mnuSua, mnuXoa, mnuLamMoi;

    public DienNuocView() {
        setTitle("Quản lý Điện Nước");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // =================================================================
        // PHẦN 1: HEADER (TIÊU ĐỀ + TÌM KIẾM)
        // =================================================================
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // 1.1 Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ THU PHÍ ĐIỆN NƯỚC", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Khu vực tìm kiếm (Căn trái)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel lblTim = new JLabel("Tìm kiếm: ");
        lblTim.setFont(new Font("Arial", Font.PLAIN, 14)); // Font Đậm
        lblTim.setForeground(new Color(0, 51, 102)); // Màu xanh đen
        
        txtTimKiem = new JTextField(25); // Độ dài chuẩn
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        
        searchPanel.add(lblTim);
        searchPanel.add(txtTimKiem);
        
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // =================================================================
        // PHẦN 2: BẢNG DỮ LIỆU
        // =================================================================
        String[] headers = {
            "Mã ĐN", "Số Phòng", "Thời Gian", 
            "Giá Điện", "Giá Nước", "Tổng Tiền", "Trạng Thái"
        };
        
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDanhSach = new JTable(tableModel);
        tblDanhSach.setRowHeight(30);
        tblDanhSach.setFont(new Font("Arial", Font.PLAIN, 14));
        tblDanhSach.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Cài đặt bộ lọc tìm kiếm
        sorter = new TableRowSorter<>(tableModel);
        tblDanhSach.setRowSorter(sorter);

        // Dữ liệu mẫu
        tableModel.addRow(new Object[]{"DN001", "P101", "10/2023", "500000", "100000", "600000", "Đã đóng"});
        tableModel.addRow(new Object[]{"DN002", "P102", "10/2023", "750000", "150000", "900000", "Chưa đóng"});
        
        JScrollPane scrollPane = new JScrollPane(tblDanhSach);
        scrollPane.setBorder(new TitledBorder("Danh sách hóa đơn"));
        tblDanhSach.setFillsViewportHeight(true);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // =================================================================
        // PHẦN 3: XỬ LÝ TÌM KIẾM (KeyAdapter)
        // =================================================================
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = txtTimKiem.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    // Tìm kiếm không phân biệt hoa thường
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        // =================================================================
        // PHẦN 4: CONTEXT MENU & SỰ KIỆN
        // =================================================================
        popupMenu = new JPopupMenu();
        mnuThem = new JMenuItem("Thêm Phiếu Mới");
        mnuSua = new JMenuItem("Sửa Phiếu Này");
        mnuXoa = new JMenuItem("Xóa Phiếu Này");
        mnuLamMoi = new JMenuItem("Làm Mới Danh Sách");

        popupMenu.add(mnuThem);
        popupMenu.add(mnuSua);
        popupMenu.add(mnuXoa);
        popupMenu.addSeparator();
        popupMenu.add(mnuLamMoi);

        // Sự kiện chuột phải
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
            @Override
            public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
            
            private void showPopup(MouseEvent e) {
                int row = tblDanhSach.rowAtPoint(e.getPoint());
                if (row >= 0 && row < tblDanhSach.getRowCount()) {
                    tblDanhSach.setRowSelectionInterval(row, row);
                } else {
                    tblDanhSach.clearSelection();
                }
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        };
        tblDanhSach.addMouseListener(mouseHandler);
        scrollPane.addMouseListener(mouseHandler);

        // --- XỬ LÝ CHỨC NĂNG ---
        
        mnuThem.addActionListener(e -> {
            ThemDienNuocView frmThem = new ThemDienNuocView(tableModel);
            frmThem.setVisible(true);
        });

        mnuSua.addActionListener(e -> {
            int row = tblDanhSach.getSelectedRow();
            if (row >= 0) {
                // Chuyển đổi chỉ số hàng khi có tìm kiếm/sắp xếp
                int modelRow = tblDanhSach.convertRowIndexToModel(row);
                SuaDienNuocView frmSua = new SuaDienNuocView(tableModel, modelRow);
                frmSua.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu để sửa!");
            }
        });

        mnuXoa.addActionListener(e -> {
            int row = tblDanhSach.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Xóa phiếu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION) {
                    int modelRow = tblDanhSach.convertRowIndexToModel(row);
                    tableModel.removeRow(modelRow);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu để xóa!");
            }
        });

        mnuLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            sorter.setRowFilter(null);
        });
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new DienNuocView().setVisible(true));
    }
}