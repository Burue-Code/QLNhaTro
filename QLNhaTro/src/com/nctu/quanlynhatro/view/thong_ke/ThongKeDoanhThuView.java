package com.nctu.quanlynhatro.view.thong_ke;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

// Import thư viện vẽ biểu đồ (JFreeChart)
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

// Import các component riêng của dự án
import com.nctu.quanlynhatro.view.component.MyButton;
import com.nctu.quanlynhatro.view.component.MyLabel;
// Import thư viện lịch (JCalendar)
import com.toedter.calendar.JDateChooser;

public class ThongKeDoanhThuView extends JPanel {

	private JDateChooser dateTuNgay, dateDenNgay;
	private MyButton btnThongKe;

	private JLabel lblDoanhThu, lblSoHoaDon, lblKhachMoi;

	private JPanel pnlChartContainer;

	public ThongKeDoanhThuView() {
		setLayout(new BorderLayout(10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);

		JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		pnlTop.setBackground(Color.WHITE);

		dateTuNgay = new JDateChooser();
		dateTuNgay.setDateFormatString("dd/MM/yyyy");
		dateTuNgay.setPreferredSize(new Dimension(150, 30));

		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
		dateTuNgay.setDate(Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()));

		dateDenNgay = new JDateChooser();
		dateDenNgay.setDateFormatString("dd/MM/yyyy");
		dateDenNgay.setPreferredSize(new Dimension(150, 30));
		dateDenNgay.setDate(new Date());

		btnThongKe = new MyButton("Xem Báo Cáo", 150, 30);
		btnThongKe.setButtonColor(new Color(0, 123, 255));
		btnThongKe.setForeground(Color.WHITE);

		pnlTop.add(new MyLabel("Từ ngày:"));
		pnlTop.add(dateTuNgay);
		pnlTop.add(new MyLabel("Đến ngày:"));
		pnlTop.add(dateDenNgay);
		pnlTop.add(btnThongKe);

		add(pnlTop, BorderLayout.NORTH);

		JPanel pnlCenter = new JPanel(new BorderLayout(0, 20));
		pnlCenter.setBackground(Color.WHITE);
		JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
		pnlCards.setBackground(Color.WHITE);

		lblDoanhThu = new JLabel("0 đ", SwingConstants.CENTER);
		lblSoHoaDon = new JLabel("0", SwingConstants.CENTER);
		lblKhachMoi = new JLabel("0", SwingConstants.CENTER);

		pnlCards.add(createCard("TỔNG DOANH THU", lblDoanhThu, new Color(255, 159, 67)));
		pnlCards.add(createCard("SỐ HÓA ĐƠN", lblSoHoaDon, new Color(46, 204, 113)));
		pnlCards.add(createCard("KHÁCH MỚI", lblKhachMoi, new Color(52, 152, 219)));

		pnlChartContainer = new JPanel(new BorderLayout());
		pnlChartContainer.setBackground(Color.WHITE);

		pnlCenter.add(pnlCards, BorderLayout.NORTH);
		pnlCenter.add(pnlChartContainer, BorderLayout.CENTER);

		add(pnlCenter, BorderLayout.CENTER);
	}

	private JPanel createCard(String title, JLabel valueLabel, Color bgColor) {
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(bgColor);
		card.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 1, true));

		JLabel lblTitle = new JLabel(title.toUpperCase(), SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

		valueLabel.setFont(new Font("Arial", Font.BOLD, 26));
		valueLabel.setForeground(Color.WHITE);

		card.add(lblTitle, BorderLayout.NORTH);
		card.add(valueLabel, BorderLayout.CENTER);

		card.setPreferredSize(new Dimension(200, 120));
		return card;
	}

	private void veBieuDoCot(Date d1, Date d2) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		JFreeChart barChart = ChartFactory.createBarChart("BIỂU ĐỒ DOANH THU THEO NGÀY", "Thời gian", "Doanh thu (VNĐ)",
				dataset, PlotOrientation.VERTICAL, false, true, false);

		customizeChart(barChart);

		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setMaximumDrawWidth(20000);
		chartPanel.setMaximumDrawHeight(20000);
		chartPanel.setMinimumDrawWidth(0);
		chartPanel.setMinimumDrawHeight(0);

		pnlChartContainer.removeAll();
		pnlChartContainer.add(chartPanel, BorderLayout.CENTER);
		pnlChartContainer.validate();
		pnlChartContainer.repaint();
	}

	private void customizeChart(JFreeChart chart) {
		chart.setBackgroundPaint(Color.WHITE);

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, new Color(70, 130, 180));
		renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
		renderer.setMaximumBarWidth(0.05);

		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("#,###")));
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelPaint(Color.BLACK);

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setNumberFormatOverride(new DecimalFormat("#,###"));
		rangeAxis.setUpperMargin(0.15);
	}

	private Map<String, Double> taoDuLieuGia(int soNgay) {
		Map<String, Double> data = new LinkedHashMap<>();
		LocalDate currentDate = LocalDate.now().minusDays(soNgay);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
		Random rand = new Random();

		for (int i = 0; i < soNgay; i++) {
			String keyNgay = currentDate.format(formatter);
			double doanhThu = 1000000 + (4000000 * rand.nextDouble());
			data.put(keyNgay, doanhThu);
			currentDate = currentDate.plusDays(1);
		}
		return data;
	}

	public com.toedter.calendar.JDateChooser getDateTuNgay() {
		return dateTuNgay;
	}

	public com.toedter.calendar.JDateChooser getDateDenNgay() {
		return dateDenNgay;
	}

	public JButton getBtnThongKe() {
		return btnThongKe;
	}

	public JLabel getLblDoanhThu() {
		return lblDoanhThu;
	}

	public JLabel getLblSoHoaDon() {
		return lblSoHoaDon;
	}

	public JLabel getLblKhachMoi() {
		return lblKhachMoi;
	}

	public JPanel getPnlChartContainer() {
		return pnlChartContainer;
	}
}