package com.nctu.quanlynhatro.controller;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.NhaTroDAO;
import com.nctu.quanlynhatro.dao.PhongDAO;
import com.nctu.quanlynhatro.dao.PhuPhiDAO;
import com.nctu.quanlynhatro.model.NhaTro;
import com.nctu.quanlynhatro.model.Phong;
import com.nctu.quanlynhatro.model.PhuPhi;
import com.nctu.quanlynhatro.view.component.MyPopupMenu;

import com.nctu.quanlynhatro.view.phong.ThemPhongView; // Vẫn dùng View cũ vì form giống nhau

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuaPhongController implements ActionListener {

    private ThemPhongView view;
    private PhongDAO phongDAO;
    private PhongController parentController;
    private Phong phongCanSua; // Đối tượng cần sửa
    private NhaTroDAO nhaTroDAO;
    private PhuPhiDAO phuPhiDAO;
    
    private JTable table;
    private DefaultTableModel model;
    
    private Map<String, Integer> mapNhaTro = new HashMap<>();
    private Map<String, Integer> mapPhuPhi = new HashMap<>();

    public SuaPhongController(ThemPhongView view, PhongController parentController, Phong phong) {
        this.view = view;
        this.table = view.getTblPhuPhi();
        this.model = view.getModelPhuPhi();
        this.parentController = parentController;
        this.phongCanSua = phong;
        this.phongDAO = new PhongDAO(DatabaseConnection.getConnection());

        // Setup giao diện cho việc SỬA
        this.view.setTitle("Cập Nhật Thông Tin Phòng: " + phong.getSoPhong());
        this.view.getBtnXacNhan().setText("Lưu Thay Đổi");
        // Khóa textbox số phòng nếu không muốn cho sửa số phòng (tùy nghiệp vụ)
        // view.getTxtSoPhong().setEditable(false); 

        initData();     // Load danh mục
        fillData(); // Đổ dữ liệu cũ vào form
        loadPhuPhiCu();
        initPopupMenu();
        addEvents();
        this.view.setVisible(true);
    }

    
    private void initPopupMenu() {
        MyPopupMenu popup = new MyPopupMenu(table);

      
        JMenuItem mnuXoa  = popup.addItem("Xóa Phiếu");
       
        mnuXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                model.removeRow(table.convertRowIndexToModel(row));
            }
        });

        
    }
    
    private void initData() {
        // --- Code load ComboBox giống nhau (Bạn có thể tách ra Utility nếu muốn) ---
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

    private void fillData() {
        // Đổ dữ liệu từ Object Phong vào các Textbox
        view.getTxtSoPhong().setText(String.valueOf(phongCanSua.getSoPhong()));
        view.getTxtGiaPhong().setText(String.valueOf(phongCanSua.getGia()));
        view.getTxtSoNguoi().setText(String.valueOf(phongCanSua.getSoNguoiToiDa()));
        view.getTxtPhuThu().setText(String.valueOf(phongCanSua.getPhuThu()));
        view.getTxtGhiChu().setText(phongCanSua.getGhiChu());
        view.getCboTrangThai().setSelectedItem(phongCanSua.getTrangThaiPhong());

        // Logic chọn đúng Nhà trọ cũ trên ComboBox
        // (Giả sử bạn có hàm lấy tên từ ID, hoặc loop map ngược lại)
        // view.getCboNhaTro().setSelectedItem(...);
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
        else if (e.getSource() == view.getBtnXacNhan()) xuLyCapNhat();
    }
    
    private void loadPhuPhiCu() {
        DefaultTableModel model = view.getModelPhuPhi();
        model.setRowCount(0);

        List<PhuPhi> ds = phuPhiDAO.getPhuPhiByMaPhong(phongCanSua.getMaPhong());

        for (PhuPhi pp : ds) {
            model.addRow(new Object[]{
                pp.getMaPP(),
                pp.getTenPP(),
                pp.getGia()
            });
        }
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

    private void xuLyCapNhat() {
        try {
            // 1. Get Data mới từ Form
            int soPhong = Integer.parseInt(view.getTxtSoPhong().getText());
            String tenNT = (String) view.getCboNhaTro().getSelectedItem();
            if (tenNT == null || !mapNhaTro.containsKey(tenNT)) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn nhà trọ!");
                return;
            }
            int maNT = mapNhaTro.get(tenNT);

            // 2. Check Trùng (Logic dành riêng cho Update)
            if (phongDAO.isSoPhongExistForUpdate(phongCanSua.getMaPhong(), soPhong, maNT)) {
                JOptionPane.showMessageDialog(view, "Số phòng đã tồn tại!");
                return;
            }

            // 3. Update dữ liệu vào Object Phong cũ
            phongCanSua.setSoPhong(soPhong);
            phongCanSua.setGia(Double.parseDouble(view.getTxtGiaPhong().getText()));
            phongCanSua.setSoNguoiToiDa(Integer.parseInt(view.getTxtSoNguoi().getText()));
            phongCanSua.setPhuThu(Double.parseDouble(view.getTxtPhuThu().getText()));
            phongCanSua.setGhiChu(view.getTxtGhiChu().getText());
            phongCanSua.setTrangThaiPhong((String) view.getCboTrangThai().getSelectedItem());
            
            // Cập nhật mã nhà trọ mới (nếu có thay đổi)
            NhaTro nt = new NhaTro();
            nt.setMaNT(maNT);
            phongCanSua.setNhaTro(nt);

            // 4. Lấy list phụ phí
            List<Integer> listMaPP = new ArrayList<>();
            DefaultTableModel model = view.getModelPhuPhi();
            for (int i = 0; i < model.getRowCount(); i++) 
                listMaPP.add(Integer.parseInt(model.getValueAt(i, 0).toString()));

            // 5. Call DAO UPDATE
            boolean kq = phongDAO.updatePhong(phongCanSua, listMaPP);

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