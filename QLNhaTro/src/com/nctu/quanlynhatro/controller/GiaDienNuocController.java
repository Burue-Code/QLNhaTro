package com.nctu.quanlynhatro.controller;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.GiaDienNuocDAO;
import com.nctu.quanlynhatro.model.GiaDienNuoc;
import com.nctu.quanlynhatro.view.gia_dien_nuoc.GiaDienNuocView;

public class GiaDienNuocController {

	private GiaDienNuocView view;
	private GiaDienNuocDAO dao;
	private DecimalFormat df = new DecimalFormat("#,###");

	public GiaDienNuocController(GiaDienNuocView view) {
		this.view = view;
		this.dao = new GiaDienNuocDAO(DatabaseConnection.getConnection());

		initData();
		initEvents();
	}

	private void initData() {
		// Load giá hiện tại từ CSDL
		GiaDienNuoc gia = dao.getGiaHienTai();
		if (gia != null) {
			view.getTxtGiaDienCu().setText(df.format(gia.getGiaDien()));
			view.getTxtGiaNuocCu().setText(df.format(gia.getGiaNuoc()));

			// Gợi ý giá mới bằng giá cũ (để người dùng dễ sửa)
			view.getTxtGiaDienMoi().setText(df.format(gia.getGiaDien()));
			view.getTxtGiaNuocMoi().setText(df.format(gia.getGiaNuoc()));
		} else {
			view.getTxtGiaDienCu().setText("0");
			view.getTxtGiaNuocCu().setText("0");
		}
	}

	private void initEvents() {
		view.getBtnHuy().addActionListener(e -> view.dispose());
		view.getBtnLuu().addActionListener(e -> xuLyLuu());
	}

	private void xuLyLuu() {
		try {
			// Parse dữ liệu đầu vào (xóa dấu phẩy nếu có)
			double giaDienMoi = parseDouble(view.getTxtGiaDienMoi().getText());
			double giaNuocMoi = parseDouble(view.getTxtGiaNuocMoi().getText());

			// Validate
			if (giaDienMoi <= 0 || giaNuocMoi <= 0) {
				JOptionPane.showMessageDialog(view, "Giá điện và nước phải lớn hơn 0!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Xác nhận
			int confirm = JOptionPane.showConfirmDialog(view,
					"Bạn có chắc muốn cập nhật đơn giá mới?\n"
							+ "Lưu ý: Giá mới sẽ được áp dụng cho các phiếu lập sau thời điểm này.",
					"Xác nhận", JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				// Gọi DAO insert giá mới
				if (dao.insertGiaMoi(giaDienMoi, giaNuocMoi)) {
					JOptionPane.showMessageDialog(view, "Cập nhật giá thành công!");
					view.dispose();
				} else {
					JOptionPane.showMessageDialog(view, "Lỗi cập nhật CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập đúng định dạng số!", "Lỗi nhập liệu",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Hàm hỗ trợ chuyển đổi chuỗi sang số (xử lý dấu phẩy nghìn)
	private double parseDouble(String s) {
		try {
			if (s == null || s.trim().isEmpty()) {
				return 0;
			}
			return Double.parseDouble(s.replace(",", "").replace(".", "").trim());
		} catch (Exception e) {
			throw e; // Ném lỗi ra để hàm xuLyLuu bắt
		}
	}
}