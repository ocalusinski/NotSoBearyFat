package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class drawMinutesOfExerciseGraph extends JPanel {
    public drawMinutesOfExerciseGraph() {
        this(-1, null, 0);
    }
    
    public drawMinutesOfExerciseGraph(int userId, DatabaseManager dbManager, int days) {
        // TODO: Connect to workout data table when available
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(60, "Minutes", "12-01-2025");
        dataset.addValue(75, "Minutes", "12-02-2025");
        dataset.addValue(30, "Minutes", "12-03-2025");

        JFreeChart chart = ChartFactory.createLineChart(
                "Minutes of Exercise",
                "Date",
                "Minutes",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 350));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);
    }
}
