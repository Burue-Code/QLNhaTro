package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.DienNuocDAO;
import com.nctu.quanlynhatro.view.dien_nuoc.ThemDienNuocView;

public class ThemDienNuocController {
	private DienNuocController parentController;
	private ThemDienNuocView view;
	private DienNuocDAO dao;
	private int maGiaDN = 0;

	private Map<String, Integer> mapNhaTro = new HashMap<>();
	private Map<String, Integer> mapPhong = new HashMap<>();

	private DecimalFormat df = new DecimalFormat("#,###");

	public ThemDienNuocController(ThemDienNuocView view, DienNuocController parentController) {
		this.view = view;
		this.parentController = parentController;
		this.dao = new DienNuocDAO(DatabaseConnection.getConnection());

		initData();
		initEvents();
	}

	public ThemDienNuocController(ThemDienNuocView view, long maPhongTruyenVao) {
		this.view = view;
		this.parentController = null;
		this.dao = new DienNuocDAO(DatabaseConnection.getConnection());

		initData();
		initEvents();

		boolean found = false;
		for (String tenNT : mapNhaTro.keySet()) {
			view.getCboNhaTro().setSelectedItem(tenNT);

			if (mapPhong.containsValue((int) maPhongTruyenVao)) {
				for (Map.Entry<String, Integer> entry : mapPhong.entrySet()) {
					if (entry.getValue() == (int) maPhongTruyenVao) {
						view.getCboSoPhong().setSelectedItem(entry.getKey());
						found = true;
						break;
					}
				}
			}
			if (found) {
				break;
			}
		}

		view.getCboNhaTro().setEnabled(false);
		view.getCboSoPhong().setEnabled(false);
	}

