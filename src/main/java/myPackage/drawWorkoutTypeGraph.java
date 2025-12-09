package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static myPackage.Constants.DB_MANAGER;

public class drawWorkoutTypeGraph extends JPanel {
    public drawWorkoutTypeGraph() {
        this(-1, 0);
    }
    
    public drawWorkoutTypeGraph(int userId, int days) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        if (userId != -1 && DB_MANAGER != null) {
            Map<String, Integer> counts = DB_MANAGER.getWorkoutCountsByType(userId, days);
            System.out.println("Workout type graph: Retrieved " + counts.size() + " workout types for user " + userId);
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                dataset.addValue(entry.getValue(), "Count", entry.getKey());
                System.out.println("Workout type graph: Adding " + entry.getValue() + " workouts of type " + entry.getKey());
            }
            System.out.println("Workout type graph: Dataset has " + dataset.getRowCount() + " rows");
        } else {
            // Fallback to example data if no database connection
            dataset.addValue(0, "Count", "Walking");
            dataset.addValue(0, "Count", "Running");
            dataset.addValue(0, "Count", "HIIT");
            dataset.addValue(0, "Count", "Lifting");
            dataset.addValue(0, "Count", "Calisthenics");
            dataset.addValue(0, "Count", "Yoga");
            dataset.addValue(0, "Count", "Other");
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Workout Count",
                "Workout Type",
                "Count",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 250));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);
    }

}
