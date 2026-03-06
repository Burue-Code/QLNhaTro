package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.NhaTroDAO;
import com.nctu.quanlynhatro.dao.PhongDAO;
import com.nctu.quanlynhatro.dao.PhuPhiDAO;
import com.nctu.quanlynhatro.model.NhaTro;
import com.nctu.quanlynhatro.model.Phong;
import com.nctu.quanlynhatro.model.PhuPhi;
import com.nctu.quanlynhatro.view.component.MyPopupMenu;
import com.nctu.quanlynhatro.view.phong.ThemPhongView;

public class SuaPhongController implements ActionListener {

	private ThemPhongView view;
	private PhongDAO phongDAO;
	private PhongController parentController;
	private Phong phongCanSua;
	private NhaTroDAO nhaTroDAO;
	private PhuPhiDAO phuPhiDAO;

	private JTable table;
	private DefaultTableModel model;

	private Map<String, Integer> mapNhaTro = new HashMap<>();
	private Map<String, Integer> mapPhuPhi = new HashMap<>();

	public SuaPhongController(ThemPhongView view, PhongController parentController, Phong phong) {
		this.view = view;
		this.table = view.getTblPhuPhi();
		this.model = view.getModelPhuPhi();
		this.parentController = parentController;
		this.phongCanSua = phong;
		this.phongDAO = new PhongDAO(DatabaseConnection.getConnection());

		this.view.setTitle("Cập Nhật Thông Tin Phòng: " + phong.getSoPhong());
		this.view.getBtnXacNhan().setText("Lưu Thay Đổi");

		initData();
		fillData();
		loadPhuPhiCu();
		initPopupMenu();
		addEvents();
		this.view.setVisible(true);
	}

