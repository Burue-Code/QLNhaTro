package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JOptionPane;


import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.NhaTroDAO;
import com.nctu.quanlynhatro.view.nha_tro.ThemNhaTroView;


public class ThemNhaTroController implements ActionListener{
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
        if (e.getSource() == view.getBtnHuy()) view.dispose();
        else if (e.getSource() == view.getBtnThem()) xuLyThemmoi();
    }
    
    private void xuLyThemmoi() {
        // 1. Validate
        if (view.getTxtTenNha().getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chưa nhập tên nhà trọ!");
            return;
        }

        try {
            // 2. Get Data
            String tenNhaTro = view.getTxtTenNha().getText();
            String diaChi  = view.getTxtDiaChi().getText();
            int slPhong = Integer.parseInt(view.getTxtSoPhong().getText());
            String ghiChu = (String)view.getTxtGhiChu().getText();
            String trangThai = (String) view.getCbTrangThai().getSelectedItem();

            // 3. Call DAO INSERT
            boolean kq = nhaTroDAO.insert(tenNhaTro,slPhong,diaChi,ghiChu,trangThai);
            
            if (kq) {
                JOptionPane.showMessageDialog(view, "Thêm thành công!");
//                parentController.();
                view.dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Thất bại!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi nhập liệu: " + ex.getMessage());
        }
    }
}
