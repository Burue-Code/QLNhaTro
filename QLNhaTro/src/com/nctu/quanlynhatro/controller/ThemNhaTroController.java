package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.NhaTroDAO;
import com.nctu.quanlynhatro.view.nha_tro.ThemNhaTroView;

public class ThemNhaTroController implements ActionListener {
	private ThemNhaTroView view;
	private NhaTroDAO nhaTroDAO;
	private NhaTroController parentController;

	public ThemNhaTroController(ThemNhaTroView view, NhaTroController parentController) {
		this.view = view;
		this.parentController = parentController;
		this.nhaTroDAO = new NhaTroDAO(DatabaseConnection.getConnection());
		this.view.setTitle("Thêm Nhà Trọ Mới");
		this.view.getBtnThem().setText("Thêm Mới");

		addEvents();
		this.view.setVisible(true);
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
			xuLyThemmoi();
		}
	}

	private void xuLyThemmoi() {
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
			boolean kq = nhaTroDAO.insert(tenNhaTro, slPhong, diaChi, ghiChu, trangThai);

			if (kq) {
				JOptionPane.showMessageDialog(view, "Thêm nhà trọ thành công!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				if (parentController != null) {
					parentController.refreshData();
				}
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, "Thêm nhà trọ thất bại. Vui lòng kiểm tra lại!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
}