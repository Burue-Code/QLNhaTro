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

	// Map để ánh xạ Tên -> ID
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

	private void initData() {
		// 1. Load Tháng (Tháng hiện tại + 11 tháng trước)
		view.getCboThang().removeAllItems();
		int currentMonth = LocalDate.now().getMonthValue();
		int currentYear = LocalDate.now().getYear();
		for (int i = 1; i <= 12; i++) {
			view.getCboThang().addItem("Tháng " + i + "/" + currentYear);
		}
		view.getCboThang().setSelectedItem("Tháng " + currentMonth + "/" + currentYear);

		// 2. Load Nhà Trọ
		mapNhaTro = dao.getListNhaTro();
		view.getCboNhaTro().removeAllItems();
		for (String tenNT : mapNhaTro.keySet()) {
			view.getCboNhaTro().addItem(tenNT);
		}

		// Trigger load phòng cho nhà trọ đầu tiên
		if (view.getCboNhaTro().getItemCount() > 0) {
			view.getCboNhaTro().setSelectedIndex(0);
			loadPhongByNhaTro();
		}

		Map<String, Object> gia = dao.getGiaDienNuocHienTai();
		if (gia.containsKey("MaGiaDN")) {
			maGiaDN = (int) gia.get("MaGiaDN");
		}
	}

	private void initEvents() {
		// Sự kiện chọn Nhà Trọ -> Load Phòng
		view.getCboNhaTro().addActionListener(e -> loadPhongByNhaTro());

		// Sự kiện chọn Phòng -> Load Chỉ số cũ
		view.getCboSoPhong().addActionListener(e -> loadChiSoCu());

		// Sự kiện nhập số mới -> Tự động tính tiền
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

		// Sự kiện nút Thêm
		view.getBtnThem().addActionListener(e -> xuLyThem());
	}

	// --- Logic Load Phòng ---
	private void loadPhongByNhaTro() {
		String tenNT = (String) view.getCboNhaTro().getSelectedItem();
		if (tenNT == null) {
			return;
		}

		int maNT = mapNhaTro.get(tenNT);
		mapPhong = dao.getListPhong(maNT);

		// Tạm gỡ listener để tránh trigger loop
		ActionListener[] listeners = view.getCboSoPhong().getActionListeners();
		for (ActionListener l : listeners) {
			view.getCboSoPhong().removeActionListener(l);
		}

		view.getCboSoPhong().removeAllItems();
		for (String soPhong : mapPhong.keySet()) {
			view.getCboSoPhong().addItem(soPhong);
		}

		// Gắn lại listener
		for (ActionListener l : listeners) {
			view.getCboSoPhong().addActionListener(l);
		}

		// Load chỉ số cũ cho phòng đầu tiên
		if (view.getCboSoPhong().getItemCount() > 0) {
			view.getCboSoPhong().setSelectedIndex(0);
			loadChiSoCu();
		}
	}

	// --- Logic Load Chỉ Số Cũ ---
	private void loadChiSoCu() {
		String tenPhong = (String) view.getCboSoPhong().getSelectedItem();
		if (tenPhong == null) {
			return;
		}

		int maPhong = mapPhong.get(tenPhong);
		Map<String, Double> chiSo = dao.getChiSoCu(maPhong);

		view.getTxtDienCu().setText(String.format("%.0f", chiSo.get("Dien")));
		view.getTxtNuocCu().setText(String.format("%.0f", chiSo.get("Nuoc")));

		// Reset ô nhập mới
		view.getTxtDienMoi().setText("");
		view.getTxtNuocMoi().setText("");
		view.getTxtTongTien().setText("0");
	}

	// --- Logic Tính Tiền ---
	private void tinhTien() {
		try {
			double dCu = parseDouble(view.getTxtDienCu().getText());
			double dMoi = parseDouble(view.getTxtDienMoi().getText());
			double nCu = parseDouble(view.getTxtNuocCu().getText());
			double nMoi = parseDouble(view.getTxtNuocMoi().getText());

			double giaDien = parseDouble(view.getTxtGiaDien().getText());
			double giaNuoc = parseDouble(view.getTxtGiaNuoc().getText());

			// Tính toán (Nếu mới < cũ coi như bằng 0 hoặc báo lỗi sau)
			double soDien = (dMoi > dCu) ? (dMoi - dCu) : 0;
			double soNuoc = (nMoi > nCu) ? (nMoi - nCu) : 0;

			double tienDien = soDien * giaDien;
			double tienNuoc = soNuoc * giaNuoc;
			double tong = tienDien + tienNuoc;

			view.getTxtTienDien().setText(df.format(tienDien));
			view.getTxtTienNuoc().setText(df.format(tienNuoc));
			view.getTxtTongTien().setText(df.format(tong));

		} catch (Exception e) {
			// Đang nhập dở số, không làm gì
		}
	}

	// --- Logic Thêm Phiếu ---
	private void xuLyThem() {
		try {
			// 1. Validate Phòng
			String tenPhong = (String) view.getCboSoPhong().getSelectedItem();
			if (tenPhong == null) {
				JOptionPane.showMessageDialog(view, "Chưa chọn phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int maPhong = mapPhong.get(tenPhong);

			// 2. Validate Chỉ số
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

			// 3. Xử lý Ngày tháng
			// Chuỗi từ CBO: "Tháng 5/2025" -> Cắt lấy "5/2025" -> Split
			String strThang = (String) view.getCboThang().getSelectedItem();
			String[] parts = strThang.replace("Tháng ", "").trim().split("/");

			// Tạo ngày mùng 1 của tháng đó
			LocalDate date = LocalDate.of(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), 1);

			// Kiểm tra xem tháng này phòng này đã đóng tiền chưa
			if (dao.checkTonTai(maPhong, date)) {
				JOptionPane.showMessageDialog(view, "Phòng này đã có phiếu điện nước cho tháng " + strThang + " rồi!",
						"Trùng dữ liệu", JOptionPane.WARNING_MESSAGE);
				return;
			}

			java.sql.Date sqlDate = java.sql.Date.valueOf(date);

			// 4. Các giá trị tiền
			double tDien = parseDouble(view.getTxtTienDien().getText());
			double tNuoc = parseDouble(view.getTxtTienNuoc().getText());
			double tong = parseDouble(view.getTxtTongTien().getText());
			double gDien = parseDouble(view.getTxtGiaDien().getText());
			double gNuoc = parseDouble(view.getTxtGiaNuoc().getText());

			// 5. Gọi DAO Insert
			// [QUAN TRỌNG]: Truyền thêm biến maGiaDN vào cuối hàm
			boolean kq = dao.insertPhieu(maPhong, sqlDate, dCu, dMoi, nCu, nMoi, tDien, tNuoc, tong, gDien, gNuoc,
					maGiaDN);

			if (kq) {
				JOptionPane.showMessageDialog(view, "Thêm thành công!");

				// Cập nhật lại bảng ngoài View cha (Nếu view cha có bảng)
				if (view.getTableModel() != null) {
					view.getTableModel().addRow(new Object[] { "Mới", // Mã (Sẽ có khi reload DB)
							tenPhong, strThang, df.format(dMoi - dCu), // Số điện tiêu thụ
							df.format(nMoi - nCu), // Số nước tiêu thụ
							df.format(tong), "Chưa đóng" });
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

	// Hàm hỗ trợ parse số có dấu phẩy (Ví dụ: 100,000 -> 100000)
	private double parseDouble(String s) {
		try {
			if (s == null || s.isEmpty()) {
				return 0;
			}
			return Double.parseDouble(s.replace(",", "").replace(".", ""));
		} catch (Exception e) {
			return 0;
		}
	}
}