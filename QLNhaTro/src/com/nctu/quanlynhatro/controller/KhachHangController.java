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
import com.nctu.quanlynhatro.dao.KhachHangDAO;
import com.nctu.quanlynhatro.model.KhachHang;
import com.nctu.quanlynhatro.view.component.MyPopupMenu;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.khach_hang.KhachHangView;
import com.nctu.quanlynhatro.view.khach_hang.ThemKhachHangView;
import com.nctu.quanlynhatro.view.khach_hang.XemKhachHangView;

public class KhachHangController {
	private KhachHangView view;
	private MyTable table;
	private DefaultTableModel model;
	private TableRowSorter<DefaultTableModel> sorter;
	private KhachHangDAO khachHangDAO;

	private List<KhachHang> listKhachHang;

	public KhachHangController(KhachHangView view) {
		this.view = view;
		this.table = view.getTable();
		this.model = table.getTableModel();
		khachHangDAO = new KhachHangDAO(DatabaseConnection.getConnection());

		initData();
		initSearch();
		initPopupMenu();
	}

	private void initData() {

		table.clear(); // clear table
		listKhachHang = khachHangDAO.getAll();
		for (KhachHang kh : listKhachHang) {
			table.addRow(new Object[] { kh.getMaKH(), kh.getTenKH(), kh.getDiaChi(),
					kh.getGioiTinh() == false ? "Nam" : "Nữ", kh.getNgaySinh(), kh.getSdt(), kh.getCccd() });
		}
	}

	public void refreshData() {
		initData();
	}

	public DefaultTableModel getModel() {
		return model;
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

		JMenuItem mnuThem = popup.addItem("Thêm Khách Hàng");
		JMenuItem mnuSua = popup.addItem("Sửa Khách Hàng");
		JMenuItem mnuXoa = popup.addItem("Xóa Khách Hàng");
		JMenuItem mnuXem = popup.addItem("Xem Khách Hàng");
		popup.addSeparator();
		JMenuItem mnuLamMoi = popup.addItem("Làm mới");

		// ==== ACTION ====
		mnuThem.addActionListener(e -> {
			ThemKhachHangView themKhachHangView = new ThemKhachHangView(model);
			themKhachHangView.setModal(true);
			new ThemKhachHangController(themKhachHangView, this);
			themKhachHangView.setVisible(true);
		});

		mnuSua.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				int modelRow = table.convertRowIndexToModel(row);
				ThemKhachHangView suaKhachHangView = new ThemKhachHangView(model);
				suaKhachHangView.setModal(true);
				long maKH = Long.parseLong(model.getValueAt(modelRow, 0).toString());
				new SuaKhachHangController(suaKhachHangView, this, maKH);
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một khách hàng để sửa!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		mnuXoa.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				int confirm = JOptionPane.showConfirmDialog(view,
						"Bạn có chắc chắn muốn xóa khách hàng này không?\nDữ liệu không thể khôi phục sau khi xóa!",
						"Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (confirm == JOptionPane.YES_OPTION) {
					int modelRow = table.convertRowIndexToModel(row);
					KhachHang khXoa = listKhachHang.get(modelRow);

					boolean kq = khachHangDAO.delete(khXoa.getMaKH());

					if (kq) {
						JOptionPane.showMessageDialog(view, "Xóa khách hàng thành công!", "Thông báo",
								JOptionPane.INFORMATION_MESSAGE);
						initData(); // Tải lại bảng ngay lập tức
					} else {
						JOptionPane.showMessageDialog(view,
								"Xóa thất bại. Khách hàng này có thể đang liên kết với Hợp đồng/Phòng!", "Lỗi",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một khách hàng để xóa!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		mnuXem.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				int modelRow = table.convertRowIndexToModel(row);
				long maKH = Long.parseLong(model.getValueAt(modelRow, 0).toString());
				XemKhachHangView xemKhachHangView = new XemKhachHangView(null);
				xemKhachHangView.setModal(true);
				new XemKhachHangController(xemKhachHangView, maKH, this);
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một khách hàng để xem!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		mnuLamMoi.addActionListener(e -> {
			view.getTxtTimKiem().setText("");
			sorter.setRowFilter(null);
			initData();
		});
	}
}
