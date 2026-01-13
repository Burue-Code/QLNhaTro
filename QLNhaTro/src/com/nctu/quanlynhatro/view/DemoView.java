package com.nctu.quanlynhatro.view;
import com.nctu.quanlynhatro.view.component.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
public class DemoView extends JFrame {
    public DemoView() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // 1. Nhãn tiêu đề
        MyLabel lblTitle = new MyLabel("THÔNG TIN NHÂN VIÊN", MyLabel.HEADER);
        
        // 2. Ô nhập có hint
        MyTextField txtName = new MyTextField("Nhập họ và tên...");
//        txtName.setPreferredSize(new Dimension(200, 35));

        // 3. ComboBox
        String[] chucVu = {"Giám Đốc", "Trưởng Phòng", "Nhân Viên"};
        MyComboBox cboRole = new MyComboBox(chucVu,200,35);
//        cboRole.setPreferredSize(new Dimension(150, 35));

        // 4. Nút bấm
        MyButton btnSave = new MyButton("Lưu Dữ Liệu",250,35);

        
        // Nút màu khác (VD: màu đỏ cho nút Xóa)
        MyButton btnDelete = new MyButton("Xóa",100,35);
        btnDelete.setButtonColor(new Color(220, 53, 69)); // Màu đỏ
        // Add vào frame
        add(lblTitle);
        add(txtName);
        add(cboRole);
        add(btnSave);
        add(btnDelete);
        
        setSize(400, 400);
        setVisible(true);
    }
}
