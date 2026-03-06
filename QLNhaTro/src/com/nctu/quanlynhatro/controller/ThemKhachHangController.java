package com.nctu.quanlynhatro.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.KhachHangDAO;
import com.nctu.quanlynhatro.model.KhachHang;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.khach_hang.ThemKhachHangView;

public class ThemKhachHangController {
	private ThemKhachHangView view;
	private MyTable table;
	private DefaultTableModel model;
	private TableRowSorter<DefaultTableModel> sorter;
	private KhachHangDAO khachHangDAO;
	private KhachHangController parentController;

	public ThemKhachHangController(ThemKhachHangView view, KhachHangController parentController) {
		this.view = view;
		this.table = view.getTblKhachHang();
		this.model = table.getTableModel();
		this.parentController = parentController;
		this.khachHangDAO = new KhachHangDAO(DatabaseConnection.getConnection());

		this.view.setTitle("Thêm Khách Hàng Mới");
		this.view.getBtnThem().setText("Thêm Mới");

		initData();
		initSearch();
		initEvents();
	}

	private void initEvents() {
		view.getBtnHuy().addActionListener(e -> view.dispose());
		view.getBtnThem().addActionListener(e -> xuLyThemMoi());

		setChiNhapSoNguyen(view.getTxtCCCD());
		setChiNhapSoNguyen(view.getTxtSDT());
		setChiNhapSoNgay(view.getTxtNgaySinh());
	}

	private void setChiNhapSoNguyen(javax.swing.JTextField txt) {
		txt.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
					e.consume();
				}
			}
		});
	}

	private void setChiNhapSoNgay(javax.swing.JTextField txt) {
		txt.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE && c != '-') {
					e.consume();
				}

				String currentText = txt.getText();
				int hyphenCount = currentText.length() - currentText.replace("-", "").length();

				if (c == '-' && hyphenCount >= 2) {
					e.consume();
				}
			}
		});
	}

	private void xuLyThemMoi() {
		String tenKH = view.getTenKhachHang().trim();
		String cccd = view.getCCCD().trim();
		String sdt = view.getSoDienThoai().trim();
		String diaChi = view.getDiaChi().trim();
		String email = view.getEmail().trim();
		String maKHC_str = view.getMaKhachHangChinh().trim();
		String ngaySinhStr = view.getNgaySinh().trim();

		boolean gioiTinh = view.getGioiTinh().equals("Nữ");

		if (tenKH.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập tên khách hàng!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!cccd.matches("\\d{12}")) {
			JOptionPane.showMessageDialog(view, "CCCD/CMND phải bao gồm đúng 12 chữ số!", "Lỗi nhập liệu",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!sdt.matches("0\\d{9}")) {
			JOptionPane.showMessageDialog(view, "Số điện thoại không hợp lệ (10 số, bắt đầu bằng số 0)!",
					"Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
			return;
		}

		java.time.LocalDate ngaySinh = null;
		if (!ngaySinhStr.isEmpty()) {
			try {
				ngaySinh = java.time.LocalDate.parse(ngaySinhStr);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(view, "Ngày sinh không đúng định dạng (Năm-Tháng-Ngày, VD: 2000-12-30)!",
						"Lỗi", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		long maKHC = 0;
		if (!maKHC_str.isEmpty()) {
			try {
				maKHC = Long.parseLong(maKHC_str);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(view, "Mã khách hàng chính phải là một con số!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		try {
			boolean kq = khachHangDAO.insert(tenKH, diaChi, ngaySinh, sdt, gioiTinh, cccd, email, maKHC);

			if (kq) {
				JOptionPane.showMessageDialog(view, "Thêm khách hàng thành công!", "Thành công",
						JOptionPane.INFORMATION_MESSAGE);

				if (parentController != null) {
					parentController.refreshData();
				}
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, "Thêm thất bại. Vui lòng kiểm tra lại dữ liệu!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initData() {
		table.clear();
		for (KhachHang dn : khachHangDAO.getAll()) {
			table.addRow(new Object[] { dn.getMaKH(), dn.getTenKH(), dn.getDiaChi(),
					dn.getGioiTinh() == false ? "Nam" : "Nữ", dn.getNgaySinh(), dn.getSdt() });
		}
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
}