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
import com.nctu.quanlynhatro.dao.HopDongDAO;
import com.nctu.quanlynhatro.model.HopDong;
import com.nctu.quanlynhatro.view.component.MyPopupMenu;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.hop_dong.HopDongView;
import com.nctu.quanlynhatro.view.hop_dong.ThemHopDongView;
import com.nctu.quanlynhatro.view.hop_dong.XemHopDongView;

public class HopDongController {

	private HopDongView view;
	private MyTable table;
	private DefaultTableModel model;
	private TableRowSorter<DefaultTableModel> sorter;
	private HopDongDAO hopDongDAO;
	private List<HopDong> listHopDong;

	public HopDongController(HopDongView view) {
		this.view = view;
		this.table = view.getTable();
		this.model = table.getTableModel();
		hopDongDAO = new HopDongDAO(DatabaseConnection.getConnection());

		initData();
		initSearch();
		initPopupMenu();
	}

	private void initData() {

		table.clear();
		listHopDong = hopDongDAO.getAll();
		for (HopDong hd : listHopDong) {
			table.addRow(new Object[] { hd.getMaHD(), hd.getTenKH(), hd.getNgayLap(), hd.getNgayKetThuc(),
					hd.getGiaThue(), hd.getSoNguoiO(), hd.getTrangThai(), hd.getGhiChu() });
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

	public DefaultTableModel getModel() {
		return model;
	}

	private void initPopupMenu() {
		MyPopupMenu popup = new MyPopupMenu(table);

		JMenuItem mnuThem = popup.addItem("Thêm Phiếu");
		JMenuItem mnuSua = popup.addItem("Sửa Phiếu");
		JMenuItem mnuXoa = popup.addItem("Xóa Phiếu");
		JMenuItem mnuXem = popup.addItem("Xem Chi Tiết Phiếu");
		popup.addSeparator();
		JMenuItem mnuLamMoi = popup.addItem("Làm mới");

		mnuThem.addActionListener(e -> {
			ThemHopDongView themHopDongView = new ThemHopDongView(model);
			themHopDongView.setModal(true);
			new ThemHopDongController(themHopDongView, this);
			themHopDongView.setVisible(true);
		});

		mnuSua.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				int modelRow = table.convertRowIndexToModel(row);
				long maHopDong = Long.parseLong(model.getValueAt(modelRow, 0).toString());
				ThemHopDongView suaHopDongView = new ThemHopDongView(model);
				suaHopDongView.setModal(true);
				new SuaHopDongController(suaHopDongView, this, maHopDong);
				suaHopDongView.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một khách hàng để sửa!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		mnuXoa.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				long maHD = Long.parseLong(table.getValueAt(row, 0).toString());
				String tenKH = table.getValueAt(row, 1).toString();
				int confirm = JOptionPane.showConfirmDialog(view,
						"Bạn có chắc chắn muốn XÓA hợp đồng của khách: " + tenKH + "?\n" + "Lưu ý: \n"
								+ "- Hợp đồng sẽ bị ẩn khỏi danh sách.\n"
								+ "- Phòng sẽ được trả về trạng thái 'Còn trống'.\n"
								+ "- Khách hàng sẽ bị xóa liên kết khỏi phòng.",
						"Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (confirm == JOptionPane.YES_OPTION) {
					boolean ketQua = hopDongDAO.deleteHopDong(maHD);

					if (ketQua) {
						JOptionPane.showMessageDialog(view, "Xóa hợp đồng thành công!");
						refreshData();
					} else {
						JOptionPane.showMessageDialog(view, "Xóa thất bại! Vui lòng kiểm tra lại.", "Lỗi",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một dòng để xóa!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		mnuXem.addActionListener(e -> {

			int row = table.getSelectedRow();
			if (row >= 0) {
				int modelRow = table.convertRowIndexToModel(row);
				long maHD = Long.parseLong(table.getModel().getValueAt(modelRow, 0).toString());

				XemHopDongView viewXem = new XemHopDongView();
				viewXem.setModal(true);
				new XemHopDongController(viewXem, maHD, this);
				viewXem.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn hợp đồng cần xem!", "Thông báo",
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
