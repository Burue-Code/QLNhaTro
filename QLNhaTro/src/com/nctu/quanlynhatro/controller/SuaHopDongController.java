package com.nctu.quanlynhatro.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.HopDongDAO;
import com.nctu.quanlynhatro.dao.KhachHangDAO;
import com.nctu.quanlynhatro.dao.NhaTroDAO;
import com.nctu.quanlynhatro.dao.PhongDAO;
import com.nctu.quanlynhatro.model.HopDong;
import com.nctu.quanlynhatro.model.KhachHang;
import com.nctu.quanlynhatro.model.Phong;
import com.nctu.quanlynhatro.view.hop_dong.ThemHopDongView;
import com.nctu.quanlynhatro.view.khach_hang.ThemKhachHangView;

public class SuaHopDongController {

	private ThemHopDongView view;
	private HopDongController parentController;
	private long maHDCur;

	private HopDongDAO hopDongDAO;
	private KhachHangDAO khachHangDAO;
	private NhaTroDAO nhaTroDAO;
	private PhongDAO phongDAO;

	private Map<String, Integer> mapNhaTro = new HashMap<>();
	private Map<String, Integer> mapPhong = new HashMap<>();

	private DefaultTableModel modelKH;
	private TableRowSorter<DefaultTableModel> sorterKH;

	private int maNhaTroCu;
	private int maPhongCu;
	private String tenPhongCu;

	public SuaHopDongController(ThemHopDongView view, HopDongController parentController, long maHD) {
		this.view = view;
		this.parentController = parentController;
		this.maHDCur = maHD;

		this.hopDongDAO = new HopDongDAO(DatabaseConnection.getConnection());
		this.khachHangDAO = new KhachHangDAO(DatabaseConnection.getConnection());
		this.nhaTroDAO = new NhaTroDAO(DatabaseConnection.getConnection());
		this.phongDAO = new PhongDAO(DatabaseConnection.getConnection());

		this.modelKH = view.getModelKH();

		this.view.setTitle("Cập Nhật Hợp Đồng - Mã: " + maHD);
		this.view.getBtnThem().setText("Lưu Thay Đổi"); // Nút btnXacNhan

		initBaseData();
		loadContractData();
		initSearch();
		initEvents();
	}

	// =================================================================
	// 1. INIT DATA (Tương đương LayDanhSachKH & LoadNhaTro)
	// =================================================================
	private void initBaseData() {
		modelKH.setRowCount(0);
		List<KhachHang> listKH = khachHangDAO.getAll();
		for (KhachHang kh : listKH) {
			modelKH.addRow(new Object[] { kh.getMaKH(), kh.getTenKH(), kh.getDiaChi(), kh.getGioiTinh() ? "Nữ" : "Nam",
					kh.getNgaySinh() != null ? kh.getNgaySinh().toString() : "" });
		}

		// Load ComboBox Nhà Trọ
		view.getCboNhaTro().removeAllItems();
		mapNhaTro.clear();
		Map<Integer, String> dataNhaTro = nhaTroDAO.getNhaTroConPhong();
		for (Map.Entry<Integer, String> entry : dataNhaTro.entrySet()) {
			view.getCboNhaTro().addItem(entry.getValue());
			mapNhaTro.put(entry.getValue(), entry.getKey());
		}
	}

