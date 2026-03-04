package com.nctu.quanlynhatro.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.HoaDonDAO;
import com.nctu.quanlynhatro.dao.PhuongThucThanhToanDAO;
import com.nctu.quanlynhatro.model.HoaDon;
import com.nctu.quanlynhatro.model.KhachHangGoiY; // Import class DTO vừa tạo
import com.nctu.quanlynhatro.view.hoa_don.ThemHoaDonView;

public class ThemHoaDonController {

	private ThemHoaDonView view;
	private HoaDonController parentController;
	private HoaDonDAO hoaDonDAO;
	private PhuongThucThanhToanDAO ptttDAO;

	// Map lưu trữ dữ liệu tính toán
	private Map<String, Long> mapDienNuoc = new HashMap<>();
	private Map<String, Double> mapGiaPhuPhi = new HashMap<>();
	private Map<String, Integer> mapPhuongThuc = new HashMap<>();

	public ThemHoaDonController(ThemHoaDonView view, HoaDonController parentController) {
		this.view = view;
		this.parentController = parentController;
		this.hoaDonDAO = new HoaDonDAO(DatabaseConnection.getConnection());
		this.ptttDAO = new PhuongThucThanhToanDAO(DatabaseConnection.getConnection());
		initData();
		initEvents();
	}

	private void initData() {
		loadPhuongThucThanhToan();
		view.getCboLoaiThanhToan().setSelectedItem("Tất Cả");
	}

	// [MỚI] Hàm load dữ liệu vào ComboBox Phương Thức
	private void loadPhuongThucThanhToan() {
		view.getCboPhuongThuc().removeAllItems();
		mapPhuongThuc.clear();

		// Gọi DAO

		Map<Integer, String> data = ptttDAO.getPhuongThucThanhToan();

		for (Map.Entry<Integer, String> entry : data.entrySet()) {
			int maPT = entry.getKey();
			String tenPT = entry.getValue();

			// Thêm vào ComboBox
			view.getCboPhuongThuc().addItem(tenPT);
			// Lưu vào Map để lát lấy ID
			mapPhuongThuc.put(tenPT, maPT);
		}
	}

