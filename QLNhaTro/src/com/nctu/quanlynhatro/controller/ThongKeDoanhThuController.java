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
		loadThongKe();
	}

	private void initEvents() {
		view.getBtnThongKe().addActionListener(e -> loadThongKe());
	}

	private void loadThongKe() {
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

		LocalDate from = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate to = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		double tongDoanhThu = dao.getTongDoanhThu(from, to);
		int soHoaDon = dao.getSoLuongHoaDon(from, to);
		int khachMoi = dao.getSoKhachMoi(from, to);

		view.getLblDoanhThu().setText(df.format(tongDoanhThu) + " đ");
		view.getLblSoHoaDon().setText(String.valueOf(soHoaDon));
		view.getLblKhachMoi().setText(String.valueOf(khachMoi));

		Map<String, Double> dataBieuDo = dao.getDoanhThuTheoNgay(from, to);
		veBieuDoCot(dataBieuDo);
	}

	private void veBieuDoCot(Map<String, Double> data) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (data.isEmpty()) {
			dataset.addValue(0, "Doanh Thu", "Không có dữ liệu");
		} else {
			for (Map.Entry<String, Double> entry : data.entrySet()) {
				dataset.addValue(entry.getValue(), "Doanh Thu", entry.getKey());
			}
		}

		JFreeChart barChart = ChartFactory.createBarChart("BIỂU ĐỒ DOANH THU THEO NGÀY", "Thời gian", "Doanh thu (VNĐ)",
				dataset, PlotOrientation.VERTICAL, false, true, false);

		customizeChart(barChart);

		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setMaximumDrawWidth(20000);
		chartPanel.setMaximumDrawHeight(20000);

		JPanel pnlContainer = view.getPnlChartContainer();
		pnlContainer.removeAll();
		pnlContainer.add(chartPanel, BorderLayout.CENTER);
		pnlContainer.revalidate();
		pnlContainer.repaint();
	}

	private void customizeChart(JFreeChart chart) {
		chart.setBackgroundPaint(Color.WHITE);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, new Color(70, 130, 180));
		renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", df));
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelPaint(Color.BLACK);

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setNumberFormatOverride(df);
	}
}