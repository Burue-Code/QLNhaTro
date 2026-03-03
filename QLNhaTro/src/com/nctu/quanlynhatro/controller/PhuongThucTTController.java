package com.nctu.quanlynhatro.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.PhuongThucThanhToanDAO;
import com.nctu.quanlynhatro.model.PhuongThucThanhToan;
import com.nctu.quanlynhatro.view.component.*;
import com.nctu.quanlynhatro.view.phuong_thuc_tt.*;

public class PhuongThucTTController {
	private PhuongThucThanhToanView view;
    private MyTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private PhuongThucThanhToanDAO phuongThucThanhToanDAO;
    private List<PhuongThucThanhToan> ListPTTT;

    public PhuongThucTTController(PhuongThucThanhToanView view) {
        this.view = view;
        this.table = view.getTable();
        this.model = table.getTableModel();
        phuongThucThanhToanDAO = new PhuongThucThanhToanDAO(DatabaseConnection.getConnection());

        initData();
        initSearch();
        initPopupMenu();
    }

   

    private void initData() {

    	table.clear();
    	ListPTTT = phuongThucThanhToanDAO.getAll();
        for (PhuongThucThanhToan pt : phuongThucThanhToanDAO.getAll()) {
        	table.addRow(new Object[]{
                   pt.getMaPT(),
                   pt.getTenPT()
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
        mnuThem.addActionListener(e ->{
        	ThemPhuongThucThanhToanView themPhuongThucThanhToanView = new ThemPhuongThucThanhToanView(model);
        	themPhuongThucThanhToanView.setModal(true);
        	new ThemPTTTController(themPhuongThucThanhToanView, this);
        	initData();
        });

        mnuSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int modelRow = table.convertRowIndexToModel(row);
                PhuongThucThanhToan pTTT = ListPTTT.get(modelRow);
                ThemPhuongThucThanhToanView themPTTTView = new ThemPhuongThucThanhToanView(model);
                themPTTTView.setModal(true);
                new SuaPTTTController(themPTTTView, this, pTTT);
                initData();
            }
        });

        mnuXoa.addActionListener(e -> {
        	int viewRow = table.getSelectedRow();
            if (viewRow < 0) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn phòng cần xóa!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "Bạn có chắc chắn muốn xóa phương thức này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            int modelRow = table.convertRowIndexToModel(viewRow);

            long maPhong = Long.parseLong(
                    table.getModel().getValueAt(modelRow, 0).toString()
            );

            if (phuongThucThanhToanDAO.deleteSoft(maPhong)) {
                JOptionPane.showMessageDialog(view, "Xóa phòng thành công!");
                initData(); // reload bảng
            } else {
                JOptionPane.showMessageDialog(view, "Xóa phòng thất bại!");
            }
        });

        mnuLamMoi.addActionListener(e -> {
            view.getTxtTimKiem().setText("");
            sorter.setRowFilter(null);
        });
    }
}
