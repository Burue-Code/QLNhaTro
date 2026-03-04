package com.nctu.quanlynhatro.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.nctu.quanlynhatro.dao.DatabaseConnection;
import com.nctu.quanlynhatro.dao.ThongKeDoanhThuDAO;
import com.nctu.quanlynhatro.view.thong_ke.ThongKeDoanhThuView;

public class ThongKeDoanhThuController {

	private ThongKeDoanhThuView view;
	private ThongKeDoanhThuDAO dao;
	private DecimalFormat df = new DecimalFormat("#,###");

	public ThongKeDoanhThuController(ThongKeDoanhThuView view) {
		this.view = view;
		this.dao = new ThongKeDoanhThuDAO(DatabaseConnection.getConnection());

		initEvents();
		loadThongKe(); // Load dữ liệu mặc định khi mở
	}

	private void initEvents() {
		// Gán sự kiện cho nút "Xem Báo Cáo"
		view.getBtnThongKe().addActionListener(e -> loadThongKe());
	}

	private void loadThongKe() {
		// 1. Lấy ngày từ View
		Date d1 = view.getDateTuNgay().getDate();
		Date d2 = view.getDateDenNgay().getDate();

		if (d1 == null || d2 == null) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn đầy đủ ngày!", "Lỗi", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (d1.after(d2)) {
			JOptionPane.showMessageDialog(view, "Ngày bắt đầu không được lớn hơn ngày kết thúc!", "Lỗi",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Chuyển đổi sang LocalDate để dùng cho DAO
		LocalDate from = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate to = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// 2. Gọi DAO lấy số liệu tổng hợp
		double tongDoanhThu = dao.getTongDoanhThu(from, to);
		int soHoaDon = dao.getSoLuongHoaDon(from, to);
		int khachMoi = dao.getSoKhachMoi(from, to);

		// 3. Cập nhật lên View (Cards)
		view.getLblDoanhThu().setText(df.format(tongDoanhThu) + " đ");
		view.getLblSoHoaDon().setText(String.valueOf(soHoaDon));
		view.getLblKhachMoi().setText(String.valueOf(khachMoi));

		// 4. Vẽ biểu đồ
		Map<String, Double> dataBieuDo = dao.getDoanhThuTheoNgay(from, to);
		veBieuDoCot(dataBieuDo);
	}

	private void veBieuDoCot(Map<String, Double> data) {
		// Chuẩn bị Dataset
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (data.isEmpty()) {
			// Nếu không có dữ liệu thì thêm cột rỗng để chart không bị lỗi
			dataset.addValue(0, "Doanh Thu", "Không có dữ liệu");
		} else {
			for (Map.Entry<String, Double> entry : data.entrySet()) {
				dataset.addValue(entry.getValue(), "Doanh Thu", entry.getKey());
			}
		}

		// Tạo Chart
		JFreeChart barChart = ChartFactory.createBarChart("BIỂU ĐỒ DOANH THU THEO NGÀY", "Thời gian", "Doanh thu (VNĐ)",
				dataset, PlotOrientation.VERTICAL, false, true, false);

		// Customize Chart (Giống code trong View cũ của bạn)
		customizeChart(barChart);

		// Đẩy Chart vào Panel
		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setMaximumDrawWidth(20000);
		chartPanel.setMaximumDrawHeight(20000);

		JPanel pnlContainer = view.getPnlChartContainer();
		pnlContainer.removeAll();
		pnlContainer.add(chartPanel, BorderLayout.CENTER);
		pnlContainer.revalidate(); // Quan trọng để refresh giao diện
		pnlContainer.repaint();
	}

	private void customizeChart(JFreeChart chart) {
		chart.setBackgroundPaint(Color.WHITE);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, new Color(70, 130, 180)); // Màu xanh
		renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter()); // Flat style

		// Hiển thị số trên cột
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", df));
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelPaint(Color.BLACK);

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setNumberFormatOverride(df);
	}
}