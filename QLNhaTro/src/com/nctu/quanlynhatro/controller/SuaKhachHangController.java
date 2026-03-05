package com.nctu.quanlynhatro.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.KhachHangDAO;
import com.nctu.quanlynhatro.model.KhachHang;
import com.nctu.quanlynhatro.view.component.MyTable;
import com.nctu.quanlynhatro.view.khach_hang.ThemKhachHangView;

public class SuaKhachHangController {
	private ThemKhachHangView view;
	private MyTable table;
	private DefaultTableModel model;
	private TableRowSorter<DefaultTableModel> sorter;
	private KhachHangDAO khachHangDAO;
	private KhachHangController parentController;
	private KhachHang khachHangCanSua;

	public SuaKhachHangController(ThemKhachHangView view, KhachHangController parentController, long maKH) {
		this.view = view;
		this.table = view.getTblKhachHang();
		this.model = table.getTableModel();
		this.parentController = parentController;

		this.khachHangDAO = new KhachHangDAO(DatabaseConnection.getConnection());
		this.khachHangCanSua = this.khachHangDAO.findById(maKH);
		if (this.khachHangCanSua == null) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy dữ liệu khách hàng này!", "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			view.dispose();
			return;
		}

		this.view.setTitle("Sửa Khách Hàng Mới");
		this.view.getBtnThem().setText("Lưu Thay Đổi");

		initData();
		initSearch();
		fillData();
		initEvents();
		this.view.setVisible(true);
	}

	private void initEvents() {
		// Nút Hủy
		view.getBtnHuy().addActionListener(e -> view.dispose());

		// Nút Cập nhật (View bạn đặt tên nút là "Thêm Mới" nhưng dùng chung form thì
		// vẫn lấy getBtnThem)
		view.getBtnThem().addActionListener(e -> xuLyCapNhat());
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

	private void fillData() {
		if (khachHangCanSua == null) {
			return;
		}

		// Đổ dữ liệu từ Object KhachHang vào View
		view.setTenKhachHang(khachHangCanSua.getTenKH());
		view.setDiaChi(khachHangCanSua.getDiaChi());
		view.setSoDienThoai(khachHangCanSua.getSdt());
		view.setCCCD(khachHangCanSua.getCccd());
		view.setEmail(khachHangCanSua.getGmail());

		// Xử lý ngày sinh (Chuyển từ LocalDate sang String)
		if (khachHangCanSua.getNgaySinh() != null) {
			view.setNgaySinh(khachHangCanSua.getNgaySinh().toString());
		}

		// Xử lý Giới tính (Giả sử false = Nam, true = Nữ theo logic của bạn)
		view.setGioiTinh(khachHangCanSua.getGioiTinh());

		// Xử lý Khách hàng chính (nếu có ghép phòng)
		if (khachHangCanSua.getKhachHangChinh() > 0) {
			view.setMaKhachHangChinh(String.valueOf(khachHangCanSua.getKhachHangChinh()));

			// Gọi hàm findById từ DAO để lấy thông tin Khách hàng chính
			KhachHang khachHangChinh = khachHangDAO.findById(khachHangCanSua.getKhachHangChinh());

			// Nếu tìm thấy, lấy tên và set lên View
			if (khachHangChinh != null) {
				view.setTenKhachHangChinh(khachHangChinh.getTenKH());
			}
		}
	}

	private void xuLyCapNhat() {
		try {
			// 1. Lấy dữ liệu mới từ Form
			String tenKH = view.getTenKhachHang();
			String cccd = view.getCCCD();
			String sdt = view.getSoDienThoai();
			String diaChi = view.getDiaChi();
			String ngaySinh = view.getNgaySinh();
			String email = view.getEmail();
			boolean gioiTinh = view.getGioiTinh().equals("Nữ"); // Giả sử false=Nam, true=Nữ
			String maKHC_str = view.getMaKhachHangChinh();

			// 2. Validate dữ liệu cơ bản
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
				JOptionPane.showMessageDialog(view, "Số điện thoại không hợp lệ (10 số, bắt đầu bằng 0)!",
						"Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// 3. Cập nhật dữ liệu mới vào Object KhachHang hiện tại (khachHangCanSua)
			khachHangCanSua.setTenKH(tenKH);
			khachHangCanSua.setCccd(cccd);
			khachHangCanSua.setSdt(sdt);
			khachHangCanSua.setDiaChi(diaChi);
			khachHangCanSua.setGioiTinh(gioiTinh);
			khachHangCanSua.setGmail(email);

			// Chuyển đổi ngày sinh từ String -> LocalDate (Nếu bạn dùng JDateChooser thì
			// getDate() rồi convert sẽ chuẩn hơn)
			if (!ngaySinh.isEmpty()) {
				try {
					khachHangCanSua.setNgaySinh(java.time.LocalDate.parse(ngaySinh));
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(view, "Ngày sinh không đúng định dạng (YYYY-MM-DD)!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				khachHangCanSua.setNgaySinh(null);
			}

			// Chuyển đổi Mã KH Chính từ String -> Long
			if (!maKHC_str.isEmpty()) {
				khachHangCanSua.setKhachHangChinh(Long.parseLong(maKHC_str));
			} else {
				khachHangCanSua.setKhachHangChinh(0); // 0 mang ý nghĩa không có KH chính
			}

			// 4. Chuẩn bị danh sách Khách Hàng Phụ
			// Hiện tại giao diện chưa có phần chọn KH Phụ nên tạm truyền list rỗng.
			List<Long> danhSachKHPhu = new ArrayList<>();

			// 5. Gọi DAO để thực hiện Update
			boolean kq = khachHangDAO.updateKhachHang(khachHangCanSua, danhSachKHPhu);

			// 6. Xử lý kết quả
			if (kq) {
				JOptionPane.showMessageDialog(view, "Cập nhật khách hàng thành công!", "Thành công",
						JOptionPane.INFORMATION_MESSAGE);
				if (parentController != null) {
					parentController.refreshData(); // Gọi lại hàm initData() bên KhachHangController để làm mới bảng
				}
				view.dispose(); // Đóng form
			} else {
				JOptionPane.showMessageDialog(view, "Lỗi khi cập nhật vào cơ sở dữ liệu!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			ex.printStackTrace(); // In lỗi ra console để dễ debug
			JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
}