	// =================================================================
	// 2. LOAD CONTRACT DATA (Tương đương LoadThongTin & LoadPhong &
	// LayDanhSachKHPT)
	// =================================================================
	private void loadContractData() {
		// Gọi DAO lấy chi tiết hợp đồng
		HopDong hd = hopDongDAO.getHopDongById(maHDCur);

		if (hd == null) {
			return;
		}
		if (hd.getDanhSachKhachHang() != null && !hd.getDanhSachKhachHang().isEmpty()) {
			KhachHang chuHo = hd.getDanhSachKhachHang().get(0);
			view.setTenKH(chuHo.getTenKH());
			view.setMaKH(String.valueOf(chuHo.getMaKH()));
		}

		// --- B. Đổ dữ liệu Ngày tháng & Tiền ---
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		if (hd.getNgayLap() != null) {
			view.getTxtNgayLap().setText(hd.getNgayLap().format(fmt));
		}
		if (hd.getNgayKetThuc() != null) {
			view.setNgayKetThuc(hd.getNgayKetThuc().format(fmt));
		}

		// Tính số tháng (Logic C# dùng DateTimePicker range, ở đây ta tính toán)
		if (hd.getNgayLap() != null && hd.getNgayKetThuc() != null) {
			try {
				long months = ChronoUnit.MONTHS.between(hd.getNgayLap().withDayOfMonth(1),
						hd.getNgayKetThuc().withDayOfMonth(1));
				view.getTxtSoThang().setText(String.valueOf(months));
			} catch (Exception e) {
				view.getTxtSoThang().setText("0");
			}
		}

		view.setSoNguoi(String.valueOf(hd.getSoNguoiO()));
		view.setGiaThue(String.valueOf((long) hd.getGiaThue()));
		view.setGhiChu(hd.getGhiChu());

		// --- C. Xử lý Phòng & Nhà Trọ (Logic LoadMaNT, LoadPhong) ---
		Phong p = hd.getPhong();
		if (p != null) {
			this.maPhongCu = (int) p.getMaPhong();
			this.tenPhongCu = String.valueOf(p.getSoPhong());

			if (p.getNhaTro() != null) {
				this.maNhaTroCu = (int) p.getNhaTro().getMaNT(); // _maNT

				// Chọn Nhà Trọ (cmbNhaTro.SelectedValue)
				String tenNhaTro = p.getNhaTro().getTenNT();
				view.getCboNhaTro().setSelectedItem(tenNhaTro);

				// Load Phòng (Tương đương LoadPhong trong C#)
				// Logic: Load các phòng trống + Phòng hiện tại đang thuê
				loadDataPhong(true);

				// Chọn Phòng cũ
				view.getCboPhong().setSelectedItem(tenPhongCu);
			}
		}

		// --- D. Danh sách Người ở ghép (Tương đương LayDanhSachKHPT) ---
		view.getModelPhuThuoc().setRowCount(0);
		long maChuHoID = -1;
		try {
			maChuHoID = Long.parseLong(view.getMaKH());
		} catch (Exception e) {
		}

		if (hd.getDanhSachKhachHang() != null) {
			for (KhachHang kh : hd.getDanhSachKhachHang()) {
				// Chỉ hiển thị người KHÔNG PHẢI là chủ hộ (phụ thuộc)
				if (kh.getMaKH() != maChuHoID) {
					view.getModelPhuThuoc()
							.addRow(new Object[] { kh.getMaKH(), kh.getTenKH(), kh.getDiaChi(),
									kh.getGioiTinh() ? "Nữ" : "Nam",
									kh.getNgaySinh() != null ? kh.getNgaySinh().toString() : "" });
				}
			}
		}

		// Tương đương hàm TinhSLNguoiO()
		capNhatSoLuongNguoi();
	}

	// Hàm phụ trợ load phòng (Thay thế LoadPhong trong C#)
	private void loadDataPhong(boolean includeCurrentRoom) {
		view.getCboNhaTro().setEnabled(false);
		try {
			view.getCboPhong().removeAllItems();
			mapPhong.clear();

			String selectedNT = (String) view.getCboNhaTro().getSelectedItem();
			if (selectedNT == null || !mapNhaTro.containsKey(selectedNT)) {
				return;
			}

			int maNT = mapNhaTro.get(selectedNT);
			// Lấy danh sách phòng trống từ DB
			Map<Integer, String> dataPhong = phongDAO.getPhongTrongByMaNT(maNT);

			// Nếu đang chọn Nhà cũ, phải thêm phòng cũ vào list (dù nó đang Bận)
			if (includeCurrentRoom && maNT == maNhaTroCu) {
				if (!dataPhong.containsKey(maPhongCu)) {
					dataPhong.put(maPhongCu, tenPhongCu);
				}
			}

			for (Map.Entry<Integer, String> entry : dataPhong.entrySet()) {
				view.getCboPhong().addItem(entry.getValue());
				mapPhong.put(entry.getValue(), entry.getKey());
			}
		} finally {
			view.getCboNhaTro().setEnabled(true);
		}
	}

