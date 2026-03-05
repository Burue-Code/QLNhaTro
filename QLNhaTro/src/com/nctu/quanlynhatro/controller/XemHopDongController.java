package com.nctu.quanlynhatro.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.HopDongDAO;
import com.nctu.quanlynhatro.view.hop_dong.ThemHopDongView;
import com.nctu.quanlynhatro.view.hop_dong.XemHopDongView;

public class XemHopDongController {

	private XemHopDongView view;
	private HopDongDAO dao;
	private long maHD;
	private HopDongController parentController;

	private DecimalFormat df = new DecimalFormat("#,###");
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public XemHopDongController(XemHopDongView view, long maHD, HopDongController parentController) {
		this.view = view;
		this.maHD = maHD;
		this.parentController = parentController;
		this.dao = new HopDongDAO(DatabaseConnection.getConnection());

		view.setTitle("Chi Tiết Hợp Đồng - Mã: " + maHD);
		loadData();
		initEvents();
	}

	private void loadData() {
		// 1. Load thông tin chung của hợp đồng
		Map<String, Object> chiTiet = dao.getChiTietHopDong(maHD);

		if (chiTiet.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin hợp đồng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			view.dispose();
			return;
		}

		// Set dữ liệu lên TextField thông qua Setter
		view.setTxtMaHopDong(String.valueOf(chiTiet.get("MaHD")));
		view.setTxtTenKhachHang((String) chiTiet.get("TenKH"));
		view.setTxtNhaTro((String) chiTiet.get("TenNT"));
		view.setTxtPhong((String) chiTiet.get("SoPhong"));
		view.setTxtSoLuongNguoiO(String.valueOf(chiTiet.get("SoNguoiO")));
		view.setTxtTrangThaiHopDong((String) chiTiet.get("TrangThaiHD"));

		String ghiChu = (String) chiTiet.get("GhiChu");
		view.setTxtGhiChu(ghiChu != null ? ghiChu : "");

		// Xử lý Ngày tháng
		java.sql.Date ngayLap = (java.sql.Date) chiTiet.get("NgayLapHD");
		view.setTxtNgayBatDau(ngayLap != null ? sdf.format(ngayLap) : "");

		java.sql.Date ngayTra = (java.sql.Date) chiTiet.get("NgayKT");
		view.setTxtNgayKetThuc(ngayTra != null ? sdf.format(ngayTra) : "Chưa xác định");

		// Xử lý Tiền tệ
		Double giaThue = (Double) chiTiet.get("Gia");
		view.setTxtGiaThue(giaThue != null ? df.format(giaThue) + " VNĐ" : "0 VNĐ");

		// 2. Load danh sách khách hàng phụ thuộc (Người ở ghép)
		List<Map<String, Object>> listKhach = dao.getKhachHangPhuThuoc(maHD);
		DefaultTableModel model = view.getModelPhuThuoc();
		model.setRowCount(0); // Xóa dữ liệu cũ trên bảng

		for (Map<String, Object> kh : listKhach) {
			java.sql.Date ngaySinh = (java.sql.Date) kh.get("NgaySinh");

			model.addRow(new Object[] { kh.get("MaKH"), kh.get("TenKH"), kh.get("DiaChi"), kh.get("GioiTinh"),
					ngaySinh != null ? sdf.format(ngaySinh) : "", kh.get("SDT") });
		}
	}

	private void initEvents() {
		// Nút Thoát
		view.getBtnThoat().addActionListener(e -> view.dispose());

		// Nút Xóa
		view.getBtnXoa().addActionListener(e -> xuLyXoa());

		// Nút Sửa
		view.getBtnSua().addActionListener(e -> xuLySua());
	}

	private void xuLyXoa() {
		int confirm = JOptionPane.showConfirmDialog(view,
				"Bạn có chắc chắn muốn xóa hợp đồng này không?\nThao tác này sẽ đánh dấu hợp đồng là đã xóa.",
				"Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			if (dao.deleteHopDong(maHD)) {
				JOptionPane.showMessageDialog(view, "Xóa hợp đồng thành công!");

				// Cập nhật lại bảng ngoài View cha
				if (parentController != null) {
					parentController.refreshData(); // Hàm load lại bảng trong HopDongController
				}

				// Đóng form xem chi tiết
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, "Xóa hợp đồng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void xuLySua() {

		try {

			view.dispose();

			ThemHopDongView suaView = new ThemHopDongView(parentController.getModel());
			suaView.setModal(true);
			new SuaHopDongController(suaView, parentController, maHD);
			suaView.setVisible(true);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}