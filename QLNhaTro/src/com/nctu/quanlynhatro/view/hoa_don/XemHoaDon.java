package com.nctu.quanlynhatro.view.hoa_don;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * XemHoaDonView - CHỈ XEM (không cho nhập/chỉnh sửa)
 * - Không có nút In
 * - Ngày thanh toán để trống
 * - Hóa đơn điện nước / phương thức / loại thanh toán: textbox hiển thị (readonly)
 * - Bảng phụ phí + điện nước: không edit, không chọn
 * - main() chỉ mở 1 form
 */
public class XemHoaDon extends JDialog {

    // --- KHU VỰC TRÁI ---
    private JTextField txtTenKH, txtNgayThanhToan;
    private JTextField txtMaHopDong, txtNhaTro, txtPhong;
    private JTextField txtGiaThue, txtGhiChu;

    // Hiển thị dạng textbox
    private JTextField txtHoaDonDienNuoc;
    private JTextField txtPhuongThucThanhToan;
    private JTextField txtLoaiThanhToan;

    // --- KHU VỰC PHẢI ---
    private JTable tblPhuPhi, tblDienNuoc;
    private DefaultTableModel modelPhuPhi, modelDienNuoc;

    // --- FOOTER ---
    private JTextField txtTongTienDN, txtTongTienPhuPhi, txtTongThanhToan;
    private JButton btnDong;

    public XemHoaDon(Window owner) {
        super(owner, "Xem Hóa Đơn", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // =========================================================
        // 1) CENTER: 2 CỘT
        // =========================================================
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 20, 0));

        // ---------- LEFT FORM ----------
        JPanel pnlLeftForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 1
        addLabel(pnlLeftForm, "Tên Khách Hàng:", 0, 0);
        addLabel(pnlLeftForm, "Ngày Thanh Toán:", 1, 0);

        txtTenKH = createTextField();
        addComponent(pnlLeftForm, txtTenKH, 0, 1);

        txtNgayThanhToan = createTextField();
        txtNgayThanhToan.setText(""); // không tự đổ ngày
        addComponent(pnlLeftForm, txtNgayThanhToan, 1, 1);

        // Row 2
        addLabel(pnlLeftForm, "Mã Hợp Đồng:", 0, 2);
        addLabel(pnlLeftForm, "Hóa Đơn Điện Nước:", 1, 2);

        txtMaHopDong = createTextField();
        addComponent(pnlLeftForm, txtMaHopDong, 0, 3);

        txtHoaDonDienNuoc = createTextField(); // textbox hiển thị
        addComponent(pnlLeftForm, txtHoaDonDienNuoc, 1, 3);

        // Row 3
        addLabel(pnlLeftForm, "Nhà Trọ:", 0, 4);
        addLabel(pnlLeftForm, "Giá Thuê:", 1, 4);

        txtNhaTro = createTextField();
        addComponent(pnlLeftForm, txtNhaTro, 0, 5);

        txtGiaThue = createTextField();
        addComponent(pnlLeftForm, txtGiaThue, 1, 5);

        // Row 4
        addLabel(pnlLeftForm, "Phòng:", 0, 6);
        addLabel(pnlLeftForm, "Ghi Chú:", 1, 6);

        txtPhong = createTextField();
        addComponent(pnlLeftForm, txtPhong, 0, 7);

        txtGhiChu = createTextField();
        addComponent(pnlLeftForm, txtGhiChu, 1, 7);

        // Row 5
        addLabel(pnlLeftForm, "Phương Thức Thanh Toán:", 0, 8);
        addLabel(pnlLeftForm, "Loại Thanh Toán:", 1, 8);

        txtPhuongThucThanhToan = createTextField(); // textbox hiển thị
        addComponent(pnlLeftForm, txtPhuongThucThanhToan, 0, 9);

        txtLoaiThanhToan = createTextField(); // textbox hiển thị
        addComponent(pnlLeftForm, txtLoaiThanhToan, 1, 9);

        // push content up
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.weighty = 1.0;
        pnlLeftForm.add(new JLabel(), gbc);

        // ---------- RIGHT TABLES ----------
        JPanel pnlRightTables = new JPanel(new GridLayout(2, 1, 0, 10));

