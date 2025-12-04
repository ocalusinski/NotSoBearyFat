package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class drawWorkoutTypeGraph extends JPanel {
    public drawWorkoutTypeGraph() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(3, "Count", "Walking");
        dataset.addValue(1, "Count", "Running");
        dataset.addValue(4, "Count", "HIIT");
        dataset.addValue(0, "Count", "Weights");
        dataset.addValue(7, "Count", "Other");

        JFreeChart chart = ChartFactory.createBarChart(
                "Workout Count",
                "Workout Type",
                "Count",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(375, 150));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.NORTH);
    }

}
