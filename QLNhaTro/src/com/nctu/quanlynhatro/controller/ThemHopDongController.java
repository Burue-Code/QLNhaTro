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

	private Map<String, Integer> mapNhaTro = new HashMap<>();
	private Map<String, Integer> mapPhong = new HashMap<>();
	private DefaultTableModel modelKH;
	private TableRowSorter<DefaultTableModel> sorterKH;

	public ThemHopDongController(ThemHopDongView view, HopDongController parentController) {
		this.view = view;
		this.parentController = parentController;
		this.hopDongDAO = new HopDongDAO(DatabaseConnection.getConnection());
		this.khachHangDAO = new KhachHangDAO(DatabaseConnection.getConnection());
		this.nhaTroDAO = new NhaTroDAO(DatabaseConnection.getConnection());
		this.phongDAO = new PhongDAO(DatabaseConnection.getConnection());
		this.modelKH = view.getModelKH();

		initData();
		initSearch();
		initEvents();
	}

	private void initData() {
		modelKH.setRowCount(0);
		List<KhachHang> listKH = khachHangDAO.getAll();
		for (KhachHang kh : listKH) {
			modelKH.addRow(new Object[] { kh.getMaKH(), kh.getTenKH(), kh.getDiaChi(), kh.getGioiTinh() ? "Nữ" : "Nam",
					kh.getNgaySinh() != null ? kh.getNgaySinh().toString() : "" });
		}

		view.getCboNhaTro().removeAllItems();
		mapNhaTro.clear();
		Map<Integer, String> dataNhaTro = nhaTroDAO.getNhaTroConPhong();
		for (Map.Entry<Integer, String> entry : dataNhaTro.entrySet()) {
			view.getCboNhaTro().addItem(entry.getValue());
			mapNhaTro.put(entry.getValue(), entry.getKey());
		}
		loadDataPhong();
	}

	private void loadDataPhong() {
		view.getCboPhong().removeAllItems();
		mapPhong.clear();
		String selectedNT = (String) view.getCboNhaTro().getSelectedItem();
		if (selectedNT == null || !mapNhaTro.containsKey(selectedNT)) {
			return;
		}

		int maNT = mapNhaTro.get(selectedNT);
		Map<Integer, String> dataPhong = phongDAO.getPhongTrongByMaNT(maNT);
		for (Map.Entry<Integer, String> entry : dataPhong.entrySet()) {
			view.getCboPhong().addItem(entry.getValue());
			mapPhong.put(entry.getValue(), entry.getKey());
		}
	}

	private void initEvents() {
		view.getBtnThoat().addActionListener(e -> view.dispose());
		view.getBtnThem().addActionListener(e -> xuLyThemHopDong());

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

		view.getCboNhaTro().addActionListener(e -> loadDataPhong());
		view.getCboPhong().addActionListener(e -> tinhGiaThue());

		view.getTblKhachHang().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
					int row = view.getTblKhachHang().getSelectedRow();
					if (row >= 0) {
						String newMaKH = view.getTblKhachHang().getValueAt(row, 0).toString();
						String newTenKH = view.getTblKhachHang().getValueAt(row, 1).toString();
						String currentMaKH = view.getMaKH();

						if (!currentMaKH.isEmpty() && !currentMaKH.equals(newMaKH)) {
							int confirm = JOptionPane.showConfirmDialog(view, "Đổi sang khách hàng: " + newTenKH + "?",
									"Xác nhận", JOptionPane.YES_NO_OPTION);
							if (confirm != JOptionPane.YES_OPTION) {
								return;
							}
						}

						view.setMaKH(newMaKH);
						view.setTenKH(newTenKH);
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
			initData(); // Refresh list sau khi thêm
		});

		setChiNhapSoNguyen(view.getTxtSoThang());
		tinhNgayKT();
		initPopupMenuKH();
	}

	private void setChiNhapSoNguyen(javax.swing.JTextField txt) {
		txt.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar())) {
					e.consume();
				}
			}
		});
	}

	private void tinhNgayKT() {
		try {
			String textThang = view.getSoThang();
			if (textThang.isEmpty()) {
				view.setNgayKetThuc("");
				return;
			}
			LocalDate ngayLap = LocalDate.parse(view.getNgayLap(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			view.setNgayKetThuc(
					ngayLap.plusMonths(Integer.parseInt(textThang)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		} catch (Exception e) {
			view.setNgayKetThuc("");
		}
	}

	private void loadKhachHangPhu(long maKHChinh) {
		view.getModelPhuThuoc().setRowCount(0);
		List<KhachHang> listKHPhu = khachHangDAO.getKhachHangPhu(maKHChinh);
		for (KhachHang kh : listKHPhu) {
			view.getModelPhuThuoc().addRow(new Object[] { kh.getMaKH(), kh.getTenKH(), kh.getDiaChi(),
					kh.getGioiTinh() ? "Nữ" : "Nam", kh.getNgaySinh() != null ? kh.getNgaySinh().toString() : "" });
		}
		capNhatSoLuongNguoi();
	}

	private void initPopupMenuKH() {
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
					JOptionPane.showMessageDialog(view, "Người này là người thuê chính!");
					return;
				}
				for (int i = 0; i < view.getModelPhuThuoc().getRowCount(); i++) {
					if (maKH.equals(view.getModelPhuThuoc().getValueAt(i, 0).toString())) {
						JOptionPane.showMessageDialog(view, "Người này đã có trong danh sách!");
						return;
					}
				}
				view.getModelPhuThuoc()
						.addRow(new Object[] { maKH, view.getTblKhachHang().getValueAt(row, 1),
								view.getTblKhachHang().getValueAt(row, 2), view.getTblKhachHang().getValueAt(row, 3),
								view.getTblKhachHang().getValueAt(row, 4) });
				capNhatSoLuongNguoi();
			}
		});

		JPopupMenu popupRemove = new JPopupMenu();
		JMenuItem mnuRemove = new JMenuItem("Xóa khỏi danh sách ở ghép");
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

	private void xuLyThemHopDong() {
		try {
			if (view.getMaKH().isEmpty() || view.getCboPhong().getSelectedItem() == null) {
				JOptionPane.showMessageDialog(view, "Thiếu thông tin khách hàng hoặc phòng!");
				return;
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate ngayBD = LocalDate.parse(view.getNgayLap(), formatter);
			LocalDate ngayKT = LocalDate.parse(view.getNgayKetThuc(), formatter);
			if (ngayKT.isBefore(ngayBD)) {
				JOptionPane.showMessageDialog(view, "Ngày kết thúc không hợp lệ!");
				return;
			}

			List<Long> listMaKHPhu = new ArrayList<>();
			for (int i = 0; i < view.getModelPhuThuoc().getRowCount(); i++) {
				listMaKHPhu.add(Long.parseLong(view.getModelPhuThuoc().getValueAt(i, 0).toString()));
			}

			String kq = hopDongDAO.insertHopDong(Long.parseLong(view.getMaKH()),
					mapPhong.get(view.getCboPhong().getSelectedItem()), ngayBD, ngayKT,
					Double.parseDouble(view.getGiaThue()), Integer.parseInt(view.getSoNguoi()), view.getGhiChu(),
					"Hiệu lực", listMaKHPhu);

			if (kq.equals("SUCCESS")) {
				JOptionPane.showMessageDialog(view, "Lập hợp đồng thành công!");
				if (parentController != null) {
					parentController.refreshData();
				}
				view.dispose();
			} else {
				JOptionPane.showMessageDialog(view, kq, "Lỗi", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(view, "Lỗi xử lý dữ liệu!");
		}
	}

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

	private void capNhatSoLuongNguoi() {
		view.setSoNguoi(String.valueOf(1 + view.getModelPhuThuoc().getRowCount()));
		tinhGiaThue();
	}

	private void tinhGiaThue() {
		try {
			String selectedPhong = (String) view.getCboPhong().getSelectedItem();
			if (selectedPhong == null || !mapPhong.containsKey(selectedPhong)) {
				return;
			}

			Phong p = phongDAO.getThongTinPhong(mapPhong.get(selectedPhong));
			if (p != null) {
				int slNguoiO = Integer.parseInt(view.getSoNguoi());
				double tongTien = p.getGia();
				if (slNguoiO > p.getSoNguoiToiDa()) {
					tongTien += (slNguoiO - p.getSoNguoiToiDa()) * p.getPhuThu();
				}
				view.setGiaThue(String.valueOf((long) tongTien));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}