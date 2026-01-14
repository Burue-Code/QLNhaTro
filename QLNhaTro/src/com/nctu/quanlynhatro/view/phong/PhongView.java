package com.nctu.quanlynhatro.view.phong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PhongView extends JPanel {

    private JTextField txtTimKiem;
    private JCheckBox chkDaThue, chkConTrong, chkBaoTri;
    private JTable tblPhong;
    private DefaultTableModel tableModel;
    
    private JPopupMenu popupMenu;
    private JMenuItem mnuThem, mnuSua, mnuXoa, mnuLamMoi; // Thêm nút làm mới cho đủ bộ
    
    private TableRowSorter<DefaultTableModel> rowSorter;

    public PhongView() {
        // Cấu hình Form
    	setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // =================================================================
        // 1. KHU VỰC NORTH: TIÊU ĐỀ + TÌM KIẾM + LỌC
        // =================================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        // 1.1 Tiêu đề (Đã thêm lại theo yêu cầu)
        JLabel lblTitle = new JLabel("DANH SÁCH PHÒNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Panel chứa Tìm kiếm (Trái) và Checkbox (Phải)
        JPanel pnlControl = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Panel Tìm Kiếm ---
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        lblTimKiem.setFont(new Font("Arial", Font.BOLD, 14)); // Font Đậm
        lblTimKiem.setForeground(new Color(0, 51, 102)); 
        
        txtTimKiem = new JTextField(25); 
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        
        pnlSearch.add(lblTimKiem);
        pnlSearch.add(txtTimKiem);

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
        gbc.weightx = 0.0; gbc.gridx = 0; pnlControl.add(pnlSearch, gbc);
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
        
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblPhong = new JTable(tableModel);
        tblPhong.setRowHeight(35); // Chiều cao dòng thoáng hơn
        tblPhong.setFont(new Font("Arial", Font.PLAIN, 14));
        tblPhong.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblPhong.getTableHeader().setBackground(new Color(230, 230, 230));
        tblPhong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Chỉnh độ rộng cột
        tblPhong.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã
        tblPhong.getColumnModel().getColumn(1).setPreferredWidth(120); // Tên
        tblPhong.getColumnModel().getColumn(2).setPreferredWidth(120); // Loại
        tblPhong.getColumnModel().getColumn(3).setPreferredWidth(100); // Giá
        tblPhong.getColumnModel().getColumn(4).setPreferredWidth(80);  // DT
        tblPhong.getColumnModel().getColumn(5).setPreferredWidth(120); // Trạng thái
        tblPhong.getColumnModel().getColumn(6).setPreferredWidth(250); // Mô tả

        // Bộ lọc tìm kiếm
        rowSorter = new TableRowSorter<>(tableModel);
        tblPhong.setRowSorter(rowSorter);
        
        // Thêm dữ liệu mẫu
        tableModel.addRow(new Object[]{"P001", "Phòng 101", "Phòng Thường", "2,500,000", "25m2", "Đã Thuê", ""});
        tableModel.addRow(new Object[]{"P002", "Phòng 102", "Phòng Máy Lạnh", "3,000,000", "30m2", "Phòng Trống", "Có ban công"});
        tableModel.addRow(new Object[]{"P003", "Phòng 201", "Phòng VIP", "4,500,000", "40m2", "Bảo Trì", "Sửa máy nước nóng"});

        JScrollPane scrollPane = new JScrollPane(tblPhong);
        scrollPane.getViewport().setBackground(Color.WHITE); 
        tblPhong.setFillsViewportHeight(true); 
        
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
        mnuThem = new JMenuItem("Thêm Phòng Mới");
        mnuSua = new JMenuItem("Sửa Thông Tin");
        mnuXoa = new JMenuItem("Xóa Phòng");
        mnuLamMoi = new JMenuItem("Làm Mới Danh Sách"); // Thêm nút làm mới

        popupMenu.add(mnuThem); 
        popupMenu.add(mnuSua); 
        popupMenu.add(mnuXoa);
        popupMenu.addSeparator();
        popupMenu.add(mnuLamMoi);

        tblPhong.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
            public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
            private void showPopup(MouseEvent e) {
                int row = tblPhong.rowAtPoint(e.getPoint());
                if (row >= 0 && row < tblPhong.getRowCount()) {
                    tblPhong.setRowSelectionInterval(row, row);
                } else {
                    tblPhong.clearSelection();
                }
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        
        // --- XỬ LÝ SỰ KIỆN MENU ---
        
        // 1. Thêm
        mnuThem.addActionListener(e -> {
            ThemPhongView frm = new ThemPhongView(tableModel);
            frm.setVisible(true);
        });
        
        // 2. Sửa
        mnuSua.addActionListener(e -> {
            int viewRow = tblPhong.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = tblPhong.convertRowIndexToModel(viewRow);
                // Truyền đúng model và dòng cần sửa
                SuaPhongView frm = new SuaPhongView(tableModel, modelRow); 
                frm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần sửa!");
            }
        });
        
        // 3. Xóa
        mnuXoa.addActionListener(e -> {
            int viewRow = tblPhong.getSelectedRow();
            if (viewRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa phòng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int modelRow = tblPhong.convertRowIndexToModel(viewRow);
                    tableModel.removeRow(modelRow);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần xóa!");
            }
        });
        
        // 4. Làm mới
        mnuLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            rowSorter.setRowFilter(null);
            // TODO: Load lại dữ liệu từ DB tại đây
            JOptionPane.showMessageDialog(this, "Đã làm mới danh sách!");
        });
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new PhongView().setVisible(true));
    }
}