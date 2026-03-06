package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.NhaTroDAO;
import com.nctu.quanlynhatro.dao.PhongDAO;
import com.nctu.quanlynhatro.dao.PhuPhiDAO;
import com.nctu.quanlynhatro.view.phong.ThemPhongView;

public class ThemPhongController implements ActionListener {

	private ThemPhongView view;
	private PhongDAO phongDAO;
	private PhongController parentController;
	private NhaTroDAO nhaTroDAO;
	private PhuPhiDAO phuPhiDAO;

	private Map<String, Integer> mapNhaTro = new HashMap<>();
	private Map<String, Integer> mapPhuPhi = new HashMap<>();

	public ThemPhongController(ThemPhongView view, PhongController parentController) {
		this.view = view;
		this.parentController = parentController;
		this.phongDAO = new PhongDAO(DatabaseConnection.getConnection());

		this.view.setTitle("Thêm Phòng Mới");
		this.view.getBtnXacNhan().setText("Thêm Mới");

		initData();
		addEvents();
		this.view.setVisible(true);
	}

	private void initData() {
		nhaTroDAO = new NhaTroDAO(DatabaseConnection.getConnection());
		phuPhiDAO = new PhuPhiDAO(DatabaseConnection.getConnection());

		view.getCboNhaTro().removeAllItems();
		mapNhaTro.clear();

		Map<Integer, String> dataNhaTro = nhaTroDAO.getNhaTroConPhong();

		for (Map.Entry<Integer, String> entry : dataNhaTro.entrySet()) {
			int maNT = entry.getKey();
			String tenNT = entry.getValue();

			view.getCboNhaTro().addItem(tenNT);
			mapNhaTro.put(tenNT, maNT);
		}

		view.getCboPhuPhi().removeAllItems();
		mapPhuPhi.clear();

		Map<Integer, String> dataPhuPhi = phuPhiDAO.getPhuPhiCB();

		for (Map.Entry<Integer, String> entry : dataPhuPhi.entrySet()) {
			int maPP = entry.getKey();
			String tenPP = entry.getValue();

			view.getCboPhuPhi().addItem(tenPP);
			mapPhuPhi.put(tenPP, maPP);
		}
	}

