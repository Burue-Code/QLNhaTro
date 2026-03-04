package com.nctu.quanlynhatro.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.HoaDonDAO;
import com.nctu.quanlynhatro.model.HoaDon;
import com.nctu.quanlynhatro.view.component.MyPopupMenu;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.hoa_don.HoaDonView;
import com.nctu.quanlynhatro.view.hoa_don.ThemHoaDonView;

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
			table.addRow(new Object[] { hd.getMaHoaDon(), hd.getNgayThanhToan(), hd.getTongTien(),
					hd.getLoaiThanhToan(), hd.getPhuongThucThanhToan().getTenPT(), hd.getGhiChu()

			});
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
			ThemHoaDonView themHoaDonView = new ThemHoaDonView(model);
			themHoaDonView.setModal(true);
			new ThemHoaDonController(themHoaDonView, this);
			themHoaDonView.setVisible(true);
		});

		mnuSua.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				int modelRow = table.convertRowIndexToModel(row);
				long maHoaDon = Long.parseLong(model.getValueAt(modelRow, 0).toString());
				ThemHoaDonView suaHoaDonView = new ThemHoaDonView(model);
				suaHoaDonView.setModal(true);
				new SuaHoaDonController(suaHoaDonView, this, maHoaDon);
				suaHoaDonView.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một hóa đơn để sửa!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		mnuXoa.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				long maHoaDon = Long.parseLong(table.getValueAt(row, 0).toString());

				int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa hóa đơn này?\n"
						+ "Lưu ý: Các phiếu điện/nước thuộc hóa đơn này sẽ được trả về trạng thái 'Chưa thanh toán'.",
						"Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (confirm == JOptionPane.YES_OPTION) {
					// Gọi DAO để xóa
					boolean kq = hoaDonDAO.deleteHoaDon(maHoaDon);
					if (kq) {
						JOptionPane.showMessageDialog(view, "Xóa thành công!");
						refreshData(); // Load lại bảng
					} else {
						JOptionPane.showMessageDialog(view, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một dòng để xóa!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		mnuLamMoi.addActionListener(e -> {
			view.getTxtTimKiem().setText("");
			if (sorter != null) {
				sorter.setRowFilter(null);
			}
			refreshData(); // Gọi hàm load lại dữ liệu từ DB
		});
	}
}
