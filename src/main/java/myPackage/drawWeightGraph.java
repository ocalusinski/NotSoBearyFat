package myPackage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class drawWeightGraph extends JPanel {
    public drawWeightGraph() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(135, "Weight", "12-01-2025");
        dataset.addValue(137, "Weight", "12-02-2025");
        dataset.addValue(139, "Weight", "12-03-2025");

        JFreeChart chart = ChartFactory.createLineChart(
                "Weight",
                "Date",
                "Weight",
                dataset
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(320, 200));

        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.NORTH);

    }
}
