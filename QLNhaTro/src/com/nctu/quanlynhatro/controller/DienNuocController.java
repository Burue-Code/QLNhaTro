package com.nctu.quanlynhatro.controller;

import com.nctu.quanlynhatro.view.dien_nuoc.*;
import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.DienNuocDAO;
import com.nctu.quanlynhatro.model.PhieuDienNuoc;
import com.nctu.quanlynhatro.view.component.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;


public class DienNuocController {
	private DienNuocView view;
    private MyTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private DienNuocDAO dienNuocDAO;

    public DienNuocController(DienNuocView view) {
        this.view = view;
        this.table = view.getTable();
        this.model = table.getTableModel();

        initData();
        initSearch();
        initPopupMenu();
    }

    /* ================= DATA MẪU ================= */
   

    private void initData() {
        dienNuocDAO = new DienNuocDAO(DatabaseConnection.getConnection());

        model.setRowCount(0); // clear table

        for (PhieuDienNuoc dn : dienNuocDAO.getAll()) {
        	model.addRow(new Object[]{
                    dn.getMaDN(),
                    // Truy cập vào đối tượng Phong để lấy số phòng
                    (dn.getPhong() != null) ? dn.getPhong().getSoPhong() : "N/A",
                    dn.getThangNam(), // Trong Model bạn đặt là thangNam chứ không phải thoiGian
                    dn.getTienDien(), // Hoặc getGiaDienTaiThoiDiem() tùy mục đích hiển thị
                    dn.getTienNuoc(),
                    dn.getTongTien(),
                    // Hiển thị trạng thái dưới dạng chữ cho người dùng dễ đọc
                    dn.getTrangThaiDN()
            });
        }
    }

    /* ================= TÌM KIẾM ================= */
    private void initSearch() {
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        view.getTxtTimKiem().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = view.getTxtTimKiem().getText();
                sorter.setRowFilter(
                        text.isBlank()
                                ? null
                                : RowFilter.regexFilter("(?i)" + text)
                );
            }
        });
    }

    /* ================= POPUP MENU ================= */
    private void initPopupMenu() {
        MyPopupMenu popup = new MyPopupMenu(table);

        JMenuItem mnuThem = popup.addItem("Thêm Phiếu");
        JMenuItem mnuSua  = popup.addItem("Sửa Phiếu");
        JMenuItem mnuXoa  = popup.addItem("Xóa Phiếu");
        popup.addSeparator();
        JMenuItem mnuLamMoi = popup.addItem("Làm mới");

        // ==== ACTION ====
        mnuThem.addActionListener(e ->
                new ThemDienNuocView(model).setVisible(true)
        );

        mnuSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int modelRow = table.convertRowIndexToModel(row);
                new SuaDienNuocView(model, modelRow).setVisible(true);
            }
        });

        mnuXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                model.removeRow(table.convertRowIndexToModel(row));
            }
        });

        mnuLamMoi.addActionListener(e -> {
            view.getTxtTimKiem().setText("");
            sorter.setRowFilter(null);
        });
    }

}
