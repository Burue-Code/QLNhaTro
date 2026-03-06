package com.nctu.quanlynhatro.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.nctu.quanlynhatro.view.dien_nuoc.ThemDienNuocView;
import com.nctu.quanlynhatro.view.hoa_don.ThemHoaDonView;

public class SuaHoaDonController {

	private ThemHoaDonView view;
	private HoaDonController parentController;
	private HoaDonDAO hoaDonDAO;
	private PhuongThucThanhToanDAO ptttDAO;

	private long maHoaDonCur;

	private Map<String, Long> mapDienNuoc = new HashMap<>();
	private Map<String, Double> mapGiaPhuPhi = new HashMap<>();
	private Map<String, Integer> mapPhuongThuc = new HashMap<>();

	public SuaHoaDonController(ThemHoaDonView view, HoaDonController parentController, long maHoaDon) {
		this.view = view;
		this.parentController = parentController;
		this.maHoaDonCur = maHoaDon;

		this.hoaDonDAO = new HoaDonDAO(DatabaseConnection.getConnection());
		this.ptttDAO = new PhuongThucThanhToanDAO(DatabaseConnection.getConnection());

		view.setTitle("Cập Nhật Hóa Đơn - Mã: " + maHoaDon);
		view.getBtnXacNhan().setText("Lưu Thay Đổi");

		view.getTxtTenKH().setEditable(false);
		view.getTxtTenKH().setFocusable(false);

		initData();
		loadOldData();
		initEvents();

	}

	private void initData() {
		loadPhuongThucThanhToan();
	}

	private void loadPhuongThucThanhToan() {
		view.getCboPhuongThuc().removeAllItems();
		mapPhuongThuc.clear();

		Map<Integer, String> data = ptttDAO.getPhuongThucThanhToan();

		for (Map.Entry<Integer, String> entry : data.entrySet()) {
			int maPT = entry.getKey();
			String tenPT = entry.getValue();
			view.getCboPhuongThuc().addItem(tenPT);
			mapPhuongThuc.put(tenPT, maPT);
		}
	}

	private void loadOldData() {
		HoaDon hd = hoaDonDAO.getHoaDonById(maHoaDonCur);

		if (hd == null) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			view.dispose();
			return;
		}

		view.getTxtMaHopDong().setText(String.valueOf(hd.getHopDong().getMaHD()));
		view.getTxtGhiChu().setText(hd.getGhiChu());
		view.getCboLoaiThanhToan().setSelectedItem(hd.getLoaiThanhToan());

		if (hd.getNgayThanhToan() != null) {
			view.getTxtNgayThanhToan().setText(hd.getNgayThanhToan().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		}

		for (Map.Entry<String, Integer> entry : mapPhuongThuc.entrySet()) {
			if (entry.getValue() == hd.getPhuongThucThanhToan().getMaPT()) {
				view.getCboPhuongThuc().setSelectedItem(entry.getKey());
				break;
			}
		}

		loadThongTinChiTiet(hd.getHopDong().getMaHD());

		loadDienNuocDaCo(maHoaDonCur);

		tinhTongTien();
	}

	private void loadThongTinChiTiet(long maHD) {
		Map<String, Object> info = hoaDonDAO.getThongTinHopDong(maHD);
		if (info != null) {
			view.getTxtNhaTro().setText((String) info.get("TenNT"));
			view.getTxtPhong().setText((String) info.get("SoPhong"));
			view.getTxtGiaThue().setText(String.format("%.0f", info.get("GiaThue")));

			view.getTxtTenKH().setText(hoaDonDAO.getTenKhachHangByMaHD(maHD));
		}

		loadPhuPhi(maHD);
		loadDienNuocChuaThanhToan(maHD);
	}

	private void loadDienNuocDaCo(long maHoaDon) {
		List<Map<String, Object>> listDaCo = hoaDonDAO.getDienNuocByMaHoaDon(maHoaDon);
		DefaultTableModel model = view.getModelDienNuoc();

		for (Map<String, Object> dn : listDaCo) {
			long maDN = (Long) dn.get("MaDN");
			String thangNam = (String) dn.get("ThangNam");
			double tongTien = (Double) dn.get("TongTien");

			model.addRow(new Object[] { maDN, thangNam, String.format("%.0f", tongTien) });
		}
	}

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

