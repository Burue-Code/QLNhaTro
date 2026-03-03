package com.nctu.quanlynhatro.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.HoaDonDAO;
import com.nctu.quanlynhatro.model.HoaDon;
import com.nctu.quanlynhatro.view.component.*;
import com.nctu.quanlynhatro.view.hoa_don.*;

public class HoaDonController {
	private HoaDonView view;
	private MyTable table;
	private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private HoaDonDAO hoaDonDAO;
    
    public HoaDonController(HoaDonView view) {
    	this.view = view;
    	this.table = view.getTable();
    	this.model = table.getTableModel();
    	hoaDonDAO = new HoaDonDAO(DatabaseConnection.getConnection());
    	
    	initData();
        initSearch();
        initPopupMenu();
    }
    
    private void initData() {

        table.clear(); // clear table

        for (HoaDon hd : hoaDonDAO.getAll()) {
        	table.addRow(new Object[]{
        			hd.getMaHoaDon(),
        			hd.getNgayThanhToan(),
        			hd.getTongTien(),
        			hd.getLoaiThanhToan(),
        			hd.getPhuongThucThanhToan(),
        			hd.getGhiChu()
                    
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
                new ThemHoaDonView(model).setVisible(true)
        );

        mnuSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int modelRow = table.convertRowIndexToModel(row);
                new SuaHoaDonView(model, modelRow).setVisible(true);
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
