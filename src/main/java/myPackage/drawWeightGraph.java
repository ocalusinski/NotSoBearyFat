package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class drawWeightGraph extends JPanel {
    public drawWeightGraph() {
        this(-1, null, 0);
    }
    
    public drawWeightGraph(int userId, DatabaseManager dbManager, int days) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        if (userId != -1 && dbManager != null) {
            List<Object[]> data = dbManager.getHistoricalUserData(userId, days);
            for (Object[] row : data) {
                String date = (String) row[0];
                Double weight = (Double) row[2];
                if (weight != null) {
                    String displayDate = formatDateForDisplay(date);
                    dataset.addValue(weight, "Weight", displayDate);
                }
            }
        } else {
            // Fallback to example data
            dataset.addValue(135, "Weight", "12-01-2025");
            dataset.addValue(137, "Weight", "12-02-2025");
            dataset.addValue(139, "Weight", "12-03-2025");
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Weight",
                "Date",
                "Weight",
                dataset
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 350));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);
    }
    
    private String formatDateForDisplay(String date) {
        if (date != null && date.length() >= 10) {
            try {
                String year = date.substring(0, 4);
                String month = date.substring(5, 7);
                String day = date.substring(8, 10);
                return month + "-" + day + "-" + year;
            } catch (Exception e) {
                return date;
            }
        }
        return date;
    }
}