	// =================================================================
	// 3. SỰ KIỆN (EVENTS)
	// =================================================================
	private void initEvents() {
		// Nút Hủy (btnHuy_Click)
		view.getBtnThoat().addActionListener(e -> {
			int dg = JOptionPane.showConfirmDialog(view, "Chấp Nhận Thoát?", "Thông Báo", JOptionPane.YES_NO_OPTION);
			if (dg == JOptionPane.YES_OPTION) {
				view.dispose();
			}
		});

		// Nút Xác Nhận (btnXacNhan_Click)
		view.getBtnThem().addActionListener(e -> xuLyCapNhat());

		// Sự kiện thay đổi ngày tháng -> Tính ngày KT
		view.getTxtSoThang().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				tinhNgayKT();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				tinhNgayKT();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				tinhNgayKT();
			}
		});

		view.getCboNhaTro().addActionListener(e -> {
			String selectedNT = (String) view.getCboNhaTro().getSelectedItem();
			if (selectedNT != null && mapNhaTro.containsKey(selectedNT)) {
				int maNTMoi = mapNhaTro.get(selectedNT);
				loadDataPhong(maNTMoi == maNhaTroCu);
			}
		});

		view.getCboPhong().addActionListener(e -> tinhGiaThue());

		view.getTblKhachHang().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// Chỉ xử lý khi click chuột trái
				if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
					int row = view.getTblKhachHang().getSelectedRow();
					if (row >= 0) {
						// 1. Lấy thông tin khách hàng MỚI vừa click vào
						String newMaKH = view.getTblKhachHang().getValueAt(row, 0).toString();
						String newTenKH = view.getTblKhachHang().getValueAt(row, 1).toString();

						// 2. Lấy thông tin khách hàng CŨ đang hiện trên form
						String currentMaKH = view.getMaKH();

						// 3. KIỂM TRA RÀNG BUỘC:
						// Nếu form đang có dữ liệu (không rỗng) VÀ Người mới khác Người cũ
						if (!currentMaKH.isEmpty() && !currentMaKH.equals(newMaKH)) {
							int confirm = JOptionPane.showConfirmDialog(view,
									"Bạn đang chọn khách hàng: " + view.getTenKH() + ".\n"
											+ "Bạn có chắc chắn muốn đổi sang: " + newTenKH + " không?\n"
											+ "Danh sách người ở ghép sẽ bị thay đổi!",
									"Xác nhận thay đổi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

							// Nếu người dùng chọn NO (Không đổi) -> Dừng lại ngay
							if (confirm != JOptionPane.YES_OPTION) {
								return;
							}
						}

						// 4. NẾU ĐƯỢC PHÉP -> THỰC HIỆN CẬP NHẬT DỮ LIỆU
						view.setMaKH(newMaKH);
						view.setTenKH(newTenKH);

						// Load lại danh sách phụ thuộc của người mới
						loadKhachHangPhu(Long.parseLong(newMaKH));
					}
				}
			}
		});
		view.getBtnThemKH().addActionListener(e -> {
			ThemKhachHangView themKhachHangView = new ThemKhachHangView(modelKH);
			themKhachHangView.setModal(true);
			new ThemKhachHangController(themKhachHangView, null);
			themKhachHangView.setVisible(true);
			initBaseData();
			loadContractData();
		});

		initPopupMenuKH();
	}

	// Logic tính ngày kết thúc
	private void tinhNgayKT() {
		try {
			String textThang = view.getSoThang();
			if (textThang.isEmpty()) {
				view.setNgayKetThuc("");
				return;
			}
			int soThang = Integer.parseInt(textThang);
			LocalDate ngayLap = LocalDate.parse(view.getNgayLap(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			view.setNgayKetThuc(ngayLap.plusMonths(soThang).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		} catch (Exception e) {
			view.setNgayKetThuc("");
		}
	}

	// Logic tính giá thuê (TinhGiaThue)
	private void tinhGiaThue() {
		try {
			String selectedPhong = (String) view.getCboPhong().getSelectedItem();
			if (selectedPhong == null || !mapPhong.containsKey(selectedPhong)) {
				return;
			}

			Phong p = phongDAO.getThongTinPhong(mapPhong.get(selectedPhong));
			if (p != null) {
				double tongTien = p.getGia();
				int slNguoiO = 0;
				try {
					slNguoiO = Integer.parseInt(view.getSoNguoi());
				} catch (Exception e) {
				}

				if (slNguoiO > p.getSoNguoiToiDa()) {
					tongTien += (slNguoiO - p.getSoNguoiToiDa()) * p.getPhuThu();
				}
				view.setGiaThue(String.valueOf((long) tongTien));
			}
		} catch (Exception ex) {
		}
	}

	// Logic load khách phụ (LayDanhSachKHPT)
	private void loadKhachHangPhu(long maKHChinh) {
		DefaultTableModel model = view.getModelPhuThuoc();
		model.setRowCount(0);
		for (KhachHang kh : khachHangDAO.getKhachHangPhu(maKHChinh)) {
			model.addRow(new Object[] { kh.getMaKH(), kh.getTenKH(), kh.getDiaChi(), kh.getGioiTinh() ? "Nữ" : "Nam",
					kh.getNgaySinh() != null ? kh.getNgaySinh().toString() : "" });
		}
		capNhatSoLuongNguoi();
	}

	// Logic tính số lượng (TinhSLNguoiO)
	private void capNhatSoLuongNguoi() {
		view.setSoNguoi(String.valueOf(1 + view.getModelPhuThuoc().getRowCount()));
		tinhGiaThue();
	}

	// Tìm kiếm (txtNVPhuThuoc_TextChanged)
	private void initSearch() {
		sorterKH = new TableRowSorter<>(modelKH);
		view.getTblKhachHang().setRowSorter(sorterKH);
		view.getTxtTimKiem().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = view.getTxtTimKiem().getText();
				sorterKH.setRowFilter(text.isBlank() ? null : RowFilter.regexFilter("(?i)" + text));
			}
		});
	}

	// Menu chuột phải (ToolStripMenuIThemPhuThuoc & toolStripMenulXoa)
	private void initPopupMenuKH() {
		// --- Menu Thêm ---
		JPopupMenu popupAdd = new JPopupMenu();
		JMenuItem mnuAdd = new JMenuItem("Thêm vào danh sách ở ghép");
		popupAdd.add(mnuAdd);

		view.getTblKhachHang().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger()) {
					int row = view.getTblKhachHang().rowAtPoint(e.getPoint());
					if (row >= 0) {
						view.getTblKhachHang().setRowSelectionInterval(row, row);
						popupAdd.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});

		mnuAdd.addActionListener(e -> {
			int row = view.getTblKhachHang().getSelectedRow();
			if (row >= 0) {
				String maKH = view.getTblKhachHang().getValueAt(row, 0).toString();
				if (maKH.equals(view.getMaKH())) {
					JOptionPane.showMessageDialog(view, "Khách Hàng đã là khách hàng chính!", "Thông Báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				DefaultTableModel model = view.getModelPhuThuoc();
				for (int i = 0; i < model.getRowCount(); i++) {
					if (maKH.equals(model.getValueAt(i, 0).toString())) {
						JOptionPane.showMessageDialog(view, "Khách Hàng đã tồn tại!", "Thông Báo",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				Object[] rowData = new Object[5];
				for (int i = 0; i < 5; i++) {
					rowData[i] = view.getTblKhachHang().getValueAt(row, i);
				}
				model.addRow(rowData);
				capNhatSoLuongNguoi();
			}
		});

		// --- Menu Xóa ---
		JPopupMenu popupRemove = new JPopupMenu();
		JMenuItem mnuRemove = new JMenuItem("Xóa khỏi danh sách");
		popupRemove.add(mnuRemove);

		view.getTblKHPhuThuoc().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger()) {
					int row = view.getTblKHPhuThuoc().rowAtPoint(e.getPoint());
					if (row >= 0) {
						view.getTblKHPhuThuoc().setRowSelectionInterval(row, row);
						popupRemove.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});

		mnuRemove.addActionListener(e -> {
			int row = view.getTblKHPhuThuoc().getSelectedRow();
			if (row >= 0) {
				view.getModelPhuThuoc().removeRow(row);
				capNhatSoLuongNguoi();
			}
		});
	}

	// =================================================================
	// 4. XỬ LÝ LƯU (Update_HopDong)
	// =================================================================
	private void xuLyCapNhat() {
		try {
			// Lấy dữ liệu
			String maKHStr = view.getMaKH();
			String selectedPhong = (String) view.getCboPhong().getSelectedItem();

			if (maKHStr.isEmpty() || selectedPhong == null) {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng và phòng!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			long maKHChinh = Long.parseLong(maKHStr);
			int maPhongMoi = mapPhong.get(selectedPhong);

			LocalDate ngayBD = LocalDate.parse(view.getNgayLap(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			LocalDate ngayKT = LocalDate.parse(view.getNgayKetThuc(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			double giaThue = Double.parseDouble(view.getGiaThue());
			int soNguoiO = Integer.parseInt(view.getSoNguoi());
			String ghiChu = view.getGhiChu();

			List<Long> listMaKHPhu = new ArrayList<>();
			DefaultTableModel model = view.getModelPhuThuoc();
			for (int i = 0; i < model.getRowCount(); i++) {
				listMaKHPhu.add(Long.parseLong(model.getValueAt(i, 0).toString()));
			}

			int confirm = JOptionPane.showConfirmDialog(view, "Chấp Nhận Sửa Hơp Đồng?", "Thông Báo",
					JOptionPane.YES_NO_OPTION);
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}

			// Gọi DAO (Tương đương transaction trong C#)
			String kq = hopDongDAO.updateHopDong(maHDCur, maKHChinh, maPhongMoi, maPhongCu, ngayBD, ngayKT, giaThue,
					soNguoiO, ghiChu, listMaKHPhu);

			if (kq.equals("SUCCESS")) {
				JOptionPane.showMessageDialog(view, "Cập nhật hợp đồng thành công!", "Thông Báo",
						JOptionPane.INFORMATION_MESSAGE);
				if (parentController != null) {
					parentController.refreshData();
				}
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, kq, "Lỗi cập nhật", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
}