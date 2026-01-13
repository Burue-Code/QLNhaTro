package com.nctu.quanlynhatro.view.component;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class MyTable extends JTable {

    private DefaultTableModel model;

    public MyTable(String[] columns) {
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // không cho sửa ô
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
        initRowStyle();
        centerText();
    }

    /* ================= HEADER ================= */
    private void initHeader() {
        JTableHeader header = getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
    }

    /* ================= DÒNG XEN KẼ ================= */
    private void initRowStyle() {
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0
                            ? Color.WHITE
                            : new Color(245, 245, 245));
                }
                return c;
            }
        });
    }

    /* ================= CĂN GIỮA ================= */
    private void centerText() {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        setDefaultRenderer(String.class, center);
        setDefaultRenderer(Integer.class, center);
        setDefaultRenderer(Double.class, center);
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
