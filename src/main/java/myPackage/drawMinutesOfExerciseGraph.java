package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class drawMinutesOfExerciseGraph extends JPanel {
    public drawMinutesOfExerciseGraph() {
        this(-1, null, 0);
    }
    
    public drawMinutesOfExerciseGraph(int userId, DatabaseManager dbManager, int days) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        if (userId != -1 && dbManager != null) {
            List<Object[]> workouts = dbManager.getWorkoutData(userId, days);
            System.out.println("Minutes graph: Retrieved " + workouts.size() + " workouts for user " + userId);
            // Sum minutes by date
            Map<String, Integer> minutesByDate = new HashMap<>();
            for (Object[] workout : workouts) {
                if (workout == null || workout.length < 3) {
                    System.out.println("Minutes graph: Invalid workout array");
                    continue;
                }
                String date = (String) workout[0];
                Object minutesObj = workout[2];
                int minutes = 0;
                if (minutesObj instanceof Integer) {
                    minutes = (Integer) minutesObj;
                } else if (minutesObj instanceof Number) {
                    minutes = ((Number) minutesObj).intValue();
                }
                System.out.println("Minutes graph: Processing workout - date: " + date + ", minutes: " + minutes + " (type: " + (minutesObj != null ? minutesObj.getClass().getName() : "null") + ")");
                if (date != null && !date.isEmpty() && minutes > 0) {
                    minutesByDate.put(date, minutesByDate.getOrDefault(date, 0) + minutes);
                    System.out.println("Minutes graph: Added minutes for date " + date + ", total: " + minutesByDate.get(date));
                } else {
                    System.out.println("Minutes graph: Skipping workout - date: " + date + ", minutes: " + minutes);
                }
            }
            
            // Add to dataset (sort by date for better display)
            List<Map.Entry<String, Integer>> sortedEntries = new java.util.ArrayList<>(minutesByDate.entrySet());
            sortedEntries.sort((e1, e2) -> e1.getKey().compareTo(e2.getKey()));
            
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                String displayDate = formatDateForDisplay(entry.getKey());
                dataset.addValue(entry.getValue(), "Minutes", displayDate);
                System.out.println("Minutes graph: Adding " + entry.getValue() + " minutes for date " + displayDate);
            }
            System.out.println("Minutes graph: Dataset has " + dataset.getRowCount() + " rows, " + dataset.getColumnCount() + " columns");
            
            // If no data, add a placeholder so the graph still displays
            if (dataset.getRowCount() == 0) {
                System.out.println("Minutes graph: No data found, adding placeholder");
                dataset.addValue(0, "Minutes", "No data");
            }
        } else {
            // Fallback to example data if no database connection
            dataset.addValue(0, "Minutes", "No data");
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Minutes of Exercise",
                "Date",
                "Minutes",
                dataset
        );
        
        // Ensure chart is visible even with empty data
        chart.getPlot().setNoDataMessage("No exercise data available");

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 250));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);
    }
    
    private String formatDateForDisplay(String date) {
        // Date is stored as MM-dd-yyyy, return as-is
        return date;
    }
}