		List<Map<String, Object>> listDN = hoaDonDAO.getDienNuocChuaThanhToan(maHD);
		if (listDN.isEmpty()) {
			view.getCboChonDienNuoc().addItem("-- Không có hóa đơn nợ mới --");
		} else {
			for (Map<String, Object> dn : listDN) {
				long maDN = (Long) dn.get("MaDN");
				String display = "Kỳ: " + dn.get("ThangNam") + " - " + String.format("%.0f", dn.get("TongTien"))
						+ " VND";
				view.getCboChonDienNuoc().addItem(display);
				mapDienNuoc.put(display, maDN);
			}
		}
	}

	private void initEvents() {
		view.getBtnHuy().addActionListener(e -> view.dispose());

		view.getBtnCongDN().addActionListener(e -> xuLyThemDienNuocVaoBang());
		view.getCboLoaiThanhToan().addActionListener(e -> tinhTongTien());
		view.getBtnXacNhan().addActionListener(e -> xuLyCapNhatHoaDon());
		view.getBtnThemPhieuMoi().addActionListener(e -> xuLyThemPhieuMoi());
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
				JOptionPane.showMessageDialog(view, "Phiếu này đã được thêm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
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
		for (Double gia : mapGiaPhuPhi.values()) {
			tongPhuPhi += gia;
		}

		double tongDienNuoc = 0;
		DefaultTableModel modelDN = view.getModelDienNuoc();
		for (int i = 0; i < modelDN.getRowCount(); i++) {
			try {
				String giaStr = modelDN.getValueAt(i, 2).toString().replace(",", "");
				tongDienNuoc += Double.parseDouble(giaStr);
			} catch (Exception e) {
			}
		}

		double giaThue = 0;
		try {
			String giaStr = view.getTxtGiaThue().getText().replace(",", "");
			if (!giaStr.isEmpty()) {
				giaThue = Double.parseDouble(giaStr);
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

	private void xuLyCapNhatHoaDon() {
		try {
			HoaDon hd = new HoaDon();
			hd.setMaHoaDon(maHoaDonCur);

			String tongTienStr = view.getTxtTongThanhToan().getText().replace(",", "");
			hd.setTongTien(Double.parseDouble(tongTienStr));

			String tongPPStr = view.getTxtTongTienPhuPhi().getText().replace(",", "");
			hd.setTongTienPP(Double.parseDouble(tongPPStr));

			hd.setGhiChu(view.getTxtGhiChu().getText());
			hd.setLoaiThanhToan((String) view.getCboLoaiThanhToan().getSelectedItem());

			String tenPT = (String) view.getCboPhuongThuc().getSelectedItem();
			if (tenPT != null && mapPhuongThuc.containsKey(tenPT)) {
				hd.getPhuongThucThanhToan().setMaPT(mapPhuongThuc.get(tenPT));
			} else {
				hd.getPhuongThucThanhToan().setMaPT(1);
			}

			hd.setNgayThanhToan(LocalDateTime.now());

			List<Long> listMaDN = new ArrayList<>();
			DefaultTableModel modelDN = view.getModelDienNuoc();
			for (int i = 0; i < modelDN.getRowCount(); i++) {
				listMaDN.add(Long.parseLong(modelDN.getValueAt(i, 0).toString()));
			}

			int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn lưu thay đổi?", "Xác nhận",
					JOptionPane.YES_NO_OPTION);
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}

			String kq = hoaDonDAO.updateHoaDon(hd, listMaDN);

			if (kq.equals("SUCCESS")) {
				JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
				if (parentController != null) {
					parentController.refreshData();
				}
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, kq, "Lỗi cập nhật", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void xuLyThemPhieuMoi() {
		String maHDStr = view.getTxtMaHopDong().getText();
		if (maHDStr.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng tìm và chọn khách hàng trước!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		long maHD = Long.parseLong(maHDStr);

		Map<String, Object> info = hoaDonDAO.getThongTinHopDong(maHD);
		if (info == null || !info.containsKey("MaPhong")) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		long maPhong = (Long) info.get("MaPhong");
		String tenPhong = (String) info.get("SoPhong");
		ThemDienNuocView dnView = new ThemDienNuocView(null);
		dnView.setModal(true);
		dnView.setTitle("Thêm Điện Nước - Phòng " + tenPhong);

		new ThemDienNuocController(dnView, maPhong);

		dnView.setVisible(true);

		loadDienNuocChuaThanhToan(maHD);
	}
}