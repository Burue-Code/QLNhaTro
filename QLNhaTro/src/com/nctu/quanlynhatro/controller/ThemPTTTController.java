package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.PhuongThucThanhToanDAO;
import com.nctu.quanlynhatro.view.phuong_thuc_tt.ThemPhuongThucThanhToanView;

public class ThemPTTTController implements ActionListener {
	private ThemPhuongThucThanhToanView view;
	private PhuongThucTTController phuongThucTTController;
	private PhuongThucThanhToanDAO phuongThucThanhToanDAO;
	
	public ThemPTTTController(ThemPhuongThucThanhToanView view, PhuongThucTTController ptttConreoller) {
		this.view = view;
		this.phuongThucTTController = ptttConreoller;
		this.phuongThucThanhToanDAO = new PhuongThucThanhToanDAO(DatabaseConnection.getConnection());
		this.view.setTitle("Thêm Phương Thức Thanh Toán Mới");
		this.view.getBtnThem().setText("Thêm Mới");
		
		addEvents();
		this.view.setVisible(true);
	}
	
	private void addEvents() {
		view.getBtnHuy().addActionListener(this);
		view.getBtnThem().addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == view.getBtnHuy()) view.dispose();
		else if(e.getSource() == view.getBtnThem()) xuLyThemMoi();
	}
	
	private void xuLyThemMoi() {
		try {
			
			String tenPT = view.getTxtTenPT().getText();
			
			boolean kq =  phuongThucThanhToanDAO.insert(tenPT);
			
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
