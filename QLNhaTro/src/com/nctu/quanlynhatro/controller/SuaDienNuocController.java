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
	private long maDNCur;
	private DecimalFormat df = new DecimalFormat("#,###.##");

	public SuaDienNuocController(ThemDienNuocView view, DienNuocController parentController, long maDN) {
		this.view = view;
		this.parentController = parentController;
		this.maDNCur = maDN;

		this.dao = new DienNuocDAO(DatabaseConnection.getConnection());

		view.setTitle("Cập Nhật Phiếu Điện Nước - Mã: " + maDN);
		view.getBtnThem().setText("Lưu Cập Nhật");

		view.getCboNhaTro().setEnabled(false);
		view.getCboSoPhong().setEnabled(false);
		view.getCboThang().setEnabled(false);

		loadDataOld();
		initEvents();

		view.setVisible(true);
	}

	private void loadDataOld() {
		PhieuDienNuoc p = dao.getById(maDNCur);
		if (p == null) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy phiếu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			view.dispose();
			return;
		}

		view.getCboNhaTro().addItem(p.getGhiChu());
		view.getCboNhaTro().setSelectedIndex(0);

		view.getCboSoPhong().addItem(String.valueOf(p.getPhong().getSoPhong()));
		view.getCboSoPhong().setSelectedIndex(0);

		if (p.getThangNam() != null) {
			String thangNam = "Tháng " + p.getThangNam().getMonthValue() + "/" + p.getThangNam().getYear();
			view.getCboThang().addItem(thangNam);
			view.getCboThang().setSelectedItem(thangNam);
		}

		view.getTxtGiaDien().setText(String.valueOf(p.getGiaDienTaiThoiDiem()));
		view.getTxtGiaNuoc().setText(String.valueOf(p.getGiaNuocTaiThoiDiem()));

		view.getTxtDienCu().setText(String.valueOf(p.getChiSoDienCu()));
		view.getTxtDienMoi().setText(String.valueOf(p.getChiSoDienMoi()));

		view.getTxtNuocCu().setText(String.valueOf(p.getChiSoNuocCu()));
		view.getTxtNuocMoi().setText(String.valueOf(p.getChiSoNuocMoi()));

		tinhTien();
	}

	private void initEvents() {
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
		view.getTxtDienCu().getDocument().addDocumentListener(docListener);
		view.getTxtNuocCu().getDocument().addDocumentListener(docListener);

		for (ActionListener al : view.getBtnThem().getActionListeners()) {
			view.getBtnThem().removeActionListener(al);
		}
		view.getBtnThem().addActionListener(e -> xuLyCapNhat());

		if (view.getBtnDong() != null) {
			view.getBtnDong().addActionListener(e -> view.dispose());
		}

		setChiNhapSoThuc(view.getTxtDienCu());
		setChiNhapSoThuc(view.getTxtDienMoi());
		setChiNhapSoThuc(view.getTxtNuocCu());
		setChiNhapSoThuc(view.getTxtNuocMoi());
	}

	private void setChiNhapSoThuc(javax.swing.JTextField txt) {
		txt.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE && c != '.') {
					e.consume();
				}
				if (c == '.' && txt.getText().contains(".")) {
					e.consume();
				}
			}
		});
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
			if (s == null || s.isEmpty()) {
				return 0;
			}
			return Double.parseDouble(s.replace(",", "").trim());
		} catch (Exception e) {
			return 0;
		}
	}

	private void xuLyCapNhat() {
		try {
			if (view.getTxtDienMoi().getText().trim().isEmpty() || view.getTxtNuocMoi().getText().trim().isEmpty()
					|| view.getTxtDienCu().getText().trim().isEmpty()
					|| view.getTxtNuocCu().getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ các chỉ số điện nước!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			double dMoi = parseDouble(view.getTxtDienMoi().getText());
			double dCu = parseDouble(view.getTxtDienCu().getText());
			if (dMoi < dCu) {
				JOptionPane.showMessageDialog(view, "Số điện mới phải lớn hơn hoặc bằng số cũ!", "Lỗi",
						JOptionPane.WARNING_MESSAGE);
				view.getTxtDienMoi().requestFocus();
				return;
			}

			double nMoi = parseDouble(view.getTxtNuocMoi().getText());
			double nCu = parseDouble(view.getTxtNuocCu().getText());
			if (nMoi < nCu) {
				JOptionPane.showMessageDialog(view, "Số nước mới phải lớn hơn hoặc bằng số cũ!", "Lỗi",
						JOptionPane.WARNING_MESSAGE);
				view.getTxtNuocMoi().requestFocus();
				return;
			}

			int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn lưu thay đổi phiếu này?",
					"Xác nhận", JOptionPane.YES_NO_OPTION);
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}

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

			if (dao.updatePhieu(p)) {
				JOptionPane.showMessageDialog(view, "Cập nhật thành công!");

				if (parentController != null) {
					parentController.refreshData();
				}
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Lỗi dữ liệu nhập: " + ex.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}