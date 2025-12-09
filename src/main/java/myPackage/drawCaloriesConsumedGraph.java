package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class drawCaloriesConsumedGraph extends JPanel {
    public drawCaloriesConsumedGraph() {
        this(-1, null, 0);
    }
    
    public drawCaloriesConsumedGraph(int userId, DatabaseManager dbManager, int days) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        if (userId != -1 && dbManager != null) {
            List<Object[]> data = dbManager.getHistoricalUserData(userId, days);
            System.out.println("Calories graph: Retrieved " + data.size() + " data points for user " + userId);
            for (Object[] row : data) {
                String date = (String) row[0];
                Integer calories = (Integer) row[1];
                if (calories != null) {
                    String displayDate = formatDateForDisplay(date);
                    dataset.addValue(calories, "Calories Consumed", displayDate);
                    System.out.println("Calories graph: Adding calories " + calories + " for date " + displayDate);
                }
            }
            System.out.println("Calories graph: Dataset has " + dataset.getRowCount() + " rows");
        } else {
            // Fallback to example data if no database connection
            dataset.addValue(2000, "Calories Consumed", "12-01-2025");
            dataset.addValue(3000, "Calories Consumed", "12-02-2025");
            dataset.addValue(4000, "Calories Consumed", "12-03-2025");
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Calories Consumed",
                "Date",
                "Calories Consumed",
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
