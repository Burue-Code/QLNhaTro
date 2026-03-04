package com.nctu.quanlynhatro.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.DienNuocDAO;
import com.nctu.quanlynhatro.model.PhieuDienNuoc;
import com.nctu.quanlynhatro.view.component.MyPopupMenu;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.dien_nuoc.DienNuocView;
import com.nctu.quanlynhatro.view.dien_nuoc.ThemDienNuocView;

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
		dienNuocDAO = new DienNuocDAO(DatabaseConnection.getConnection());

		initData();
		initSearch();
		initPopupMenu();
	}

	private void initData() {

		table.clear(); // clear table

		for (PhieuDienNuoc dn : dienNuocDAO.getAll()) {
			table.addRow(new Object[] { dn.getMaDN(),
					// Truy cập vào đối tượng Phong để lấy số phòng
					(dn.getPhong() != null) ? dn.getPhong().getSoPhong() : "N/A", dn.getThangNam(), // Trong Model bạn
																									// đặt là thangNam
																									// chứ không phải
																									// thoiGian
					dn.getTienDien(), // Hoặc getGiaDienTaiThoiDiem() tùy mục đích hiển thị
					dn.getTienNuoc(), dn.getTongTien(),
					// Hiển thị trạng thái dưới dạng chữ cho người dùng dễ đọc
					dn.getTrangThaiDN() });
		}
	}

	public void refreshData() {
		initData();
	}

	/* ================= TÌM KIẾM ================= */
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

	/* ================= POPUP MENU ================= */
	private void initPopupMenu() {
		MyPopupMenu popup = new MyPopupMenu(table);

		JMenuItem mnuThem = popup.addItem("Thêm Phiếu");
		JMenuItem mnuSua = popup.addItem("Sửa Phiếu");
		JMenuItem mnuXoa = popup.addItem("Xóa Phiếu");
		popup.addSeparator();
		JMenuItem mnuLamMoi = popup.addItem("Làm mới");

		// ==== ACTION ====
		mnuThem.addActionListener(e -> {
			ThemDienNuocView themDienNuocView = new ThemDienNuocView(model);
			themDienNuocView.setModal(true);
			new ThemDienNuocController(themDienNuocView, this);
			themDienNuocView.setVisible(true);
		}

		);

		mnuSua.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				int modelRow = table.convertRowIndexToModel(row);
				ThemDienNuocView suaDienNuocView = new ThemDienNuocView(model);
				suaDienNuocView.setModal(true);
				new SuaDienNuocController(suaDienNuocView, this, modelRow);
				suaDienNuocView.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một phiếu để sửa!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		mnuXoa.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				long maDN = Long.parseLong(table.getValueAt(row, 0).toString());

				int confirm = JOptionPane.showConfirmDialog(view,
						"Bạn có chắc chắn muốn xóa phiếu điện nước này không?", "Xác nhận xóa",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (confirm == JOptionPane.YES_OPTION) {
					// Gọi DAO để xóa mềm
					if (dienNuocDAO.delete(maDN)) {
						JOptionPane.showMessageDialog(view, "Xóa thành công!");
						refreshData(); // Load lại dữ liệu từ CSDL lên bảng
					} else {
						JOptionPane.showMessageDialog(view, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một dòng để xóa!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		mnuLamMoi.addActionListener(e -> {
			view.getTxtTimKiem().setText("");
			if (sorter != null) {
				sorter.setRowFilter(null);
			}
			refreshData();
		});
	}

}
