package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.PhuPhiDAO;
import com.nctu.quanlynhatro.model.PhuPhi;
import com.nctu.quanlynhatro.view.phu_phi.ThemPhuPhiView;

public class SuaPhuPhiController implements ActionListener {
	private ThemPhuPhiView view;
	private PhuPhiController phuPhiController;
	private PhuPhiDAO phuPhiDAO;
	private PhuPhi phuPhi;
	
	public SuaPhuPhiController(ThemPhuPhiView view,PhuPhiController phuPhiController, PhuPhi phuPhi) {
		this.view = view;
		this.phuPhiController = phuPhiController;
		this.phuPhi = phuPhi;
		this.phuPhiDAO = new PhuPhiDAO(DatabaseConnection.getConnection());
		this.view.setTitle("Sửa Phụ Phí");
		this.view.getBtnXatNhan().setText("Xác Nhận");
		
		fillData();
		addEvents();
		this.view.setVisible(true);
	}
	
	private void fillData() {
		view.getTxtTenPP().setText(phuPhi.getTenPP());
		view.getTxtGia().setText(String.valueOf(phuPhi.getGia()));
	}
	
	private void addEvents() {
		view.getBtnThoat().addActionListener(this);
		view.getBtnXatNhan().addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == view.getBtnThoat()) view.dispose();
		else if(e.getSource() == view.getBtnXatNhan()) xuLyCapNhat();
	}
	
	private void xuLyCapNhat() {
		try {
			
			phuPhi.setTenPP((String)view.getTxtTenPP().getText());
			phuPhi.setGia(Double.parseDouble(view.getTxtGia().getText()));
			
			boolean kq = phuPhiDAO.update(phuPhi);
			
			if (kq) {
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
//                parentController.refreshData();
                view.dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Lỗi khi cập nhật!");
            }

		} catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(view, "Vui lòng nhập giá là số hợp lệ!");
	    } catch (Exception ex) {
	        ex.printStackTrace(); // In lỗi ra console để dễ debug
	        JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
	    }
	}
}
