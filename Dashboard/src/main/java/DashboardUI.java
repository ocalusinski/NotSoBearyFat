import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JFrame {
    private JLabel caloriesLabel;
    private JLabel weightLabel;
    private JLabel sleepLabel;
    private JLabel burnedLabel;
    private JLabel messageLabel;

    public DashboardUI(String username) {
        setTitle("Dashboard - Not So Beary Fat");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Welcome back, " + username + "!", SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(new Color(108, 93, 128));
        header.setForeground(Color.WHITE);
        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(new Color(219, 179, 203));

        caloriesLabel = new JLabel("Calories Consumed: 2100 kcal");
        burnedLabel = new JLabel("Calories Burned: 500 kcal");
        weightLabel = new JLabel("Weight: 150 lbs");
        sleepLabel = new JLabel("Sleep: 7 hrs");

        mainPanel.add(new JLabel("Calorie Intake:"));
        mainPanel.add(caloriesLabel);
        mainPanel.add(new JLabel("Calories Burned:"));
        mainPanel.add(burnedLabel);
        mainPanel.add(new JLabel("Weight:"));
        mainPanel.add(weightLabel);
        mainPanel.add(new JLabel("Sleep:"));
        mainPanel.add(sleepLabel);
        add(mainPanel, BorderLayout.CENTER);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(messageLabel, BorderLayout.SOUTH);

        checkForReminders();
        setVisible(true);

    }

    private void checkForReminders() {
        boolean noRecentData = Math.random() > 0.7;
        boolean loadError = Math.random() > 0.85;

        if (loadError) {
            messageLabel.setText("We're having trouble displaying your progress right now.");
            messageLabel.setForeground(Color.RED);
        }
        else if (noRecentData) {
            messageLabel.setText("No entries for the last 7 days. Add today's data to get back on track!");
            messageLabel.setForeground(new Color(90, 0, 128));
            addQuickLogButtons();
        }
        else {
            messageLabel.setText("Progress data updated successfully");
            messageLabel.setForeground(new Color(0, 128, 64));
        }
    }

    private void addQuickLogButtons() {
        JPanel quickPanel = new JPanel(new FlowLayout());
        JButton quickCalories = new JButton("Quick Add Calories");
        JButton quickWorkout = new JButton("Quick Add Workout");
        quickPanel.add(quickCalories);
        quickPanel.add(quickWorkout);
        add(quickPanel, BorderLayout.EAST);
        validate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardUI("Lademi"));
    }

}
