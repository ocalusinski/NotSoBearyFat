package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.List;

public class drawSleepGraph extends JPanel {
    public drawSleepGraph() {
        this(-1, null, 0);
    }
    
    public drawSleepGraph(int userId, DatabaseManager dbManager, int days) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        if (userId != -1 && dbManager != null) {
            List<Object[]> data = dbManager.getHistoricalUserData(userId, days);
            System.out.println("Sleep graph: Retrieved " + data.size() + " data points for user " + userId);
            for (Object[] row : data) {
                String date = (String) row[0];
                Double sleepHours = (Double) row[3];
                if (sleepHours != null && sleepHours > 0) {
                    String displayDate = formatDateForDisplay(date);
                    dataset.addValue(sleepHours, "Hours", displayDate);
                    System.out.println("Sleep graph: Adding sleep " + sleepHours + " for date " + displayDate);
                }
            }
            System.out.println("Sleep graph: Dataset has " + dataset.getRowCount() + " rows");
        } else {
            // Fallback to example data
            dataset.addValue(8, "Hours", "12-01-2025");
            dataset.addValue(7, "Hours", "12-02-2025");
            dataset.addValue(9, "Hours", "12-03-2025");
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Hours of Sleep",
                "Date",
                "Hours of Sleep",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 250));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);
    }
    
    private String formatDateForDisplay(String date) {
        // Date is stored as MM-dd-yyyy, return as-is
        if (date != null && date.length() >= 10) {
            return date;
        }
        return date;
    }
}
