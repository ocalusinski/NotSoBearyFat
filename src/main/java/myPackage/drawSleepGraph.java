package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class drawSleepGraph extends JPanel {
    public drawSleepGraph() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(8, "Hours", "12-01-2025");
        dataset.addValue(7, "Hours", "12-02-2025");
        dataset.addValue(9, "Hours", "12-03-2025");

        JFreeChart chart = ChartFactory.createLineChart(
                "Hours of Sleep",
                "Date",
                "Hours of Sleep",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(320, 200));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.SOUTH);
    }
}
