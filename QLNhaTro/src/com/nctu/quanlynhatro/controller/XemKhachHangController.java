package com.nctu.quanlynhatro.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.KhachHangDAO;
import com.nctu.quanlynhatro.view.khach_hang.ThemKhachHangView;
import com.nctu.quanlynhatro.view.khach_hang.XemKhachHangView;

public class XemKhachHangController {

	private XemKhachHangView view;
	private KhachHangDAO dao;
	private long maKH;
	private KhachHangController parentController;
	private String maPhongLuuTam = null;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public XemKhachHangController(XemKhachHangView view, long maKH, KhachHangController parentController) {
		this.view = view;
		this.maKH = maKH;
		this.parentController = parentController;
		this.dao = new KhachHangDAO(DatabaseConnection.getConnection());

		view.setTitle("Xem Hồ Sơ Khách Hàng - Mã: " + maKH);

		loadData();
		initEvents();

		view.setVisible(true);
	}

	private void loadData() {
		Map<String, Object> kh = dao.getChiTietKhachHang(maKH);
		if (kh.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy dữ liệu khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			view.dispose();
			return;
		}

		view.setTxtTenKH((String) kh.get("TenKH"));
		view.setTxtGioiTinh((Boolean) kh.get("GioiTinh") ? "Nam" : "Nữ");
		view.setTxtSDT((String) kh.get("SDT"));
		view.setTxtDiaChi((String) kh.get("DiaChi"));
		view.setTxtCCCD((String) kh.get("SoCCCD"));
		view.setTxtGmail((String) kh.get("Gmail"));

		java.sql.Date ngaySinh = (java.sql.Date) kh.get("NgaySinh");
		view.setTxtNgaySinh(ngaySinh != null ? sdf.format(ngaySinh) : "");

		maPhongLuuTam = (String) kh.get("SoPhong");
		view.setTxtOPHong(maPhongLuuTam != null ? maPhongLuuTam : "Chưa xếp phòng");

		String maHD = (String) kh.get("MaHD");
		view.setTxtThuocHopDong(maHD != null ? maHD : "Không có");

		String tenKhachChinh = (String) kh.get("TenKhachChinh");
		view.setTxtTenKhachHangChinh(tenKhachChinh != null ? tenKhachChinh : "Là khách chính");

		if (maPhongLuuTam != null) {
			List<Map<String, Object>> phuThuoc = dao.getKhachHangPhuThuocTheoPhong(maPhongLuuTam, maKH);
			DefaultTableModel model = view.getModelPhuThuoc();
			model.setRowCount(0);

			for (Map<String, Object> pt : phuThuoc) {
				java.sql.Date ns = (java.sql.Date) pt.get("NgaySinh");
				model.addRow(new Object[] { pt.get("MaKH"), pt.get("TenKH"), pt.get("DiaChi"), pt.get("GioiTinh"),
						ns != null ? sdf.format(ns) : "", pt.get("SDT") });
			}
		}
	}

	private void initEvents() {
		view.getBtnThoat().addActionListener(e -> view.dispose());
		view.getBtnXoa().addActionListener(e -> xuLyXoa());
		view.getBtnSua().addActionListener(e -> xuLySua());
	}

	private void xuLyXoa() {
		int confirm = JOptionPane.showConfirmDialog(view,
				"Bạn có chắc chắn muốn XÓA khách hàng này?\nKhách hàng sẽ bị ẩn khỏi hệ thống.", "Xác nhận xóa",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			if (dao.delete(maKH)) {
				JOptionPane.showMessageDialog(view, "Đã xóa khách hàng!");
				if (parentController != null) {
					parentController.refreshData();
				}
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void xuLySua() {
		try {
			view.dispose();

			DefaultTableModel model = (parentController != null) ? parentController.getModel() : null;

			ThemKhachHangView suaView = new ThemKhachHangView(model);
			suaView.setModal(true);
			new SuaKhachHangController(suaView, parentController, maKH);

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi mở form sửa: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}