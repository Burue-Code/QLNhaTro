package com.nctu.quanlynhatro.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.HoaDonDAO;
import com.nctu.quanlynhatro.view.hoa_don.ThemHoaDonView;
import com.nctu.quanlynhatro.view.hoa_don.XemHoaDonView;

public class XemHoaDonController {

	private XemHoaDonView view;
	private HoaDonDAO dao;
	private long maHoaDon;
	private HoaDonController parentController;

	private DecimalFormat df = new DecimalFormat("#,###");
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private SimpleDateFormat sdfMonth = new SimpleDateFormat("MM/yyyy");

	public XemHoaDonController(XemHoaDonView view, long maHoaDon, HoaDonController parentController) {
		this.view = view;
		this.maHoaDon = maHoaDon;
		this.parentController = parentController;
		this.dao = new HoaDonDAO(DatabaseConnection.getConnection());

		view.setTitle("Xem Chi Tiết Hóa Đơn - Mã: " + maHoaDon);

		loadData();
		initEvents();
	}

	private void loadData() {
		Map<String, Object> hd = dao.getChiTietHoaDon(maHoaDon);
		if (hd.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy dữ liệu hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			view.dispose();
			return;
		}

		view.setTxtTenKH((String) hd.get("TenKH"));
		view.setTxtMaHopDong(String.valueOf(hd.get("MaHopDong")));
		view.setTxtNhaTro((String) hd.get("TenNT"));
		view.setTxtPhong((String) hd.get("SoPhong"));

		Double gia = (Double) hd.get("Gia");
		view.setTxtGiaThue(gia != null ? df.format(gia) : "0");

		view.setTxtGhiChu((String) hd.get("GhiChu"));

		String tenPT = (String) hd.get("TenPT");
		view.setTxtPhuongThucThanhToan(tenPT != null ? tenPT : "Chưa xác định");
		view.setTxtLoaiThanhToan((String) hd.get("LoaiTT"));

		java.sql.Timestamp ngayTT = (java.sql.Timestamp) hd.get("NgayTT");
		view.setTxtNgayThanhToan(ngayTT != null ? sdf.format(ngayTT) : "Chưa thanh toán");

		List<Map<String, Object>> listDN = dao.getDienNuocMaHoaDon(maHoaDon);
		DefaultTableModel modelDN = view.getModelDienNuoc();
		modelDN.setRowCount(0);
		double tongDN = 0;
		int countDN = 0;

		for (Map<String, Object> dn : listDN) {
			countDN++;
			double tienDN = (Double) dn.get("TongTien");
			tongDN += tienDN;
			java.sql.Date thangNam = (java.sql.Date) dn.get("ThangNam");

			modelDN.addRow(new Object[] { dn.get("MaDN"), thangNam != null ? sdfMonth.format(thangNam) : "",
					df.format(tienDN) });
		}
		view.setTxtHoaDonDienNuoc(countDN + " phiếu đính kèm");

		long maHD = (Long) hd.get("MaHopDong");
		List<Map<String, Object>> listPP = dao.getPhuPhiByHopDong(maHD);
		DefaultTableModel modelPP = view.getModelPhuPhi();
		modelPP.setRowCount(0);

		for (Map<String, Object> pp : listPP) {
			modelPP.addRow(new Object[] { pp.get("MaPP"), pp.get("TenPP"), df.format(pp.get("Gia")) });
		}

		Double tongPP_DB = (Double) hd.get("TongTienPP");
		Double tongThanhToan_DB = (Double) hd.get("SoTienTT");

		view.setTxtTongTienDN(df.format(tongDN));
		view.setTxtTongTienPhuPhi(tongPP_DB != null ? df.format(tongPP_DB) : "0");
		view.setTxtTongThanhToan(tongThanhToan_DB != null ? df.format(tongThanhToan_DB) : "0");
	}

	private void initEvents() {
		view.getBtnDong().addActionListener(e -> view.dispose());
		view.getBtnXoa().addActionListener(e -> xuLyXoa());
		view.getBtnSua().addActionListener(e -> xuLySua());
	}

	private void xuLyXoa() {
		int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn XÓA hóa đơn này không?",
				"Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			if (dao.deleteHoaDon(maHoaDon)) {
				JOptionPane.showMessageDialog(view, "Đã xóa hóa đơn!");
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

			ThemHoaDonView suaView = new ThemHoaDonView(parentController.getModel());
			suaView.setModal(true);
			new SuaHoaDonController(suaView, parentController, maHoaDon);
			suaView.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}