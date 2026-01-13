package com.nctu.quanlynhatro.view.thong_ke;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class ThongKeDoanhThuView extends JFrame {

    private JComboBox<Integer> cboNam;
    private JButton btnThongKe, btnXuatExcel;
    private JTable tblDoanhThu;
    private DefaultTableModel tableModel;
    private JLabel lblTongDoanhThu;

    public ThongKeDoanhThuView() {
        setTitle("Thống Kê Doanh Thu");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // =================================================================
        // 1. KHU VỰC NORTH: TIÊU ĐỀ + BỘ LỌC
        // =================================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        // 1.1 Tiêu đề
        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU NHÀ TRỌ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        // 1.2 Panel Bộ Lọc (Chọn Năm)
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        
        JLabel lblNam = new JLabel("Chọn Năm:");
        lblNam.setFont(new Font("Arial", Font.BOLD, 14));
        
        cboNam = new JComboBox<>();
        cboNam.setPreferredSize(new Dimension(100, 30));
        // Load năm: từ 2020 đến năm hiện tại
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= 2020; i--) {
            cboNam.addItem(i);
        }

        // --- SỬA LỖI TẠI ĐÂY: Khởi tạo nút Thống Kê ---
        btnThongKe = new JButton("Xem Thống Kê");
        btnThongKe.setPreferredSize(new Dimension(130, 30));
        btnThongKe.setBackground(new Color(0, 102, 204));
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setFocusPainted(false);

        btnXuatExcel = new JButton("Xuất Excel");
        btnXuatExcel.setPreferredSize(new Dimension(130, 30));
        btnXuatExcel.setBackground(new Color(34, 139, 34)); // Màu xanh lá
        btnXuatExcel.setForeground(Color.WHITE);
        btnXuatExcel.setFocusPainted(false);

        pnlFilter.add(lblNam);
        pnlFilter.add(cboNam);
        pnlFilter.add(btnThongKe); // Add vào panel sau khi đã new
        pnlFilter.add(btnXuatExcel);

        pnlNorth.add(pnlFilter, BorderLayout.SOUTH);
        mainPanel.add(pnlNorth, BorderLayout.NORTH);


        // =================================================================
        // 2. BẢNG DỮ LIỆU (CENTER)
        // =================================================================
        String[] headers = {
            "Tháng", "Số HĐ Đã Thu", "Tiền Phòng", "Tiền Điện Nước", "Phụ Phí", "Tổng Doanh Thu"
        };
        
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        tblDoanhThu = new JTable(tableModel);
        tblDoanhThu.setRowHeight(30);
        tblDoanhThu.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Header font thường
        tblDoanhThu.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 14));
        tblDoanhThu.getTableHeader().setBackground(new Color(230, 230, 230));
        tblDoanhThu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tblDoanhThu);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tblDoanhThu.setFillsViewportHeight(true);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        // =================================================================
        // 3. KHU VỰC SOUTH: TỔNG KẾT
        // =================================================================
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnlSouth.setBackground(new Color(245, 245, 245));
        pnlSouth.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JLabel lblTextTong = new JLabel("TỔNG DOANH THU CẢ NĂM:");
        lblTextTong.setFont(new Font("Arial", Font.BOLD, 16));
        
        lblTongDoanhThu = new JLabel("0 VNĐ");
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongDoanhThu.setForeground(Color.RED);

        pnlSouth.add(lblTextTong);
        pnlSouth.add(lblTongDoanhThu);

        mainPanel.add(pnlSouth, BorderLayout.SOUTH);

        // =================================================================
        // 4. SỰ KIỆN
        // =================================================================
        
        // Load dữ liệu mẫu khi mở
        loadDummyData();

        // Thêm sự kiện cho nút Thống Kê
        btnThongKe.addActionListener(e -> {
            int nam = (int) cboNam.getSelectedItem();
            JOptionPane.showMessageDialog(this, "Đang tải dữ liệu thống kê năm " + nam + "...");
            loadDummyData(); // Reload lại dữ liệu demo
        });

        btnXuatExcel.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chức năng xuất báo cáo Excel đang phát triển!");
        });
    }

    // Hàm tạo dữ liệu giả để test giao diện
    private void loadDummyData() {
        tableModel.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        long tongNam = 0;

        // Giả lập 12 tháng
        for (int i = 1; i <= 12; i++) {
            long tienPhong = 10000000 + (i * 500000);
            long dienNuoc = 3000000 + (i * 100000);
            long phuPhi = 500000;
            long tongThang = tienPhong + dienNuoc + phuPhi;
            
            tongNam += tongThang;

            tableModel.addRow(new Object[]{
                "Tháng " + i,
                "10", // Số HĐ
                df.format(tienPhong),
                df.format(dienNuoc),
                df.format(phuPhi),
                df.format(tongThang)
            });
        }
        
        // Cập nhật tổng
        lblTongDoanhThu.setText(df.format(tongNam) + " VNĐ");
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new ThongKeDoanhThuView().setVisible(true));
    }
}