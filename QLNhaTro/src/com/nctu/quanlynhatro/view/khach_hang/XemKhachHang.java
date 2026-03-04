package com.nctu.quanlynhatro.view.khach_hang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class XemKhachHang extends JDialog {

    // ====== Text fields (chỉ hiển thị) ======
    private JTextField txtTenKH;
    private JTextField txtNgaySinh;
    private JTextField txtGioiTinh;
    private JTextField txtSDT;
    private JTextField txtDiaChi;

    private JTextField txtCCCD;
    private JTextField txtGmail;
    private JTextField txtOPHong;
    private JTextField txtThuocHopDong;
    private JTextField txtTenKhachHangChinh;

    // ====== Table ======
    private JTable tblKhachHangPhuThuoc;
    private DefaultTableModel modelPhuThuoc;

    // ====== Buttons ======
    private JButton btnThoat;

    public XemKhachHang(Frame owner) {
        super(owner, "Xem Hồ Sơ Khách Hàng", true);

        setSize(920, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // ============================================================
        // TOP: KHU VỰC THÔNG TIN (giống bố cục ảnh)
        // ============================================================
        JPanel pnlTop = new JPanel(new GridBagLayout());
        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.fill = GridBagConstraints.BOTH;
        gbcTop.weightx = 1.0;
        gbcTop.weighty = 0;

        JPanel colLeft = buildLeftColumn();
        JPanel colRight = buildRightColumn();

        gbcTop.gridx = 0;
        gbcTop.gridy = 0;
        gbcTop.insets = new Insets(0, 0, 0, 0);
        pnlTop.add(colLeft, gbcTop);

        gbcTop.gridx = 1;
        gbcTop.gridy = 0;
        gbcTop.insets = new Insets(0, 20, 0, 0); // khoảng cách giữa 2 cột
        pnlTop.add(colRight, gbcTop);

        contentPane.add(pnlTop, BorderLayout.NORTH);

        // ============================================================
        // CENTER: BẢNG "Khách Hàng Phụ Thuộc"
        // ============================================================
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBorder(new TitledBorder("Khách Hàng Phụ Thuộc"));

        String[] headers = {"MaKH", "Tên Khách Hàng", "Địa Chỉ", "Giới Tính", "Ngày Sinh", "Số Điện Thoại"};
        modelPhuThuoc = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblKhachHangPhuThuoc = new JTable(modelPhuThuoc);
        tblKhachHangPhuThuoc.setRowHeight(26);
        tblKhachHangPhuThuoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKhachHangPhuThuoc.setEnabled(false); // chỉ xem, không chọn/click

        JScrollPane sp = new JScrollPane(tblKhachHangPhuThuoc);
        pnlCenter.add(sp, BorderLayout.CENTER);

        contentPane.add(pnlCenter, BorderLayout.CENTER);

        // ============================================================
        // SOUTH: NÚT (Thoát - Sửa - Xóa) căn phải
        // ============================================================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

        btnThoat = new JButton("Thoát");


        Dimension btnSize = new Dimension(110, 35);
        btnThoat.setPreferredSize(btnSize);

        pnlBottom.add(btnThoat);

        contentPane.add(pnlBottom, BorderLayout.SOUTH);

        // Thoát
        btnThoat.addActionListener(e -> dispose());
    }

    // ======= Cột trái: TenKH / NgaySinh+GioiTinh / SDT / DiaChi =======
    private JPanel buildLeftColumn() {
        JPanel pnl = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = baseGbc();

        // Row 0: Tên Khách Hàng (full)
        gbc.gridy = 0;
        pnl.add(labelAbove("Tên Khách Hàng"), gbc);
        gbc.gridy = 1;
        txtTenKH = viewOnlyField();
        pnl.add(txtTenKH, gbc);

        // Row 1: Ngày Sinh (left) + Giới Tính (right)
        gbc.gridy = 2;
        gbc.insets = new Insets(16, 0, 0, 0);
        JPanel row2 = new JPanel(new GridLayout(1, 2, 20, 0));
        row2.add(stack("Ngày Sinh", txtNgaySinh = viewOnlyField()));
        row2.add(stack("Giới Tính", txtGioiTinh = viewOnlyField()));
        pnl.add(row2, gbc);

        // Row 2: Số Điện Thoại (full)
        gbc.gridy = 3;
        gbc.insets = new Insets(16, 0, 0, 0);
        pnl.add(labelAbove("Số Điện Thoại"), gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(6, 0, 0, 0);
        txtSDT = viewOnlyField();
        pnl.add(txtSDT, gbc);

        // Row 3: Địa Chỉ (full)
        gbc.gridy = 5;
        gbc.insets = new Insets(16, 0, 0, 0);
        pnl.add(labelAbove("Địa Chỉ"), gbc);
        gbc.gridy = 6;
        gbc.insets = new Insets(6, 0, 0, 0);
        txtDiaChi = viewOnlyField();
        pnl.add(txtDiaChi, gbc);

        return pnl;
    }

    // ======= Cột phải: CCCD / Gmail / OPhong+ThuocHopDong / TenKHChinh =======
    private JPanel buildRightColumn() {
        JPanel pnl = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = baseGbc();

        // Row 0: CCCD (full)
        gbc.gridy = 0;
        pnl.add(labelAbove("Căn Cước Công Dân"), gbc);
        gbc.gridy = 1;
        txtCCCD = viewOnlyField();
        pnl.add(txtCCCD, gbc);

        // Row 1: Gmail (full)
        gbc.gridy = 2;
        gbc.insets = new Insets(16, 0, 0, 0);
        pnl.add(labelAbove("Gmail"), gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(6, 0, 0, 0);
        txtGmail = viewOnlyField();
        pnl.add(txtGmail, gbc);

        // Row 2: Ở Phòng (left) + Thuộc Hợp Đồng (right)
        gbc.gridy = 4;
        gbc.insets = new Insets(16, 0, 0, 0);
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.add(stack("Ở Phòng", txtOPHong = viewOnlyField()));
        row.add(stack("Thuộc Hợp Đồng", txtThuocHopDong = viewOnlyField()));
        pnl.add(row, gbc);

        // Row 3: Tên KH chính (full)
        gbc.gridy = 5;
        gbc.insets = new Insets(16, 0, 0, 0);
        pnl.add(labelAbove("Tên Khách Hàng Chính"), gbc);
        gbc.gridy = 6;
        gbc.insets = new Insets(6, 0, 0, 0);
        txtTenKhachHangChinh = viewOnlyField();
        pnl.add(txtTenKhachHangChinh, gbc);

        return pnl;
    }

    // ===== Helpers =====
    private GridBagConstraints baseGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        return gbc;
    }

    private JLabel labelAbove(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setBorder(new EmptyBorder(0, 2, 4, 0));
        return lbl;
    }

    private JPanel stack(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.add(new JLabel(label), BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JTextField viewOnlyField() {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(0, 32));
        tf.setEditable(false);
        tf.setFocusable(false);              // không focus được -> đúng kiểu "chỉ xem"
        tf.setBackground(new Color(240, 240, 240)); // giống kiểu read-only trong Thêm/Sửa của bạn
        return tf;
    }

    // ===== MAIN để chạy thử UI =====
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            } catch (Exception ignored) {}
//
//            XemKhachHang dlg = new XemKhachHang(null);
//            dlg.setVisible(true);
//        });
//    }
}