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
import com.nctu.quanlynhatro.dao.NhaTroDAO;
import com.nctu.quanlynhatro.model.NhaTro;
import com.nctu.quanlynhatro.view.component.MyPopupMenu;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.nha_tro.NhaTroView;
import com.nctu.quanlynhatro.view.nha_tro.ThemNhaTroView;

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
		initSearch();
		initPopupMenu();
	}

	private void initData() {
		nhaTroDAO = new NhaTroDAO(DatabaseConnection.getConnection());

		table.clear(); // clear table
		listNhaTro = nhaTroDAO.getAll();
		for (NhaTro nt : listNhaTro) {
			table.addRow(new Object[] { nt.getMaNT(), nt.getTenNT(), nt.getSLPhong(), nt.getDiaChi(),
					nt.getTrangThaiNT(), nt.getGhiChu() });
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

		JMenuItem mnuThem = popup.addItem("Thêm Nhà Trọ");
		JMenuItem mnuSua = popup.addItem("Sửa Nhà Trọ");
		JMenuItem mnuXoa = popup.addItem("Xóa Nhà Trọ");
		popup.addSeparator();
		JMenuItem mnuLamMoi = popup.addItem("Làm mới");

		mnuThem.addActionListener(e -> {
			ThemNhaTroView themNhaTroView = new ThemNhaTroView(model);
			themNhaTroView.setModal(true);

			new ThemNhaTroController(themNhaTroView, this);
		});

		mnuSua.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				int modelRow = table.convertRowIndexToModel(row);

				ThemNhaTroView suaNhaTroView = new ThemNhaTroView(model);
				suaNhaTroView.setModal(true);

				NhaTro nhaTroCanSua = listNhaTro.get(modelRow);
				new SuaNhaTroController(suaNhaTroView, this, nhaTroCanSua);
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một nhà trọ để sửa!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		mnuXoa.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				int modelRow = table.convertRowIndexToModel(row);

				long maNT = Long.parseLong(model.getValueAt(modelRow, 0).toString());
				String tenNT = model.getValueAt(modelRow, 1).toString();

				int confirm = JOptionPane.showConfirmDialog(view,
						"Bạn có chắc chắn muốn XÓA nhà trọ: " + tenNT
								+ "?\nLưu ý: Không thể xóa nếu nhà trọ này đang chứa các phòng.",
						"Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (confirm == JOptionPane.YES_OPTION) {
					boolean kq = nhaTroDAO.deleteSoft(maNT);

					if (kq) {
						JOptionPane.showMessageDialog(view, "Xóa nhà trọ thành công!", "Thông báo",
								JOptionPane.INFORMATION_MESSAGE);
						refreshData();
					} else {
						JOptionPane.showMessageDialog(view, "Xóa thất bại! Vui lòng kiểm tra lại.", "Lỗi",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn một nhà trọ để xóa!", "Cảnh báo",
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