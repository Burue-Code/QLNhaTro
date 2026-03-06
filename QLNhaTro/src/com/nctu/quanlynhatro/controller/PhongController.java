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
import com.nctu.quanlynhatro.dao.PhongDAO;
import com.nctu.quanlynhatro.model.Phong;
import com.nctu.quanlynhatro.view.component.MyPopupMenu;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.phong.PhongView;
import com.nctu.quanlynhatro.view.phong.ThemPhongView;

public class PhongController {
	private PhongView view;
	private MyTable table;
	private DefaultTableModel model;
	private TableRowSorter<DefaultTableModel> sorter;
	private PhongDAO phongDAO;

	private List<Phong> listPhong;

	public PhongController(PhongView view) {
		this.view = view;
		this.table = view.getTable();
		this.model = table.getTableModel();
		phongDAO = new PhongDAO(DatabaseConnection.getConnection());

		initData();
		initSearch();
		initPopupMenu();
	}

	private void initData() {
		table.clear();
		listPhong = phongDAO.getAll();

		for (Phong p : listPhong) {
			table.addRow(new Object[] { p.getMaPhong(), p.getSoPhong(), p.getGia(), p.getSoNguoiToiDa(), p.getPhuThu(),
					p.getTrangThaiPhong(), p.getGhiChu() });
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
				apDungRowFilter();
			}
		});

		view.getChkDaThue().addActionListener(e -> thucHienTimKiem());
		view.getChkConTrong().addActionListener(e -> thucHienTimKiem());
		view.getChkBaoTri().addActionListener(e -> thucHienTimKiem());
	}

	private void thucHienTimKiem() {
		String keyword = view.getTxtTimKiem().getText().trim();
		boolean daThue = view.getChkDaThue().isSelected();
		boolean conTrong = view.getChkConTrong().isSelected();
		boolean baoTri = view.getChkBaoTri().isSelected();

		List<Phong> list = phongDAO.search(keyword, daThue, conTrong, baoTri);

		loadTable(list);
		apDungRowFilter();
	}

	private void apDungRowFilter() {
		String text = view.getTxtTimKiem().getText().trim();
		if (text.isEmpty()) {
			sorter.setRowFilter(null);
		} else {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
		}
	}

	private void loadTable(List<Phong> list) {
		model.setRowCount(0);

		this.listPhong = list;

		for (Phong p : list) {
			model.addRow(new Object[] { p.getMaPhong(), p.getSoPhong(), p.getGia(), p.getSoNguoiToiDa(), p.getPhuThu(),
					p.getTrangThaiPhong(), p.getGhiChu() });
		}
	}

	private void initPopupMenu() {
		MyPopupMenu popup = new MyPopupMenu(table);

		JMenuItem mnuThem = popup.addItem("Thêm Phòng");
		JMenuItem mnuSua = popup.addItem("Sửa Phòng");
		JMenuItem mnuXoa = popup.addItem("Xóa Phòng");
		popup.addSeparator();
		JMenuItem mnuLamMoi = popup.addItem("Làm Mới");

		mnuThem.addActionListener(e -> {
			ThemPhongView themPhongView = new ThemPhongView(model);
			themPhongView.setModal(true);
			new ThemPhongController(themPhongView, this);
		});

		mnuSua.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				int modelRow = table.convertRowIndexToModel(row);
				ThemPhongView suaPhongView = new ThemPhongView(model);
				suaPhongView.setModal(true);

				Phong phongCanSua = listPhong.get(modelRow);
				new SuaPhongController(suaPhongView, this, phongCanSua);

			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một phòng để sửa!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		mnuXoa.addActionListener(e -> {
			int viewRow = table.getSelectedRow();
			if (viewRow < 0) {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn phòng cần xóa!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			int confirm = JOptionPane.showConfirmDialog(view,
					"Bạn có chắc chắn muốn xóa phòng này không?\nLưu ý: Không thể xóa nếu phòng đang có khách thuê.",
					"Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			if (confirm == JOptionPane.YES_OPTION) {
				int modelRow = table.convertRowIndexToModel(viewRow);
				long maPhong = Long.parseLong(table.getModel().getValueAt(modelRow, 0).toString());

				if (phongDAO.deleteSoft(maPhong)) {
					JOptionPane.showMessageDialog(view, "Xóa phòng thành công!", "Thông báo",
							JOptionPane.INFORMATION_MESSAGE);
					refreshData(); // reload bảng
				} else {
					JOptionPane.showMessageDialog(view, "Xóa phòng thất bại! Vui lòng kiểm tra lại.", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		mnuLamMoi.addActionListener(e -> {
			view.getTxtTimKiem().setText("");
			if (sorter != null) {
				sorter.setRowFilter(null);
			}
			view.getChkDaThue().setSelected(false);
			view.getChkConTrong().setSelected(false);
			view.getChkBaoTri().setSelected(false);

			refreshData();
		});
	}
}