package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.DienNuocDAO;
import com.nctu.quanlynhatro.model.PhieuDienNuoc;
import com.nctu.quanlynhatro.view.dien_nuoc.ThemDienNuocView;

public class SuaDienNuocController {

	private ThemDienNuocView view;
	private DienNuocDAO dao;
	private DienNuocController parentController;
	private long maDNCur; // ID phiếu đang sửa
	private DecimalFormat df = new DecimalFormat("#,###");

	public SuaDienNuocController(ThemDienNuocView view, DienNuocController parentController, long maDN) {
		this.view = view;
		this.parentController = parentController;
		this.maDNCur = maDN;

		this.dao = new DienNuocDAO(DatabaseConnection.getConnection());

		// Cấu hình giao diện chế độ Sửa
		view.setTitle("Cập Nhật Phiếu Điện Nước - Mã: " + maDN);
		view.getBtnThem().setText("Lưu Cập Nhật");

		// Khóa các trường không được sửa (Để bảo toàn dữ liệu)
		view.getCboNhaTro().setEnabled(false);
		view.getCboSoPhong().setEnabled(false);
		view.getCboThang().setEnabled(false);

		loadDataOld();
		initEvents();

	}

	private void loadDataOld() {
		PhieuDienNuoc p = dao.getById(maDNCur);
		if (p == null) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy phiếu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			view.dispose();
			return;
		}

		// 1. Đổ dữ liệu hiển thị (Fake item vào combobox vì ta đã disable nó)
		view.getCboNhaTro().addItem(p.getGhiChu()); // Tên NT lấy từ DAO
		view.getCboNhaTro().setSelectedIndex(0);

		view.getCboSoPhong().addItem(String.valueOf(p.getPhong().getSoPhong()));
		view.getCboSoPhong().setSelectedIndex(0);

		String thangNam = "Tháng " + p.getThangNam().getMonthValue() + "/" + p.getThangNam().getYear();
		view.getCboThang().addItem(thangNam);
		view.getCboThang().setSelectedItem(thangNam);

		// 2. Đổ chỉ số và giá
		view.getTxtGiaDien().setText(df.format(p.getGiaDienTaiThoiDiem()));
		view.getTxtGiaNuoc().setText(df.format(p.getGiaNuocTaiThoiDiem()));

		view.getTxtDienCu().setText(df.format(p.getChiSoDienCu()));
		view.getTxtDienMoi().setText(df.format(p.getChiSoDienMoi()));

		view.getTxtNuocCu().setText(df.format(p.getChiSoNuocCu()));
		view.getTxtNuocMoi().setText(df.format(p.getChiSoNuocMoi()));

		// 3. Tính toán lại tiền để hiển thị khớp
		tinhTien();
	}

	private void initEvents() {
		// Sự kiện tự động tính tiền khi sửa số mới
		DocumentListener docListener = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				tinhTien();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				tinhTien();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				tinhTien();
			}
		};
		view.getTxtDienMoi().getDocument().addDocumentListener(docListener);
		view.getTxtNuocMoi().getDocument().addDocumentListener(docListener);
		view.getTxtDienCu().getDocument().addDocumentListener(docListener); // Cho phép sửa cả số cũ nếu nhập sai
		view.getTxtNuocCu().getDocument().addDocumentListener(docListener);

		// Nút Lưu
		// Xóa action cũ nếu có (đề phòng)
		for (ActionListener al : view.getBtnThem().getActionListeners()) {
			view.getBtnThem().removeActionListener(al);
		}
		view.getBtnThem().addActionListener(e -> xuLyCapNhat());

		// Nút Hủy (Giả sử view có nút Hủy, nếu chưa có thì thôi)
		// view.getBtnHuy().addActionListener(e -> view.dispose());
	}

	private void tinhTien() {
		try {
			double dCu = parseDouble(view.getTxtDienCu().getText());
			double dMoi = parseDouble(view.getTxtDienMoi().getText());
			double nCu = parseDouble(view.getTxtNuocCu().getText());
			double nMoi = parseDouble(view.getTxtNuocMoi().getText());

			double giaDien = parseDouble(view.getTxtGiaDien().getText());
			double giaNuoc = parseDouble(view.getTxtGiaNuoc().getText());

			double tienDien = (dMoi > dCu) ? (dMoi - dCu) * giaDien : 0;
			double tienNuoc = (nMoi > nCu) ? (nMoi - nCu) * giaNuoc : 0;
			double tong = tienDien + tienNuoc;

			view.getTxtTienDien().setText(df.format(tienDien));
			view.getTxtTienNuoc().setText(df.format(tienNuoc));
			view.getTxtTongTien().setText(df.format(tong));

		} catch (Exception e) {
		}
	}

	private double parseDouble(String s) {
		try {
			return Double.parseDouble(s.replace(",", "").replace(".", "").trim());
		} catch (Exception e) {
			return 0;
		}
	}

	private void xuLyCapNhat() {
		try {
			// Validate
			double dMoi = parseDouble(view.getTxtDienMoi().getText());
			double dCu = parseDouble(view.getTxtDienCu().getText());
			if (dMoi < dCu) {
				JOptionPane.showMessageDialog(view, "Số điện mới phải >= số cũ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
				return;
			}

			double nMoi = parseDouble(view.getTxtNuocMoi().getText());
			double nCu = parseDouble(view.getTxtNuocCu().getText());
			if (nMoi < nCu) {
				JOptionPane.showMessageDialog(view, "Số nước mới phải >= số cũ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Tạo object cập nhật
			PhieuDienNuoc p = new PhieuDienNuoc();
			p.setMaDN(maDNCur);
			p.setChiSoDienCu((float) dCu);
			p.setChiSoDienMoi((float) dMoi);
			p.setChiSoNuocCu((float) nCu);
			p.setChiSoNuocMoi((float) nMoi);

			p.setGiaDienTaiThoiDiem(parseDouble(view.getTxtGiaDien().getText()));
			p.setGiaNuocTaiThoiDiem(parseDouble(view.getTxtGiaNuoc().getText()));

			p.setTienDien(parseDouble(view.getTxtTienDien().getText()));
			p.setTienNuoc(parseDouble(view.getTxtTienNuoc().getText()));
			p.setTongTien(parseDouble(view.getTxtTongTien().getText()));

			int confirm = JOptionPane.showConfirmDialog(view, "Lưu thay đổi?", "Xác nhận", JOptionPane.YES_NO_OPTION);
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}

			if (dao.updatePhieu(p)) {
				JOptionPane.showMessageDialog(view, "Cập nhật thành công!");

				// Cập nhật lại model bảng bên ngoài (nếu view cha có truyền model vào)
				if (view.getTableModel() != null) {
					// Cần tìm dòng có MaDN tương ứng để update, hoặc đơn giản là reload table cha
					// view.getTableModel().setValueAt(...);
				}

				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(view, "Lỗi dữ liệu nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
}