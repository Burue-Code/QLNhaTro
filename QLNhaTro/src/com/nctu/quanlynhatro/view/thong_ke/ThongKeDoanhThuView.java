package com.nctu.quanlynhatro.view.thong_ke;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

// Import thư viện lịch (JCalendar)
import com.toedter.calendar.JDateChooser;

// Import các component riêng của dự án
import com.nctu.quanlynhatro.view.component.*;

public class ThongKeDoanhThuView extends JPanel {

    // Components giao diện
    private JDateChooser dateTuNgay, dateDenNgay;
    private MyButton btnThongKe;
    
    // Các Label hiển thị số liệu tổng
    private JLabel lblDoanhThu, lblSoHoaDon, lblKhachMoi;
    
    // Panel chứa biểu đồ
    private JPanel pnlChartContainer;

    public ThongKeDoanhThuView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // =================================================================
        // 1. KHU VỰC NORTH: BỘ LỌC THỜI GIAN
        // =================================================================
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlTop.setBackground(Color.WHITE);

        // Date Choosers
        dateTuNgay = new JDateChooser();
        dateTuNgay.setDateFormatString("dd/MM/yyyy");
        dateTuNgay.setPreferredSize(new Dimension(150, 30));
        
        // Mặc định ngày đầu tháng
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        dateTuNgay.setDate(Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        dateDenNgay = new JDateChooser();
        dateDenNgay.setDateFormatString("dd/MM/yyyy");
        dateDenNgay.setPreferredSize(new Dimension(150, 30));
        dateDenNgay.setDate(new Date()); // Mặc định hôm nay

        // Button Xem Thống Kê (Dùng MyButton của bạn)
        btnThongKe = new MyButton("Xem Báo Cáo", 150, 30);
        btnThongKe.setButtonColor(new Color(0, 123, 255)); // Màu xanh dương
        btnThongKe.setForeground(Color.WHITE);

        // Add components vào Top Panel
        pnlTop.add(new MyLabel("Từ ngày:"));
        pnlTop.add(dateTuNgay);
        pnlTop.add(new MyLabel("Đến ngày:"));
        pnlTop.add(dateDenNgay);
        pnlTop.add(btnThongKe);

        add(pnlTop, BorderLayout.NORTH);

        // =================================================================
        // 2. CONTENT (CENTER): CARDS + CHART
        // =================================================================
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 20));
        pnlCenter.setBackground(Color.WHITE);

        // 2a. Các Card thống kê (3 ô màu)
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0)); // 1 hàng, 3 cột
        pnlCards.setBackground(Color.WHITE);
        
        lblDoanhThu = new JLabel("0 đ", SwingConstants.CENTER);
        lblSoHoaDon = new JLabel("0", SwingConstants.CENTER);
        lblKhachMoi = new JLabel("0", SwingConstants.CENTER);

        // Màu sắc: Cam (Doanh thu), Xanh lá (Hóa đơn), Xanh dương (Khách)
        pnlCards.add(createCard("TỔNG DOANH THU", lblDoanhThu, new Color(255, 159, 67)));
        pnlCards.add(createCard("SỐ HÓA ĐƠN", lblSoHoaDon, new Color(46, 204, 113)));
        pnlCards.add(createCard("KHÁCH MỚI", lblKhachMoi, new Color(52, 152, 219)));

        // 2b. Biểu đồ doanh thu
        pnlChartContainer = new JPanel(new BorderLayout());
        pnlChartContainer.setBackground(Color.WHITE);

        pnlCenter.add(pnlCards, BorderLayout.NORTH);
        pnlCenter.add(pnlChartContainer, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // =================================================================
        // 3. SỰ KIỆN
        // =================================================================
        btnThongKe.addActionListener(e -> refreshData());
        
        // Load dữ liệu lần đầu
        refreshData();
    }

    /**
     * Hàm tạo giao diện cho 1 Card (Ô màu hiển thị số liệu)
     */
    private JPanel createCard(String title, JLabel valueLabel, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 1, true));

        // Tiêu đề card
        JLabel lblTitle = new JLabel(title.toUpperCase(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Giá trị số liệu
        valueLabel.setFont(new Font("Arial", Font.BOLD, 26));
        valueLabel.setForeground(Color.WHITE);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        card.setPreferredSize(new Dimension(200, 120));
        return card;
    }

    /**
     * Hàm xử lý khi nhấn nút Xem Báo Cáo
     */
    private void refreshData() {
        Date d1 = dateTuNgay.getDate();
        Date d2 = dateDenNgay.getDate();

        if (d1 == null || d2 == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày!");
            return;
        }

        // 1. Cập nhật số liệu trên Cards (Giả lập dữ liệu)
        // Trong thực tế bạn sẽ gọi Service để lấy số liệu thật
        double tongDoanhThu = 150500000;
        int soHoaDon = 45;
        int khachMoi = 12;

        lblDoanhThu.setText(new DecimalFormat("#,###").format(tongDoanhThu) + " đ");
        lblSoHoaDon.setText(String.valueOf(soHoaDon));
        lblKhachMoi.setText(String.valueOf(khachMoi));

        // 2. Vẽ biểu đồ
        veBieuDoCot(d1, d2);
    }

    /**
     * Hàm vẽ biểu đồ cột
     */
    private void veBieuDoCot(Date d1, Date d2) {
        // Chuẩn bị Dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Giả lập dữ liệu ngẫu nhiên để vẽ biểu đồ
        Map<String, Double> data = taoDuLieuGia(10); 
        
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), "Doanh Thu", entry.getKey());
        }

        // Tạo biểu đồ
        JFreeChart barChart = ChartFactory.createBarChart(
                "BIỂU ĐỒ DOANH THU THEO NGÀY", 
                "Thời gian", 
                "Doanh thu (VNĐ)", 
                dataset, 
                PlotOrientation.VERTICAL, 
                false, true, false);

        // Tùy chỉnh giao diện biểu đồ
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

    /**
     * Tùy chỉnh giao diện biểu đồ cho đẹp
     */
    private void customizeChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(70, 130, 180)); // Màu xanh
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter()); // Flat style
        renderer.setMaximumBarWidth(0.05);

        // Hiển thị số tiền trên đầu cột
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("#,###")));
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelPaint(Color.BLACK);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(new DecimalFormat("#,###"));
        rangeAxis.setUpperMargin(0.15);
    }

    /**
     * Tạo dữ liệu giả để test biểu đồ
     */
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
}