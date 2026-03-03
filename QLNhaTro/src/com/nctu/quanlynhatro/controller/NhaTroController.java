package com.nctu.quanlynhatro.controller;

import com.nctu.quanlynhatro.view.nha_tro.*;
import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.NhaTroDAO;
import com.nctu.quanlynhatro.model.NhaTro;
import com.nctu.quanlynhatro.view.component.*;

import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

public class NhaTroController {
	private NhaTroView view;
	private MyTable table;
	private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private NhaTroDAO nhaTroDAO;
    private List<NhaTro> listNhaTro;
    
    public NhaTroController(NhaTroView view) {
    	this.view = view;
    	this.table = view.getTable();
    	this.model = table.getTableModel();
    	
    	initData();
    	initPopupMenu();
    }
    
    private void initData() {
        nhaTroDAO = new NhaTroDAO(DatabaseConnection.getConnection());

        table.clear(); // lear table
        listNhaTro = nhaTroDAO.getAll();
        for (NhaTro nt : listNhaTro) {
            table.addRow(new Object[]{
                    nt.getMaNT(),
                    nt.getTenNT(),
                    nt.getSLPhong(),
                    nt.getDiaChi(),
                    nt.getTrangThaiNT(),
                    nt.getGhiChu()
            });
        }
    }
    
    private void initPopupMenu() {
        MyPopupMenu popup = new MyPopupMenu(table);

        JMenuItem mnuThem = popup.addItem("Thêm Nhà Trọ");
        JMenuItem mnuSua  = popup.addItem("Sửa Nhà Trọ");
        JMenuItem mnuXoa  = popup.addItem("Xóa Nhà Trọ");
        popup.addSeparator();
        JMenuItem mnuLamMoi = popup.addItem("Làm mới");

        // ==== ACTION ====
        mnuThem.addActionListener(e ->{
        	ThemNhaTroView themNhaTroView =  new ThemNhaTroView(model); 
        	themNhaTroView.setModal(true);

        	new ThemNhaTroController(themNhaTroView, this);
        	initData();
        });

        mnuSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int modelRow = table.convertRowIndexToModel(row);
                
                ThemNhaTroView suaNhaTroView = new ThemNhaTroView(model);
                suaNhaTroView.setModal(true);

                NhaTro nhaTroCanSua = listNhaTro.get(modelRow);
                new SuaNhaTroController(suaNhaTroView, this, nhaTroCanSua);
                initData();
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
