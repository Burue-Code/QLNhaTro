package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.NhaTroDAO;
import com.nctu.quanlynhatro.model.NhaTro;
import com.nctu.quanlynhatro.view.nha_tro.ThemNhaTroView;

public class SuaNhaTroController implements ActionListener {
	private ThemNhaTroView view;
	private NhaTroDAO nhaTroDAO;
	private NhaTroController parentController;
	private NhaTro nhaTroCanSua;

	public SuaNhaTroController(ThemNhaTroView view, NhaTroController parentController, NhaTro nhaTro) {
		this.view = view;
		this.parentController = parentController;
		this.nhaTroCanSua = nhaTro;
		this.nhaTroDAO = new NhaTroDAO(DatabaseConnection.getConnection());
		this.view.setTitle("Sửa Nhà Trọ");
		this.view.getBtnThem().setText("Xác Nhận ");

		fillData();
		addEvents();
		this.view.setVisible(true);
	}

	private void fillData() {
		view.getTxtTenNha().setText(nhaTroCanSua.getTenNT());
		view.getTxtSoPhong().setText(String.valueOf(nhaTroCanSua.getSLPhong()));
		view.getTxtDiaChi().setText(nhaTroCanSua.getDiaChi());
		view.getTxtGhiChu().setText(nhaTroCanSua.getGhiChu());

		String trangThaiDB = nhaTroCanSua.getTrangThaiNT();
		if (trangThaiDB != null) {
			trangThaiDB = trangThaiDB.trim();
			boolean isMatched = false;

			for (int i = 0; i < view.getCbTrangThai().getItemCount(); i++) {
				String item = view.getCbTrangThai().getItemAt(i);
				if (item != null && item.trim().equalsIgnoreCase(trangThaiDB)) {
					view.getCbTrangThai().setSelectedIndex(i);
					isMatched = true;
					break;
				}
			}

			if (!isMatched) {
				view.getCbTrangThai().setSelectedItem(trangThaiDB);
			}
		}
	}

	private void addEvents() {
		view.getBtnThem().addActionListener(this);
		view.getBtnHuy().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == view.getBtnHuy()) {
			view.dispose();
		} else if (e.getSource() == view.getBtnThem()) {
			xuLyCapNhat();
		}
	}

	private void xuLyCapNhat() {

		String tenNhaTro = view.getTxtTenNha().getText().trim();
		String diaChi = view.getTxtDiaChi().getText().trim();
		String slPhongStr = view.getTxtSoPhong().getText().trim();
		String ghiChu = view.getTxtGhiChu().getText().trim();
		String trangThai = (String) view.getCbTrangThai().getSelectedItem();

		if (tenNhaTro.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập tên nhà trọ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			view.getTxtTenNha().requestFocus();
			return;
		}

		if (diaChi.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập địa chỉ nhà trọ!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			view.getTxtDiaChi().requestFocus();
			return;
		}

		if (slPhongStr.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng nhập số lượng phòng!", "Cảnh báo",
					JOptionPane.WARNING_MESSAGE);
			view.getTxtSoPhong().requestFocus();
			return;
		}

		int slPhong = 0;
		try {
			slPhong = Integer.parseInt(slPhongStr);
			if (slPhong <= 0) {
				JOptionPane.showMessageDialog(view, "Số lượng phòng phải là số nguyên lớn hơn 0!", "Lỗi nhập liệu",
						JOptionPane.ERROR_MESSAGE);
				view.getTxtSoPhong().requestFocus();
				return;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(view, "Số lượng phòng không hợp lệ (Không được chứa chữ cái)!",
					"Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
			view.getTxtSoPhong().requestFocus();
			return;
		}

		if (trangThai == null || trangThai.trim().isEmpty()) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn trạng thái!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
			view.getCbTrangThai().requestFocus();
			return;
		}

		try {

			if (nhaTroDAO.isTenNhaTroExistForUpdate((int) nhaTroCanSua.getMaNT(), tenNhaTro)) {
				JOptionPane.showMessageDialog(view, "Tên nhà trọ này đã tồn tại! Vui lòng chọn tên khác.", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				view.getTxtTenNha().requestFocus();
				return;
			}

			nhaTroCanSua.setTenNT(tenNhaTro);
			nhaTroCanSua.setSLPhong(slPhong);
			nhaTroCanSua.setDiaChi(diaChi);
			nhaTroCanSua.setGhiChu(ghiChu);
			nhaTroCanSua.setTrangThaiNT(trangThai);

			boolean kq = nhaTroDAO.update(nhaTroCanSua);

			if (kq) {
				JOptionPane.showMessageDialog(view, "Cập nhật nhà trọ thành công!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);

				if (parentController != null) {
					parentController.refreshData();
				}

				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, "Lỗi khi cập nhật vào cơ sở dữ liệu!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
}
