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

import static myPackage.Constants.*;

public class drawActiveCaloriesBurntGraph extends JPanel {

    public drawActiveCaloriesBurntGraph() {
        this(-1, 0);
    }
    
    public drawActiveCaloriesBurntGraph(int userId, int days) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        if (userId != -1 && DB_MANAGER != null) {
            List<Object[]> workouts = DB_MANAGER.getWorkoutData(userId, days);
            System.out.println("Active calories graph: Retrieved " + workouts.size() + " workouts for user " + userId);
            // Sum calories by date
            Map<String, Integer> caloriesByDate = new HashMap<>();
            for (Object[] workout : workouts) {
                if (workout == null || workout.length < 4) {
                    System.out.println("Active calories graph: Invalid workout array");
                    continue;
                }
                String date = (String) workout[0];
                Object caloriesObj = workout[3]; // calories_burnt is at index 3
                int calories = 0;
                if (caloriesObj instanceof Integer) {
                    calories = (Integer) caloriesObj;
                } else if (caloriesObj instanceof Number) {
                    calories = ((Number) caloriesObj).intValue();
                }
                System.out.println("Active calories graph: Processing workout - date: " + date + ", calories: " + calories + " (type: " + (caloriesObj != null ? caloriesObj.getClass().getName() : "null") + ")");
                if (date != null && !date.isEmpty() && calories > 0) {
                    caloriesByDate.put(date, caloriesByDate.getOrDefault(date, 0) + calories);
                    System.out.println("Active calories graph: Added calories for date " + date + ", total: " + caloriesByDate.get(date));
                } else {
                    System.out.println("Active calories graph: Skipping workout - date: " + date + ", calories: " + calories);
                }
            }
            
            // Add to dataset (sort by date for better display)
            List<Map.Entry<String, Integer>> sortedEntries = new java.util.ArrayList<>(caloriesByDate.entrySet());
            sortedEntries.sort((e1, e2) -> e1.getKey().compareTo(e2.getKey()));
            
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                String displayDate = formatDateForDisplay(entry.getKey());
                dataset.addValue(entry.getValue(), "Calories", displayDate);
                System.out.println("Active calories graph: Adding " + entry.getValue() + " calories for date " + displayDate);
            }
            System.out.println("Active calories graph: Dataset has " + dataset.getRowCount() + " rows, " + dataset.getColumnCount() + " columns");
            
            // If no data, add a placeholder so the graph still displays
            if (dataset.getRowCount() == 0) {
                System.out.println("Active calories graph: No data found, adding placeholder");
                dataset.addValue(0, "Calories", "No data");
            }
        } else {
            // Fallback to example data if no database connection
            dataset.addValue(0, "Calories", "No data");
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Calories Burnt During Exercise",
                "Date",
                "Calories",
                dataset
        );
        
        // Ensure chart is visible even with empty data
        chart.getPlot().setNoDataMessage("No workout calories data available");

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
