package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class drawCaloriesConsumedGraph extends JPanel {
    public drawCaloriesConsumedGraph() {

        //example data, will connect to database later
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(2000, "Calories Consumed", "12-01-2025");
        dataset.addValue(3000, "Calories Consumed", "12-02-2025");
        dataset.addValue(4000, "Calories Consumed", "12-03-2025");

        JFreeChart chart = ChartFactory.createLineChart(
                "Calories Consumed",
                "Date",
                "Calories Consumed",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(320, 200));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.NORTH);
    }
}
