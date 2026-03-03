package com.nctu.quanlynhatro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.NhaTroDAO;
import com.nctu.quanlynhatro.model.NhaTro;
import com.nctu.quanlynhatro.view.nha_tro.ThemNhaTroView;

public class SuaNhaTroController implements ActionListener{
	private ThemNhaTroView view;
	private NhaTroDAO nhaTroDAO;
	private NhaTroController parentController;
	private NhaTro nhaTroCanSua;
	public SuaNhaTroController(ThemNhaTroView view, NhaTroController parentController,NhaTro nhaTro) {
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
        // Đổ dữ liệu từ Object Phong vào các Textbox
        view.getTxtTenNha().setText(nhaTroCanSua.getTenNT());
        view.getTxtSoPhong().setText(String.valueOf(nhaTroCanSua.getSLPhong()));
        view.getTxtDiaChi().setText(nhaTroCanSua.getDiaChi());
        view.getTxtGhiChu().setText(nhaTroCanSua.getGhiChu());
        view.getCbTrangThai().setSelectedItem(nhaTroCanSua.getTrangThaiNT());
    }
	
	private void addEvents() {
        view.getBtnThem().addActionListener(this);
        view.getBtnHuy().addActionListener(this);
    }
	@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBtnHuy()) view.dispose();
        else if (e.getSource() == view.getBtnThem()) xuLyCapNhat();
    }
    
    private void xuLyCapNhat() {
        try {
            // 2. Check Trùng (Logic dành riêng cho Update)
            if (nhaTroDAO.isTenNhaTroExistForUpdate((int)nhaTroCanSua.getMaNT(),nhaTroCanSua.getTenNT())) {
                JOptionPane.showMessageDialog(view, "Số phòng đã tồn tại!");
                return;
            }

            // 3. Update dữ liệu vào Object Phong cũ
            nhaTroCanSua.setTenNT(view.getTxtTenNha().getText());
            nhaTroCanSua.setSLPhong(Integer.parseInt(view.getTxtSoPhong().getText()));
            nhaTroCanSua.setDiaChi(view.getTxtDiaChi().getText());
            nhaTroCanSua.setGhiChu(view.getTxtGhiChu().getText());
            nhaTroCanSua.setTrangThaiNT((String) view.getCbTrangThai().getSelectedItem());
            

            // 5. Call DAO UPDATE
            boolean kq = nhaTroDAO.update(nhaTroCanSua);

            if (kq) {
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
//                parentController.refreshData();
                view.dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Lỗi khi cập nhật!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi nhập liệu: " + ex.getMessage());
        }
    }
}