	private void initPopupMenu() {
		MyPopupMenu popup = new MyPopupMenu(table);
		JMenuItem mnuXoa = popup.addItem("Xóa Phụ Phí Này");
		mnuXoa.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				model.removeRow(table.convertRowIndexToModel(row));
			}
		});
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

	private void fillData() {
		view.getTxtSoPhong().setText(String.valueOf(phongCanSua.getSoPhong()));
		view.getTxtGiaPhong().setText(String.format("%.0f", phongCanSua.getGia()));
		view.getTxtSoNguoi().setText(String.valueOf(phongCanSua.getSoNguoiToiDa()));
		view.getTxtPhuThu().setText(String.format("%.0f", phongCanSua.getPhuThu()));
		view.getTxtGhiChu().setText(phongCanSua.getGhiChu());

		if (phongCanSua.getNhaTro() != null) {
			long maNhaTroCu = phongCanSua.getNhaTro().getMaNT();
			for (Map.Entry<String, Integer> entry : mapNhaTro.entrySet()) {
				if (entry.getValue() == maNhaTroCu) {
					view.getCboNhaTro().setSelectedItem(entry.getKey());
					break;
				}
			}
		}

		String trangThaiDB = phongCanSua.getTrangThaiPhong();
		if (trangThaiDB != null) {
			trangThaiDB = trangThaiDB.trim();
			boolean isMatched = false;
			for (int i = 0; i < view.getCboTrangThai().getItemCount(); i++) {
				String item = view.getCboTrangThai().getItemAt(i);
				if (item != null && item.trim().equalsIgnoreCase(trangThaiDB)) {
					view.getCboTrangThai().setSelectedIndex(i);
					isMatched = true;
					break;
				}
			}
			if (!isMatched) {
				view.getCboTrangThai().setSelectedItem(trangThaiDB);
			}
		}
	}

	private void loadPhuPhiCu() {
		DefaultTableModel model = view.getModelPhuPhi();
		model.setRowCount(0);
		List<PhuPhi> ds = phuPhiDAO.getPhuPhiByMaPhong(phongCanSua.getMaPhong());
		for (PhuPhi pp : ds) {
			model.addRow(new Object[] { pp.getMaPP(), pp.getTenPP(), String.format("%.0f", pp.getGia()) });
		}
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == view.getBtnThoat()) {
			view.dispose();
		} else if (e.getSource() == view.getBtnThemPhuPhi()) {
			xuLyThemPhuPhi();
		} else if (e.getSource() == view.getBtnXacNhan()) {
			xuLyCapNhat();
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

		for (int i = 0; i < model.getRowCount(); i++) {
			Object cell = model.getValueAt(i, 0);
			if (cell != null && Integer.parseInt(cell.toString()) == maPP) {
				JOptionPane.showMessageDialog(view, "Phụ phí này đã được thêm rồi!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		}

		String soTien = phuPhiDAO.getSoTienByMaPP(maPP);
		model.addRow(new Object[] { maPP, tenPP, soTien });
	}

	private void xuLyCapNhat() {
		String soPhongStr = view.getTxtSoPhong().getText().trim();
		String giaStr = view.getTxtGiaPhong().getText().trim();
		String slNguoiStr = view.getTxtSoNguoi().getText().trim();
		String phuThuStr = view.getTxtPhuThu().getText().trim();
		String ghiChu = view.getTxtGhiChu().getText().trim();

		String tenNT = (String) view.getCboNhaTro().getSelectedItem();
		String trangThai = (String) view.getCboTrangThai().getSelectedItem();

		if (tenNT == null || !mapNhaTro.containsKey(tenNT)) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn nhà trọ hợp lệ!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		int maNT = mapNhaTro.get(tenNT);

		if (soPhongStr.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập số phòng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			view.getTxtSoPhong().requestFocus();
			return;
		}
		int soPhong = 0;
		try {
			soPhong = Integer.parseInt(soPhongStr);
			if (soPhong <= 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(view, "Số phòng phải là số nguyên > 0!", "Lỗi nhập liệu",
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
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(view, "Giá phòng phải là số >= 0!", "Lỗi nhập liệu",
					JOptionPane.ERROR_MESSAGE);
			view.getTxtGiaPhong().requestFocus();
			return;
		}

		if (slNguoiStr.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập số người ở tối đa!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			view.getTxtSoNguoi().requestFocus();
			return;
		}
		int slNguoi = 0;
		try {
			slNguoi = Integer.parseInt(slNguoiStr);
			if (slNguoi <= 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(view, "Số người ở phải là số nguyên > 0!", "Lỗi nhập liệu",
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
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(view, "Phụ thu phải là số >= 0!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
			view.getTxtPhuThu().requestFocus();
			return;
		}

		try {
			if (phongDAO.isSoPhongExistForUpdate(phongCanSua.getMaPhong(), soPhong, maNT)) {
				JOptionPane.showMessageDialog(view, "Số phòng này đã tồn tại trong nhà trọ được chọn!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				view.getTxtSoPhong().requestFocus();
				return;
			}

			phongCanSua.setSoPhong(soPhong);
			phongCanSua.setGia(gia);
			phongCanSua.setSoNguoiToiDa(slNguoi);
			phongCanSua.setPhuThu(phuThu);
			phongCanSua.setGhiChu(ghiChu);
			phongCanSua.setTrangThaiPhong(trangThai);

			NhaTro nt = new NhaTro();
			nt.setMaNT(maNT);
			phongCanSua.setNhaTro(nt);

			List<Integer> listMaPP = new ArrayList<>();
			for (int i = 0; i < model.getRowCount(); i++) {
				listMaPP.add(Integer.parseInt(model.getValueAt(i, 0).toString()));
			}

			boolean kq = phongDAO.updatePhong(phongCanSua, listMaPP);

			if (kq) {
				JOptionPane.showMessageDialog(view, "Cập nhật phòng thành công!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				if (parentController != null) {
					parentController.refreshData();
				}
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, "Lỗi khi cập nhật vào CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
}