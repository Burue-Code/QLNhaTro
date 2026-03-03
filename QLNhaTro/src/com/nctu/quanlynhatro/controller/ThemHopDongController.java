package com.nctu.quanlynhatro.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.nctu.quanlynhatro.model.KhachHang;
import com.nctu.quanlynhatro.model.Phong;
import com.nctu.quanlynhatro.view.hop_dong.ThemHopDongView;
import com.nctu.quanlynhatro.view.khach_hang.ThemKhachHangView;

public class ThemHopDongController {

	private ThemHopDongView view;
	private HopDongController parentController;

	private HopDongDAO hopDongDAO;
	private KhachHangDAO khachHangDAO;
	private NhaTroDAO nhaTroDAO;
	private PhongDAO phongDAO;

	// Map lưu ID cho ComboBox
	private Map<String, Integer> mapNhaTro = new HashMap<>();
	private Map<String, Integer> mapPhong = new HashMap<>();

	private DefaultTableModel modelKH;
	private TableRowSorter<DefaultTableModel> sorterKH;

	public ThemHopDongController(ThemHopDongView view, HopDongController parentController) {
		this.view = view;
		this.parentController = parentController;

		// Khởi tạo các DAO kết nối Database
		this.hopDongDAO = new HopDongDAO(DatabaseConnection.getConnection());
		this.khachHangDAO = new KhachHangDAO(DatabaseConnection.getConnection());

		this.modelKH = view.getModelKH();

		initData();
		initSearch();
		initEvents();
	}

	// =================================================================
	// 1. KHỞI TẠO DỮ LIỆU
	// =================================================================
	private void initData() {
		// Load danh sách khách hàng lên bảng tìm kiếm
		modelKH.setRowCount(0);
		List<KhachHang> listKH = khachHangDAO.getAll();
		for (KhachHang kh : listKH) {
			modelKH.addRow(new Object[] { kh.getMaKH(), kh.getTenKH(), kh.getDiaChi(), kh.getGioiTinh() ? "Nữ" : "Nam", // false=Nam,
																														// true=Nữ
					kh.getNgaySinh() != null ? kh.getNgaySinh().toString() : "" });
		}

		nhaTroDAO = new NhaTroDAO(DatabaseConnection.getConnection());
		phongDAO = new PhongDAO(DatabaseConnection.getConnection());

		view.getCboNhaTro().removeAllItems();
		mapNhaTro.clear();

		Map<Integer, String> dataNhaTro = nhaTroDAO.getNhaTroConPhong();

		for (Map.Entry<Integer, String> entry : dataNhaTro.entrySet()) {
			int maNT = entry.getKey();
			String tenNT = entry.getValue();

			view.getCboNhaTro().addItem(tenNT);
			mapNhaTro.put(tenNT, maNT);
		}

		loadDataPhong();
	}

	private void loadDataPhong() {
		// 1. Tạm thời tắt sự kiện để không bị đụng độ khi đang load dữ liệu
		view.getCboNhaTro().setEnabled(false);

		try {
			// Xóa sạch dữ liệu cũ của ComboBox Phòng
			view.getCboPhong().removeAllItems();
			mapPhong.clear();

			// Lấy tên Nhà trọ đang được chọn
			String selectedNT = (String) view.getCboNhaTro().getSelectedItem();

			// KIỂM TRA ĐIỀU KIỆN KÉP (Tránh lỗi NullPointerException)
			if (selectedNT == null || selectedNT.trim().isEmpty() || !mapNhaTro.containsKey(selectedNT)) {
				return; // Thoát ngay nếu dữ liệu không hợp lệ
			}

			// Lấy Mã Nhà Trọ từ mapNhaTro
			int maNT = mapNhaTro.get(selectedNT);

			// Gọi DAO lấy danh sách Phòng trống của nhà trọ đó
			Map<Integer, String> dataPhong = phongDAO.getPhongTrongByMaNT(maNT);

			// Đổ lên giao diện
			for (Map.Entry<Integer, String> entry : dataPhong.entrySet()) {
				int maPhong = entry.getKey();
				String soPhong = entry.getValue();

				view.getCboPhong().addItem(soPhong);
				mapPhong.put(soPhong, maPhong);
			}
		} finally {
			// 2. Mở khóa lại sự kiện sau khi load xong
			view.getCboNhaTro().setEnabled(true);
		}
	}

