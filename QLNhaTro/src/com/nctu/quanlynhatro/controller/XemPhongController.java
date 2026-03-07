package com.nctu.quanlynhatro.controller;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.KhachHangDAO;
import com.nctu.quanlynhatro.dao.PhongDAO;
import com.nctu.quanlynhatro.dao.PhuPhiDAO;
import com.nctu.quanlynhatro.model.KhachHang;
import com.nctu.quanlynhatro.model.Phong;
import com.nctu.quanlynhatro.model.PhuPhi;
import com.nctu.quanlynhatro.view.phong.ThemPhongView;
import com.nctu.quanlynhatro.view.phong.XemPhong;

public class XemPhongController {
	private XemPhong view;
	private Phong phong;
	private long maPhong;
	private PhongDAO phongDAO;
	private PhongController parentController;

	public XemPhongController(XemPhong view, long maPhong, PhongController parentController) {
		this.view = view;
		this.maPhong = maPhong;
		this.parentController = parentController;
		this.phongDAO = new PhongDAO(DatabaseConnection.getConnection());
		this.phong = phongDAO.getPhongById(maPhong);
		if (this.phong != null) {
			fillData();
			addEvents();
			this.view.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			view.dispose();
		}
	}

	private void fillData() {
		view.getTxtSoPhong().setText(String.valueOf(phong.getSoPhong()));
		view.getTxtGiaPhong().setText(String.format("%,.0f", phong.getGia()));
		view.getTxtSoNguoi().setText(String.valueOf(phong.getSoNguoiToiDa()));
		view.getTxtPhuThu().setText(String.format("%,.0f", phong.getPhuThu()));
		view.getTxtTrangThai().setText(phong.getTrangThaiPhong());
		view.getTxtGhiChu().setText(phong.getGhiChu());

		DefaultTableModel modelPP = (DefaultTableModel) view.getTblPhuPhi().getModel();
		modelPP.setRowCount(0);
		PhuPhiDAO phuPhiDAO = new PhuPhiDAO(DatabaseConnection.getConnection());
		List<PhuPhi> dsPP = phuPhiDAO.getPhuPhiByMaPhong(maPhong);
		for (PhuPhi pp : dsPP) {
			modelPP.addRow(new Object[] { pp.getMaPP(), pp.getTenPP(), String.format("%,.0f", pp.getGia()) });
		}

		DefaultTableModel modelKH = (DefaultTableModel) view.getTblKhachHang().getModel();
		modelKH.setRowCount(0);
		KhachHangDAO khDAO = new KhachHangDAO(DatabaseConnection.getConnection());
		List<KhachHang> dsKH = khDAO.getKhachHangByPhong(maPhong);
		for (KhachHang kh : dsKH) {
			modelKH.addRow(new Object[] { kh.getMaKH(), kh.getTenKH(), kh.getDiaChi(), kh.getGioiTinh() ? "Nữ" : "Nam",
					kh.getNgaySinh() });
		}
	}

	private void addEvents() {
		view.getBtnXoa().addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa phòng này?", "Xác nhận",
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				if (phongDAO.deleteSoft(phong.getMaPhong())) {
					JOptionPane.showMessageDialog(view, "Xóa thành công!");
					if (parentController != null) {
						parentController.refreshData();
					}
					view.dispose();
				}
			}
		});

		view.getBtnSua().addActionListener(e -> {
			try {
				view.dispose();

				DefaultTableModel model = (parentController != null) ? parentController.getModel() : null;

				ThemPhongView suaView = new ThemPhongView(model);
				suaView.setModal(true);
				new SuaPhongController(suaView, parentController, phong);

			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Lỗi khi mở form sửa: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}