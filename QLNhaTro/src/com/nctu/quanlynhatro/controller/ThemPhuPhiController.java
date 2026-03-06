package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.PhuPhiDAO;
import com.nctu.quanlynhatro.view.phu_phi.ThemPhuPhiView;

public class ThemPhuPhiController implements ActionListener {
	private ThemPhuPhiView view;
	private PhuPhiController phuPhiController;
	private PhuPhiDAO phuPhiDAO;

	public ThemPhuPhiController(ThemPhuPhiView view, PhuPhiController phuPhiController) {
		this.view = view;
		this.phuPhiController = phuPhiController;
		this.phuPhiDAO = new PhuPhiDAO(DatabaseConnection.getConnection());
		this.view.setTitle("Thêm Phụ Phí Mới");
		this.view.getBtnXatNhan().setText("Thêm Mới");

		addEvents();
		this.view.setVisible(true);
	}

	private void addEvents() {
		view.getBtnThoat().addActionListener(this);
		view.getBtnXatNhan().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == view.getBtnThoat()) {
			view.dispose();
		} else if (e.getSource() == view.getBtnXatNhan()) {
			xuLyThemMoi();
		}
	}

	private void xuLyThemMoi() {
		if (view.getTxtTenPP().getText().isEmpty()) {
			JOptionPane.showMessageDialog(view, "Chưa nhập tên phụ phí");
			return;
		}

		try {

			String tenPP = view.getTxtTenPP().getText();
			double gia = Double.parseDouble(view.getTxtGia().getText());
			boolean kq = phuPhiDAO.insert(tenPP, gia);
			if (kq) {
				JOptionPane.showMessageDialog(view, "Thêm thành công!");
				phuPhiController.refreshData();
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, "Thất bại!");
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(view, "Lỗi nhập liệu: " + ex.getMessage());
		}
	}
}
