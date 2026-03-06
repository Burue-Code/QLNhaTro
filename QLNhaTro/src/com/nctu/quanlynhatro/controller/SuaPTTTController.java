package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.PhuongThucThanhToanDAO;
import com.nctu.quanlynhatro.model.PhuongThucThanhToan;
import com.nctu.quanlynhatro.view.phuong_thuc_tt.ThemPhuongThucThanhToanView;

public class SuaPTTTController implements ActionListener {
	private ThemPhuongThucThanhToanView view;
	private PhuongThucTTController phuongThucTTController;
	private PhuongThucThanhToanDAO phuongThucThanhToanDAO;
	private PhuongThucThanhToan phuongThucThanhToan;

	public SuaPTTTController(ThemPhuongThucThanhToanView view, PhuongThucTTController phuongThucTTController,
			PhuongThucThanhToan phuongThucThanhToan) {
		this.view = view;
		this.phuongThucTTController = phuongThucTTController;
		this.phuongThucThanhToan = phuongThucThanhToan;
		this.phuongThucThanhToanDAO = new PhuongThucThanhToanDAO(DatabaseConnection.getConnection());

		this.view.setTitle("Sửa Phương Thức Thanh Toán");
		this.view.getBtnThem().setText("Xác Nhận");

		fillData();
		addEvents();

		this.view.setVisible(true);
	}

	private void fillData() {
		view.getTxtTenPT().setText(phuongThucThanhToan.getTenPT());
	}

	private void addEvents() {
		view.getBtnHuy().addActionListener(this);
		view.getBtnThem().addActionListener(this);
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
		try {
			String tenPT = view.getTxtTenPT().getText().trim();
			if (tenPT.isEmpty()) {
				JOptionPane.showMessageDialog(view, "Tên phương thức không được để trống!");
				return;
			}

			phuongThucThanhToan.setTenPT(tenPT);
			boolean kq = phuongThucThanhToanDAO.update(phuongThucThanhToan);

			if (kq) {
				JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
				if (phuongThucTTController != null) {
					phuongThucTTController.refreshData();
				}
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, "Lỗi khi cập nhật!");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
		}
	}
}