package com.nctu.quanlynhatro.view.component;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class MyTable extends JTable {

    private DefaultTableModel model;

    public MyTable(String[] columns) {
        // --- SỬA 1: Override getColumnClass để Table nhận diện đúng kiểu dữ liệu (Số, Chữ) ---
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Nếu bảng có dữ liệu, lấy kiểu dữ liệu của dòng đầu tiên
                if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
                    return getValueAt(0, columnIndex).getClass();
                }
                return Object.class;
            }
        };

        setModel(model);
        setRowHeight(32);
        setFont(new Font("SansSerif", Font.PLAIN, 13));
        setSelectionBackground(new Color(220, 235, 250));
        setSelectionForeground(Color.BLACK);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));

        initHeader();
        
        // --- SỬA 2: Gọi hàm setup render chung (gộp cả màu nền và căn lề) ---
        setupCellRenderers();
    }

    /* ================= HEADER ================= */
    private void initHeader() {
        JTableHeader header = getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
    }

    /* ================= XỬ LÝ RENDER (MÀU NỀN + CĂN LỀ) ================= */
    private void setupCellRenderers() {
        // Tạo các Renderer riêng cho từng kiểu căn lề, nhưng đều dùng chung logic tô màu nền
        setDefaultRenderer(String.class, new MyCustomRenderer(SwingConstants.LEFT));
        setDefaultRenderer(Integer.class, new MyCustomRenderer(SwingConstants.CENTER));
        setDefaultRenderer(Long.class, new MyCustomRenderer(SwingConstants.CENTER));
        setDefaultRenderer(Double.class, new MyCustomRenderer(SwingConstants.CENTER));
        setDefaultRenderer(Object.class, new MyCustomRenderer(SwingConstants.CENTER));
    }

    // Class con để xử lý giao diện cho từng ô
    private class MyCustomRenderer extends DefaultTableCellRenderer {
        private int alignment;

        public MyCustomRenderer(int alignment) {
            this.alignment = alignment;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // 1. Căn lề
            setHorizontalAlignment(this.alignment);

            // 2. Tô màu nền xen kẽ (Zebra Stripe)
            if (!isSelected) {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
            } else {
                setBackground(table.getSelectionBackground());
            }

            return this;
        }
    }

    /* ================= API TIỆN DÙNG ================= */
    public void addRow(Object[] row) {
        model.addRow(row);
    }

    public void clear() {
        model.setRowCount(0);
    }

    public DefaultTableModel getTableModel() {
        return model;
    }
}