	private void initData() {
		view.getCboThang().removeAllItems();
		int currentMonth = LocalDate.now().getMonthValue();
		int currentYear = LocalDate.now().getYear();
		for (int i = 1; i <= 12; i++) {
			view.getCboThang().addItem("Tháng " + i + "/" + currentYear);
		}
		view.getCboThang().setSelectedItem("Tháng " + currentMonth + "/" + currentYear);

		mapNhaTro = dao.getListNhaTro();
		view.getCboNhaTro().removeAllItems();
		for (String tenNT : mapNhaTro.keySet()) {
			view.getCboNhaTro().addItem(tenNT);
		}

		if (view.getCboNhaTro().getItemCount() > 0) {
			view.getCboNhaTro().setSelectedIndex(0);
			loadPhongByNhaTro();
		}

		Map<String, Object> gia = dao.getGiaDienNuocHienTai();
		if (gia.containsKey("MaGiaDN")) {
			maGiaDN = (int) gia.get("MaGiaDN");

			double giaDien = (Double) gia.get("GiaDien");
			double giaNuoc = (Double) gia.get("GiaNuoc");
			view.getTxtGiaDien().setText(df.format(giaDien));
			view.getTxtGiaNuoc().setText(df.format(giaNuoc));
		} else {
			JOptionPane.showMessageDialog(view, "Chưa thiết lập Bảng Giá Điện Nước trong hệ thống!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void initEvents() {
		view.getCboNhaTro().addActionListener(e -> loadPhongByNhaTro());

		view.getCboSoPhong().addActionListener(e -> loadChiSoCu());
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

		view.getBtnThem().addActionListener(e -> xuLyThem());
		if (view.getBtnDong() != null) {
			view.getBtnDong().addActionListener(e -> view.dispose());
		}
		setChiNhapSoThuc(view.getTxtDienMoi());
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

	private void loadPhongByNhaTro() {
		String tenNT = (String) view.getCboNhaTro().getSelectedItem();
		if (tenNT == null) {
			return;
		}

		int maNT = mapNhaTro.get(tenNT);
		mapPhong = dao.getListPhong(maNT);

		ActionListener[] listeners = view.getCboSoPhong().getActionListeners();
		for (ActionListener l : listeners) {
			view.getCboSoPhong().removeActionListener(l);
		}

		view.getCboSoPhong().removeAllItems();
		for (String soPhong : mapPhong.keySet()) {
			view.getCboSoPhong().addItem(soPhong);
		}

		for (ActionListener l : listeners) {
			view.getCboSoPhong().addActionListener(l);
		}

		if (view.getCboSoPhong().getItemCount() > 0) {
			view.getCboSoPhong().setSelectedIndex(0);
			loadChiSoCu();
		}
	}

	private void loadChiSoCu() {
		String tenPhong = (String) view.getCboSoPhong().getSelectedItem();
		if (tenPhong == null) {
			return;
		}

		int maPhong = mapPhong.get(tenPhong);
		Map<String, Double> chiSo = dao.getChiSoCu(maPhong);

		view.getTxtDienCu().setText(String.format("%.0f", chiSo.get("Dien")));
		view.getTxtNuocCu().setText(String.format("%.0f", chiSo.get("Nuoc")));

		view.getTxtDienMoi().setText("");
		view.getTxtNuocMoi().setText("");
		view.getTxtTongTien().setText("0");
	}

	private void tinhTien() {
		try {
			double dCu = parseDouble(view.getTxtDienCu().getText());
			double dMoi = parseDouble(view.getTxtDienMoi().getText());
			double nCu = parseDouble(view.getTxtNuocCu().getText());
			double nMoi = parseDouble(view.getTxtNuocMoi().getText());

			double giaDien = parseDouble(view.getTxtGiaDien().getText());
			double giaNuoc = parseDouble(view.getTxtGiaNuoc().getText());

			double soDien = (dMoi > dCu) ? (dMoi - dCu) : 0;
			double soNuoc = (nMoi > nCu) ? (nMoi - nCu) : 0;

			double tienDien = soDien * giaDien;
			double tienNuoc = soNuoc * giaNuoc;
			double tong = tienDien + tienNuoc;

			view.getTxtTienDien().setText(df.format(tienDien));
			view.getTxtTienNuoc().setText(df.format(tienNuoc));
			view.getTxtTongTien().setText(df.format(tong));

		} catch (Exception e) {
		}
	}

	private void xuLyThem() {
		try {
			String tenPhong = (String) view.getCboSoPhong().getSelectedItem();
			if (tenPhong == null) {
				JOptionPane.showMessageDialog(view, "Chưa chọn phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int maPhong = mapPhong.get(tenPhong);

			double dMoi = 0, dCu = 0, nMoi = 0, nCu = 0;
			try {
				dMoi = Double.parseDouble(view.getTxtDienMoi().getText().replace(",", ""));
				dCu = Double.parseDouble(view.getTxtDienCu().getText().replace(",", ""));
				nMoi = Double.parseDouble(view.getTxtNuocMoi().getText().replace(",", ""));
				nCu = Double.parseDouble(view.getTxtNuocCu().getText().replace(",", ""));
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(view, "Chỉ số điện nước phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (dMoi < dCu) {
				JOptionPane.showMessageDialog(view, "Số điện mới phải lớn hơn số cũ!", "Lỗi",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (nMoi < nCu) {
				JOptionPane.showMessageDialog(view, "Số nước mới phải lớn hơn số cũ!", "Lỗi",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			String strThang = (String) view.getCboThang().getSelectedItem();
			String[] parts = strThang.replace("Tháng ", "").trim().split("/");

			LocalDate date = LocalDate.of(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), 1);

			if (dao.checkTonTai(maPhong, date)) {
				JOptionPane.showMessageDialog(view, "Phòng này đã có phiếu điện nước cho tháng " + strThang + " rồi!",
						"Trùng dữ liệu", JOptionPane.WARNING_MESSAGE);
				return;
			}

			java.sql.Date sqlDate = java.sql.Date.valueOf(date);

			double tDien = parseDouble(view.getTxtTienDien().getText());
			double tNuoc = parseDouble(view.getTxtTienNuoc().getText());
			double tong = parseDouble(view.getTxtTongTien().getText());
			double gDien = parseDouble(view.getTxtGiaDien().getText());
			double gNuoc = parseDouble(view.getTxtGiaNuoc().getText());

			boolean kq = dao.insertPhieu(maPhong, sqlDate, dCu, dMoi, nCu, nMoi, tDien, tNuoc, tong, gDien, gNuoc,
					maGiaDN);

			if (kq) {
				JOptionPane.showMessageDialog(view, "Thêm thành công!");

				if (view.getTableModel() != null) {
					view.getTableModel().addRow(new Object[] { "Mới", tenPhong, strThang, df.format(dMoi - dCu),
							df.format(nMoi - nCu), df.format(tong), "Chưa đóng" });
				}

				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, "Lỗi thêm dữ liệu vào CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập đúng định dạng số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private double parseDouble(String s) {
		try {
			if (s == null || s.isEmpty()) {
				return 0;
			}
			return Double.parseDouble(s.replace(",", ""));
		} catch (Exception e) {
			return 0;
		}
	}
}