	private void setChiNhapSoNguyen(javax.swing.JTextField txt) {
		txt.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
					e.consume();
				}
			}
		});
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

	private void addEvents() {
		view.getBtnXacNhan().addActionListener(this);
		view.getBtnThoat().addActionListener(this);
		view.getBtnThemPhuPhi().addActionListener(this);

		setChiNhapSoNguyen(view.getTxtSoPhong());
		setChiNhapSoNguyen(view.getTxtSoNguoi());

		setChiNhapSoThuc(view.getTxtGiaPhong());
		setChiNhapSoThuc(view.getTxtPhuThu());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == view.getBtnThoat()) {
			view.dispose();
		} else if (e.getSource() == view.getBtnThemPhuPhi()) {
			xuLyThemPhuPhi();
		} else if (e.getSource() == view.getBtnXacNhan()) {
			xuLyThemmoi();
		}
	}

	private void xuLyThemPhuPhi() {
		String tenPP = (String) view.getCboPhuPhi().getSelectedItem();
		if (tenPP == null) {
			return;
		}

		Integer maPP = mapPhuPhi.get(tenPP);
		if (maPP == null) {
			JOptionPane.showMessageDialog(view, "Không tìm thấy phụ phí!");
			return;
		}
		DefaultTableModel model = view.getModelPhuPhi();

		for (int i = 0; i < model.getRowCount(); i++) {
			Object cell = model.getValueAt(i, 0);
			if (cell != null && Integer.parseInt(cell.toString()) == maPP) {
				JOptionPane.showMessageDialog(view, "Phụ phí đã tồn tại!");
				return;
			}
		}
		String soTien = phuPhiDAO.getSoTienByMaPP(maPP);

		model.addRow(new Object[] { maPP, tenPP, soTien });
	}

	private void xuLyThemmoi() {
		String soPhongStr = view.getTxtSoPhong().getText().trim();
		String giaStr = view.getTxtGiaPhong().getText().trim();
		String slNguoiStr = view.getTxtSoNguoi().getText().trim();
		String phuThuStr = view.getTxtPhuThu().getText().trim();
		String ghiChu = view.getTxtGhiChu().getText().trim();

		String nhaTroSelected = (String) view.getCboNhaTro().getSelectedItem();
		String trangThai = (String) view.getCboTrangThai().getSelectedItem();

		if (nhaTroSelected == null || nhaTroSelected.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn nhà trọ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			view.getCboNhaTro().requestFocus();
			return;
		}

		if (soPhongStr.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập số phòng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			view.getTxtSoPhong().requestFocus();
			return;
		}
		int soPhong = 0;
		try {
			soPhong = Integer.parseInt(soPhongStr);
			if (soPhong <= 0) {
				JOptionPane.showMessageDialog(view, "Số phòng phải là số nguyên lớn hơn 0!", "Lỗi nhập liệu",
						JOptionPane.ERROR_MESSAGE);
				view.getTxtSoPhong().requestFocus();
				return;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(view, "Số phòng không hợp lệ (Không được chứa chữ cái)!", "Lỗi nhập liệu",
					JOptionPane.ERROR_MESSAGE);
			view.getTxtSoPhong().requestFocus();
			return;
		}

		if (giaStr.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập giá phòng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			view.getTxtGiaPhong().requestFocus();
			return;
		}
		double gia = 0;
		try {
			gia = Double.parseDouble(giaStr);
			if (gia < 0) {
				JOptionPane.showMessageDialog(view, "Giá phòng không được là số âm!", "Lỗi nhập liệu",
						JOptionPane.ERROR_MESSAGE);
				view.getTxtGiaPhong().requestFocus();
				return;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(view, "Giá phòng không hợp lệ (Chỉ nhập số)!", "Lỗi nhập liệu",
					JOptionPane.ERROR_MESSAGE);
			view.getTxtGiaPhong().requestFocus();
			return;
		}

		if (slNguoiStr.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập số lượng người tối đa!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			view.getTxtSoNguoi().requestFocus();
			return;
		}
		int slNguoi = 0;
		try {
			slNguoi = Integer.parseInt(slNguoiStr);
			if (slNguoi <= 0) {
				JOptionPane.showMessageDialog(view, "Số lượng người phải lớn hơn 0!", "Lỗi nhập liệu",
						JOptionPane.ERROR_MESSAGE);
				view.getTxtSoNguoi().requestFocus();
				return;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(view, "Số lượng người không hợp lệ (Chỉ nhập số nguyên)!", "Lỗi nhập liệu",
					JOptionPane.ERROR_MESSAGE);
			view.getTxtSoNguoi().requestFocus();
			return;
		}

		if (phuThuStr.isEmpty()) {
			phuThuStr = "0";
		}
		double phuThu = 0;
		try {
			phuThu = Double.parseDouble(phuThuStr);
			if (phuThu < 0) {
				JOptionPane.showMessageDialog(view, "Phí phụ thu không được là số âm!", "Lỗi nhập liệu",
						JOptionPane.ERROR_MESSAGE);
				view.getTxtPhuThu().requestFocus();
				return;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(view, "Phí phụ thu không hợp lệ (Chỉ nhập số)!", "Lỗi nhập liệu",
					JOptionPane.ERROR_MESSAGE);
			view.getTxtPhuThu().requestFocus();
			return;
		}

		if (trangThai == null || trangThai.trim().isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn trạng thái phòng!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			view.getCboTrangThai().requestFocus();
			return;
		}

		try {
			int maNT = mapNhaTro.get(nhaTroSelected);

			List<Integer> listMaPP = new ArrayList<>();
			DefaultTableModel model = view.getModelPhuPhi();
			for (int i = 0; i < model.getRowCount(); i++) {
				listMaPP.add(Integer.parseInt(model.getValueAt(i, 0).toString()));
			}

			boolean kq = phongDAO.insertPhong(soPhong, gia, slNguoi, phuThu, trangThai, ghiChu, maNT, listMaPP);

			if (kq) {
				JOptionPane.showMessageDialog(view, "Thêm phòng thành công!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);

				if (parentController != null) {
					parentController.refreshData();
				}

				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view,
						"Thêm phòng thất bại! Có thể do trùng số phòng trong cùng một nhà trọ.", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
}