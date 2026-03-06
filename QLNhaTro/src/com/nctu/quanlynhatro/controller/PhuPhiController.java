package com.nctu.quanlynhatro.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.PhuPhiDAO;
import com.nctu.quanlynhatro.model.PhuPhi;
import com.nctu.quanlynhatro.view.component.MyPopupMenu;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.phu_phi.PhuPhiView;
import com.nctu.quanlynhatro.view.phu_phi.ThemPhuPhiView;

public class PhuPhiController {
	private PhuPhiView view;
	private MyTable table;
	private DefaultTableModel model;
	private TableRowSorter<DefaultTableModel> sorter;
	private PhuPhiDAO phuPhiDAO;
	private List<PhuPhi> listPhuPhi;

	public PhuPhiController(PhuPhiView view) {
		this.view = view;
		this.table = view.getTable();
		this.model = table.getTableModel();
		phuPhiDAO = new PhuPhiDAO(DatabaseConnection.getConnection());

		initData();
		initSearch();
		initPopupMenu();
	}

	private void initData() {

		table.clear(); // clear table
		listPhuPhi = phuPhiDAO.getAll();
		for (PhuPhi pp : listPhuPhi) {
			table.addRow(new Object[] { pp.getMaPP(), pp.getTenPP(), pp.getGia() });
		}
	}

	public void refreshData() {
		initData();
	}

	private void initSearch() {
		sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);

		view.getTxtTimKiem().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = view.getTxtTimKiem().getText();
				sorter.setRowFilter(text.isBlank() ? null : RowFilter.regexFilter("(?i)" + text));
			}
		});
	}

	private void initPopupMenu() {
		MyPopupMenu popup = new MyPopupMenu(table);

		JMenuItem mnuThem = popup.addItem("Thêm Phiếu");
		JMenuItem mnuSua = popup.addItem("Sửa Phiếu");
		JMenuItem mnuXoa = popup.addItem("Xóa Phiếu");
		popup.addSeparator();
		JMenuItem mnuLamMoi = popup.addItem("Làm mới");

		mnuThem.addActionListener(e -> {
			ThemPhuPhiView themPhuPhiView = new ThemPhuPhiView(model);
			themPhuPhiView.setModal(true);
			new ThemPhuPhiController(themPhuPhiView, this);
			initData();
		});

		mnuSua.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				int modelRow = table.convertRowIndexToModel(row);
				PhuPhi phuPhiCanSua = listPhuPhi.get(modelRow);
				ThemPhuPhiView suaPhuPhiView = new ThemPhuPhiView(model);
				suaPhuPhiView.setModal(true);
				new SuaPhuPhiController(suaPhuPhiView, this, phuPhiCanSua);
			}
			initData();
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