	// =================================================================
	// 2. KHỞI TẠO SỰ KIỆN (EVENTS)
	// =================================================================
	private void initEvents() {
		// Nút Thoát
		view.getBtnThoat().addActionListener(e -> view.dispose());

		// Nút Thêm Hợp Đồng
		view.getBtnThem().addActionListener(e -> xuLyThemHopDong());

		// Bắt sự kiện người dùng GÕ số tháng -> Tự động tính ngày kết thúc
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
			loadDataPhong();
		});

		view.getTblKhachHang().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// Chỉ xử lý khi click chuột trái (nút 1)
				if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
					int row = view.getTblKhachHang().getSelectedRow();
					if (row >= 0) {
						// Lấy Mã KH từ cột đầu tiên (cột 0)
						long maKH = Long.parseLong(view.getTblKhachHang().getValueAt(row, 0).toString());

						// GỌI HÀM LOAD KHÁCH PHỤ Ở ĐÂY:
						loadKhachHangPhu(maKH);
					}
				}
			}
		});

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

		view.getCboPhong().addActionListener(e -> {
			tinhGiaThue();
		});

		view.getBtnThemKH().addActionListener(e -> {
			ThemKhachHangView themKhachHangView = new ThemKhachHangView(modelKH);
			themKhachHangView.setModal(true);
			new ThemKhachHangController(themKhachHangView, null);
			themKhachHangView.setVisible(true);
			initData();
		});
		// Tính toán ngay lần đầu mở form (nếu có giá trị mặc định)
		tinhNgayKT();
		initPopupMenuKH();
	}

	// =================================================================
	// 3. XỬ LÝ LOGIC NGHIỆP VỤ
	// =================================================================

	// Tự động cộng tháng để ra ngày kết thúc hợp đồng
	private void tinhNgayKT() {
		try {
			String textThang = view.getSoThang();
			if (textThang.isEmpty()) {
				view.setNgayKetThuc("");
				return;
			}

			int soThang = Integer.parseInt(textThang);
			LocalDate ngayLap = LocalDate.parse(view.getNgayLap(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			LocalDate ngayKT = ngayLap.plusMonths(soThang);

			view.setNgayKetThuc(ngayKT.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		} catch (Exception e) {
			// Nhập sai định dạng chữ hoặc lỗi parse thì để trống
			view.setNgayKetThuc("");
		}
	}

	private void loadKhachHangPhu(long maKHChinh) {
		DefaultTableModel modelPhuThuoc = view.getModelPhuThuoc();
		modelPhuThuoc.setRowCount(0);

		List<KhachHang> listKHPhu = khachHangDAO.getKhachHangPhu(maKHChinh);

		for (KhachHang kh : listKHPhu) {
			modelPhuThuoc.addRow(new Object[] { kh.getMaKH(), kh.getTenKH(), kh.getDiaChi(),
					kh.getGioiTinh() ? "Nữ" : "Nam", kh.getNgaySinh() != null ? kh.getNgaySinh().toString() : "" });
		}
		capNhatSoLuongNguoi();
	}

	private void initPopupMenuKH() {
		// -------------------------------------------------------------
		// 1. MENU BẢNG TRÊN: THÊM VÀO BẢNG DƯỚI
		// -------------------------------------------------------------
		JPopupMenu popupAdd = new JPopupMenu();
		JMenuItem mnuAdd = new JMenuItem("Thêm người này vào danh sách ở ghép");
		popupAdd.add(mnuAdd);

		// Bắt sự kiện Click CHUỘT PHẢI trên bảng Khách Hàng (Bảng trên)
		view.getTblKhachHang().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger()) {
					// Tự động bôi đen dòng tại vị trí click chuột phải (Tránh kích hoạt chuột trái)
					int row = view.getTblKhachHang().rowAtPoint(e.getPoint());
					if (row >= 0 && row < view.getTblKhachHang().getRowCount()) {
						view.getTblKhachHang().setRowSelectionInterval(row, row);
						popupAdd.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});

		// Xử lý khi bấm nút "Thêm..." trên Menu
		mnuAdd.addActionListener(e -> {
			int row = view.getTblKhachHang().getSelectedRow();
			if (row >= 0) {
				// Lấy thông tin người được chọn
				String maKH = view.getTblKhachHang().getValueAt(row, 0).toString();
				String tenKH = view.getTblKhachHang().getValueAt(row, 1).toString();
				String diaChi = view.getTblKhachHang().getValueAt(row, 2).toString();
				String gioiTinh = view.getTblKhachHang().getValueAt(row, 3).toString();
				String ngaySinh = view.getTblKhachHang().getValueAt(row, 4) != null
						? view.getTblKhachHang().getValueAt(row, 4).toString()
						: "";

				// Ràng buộc 1: Không được lấy ông Thuê Chính tự cho vào danh sách Phụ Thuộc của
				// ổng
				if (maKH.equals(view.getMaKH())) {
					JOptionPane.showMessageDialog(view,
							"Người này đang là người thuê chính, không thể tự ở ghép với chính mình!", "Cảnh báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				// Ràng buộc 2: Chống thêm trùng lặp 2 lần
				DefaultTableModel modelPhuThuoc = view.getModelPhuThuoc();
				for (int i = 0; i < modelPhuThuoc.getRowCount(); i++) {
					if (maKH.equals(modelPhuThuoc.getValueAt(i, 0).toString())) {
						JOptionPane.showMessageDialog(view, "Người này đã có trong danh sách ở ghép rồi!", "Cảnh báo",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
				}

				// Đẩy dữ liệu xuống bảng Phụ thuộc (Bảng dưới)
				modelPhuThuoc.addRow(new Object[] { maKH, tenKH, diaChi, gioiTinh, ngaySinh });
				capNhatSoLuongNguoi();
			}
		});

		// -------------------------------------------------------------
		// 2. MENU BẢNG DƯỚI: XÓA KHỎI DANH SÁCH
		// -------------------------------------------------------------
		JPopupMenu popupRemove = new JPopupMenu();
		JMenuItem mnuRemove = new JMenuItem("Xóa khỏi danh sách ở ghép");
		popupRemove.add(mnuRemove);

		// Bắt sự kiện Click CHUỘT PHẢI trên bảng Phụ thuộc (Bảng dưới)
		view.getTblKHPhuThuoc().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger()) {
					int row = view.getTblKHPhuThuoc().rowAtPoint(e.getPoint());
					if (row >= 0 && row < view.getTblKHPhuThuoc().getRowCount()) {
						view.getTblKHPhuThuoc().setRowSelectionInterval(row, row);
						popupRemove.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});

		// Xử lý khi bấm nút "Xóa..."
		mnuRemove.addActionListener(e -> {
			int row = view.getTblKHPhuThuoc().getSelectedRow();
			if (row >= 0) {
				view.getModelPhuThuoc().removeRow(row); // Xóa dòng trên giao diện
				capNhatSoLuongNguoi();
			}
		});
	}

	// Thực hiện lưu dữ liệu xuống DB
	private void xuLyThemHopDong() {
		try {
			// 1. Validate chọn Khách hàng & Phòng
			String maKHStr = view.getMaKH();
			if (maKHStr.isEmpty()) {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng thuê từ danh sách!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (view.getCboPhong().getSelectedItem() == null) {
				JOptionPane.showMessageDialog(view, "Vui lòng chọn phòng cần thuê!", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// 2. Lấy & Ép kiểu dữ liệu nhập liệu
			long maKH = Long.parseLong(maKHStr);

			String selectedPhong = (String) view.getCboPhong().getSelectedItem();
			int maPhong = mapPhong.get(selectedPhong);

			LocalDate ngayBD = LocalDate.parse(view.getNgayLap(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			LocalDate ngayKT = LocalDate.parse(view.getNgayKetThuc(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			double giaThue = 0;
			int soNguoiO = 0;

			try {
				giaThue = Double.parseDouble(view.getGiaThue());
				if (giaThue <= 0) {
					throw new Exception();
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(view, "Giá thuê phải là số lớn hơn 0!", "Lỗi nhập liệu",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				soNguoiO = Integer.parseInt(view.getSoNguoi());
				if (soNguoiO <= 0 || soNguoiO > 5) {
					throw new Exception(); // Giả sử tối đa 5 người
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(view, "Số người ở phải là số hợp lệ (1-5 người)!", "Lỗi nhập liệu",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			String ghiChu = view.getGhiChu();
			String trangThai = "Hiệu lực";

			// 3. Lấy danh sách Khách Hàng Phụ (Nếu có người ở ghép)
			List<Long> listMaKHPhu = new ArrayList<>();
			DefaultTableModel modelPhuThuoc = view.getModelPhuThuoc();
			for (int i = 0; i < modelPhuThuoc.getRowCount(); i++) {
				// Giả định cột 0 là cột Mã KH
				listMaKHPhu.add(Long.parseLong(modelPhuThuoc.getValueAt(i, 0).toString()));
			}

			// 4. Xác nhận trước khi lưu
			int confirm = JOptionPane.showConfirmDialog(view,
					"Xác nhận tạo hợp đồng mới cho Khách hàng: " + view.getTenKH() + "?", "Xác nhận",
					JOptionPane.YES_NO_OPTION);
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}

			// 5. Gọi DAO thực thi (File HopDongDAO bạn vừa tạo)
			String kq = hopDongDAO.insertHopDong(maKH, maPhong, ngayBD, ngayKT, giaThue, soNguoiO, ghiChu, trangThai,
					listMaKHPhu);

			// 6. Thông báo kết quả
			if (kq.equals("SUCCESS")) {
				JOptionPane.showMessageDialog(view, "Đã lập hợp đồng thành công!", "Thông Báo",
						JOptionPane.INFORMATION_MESSAGE);
				if (parentController != null) {
					parentController.refreshData();
				}
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, kq, "Lỗi tạo Hợp Đồng", JOptionPane.WARNING_MESSAGE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view, "Có lỗi xảy ra trong quá trình xử lý dữ liệu!", "Lỗi Hệ Thống",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// =================================================================
	// 4. TÌM KIẾM KHÁCH HÀNG TRÊN BẢNG
	// =================================================================
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

	// =================================================================
	// TỰ ĐỘNG TÍNH SỐ LƯỢNG NGƯỜI Ở
	// =================================================================
	private void capNhatSoLuongNguoi() {
		// Lấy số lượng dòng trong bảng Khách Hàng Phụ
		int soLuongPhu = view.getModelPhuThuoc().getRowCount();

		// Tổng = 1 (Người thuê chính) + Số người ở ghép
		int tongSo = 1 + soLuongPhu;

		// Cập nhật lên giao diện (Ô txtSoNguoi)
		view.setSoNguoi(String.valueOf(tongSo));
		tinhGiaThue();
	}

	private void tinhGiaThue() {
		try {
			// 1. Lấy tên phòng đang chọn từ ComboBox
			String selectedPhong = (String) view.getCboPhong().getSelectedItem();

			// Kiểm tra null và kiểm tra Map có chứa phòng đó không
			if (selectedPhong == null || !mapPhong.containsKey(selectedPhong)) {
				return;
			}

			// Lấy ID phòng từ Map
			int maPhong = mapPhong.get(selectedPhong);

			// 2. Lấy số người ở từ giao diện
			int slNguoiO = 0;
			try {
				String textSoNguoi = view.getSoNguoi(); // Hàm getSoNguoi() từ View
				if (textSoNguoi != null && !textSoNguoi.isEmpty()) {
					slNguoiO = Integer.parseInt(textSoNguoi);
				}
			} catch (NumberFormatException e) {
				slNguoiO = 0; // Nếu nhập sai thì coi như 0
			}

			// 3. Gọi DAO lấy thông tin giá
			Phong p = phongDAO.getThongTinPhong(maPhong);

			if (p != null) {
				// === SỬA CÁC GETTER CHO KHỚP VỚI MODEL PHONG ===
				double giaGoc = p.getGia();
				int slMax = p.getSoNguoiToiDa();
				double giaPhuThu = p.getPhuThu();

				double tongTien = giaGoc;

				// 4. Logic tính phụ thu
				if (slNguoiO > slMax) {
					int soNguoiVuot = slNguoiO - slMax;
					double tienPhuThu = soNguoiVuot * giaPhuThu;
					tongTien += tienPhuThu;
				}

				// 5. Hiển thị (ép kiểu long để bỏ số thập phân .0 cho đẹp)
				long tongTienHienThi = (long) tongTien;
				view.setGiaThue(String.valueOf(tongTienHienThi));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}