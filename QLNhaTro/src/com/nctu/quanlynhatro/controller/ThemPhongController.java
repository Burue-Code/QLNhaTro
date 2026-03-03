package com.nctu.quanlynhatro.controller;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.NhaTroDAO;
import com.nctu.quanlynhatro.dao.PhongDAO;
import com.nctu.quanlynhatro.dao.PhuPhiDAO;
import com.nctu.quanlynhatro.view.phong.ThemPhongView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemPhongController implements ActionListener {

    private ThemPhongView view;
    private PhongDAO phongDAO;
    private PhongController parentController; 
    private NhaTroDAO nhaTroDAO;
    private PhuPhiDAO phuPhiDAO;

    // Map lưu ID cho ComboBox
    private Map<String, Integer> mapNhaTro = new HashMap<>();
    private Map<String, Integer> mapPhuPhi = new HashMap<>();

    public ThemPhongController(ThemPhongView view, PhongController parentController) {
        this.view = view;
        this.parentController = parentController;
        this.phongDAO = new PhongDAO(DatabaseConnection.getConnection());
        
        // Setup giao diện cho việc THÊM
        this.view.setTitle("Thêm Phòng Mới");
        this.view.getBtnXacNhan().setText("Thêm Mới");

        initData();
        addEvents();
        this.view.setVisible(true);
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

    private void addEvents() {
        view.getBtnXacNhan().addActionListener(this);
        view.getBtnThoat().addActionListener(this);
        view.getBtnThemPhuPhi().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBtnThoat()) view.dispose();
        else if (e.getSource() == view.getBtnThemPhuPhi()) xuLyThemPhuPhi();
        else if (e.getSource() == view.getBtnXacNhan()) xuLyThemmoi();
    }

    private void xuLyThemPhuPhi() {
        String tenPP = (String) view.getCboPhuPhi().getSelectedItem();
        if (tenPP == null) return;
        
        Integer maPP = mapPhuPhi.get(tenPP);
        if (maPP == null) {
            JOptionPane.showMessageDialog(view, "Không tìm thấy phụ phí!");
            return;
        }
        DefaultTableModel model = view.getModelPhuPhi();
        
        // Check trùng trên bảng
        for (int i = 0; i < model.getRowCount(); i++) {
            Object cell = model.getValueAt(i, 0); // cột MaPP
            if (cell != null && Integer.parseInt(cell.toString()) == maPP) {
                JOptionPane.showMessageDialog(view, "Phụ phí đã tồn tại!");
                return;
            }
        }
        String soTien = phuPhiDAO.getSoTienByMaPP(maPP);
        
        model.addRow(new Object[]{
                maPP,
                tenPP,
                soTien
            });
    }

    private void xuLyThemmoi() {
        // 1. Validate
        if (view.getTxtSoPhong().getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chưa nhập số phòng!");
            return;
        }

        try {
            // 2. Get Data
            int soPhong = Integer.parseInt(view.getTxtSoPhong().getText());
            double gia = Double.parseDouble(view.getTxtGiaPhong().getText());
            int slNguoi = Integer.parseInt(view.getTxtSoNguoi().getText());
            double phuThu = Double.parseDouble(view.getTxtPhuThu().getText());
            String ghiChu = view.getTxtGhiChu().getText();
            String trangThai = (String) view.getCboTrangThai().getSelectedItem();
            int maNT = mapNhaTro.get(view.getCboNhaTro().getSelectedItem());

            List<Integer> listMaPP = new ArrayList<>();
            DefaultTableModel model = view.getModelPhuPhi();
            for (int i = 0; i < model.getRowCount(); i++) 
                listMaPP.add(Integer.parseInt(model.getValueAt(i, 0).toString()));

            // 3. Call DAO INSERT
            boolean kq = phongDAO.insertPhong(soPhong, gia, slNguoi, phuThu, trangThai, ghiChu, maNT, listMaPP);
            
            if (kq) {
                JOptionPane.showMessageDialog(view, "Thêm thành công!");
//                parentController.();
                view.dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Thất bại! Có thể trùng số phòng.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi nhập liệu: " + ex.getMessage());
        }
    }
}