	private void initEvents() {
		view.getBtnHuy().addActionListener(e -> view.dispose());

		// --- XỬ LÝ GỢI Ý KHÁCH HÀNG ---
		view.getTxtTenKH().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timKiemVaHienThiGoiY();
			}
		});

		// Sự kiện khi click chọn vào danh sách gợi ý
		view.getListGoiY().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				chonKhachHangTuGoiY();
			}
		});

		// Các sự kiện khác giữ nguyên
		view.getBtnCongDN().addActionListener(e -> xuLyThemDienNuocVaoBang());
		view.getCboLoaiThanhToan().addActionListener(e -> tinhTongTien());
		view.getBtnXacNhan().addActionListener(e -> xuLyLuuHoaDon());
		view.getBtnThemPhieuMoi().addActionListener(e -> xuLyThemPhieuMoi());
	}

	// =================================================================
	// LOGIC TÌM KIẾM & ĐIỀN DỮ LIỆU TỰ ĐỘNG
	// =================================================================

	private void timKiemVaHienThiGoiY() {
		String keyword = view.getTxtTenKH().getText().trim();
		view.getPopupMenu().setVisible(false);

		if (keyword.isEmpty()) {
			return;
		}

		// 1. Tìm kiếm từ DAO
		List<KhachHangGoiY> results = hoaDonDAO.searchKhachHang(keyword);

		// 2. Cập nhật Model cho JList
		view.getListModel().clear();
		if (!results.isEmpty()) {
			for (KhachHangGoiY kh : results) {
				view.getListModel().addElement(kh);
			}

			// 3. Hiển thị Popup ngay dưới txtTenKH
			view.getPopupMenu().setPopupSize(view.getTxtTenKH().getWidth(), 150);
			view.getPopupMenu().show(view.getTxtTenKH(), 0, view.getTxtTenKH().getHeight());
			view.getTxtTenKH().requestFocus(); // Giữ focus để gõ tiếp
		}
	}

	private void chonKhachHangTuGoiY() {
		KhachHangGoiY selected = view.getListGoiY().getSelectedValue();
		if (selected != null) {
			// 1. Điền thông tin cơ bản
			view.getTxtTenKH().setText(selected.getTenKH());
			view.getTxtMaHopDong().setText(String.valueOf(selected.getMaHD()));
			view.getPopupMenu().setVisible(false); // Ẩn popup

			// 2. Load toàn bộ thông tin chi tiết dựa vào MaHD
			loadThongTinChiTiet(selected.getMaHD());
		}
	}

	private void loadThongTinChiTiet(long maHD) {
		// A. Lấy thông tin Hợp Đồng (Phòng, Nhà trọ, Giá...)
		Map<String, Object> info = hoaDonDAO.getThongTinHopDong(maHD);
		if (info != null) {
			view.getTxtNhaTro().setText((String) info.get("TenNT"));
			view.getTxtPhong().setText((String) info.get("SoPhong"));
			view.getTxtGiaThue().setText(String.format("%.0f", info.get("GiaThue")));
		}

		// B. Load Phụ Phí
		loadPhuPhi(maHD);

		// C. Load Điện Nước chưa thanh toán
		loadDienNuocChuaThanhToan(maHD);

		// D. Tính tổng tiền
		tinhTongTien();
	}

	// =================================================================
	// CÁC HÀM HỖ TRỢ (GIỮ NGUYÊN LOGIC CŨ NHƯNG SỬA VIEW)
	// =================================================================

	private void loadPhuPhi(long maHD) {
		DefaultTableModel model = view.getModelPhuPhi();
		model.setRowCount(0);
		mapGiaPhuPhi.clear();

		List<Map<String, Object>> listPP = hoaDonDAO.getPhuPhiByHopDong(maHD);
		for (Map<String, Object> pp : listPP) {
			model.addRow(new Object[] { pp.get("MaPP"), pp.get("TenPP"), String.format("%.0f", pp.get("Gia")) });
			mapGiaPhuPhi.put(pp.get("TenPP").toString(), (Double) pp.get("Gia"));
		}
	}

	private void loadDienNuocChuaThanhToan(long maHD) {
		view.getCboChonDienNuoc().removeAllItems();
		mapDienNuoc.clear();
		view.getModelDienNuoc().setRowCount(0);

		List<Map<String, Object>> listDN = hoaDonDAO.getDienNuocChuaThanhToan(maHD);
		if (listDN.isEmpty()) {
			view.getCboChonDienNuoc().addItem("-- Không có hóa đơn nợ --");
		} else {
			for (Map<String, Object> dn : listDN) {
				long maDN = (Long) dn.get("MaDN");
				String thangNam = (String) dn.get("ThangNam");
				double tongTien = (Double) dn.get("TongTien");

				String display = "Kỳ: " + thangNam + " - " + String.format("%.0f", tongTien) + " VND";
				view.getCboChonDienNuoc().addItem(display);
				mapDienNuoc.put(display, maDN);
			}
		}
	}

	private void xuLyThemDienNuocVaoBang() {
		String selected = (String) view.getCboChonDienNuoc().getSelectedItem();
		if (selected == null || !mapDienNuoc.containsKey(selected)) {
			return;
		}

		long maDN = mapDienNuoc.get(selected);

		DefaultTableModel model = view.getModelDienNuoc();
		for (int i = 0; i < model.getRowCount(); i++) {
			if (Long.parseLong(model.getValueAt(i, 0).toString()) == maDN) {
				JOptionPane.showMessageDialog(view, "Phiếu này đã được thêm!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		}

		String[] parts = selected.split(" - ");
		String ky = parts[0].replace("Kỳ: ", "");
		String giaStr = parts[1].replace(" VND", "");

		model.addRow(new Object[] { maDN, ky, giaStr });
		tinhTongTien();
	}

	private void tinhTongTien() {
		double tongPhuPhi = 0;
		double tongDienNuoc = 0;
		double giaThue = 0;

		for (Double gia : mapGiaPhuPhi.values()) {
			tongPhuPhi += gia;
		}

		DefaultTableModel modelDN = view.getModelDienNuoc();
		for (int i = 0; i < modelDN.getRowCount(); i++) {
			try {
				String giaStr = modelDN.getValueAt(i, 2).toString().replace(",", "");
				tongDienNuoc += Double.parseDouble(giaStr);
			} catch (Exception e) {
			}
		}

		try {
			String giaThueStr = view.getTxtGiaThue().getText().replace(",", "");
			if (!giaThueStr.isEmpty()) {
				giaThue = Double.parseDouble(giaThueStr);
			}
		} catch (Exception e) {
		}

		String loaiTT = (String) view.getCboLoaiThanhToan().getSelectedItem();
		double tongThanhToan = 0;

		if ("Tiền Trọ".equals(loaiTT)) {
			tongThanhToan = giaThue + tongPhuPhi;
			tongDienNuoc = 0;
		} else if ("Điện Nước".equals(loaiTT)) {
			tongThanhToan = tongDienNuoc;
			giaThue = 0;
			tongPhuPhi = 0;
		} else {
			tongThanhToan = giaThue + tongPhuPhi + tongDienNuoc;
		}

		view.setTongTienPhuPhi(String.format("%.0f", tongPhuPhi));
		view.setTongTienDN(String.format("%.0f", tongDienNuoc));
		view.setTongThanhToan(String.format("%.0f", tongThanhToan));
	}

	private void xuLyLuuHoaDon() {
		try {
			String maHDStr = view.getTxtMaHopDong().getText();
			if (maHDStr.isEmpty()) {
				JOptionPane.showMessageDialog(view, "Vui lòng tìm và chọn khách hàng trước!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			HoaDon hd = new HoaDon();
			hd.getHopDong().setMaHD(Long.parseLong(maHDStr));
			hd.setNgayThanhToan(LocalDateTime.now());

			String tongTienStr = view.getTxtTongThanhToan().getText().replace(",", "");
			hd.setTongTien(Double.parseDouble(tongTienStr));

			String tongPPStr = view.getTxtTongTienPhuPhi().getText().replace(",", "");
			hd.setTongTienPP(Double.parseDouble(tongPPStr));

			hd.setGhiChu(view.getTxtGhiChu().getText());
			hd.setLoaiThanhToan((String) view.getCboLoaiThanhToan().getSelectedItem());
			hd.getPhuongThucThanhToan().setMaPT(view.getCboPhuongThuc().getSelectedIndex() + 1);

			List<Long> listMaDN = new ArrayList<>();
			DefaultTableModel modelDN = view.getModelDienNuoc();
			for (int i = 0; i < modelDN.getRowCount(); i++) {
				listMaDN.add(Long.parseLong(modelDN.getValueAt(i, 0).toString()));
			}

			String kq = hoaDonDAO.insertHoaDon(hd, listMaDN);

			if (kq.equals("SUCCESS")) {
				JOptionPane.showMessageDialog(view, "Lập hóa đơn thành công!");
				if (parentController != null) {
					parentController.refreshData();
				}
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, kq, "Lỗi lưu dữ liệu", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void xuLyThemPhieuMoi() {
		// 1. Kiểm tra đã có Hợp đồng chưa
		String maHDStr = view.getTxtMaHopDong().getText();
		if (maHDStr.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng tìm và chọn khách hàng trước!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		long maHD = Long.parseLong(maHDStr);

		// 2. Lấy Mã Phòng từ thông tin hợp đồng (để truyền sang form điện nước)
		// Hàm getThongTinHopDong đã được viết trong DAO ở các bước trước
		Map<String, Object> info = hoaDonDAO.getThongTinHopDong(maHD);
		if (info == null || !info.containsKey("MaPhong")) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		long maPhong = (Long) info.get("MaPhong");
		String tenPhong = (String) info.get("SoPhong"); // Lấy thêm số phòng để hiển thị title nếu cần

//        ThemDienNuocView dnView = new ThemDienNuocView(); // Khởi tạo View
//        dnView.setModal(true); // QUAN TRỌNG: Chặn tương tác form cha để chờ form con đóng
//        dnView.setTitle("Thêm Điện Nước - Phòng " + tenPhong);
//        
//        new ThemDienNuocController(dnView, maPhong); 
//        
//        dnView.setVisible(true); // Hiển thị lên
//        

		// Demo thông báo (Xóa dòng này khi đã có form thật)
		JOptionPane.showMessageDialog(view,
				"Đang mở form thêm điện nước cho Phòng: " + tenPhong + " (Mã: " + maPhong + ")\n"
						+ "Sau khi thêm xong, danh sách bên cạnh sẽ tự cập nhật.",
				"Giả lập", JOptionPane.INFORMATION_MESSAGE);

		// 4. Load lại ComboBox điện nước sau khi form kia đóng
		// Để người dùng thấy ngay phiếu vừa tạo
		loadDienNuocChuaThanhToan(maHD);
	}
}