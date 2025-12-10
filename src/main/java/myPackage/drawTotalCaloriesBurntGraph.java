package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import static myPackage.Constants.*;
public class drawTotalCaloriesBurntGraph extends JPanel {
    /**
     * Constructs a new graph for tracking total calories burnt with default settings.
     * @author ocalusinski
     */
    public drawTotalCaloriesBurntGraph() {
        this(-1, 0);
    }
    
    /**
     * Constructs a new graph for tracking total calories burnt for a specific user and number of days.
     * @author Owen Chipman
     */
    public drawTotalCaloriesBurntGraph(int userId, int days) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        if (userId != -1 && DB_MANAGER != null) {
            List<Object[]> data = DB_MANAGER.getHistoricalUserData(userId, days);
            System.out.println("Total calories burnt graph: Retrieved " + data.size() + " data points for user " + userId);
            for (Object[] row : data) {
                String date = (String) row[0];
                Integer caloriesBurnt = (Integer) row[4];
                if (caloriesBurnt != null) {
                    String displayDate = formatDateForDisplay(date);
                    dataset.addValue(caloriesBurnt, "Calories Burnt", displayDate);
                    System.out.println("Total calories burnt graph: Adding calories " + caloriesBurnt + " for date " + displayDate);
                }
            }
            System.out.println("Total calories burnt graph: Dataset has " + dataset.getRowCount() + " rows");
        } else {
            // Fallback to example data
            dataset.addValue(2000, "Calories Burnt", "12-01-2025");
            dataset.addValue(2200, "Calories Burnt", "12-02-2025");
            dataset.addValue(2100, "Calories Burnt", "12-03-2025");
        }

        JFreeChart chart = ChartFactory.createLineChart(
          "Total Calories Burnt",
          "Date",
          "Calories Burnt",
          dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 250));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);
    }
    
    /**
     * Formats a date string for display.
     * @author zachtaylorcsc
     * @return The formatted date string.
     */
    private String formatDateForDisplay(String date) {
        // Date is stored as MM-dd-yyyy, return as-is
        if (date != null && date.length() >= 10) {
            return date;
        }
        return date;
    }
}
