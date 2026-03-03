package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.nctu.quanlynhatro.dao.PhuongThucThanhToanDAO;
import com.nctu.quanlynhatro.model.PhuongThucThanhToan;
import com.nctu.quanlynhatro.view.phuong_thuc_tt.ThemPhuongThucThanhToanView;

public class SuaPTTTController implements ActionListener{
	private ThemPhuongThucThanhToanView view;
	private PhuongThucTTController phuongThucTTController;
	private PhuongThucThanhToanDAO phuongThucThanhToanDAO;
	private PhuongThucThanhToan phuongThucThanhToan;
	
	public SuaPTTTController(ThemPhuongThucThanhToanView view, PhuongThucTTController phuongThucTTController, PhuongThucThanhToan phuongThucThanhToan ) {
		this.view = view;
		this.phuongThucTTController = phuongThucTTController;
		this.phuongThucThanhToan = phuongThucThanhToan;
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
		if(e.getSource() == view.getBtnHuy()) view.dispose();
		else if(e.getSource() == view.getBtnThem()) xuLyCapNhat();
	}
	
	private void xuLyCapNhat() {
		try {
			phuongThucThanhToan.setTenPT(view.getTxtTenPT().getText());
			
			boolean kq =  phuongThucThanhToanDAO.update(phuongThucThanhToan);
			
			if (kq) {
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
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
