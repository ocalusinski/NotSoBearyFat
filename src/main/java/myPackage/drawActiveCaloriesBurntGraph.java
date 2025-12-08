package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class drawActiveCaloriesBurntGraph extends JPanel {

    public drawActiveCaloriesBurntGraph() {
        this(-1, null, 0);
    }
    
    public drawActiveCaloriesBurntGraph(int userId, DatabaseManager dbManager, int days) {
        // TODO: Connect to workout data table when available
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(500, "Calories", "12-01-2025");
        dataset.addValue(200, "Calories", "12-02-2025");
        dataset.addValue(300, "Calories", "12-03-2025");

        JFreeChart chart = ChartFactory.createLineChart(
                "Calories Burnt During Exercise",
                "Date",
                "Calories",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 350));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);
    }
}