        // Phụ phí
        JPanel pnlPhuPhi = new JPanel(new BorderLayout());
        pnlPhuPhi.setBorder(new TitledBorder("Phụ Phí"));
        String[] colsPhuPhi = { "MaPP", "Tên Phụ Phí", "Giá" };
        modelPhuPhi = new DefaultTableModel(colsPhuPhi, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // khóa edit
            }
        };
        tblPhuPhi = new JTable(modelPhuPhi);
        tblPhuPhi.setRowHeight(25);
        tblPhuPhi.setPreferredScrollableViewportSize(new Dimension(450, 100));
        tblPhuPhi.setRowSelectionAllowed(false);
        tblPhuPhi.setCellSelectionEnabled(false);
        pnlPhuPhi.add(new JScrollPane(tblPhuPhi), BorderLayout.CENTER);

        // Điện nước
        JPanel pnlDienNuoc = new JPanel(new BorderLayout());
        pnlDienNuoc.setBorder(new TitledBorder("Hóa Đơn Điện Nước"));
        String[] colsDN = { "MaDN", "Thời Gian", "Giá" };
        modelDienNuoc = new DefaultTableModel(colsDN, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // khóa edit
            }
        };
        tblDienNuoc = new JTable(modelDienNuoc);
        tblDienNuoc.setRowHeight(25);
        tblDienNuoc.setPreferredScrollableViewportSize(new Dimension(450, 100));
        tblDienNuoc.setRowSelectionAllowed(false);
        tblDienNuoc.setCellSelectionEnabled(false);
        pnlDienNuoc.add(new JScrollPane(tblDienNuoc), BorderLayout.CENTER);

        pnlRightTables.add(pnlPhuPhi);
        pnlRightTables.add(pnlDienNuoc);

        pnlCenter.add(pnlLeftForm);
        pnlCenter.add(pnlRightTables);
        contentPane.add(pnlCenter, BorderLayout.CENTER);

        // =========================================================
        // 2) SOUTH: FOOTER
        // =========================================================
        JPanel pnlFooter = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlFooter.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel pnlTotalContainer = new JPanel(new BorderLayout());
        pnlTotalContainer.setBorder(new TitledBorder("Tổng Tiền"));

        JPanel pnlTotalFields = new JPanel(new GridLayout(3, 1, 5, 5));
        txtTongTienDN = createTotalField();
        txtTongTienPhuPhi = createTotalField();
        txtTongThanhToan = createTotalField();

        pnlTotalFields.add(createLabeledPanel("Tổng Tiền Điện Nước:", txtTongTienDN));
        pnlTotalFields.add(createLabeledPanel("Tổng Tiền Phụ Phí:", txtTongTienPhuPhi));
        pnlTotalFields.add(createLabeledPanel("Tổng Tiền Thanh Toán:", txtTongThanhToan));

        pnlTotalContainer.add(pnlTotalFields, BorderLayout.CENTER);
        pnlFooter.add(pnlTotalContainer);

        JPanel pnlButtonsContainer = new JPanel(new BorderLayout());
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        btnDong = new JButton("Đóng");
        btnDong.setPreferredSize(new Dimension(100, 40));
        pnlButtons.add(btnDong);

        pnlButtonsContainer.add(pnlButtons, BorderLayout.SOUTH);
        pnlFooter.add(pnlButtonsContainer);

        contentPane.add(pnlFooter, BorderLayout.SOUTH);

        // Events
        btnDong.addActionListener(e -> dispose());

        // KHÓA TOÀN BỘ INPUT -> CHỈ XEM
        setViewOnly();

        pack();
        setLocationRelativeTo(owner);
    }

    // ---------------- CHỈ XEM ----------------
    private void setViewOnly() {
        JTextField[] fields = {
                txtTenKH, txtNgayThanhToan, txtMaHopDong, txtNhaTro, txtPhong,
                txtGiaThue, txtGhiChu, txtHoaDonDienNuoc, txtPhuongThucThanhToan, txtLoaiThanhToan,
                txtTongTienDN, txtTongTienPhuPhi, txtTongThanhToan
        };
        for (JTextField f : fields) {
            f.setEditable(false);
            // nếu bạn muốn không focus luôn thì bật dòng dưới:
            // f.setFocusable(false);
        }

        // Chắc chắn JTable không tương tác
        tblPhuPhi.setEnabled(false);
        tblDienNuoc.setEnabled(false);
    }

    // ---------------- Helper UI ----------------
    private void addLabel(JPanel panel, String text, int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 0, 5);
        panel.add(new JLabel(text), gbc);
    }

    private void addComponent(JPanel panel, JComponent comp, int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 5, 10, 5);
        panel.add(comp, gbc);
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(0, 30));
        return txt;
    }

    private JTextField createTotalField() {
        JTextField txt = new JTextField("0");
        txt.setEditable(false);
        txt.setBackground(new Color(230, 230, 230));
        txt.setForeground(Color.RED);
        txt.setHorizontalAlignment(JTextField.RIGHT);
        txt.setFont(new Font("Arial", Font.BOLD, 14));
        txt.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return txt;
    }

    private JPanel createLabeledPanel(String label, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout(5, 0));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(150, 0));
        p.add(lbl, BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    // ---------------- Main: chỉ mở 1 form ----------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            // Chỉ mở đúng 1 dialog, không tạo JFrame demo nữa
            XemHoaDon dlg = new XemHoaDon(null);
            dlg.setVisible(true);
        });
    }
}