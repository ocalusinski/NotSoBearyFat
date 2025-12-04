package myPackage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class drawGraphs {
    public drawGraphs() {

        JFrame frame = new JFrame("Historical Trends Graphs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,600);

        JPanel menuBar = new JPanel();
        menuBar.setLayout(new GridBagLayout());
        menuBar.setPreferredSize(new Dimension(700, 300));
        GridBagConstraints g = new GridBagConstraints();
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(100, 30));
        JLabel item = new JLabel("Historical Trends Graphs");
        menuBar.add(item, g);

        JButton nextButton = new JButton("Next");

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());


        drawCaloriesConsumedGraph calorieGraph = new drawCaloriesConsumedGraph();
        drawWeightGraph weightGraph = new drawWeightGraph();
        drawSleepGraph sleepGraph = new drawSleepGraph();
        drawTotalCaloriesBurntGraph burntGraph = new drawTotalCaloriesBurntGraph();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 3, 3, 3);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(calorieGraph, gbc);
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(weightGraph, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(sleepGraph, gbc);
        gbc.insets = new Insets(0, 3, 3, 3);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(burntGraph, gbc);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(nextButton, BorderLayout.EAST);

        frame.add(menuBar, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawNextPage();
                frame.setVisible(false);
            }
        });
    }

    private static void drawNextPage(){
        JFrame frame = new JFrame("Workout Trends Graphs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,600);

        JPanel menuBar = new JPanel();
        menuBar.setLayout(new GridBagLayout());
        menuBar.setPreferredSize(new Dimension(700, 300));
        GridBagConstraints g = new GridBagConstraints();
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(100, 30));
        JLabel item = new JLabel("Workout Trends Graphs");
        menuBar.add(item, g);

        JButton backButton = new JButton("Back");

        drawWorkoutTypeGraph drawWorkoutTypeGraph = new drawWorkoutTypeGraph();
        drawMinutesOfExerciseGraph drawMinutesOfExerciseGraph = new drawMinutesOfExerciseGraph();
        drawActiveCaloriesBurntGraph drawActiveCaloriesBurntGraph = new drawActiveCaloriesBurntGraph();

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(drawWorkoutTypeGraph, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 0, 20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(drawMinutesOfExerciseGraph, gbc);
        gbc.insets = new Insets(10, 20, 0, 0);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(drawActiveCaloriesBurntGraph, gbc);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(backButton, BorderLayout.WEST);

        frame.add(menuBar, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new drawGraphs();
                frame.setVisible(false);
            }
        });
    }

    public static void main(String[] args) {
        new drawGraphs();
    }
}
