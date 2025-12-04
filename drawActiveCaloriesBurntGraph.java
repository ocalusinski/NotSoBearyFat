import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class drawActiveCaloriesBurntGraph extends JPanel {

    public drawActiveCaloriesBurntGraph() {

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
        chartPanel.setPreferredSize(new java.awt.Dimension(320, 150));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.SOUTH);
    }
}
