import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JFrame {
    private JLabel caloriesLabel;
    private JLabel weightLabel;
    private JLabel sleepLabel;
    private JLabel burnedLabel;
    private JLabel messageLabel;
    private DatabaseManager dbManager;
    private int userId;
    private String username;
    private String userType;
    
    // Baylor green color scheme
    private static final Color BAYLOR_GREEN = new Color(0, 71, 56);
    private static final Color LIGHT_GREEN = new Color(0, 100, 80);
    private static final Color BACKGROUND_COLOR = new Color(240, 255, 250);

    public DashboardUI(String username) {
        this(username, null);
    }
    
    public DashboardUI(String username, String userType) {
        this.username = username;
        this.userType = userType;
        this.dbManager = new DatabaseManager();
        
        // Try to get user ID by username first, then by first name (for backward compatibility)
        this.userId = dbManager.getUserIdByUsername(username);
        if (this.userId == -1) {
            this.userId = dbManager.getUserIdByFirstName(username);
        }
        
        // If userType not provided, try to get it from database
        if (this.userType == null && this.userId != -1) {
            this.userType = dbManager.getUserType(this.userId);
        }
        
        setTitle("Dashboard - Not So Beary Fat");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Header with logout button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BAYLOR_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel header = new JLabel("Welcome back, " + username + "!", SwingConstants.CENTER);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(header, BorderLayout.CENTER);
        
        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(LIGHT_GREEN);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setMargin(new Insets(5, 10, 5, 10));
        logoutButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginPage());
        });
        
        // Add hover effect
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(0, 120, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(LIGHT_GREEN);
            }
        });
        
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(BAYLOR_GREEN);
        
        // Data Tab (main dashboard with real data)
        JPanel dataTab = createDataTab();
        tabbedPane.addTab("Data", dataTab);
        
        // Friends Tab (placeholder)
        JPanel friendsTab = createFriendsTab();
        tabbedPane.addTab("Friends", friendsTab);
        
        // Classes Tab (placeholder)
        JPanel classesTab = createClassesTab();
        tabbedPane.addTab("Classes", classesTab);
        
        // Create Class Tab (only for trainers)
        if (isTrainer()) {
            JPanel createClassTab = createCreateClassTab();
            tabbedPane.addTab("Create Class", createClassTab);
        }
        
        // Achievements Tab (placeholder)
        JPanel achievementsTab = createAchievementsTab();
        tabbedPane.addTab("Achievements", achievementsTab);
        
        add(tabbedPane, BorderLayout.CENTER);

        // Message label at bottom
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(messageLabel, BorderLayout.SOUTH);

        // Load and display real data
        loadUserData();
        checkForReminders();
        
        setVisible(true);
    }

    private JPanel createDataTab() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(BACKGROUND_COLOR);

        caloriesLabel = new JLabel("Calories Consumed: -- kcal");
        burnedLabel = new JLabel("Calories Burned: -- kcal");
        weightLabel = new JLabel("Weight: -- lbs");
        sleepLabel = new JLabel("Sleep: -- hrs");

        // Style the labels
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        caloriesLabel.setFont(labelFont);
        burnedLabel.setFont(labelFont);
        weightLabel.setFont(labelFont);
        sleepLabel.setFont(labelFont);

        mainPanel.add(new JLabel("Calorie Intake:"));
        mainPanel.add(caloriesLabel);
        mainPanel.add(new JLabel("Calories Burned:"));
        mainPanel.add(burnedLabel);
        mainPanel.add(new JLabel("Weight:"));
        mainPanel.add(weightLabel);
        mainPanel.add(new JLabel("Sleep:"));
        mainPanel.add(sleepLabel);
        
        return mainPanel;
    }

    private JPanel createFriendsTab() {
        JPanel friendsPanel = new JPanel(new BorderLayout());
        friendsPanel.setBackground(BACKGROUND_COLOR);
        friendsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel placeholderLabel = new JLabel(
            "<html><div style='text-align: center;'>" +
            "<h2>Friends</h2>" +
            "<p>This feature will be implemented in the future.</p>" +
            "<p>Here you'll be able to:</p>" +
            "<ul style='text-align: left; display: inline-block;'>" +
            "<li>View your friends list</li>" +
            "<li>Add new friends</li>" +
            "<li>See friends' progress</li>" +
            "<li>Compare achievements</li>" +
            "</ul>" +
            "</div></html>",
            SwingConstants.CENTER
        );
        placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        friendsPanel.add(placeholderLabel, BorderLayout.CENTER);
        
        return friendsPanel;
    }

    private JPanel createClassesTab() {
        JPanel classesPanel = new JPanel(new BorderLayout());
        classesPanel.setBackground(BACKGROUND_COLOR);
        classesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel placeholderLabel = new JLabel(
            "<html><div style='text-align: center;'>" +
            "<h2>Classes</h2>" +
            "<p>This feature will be implemented in the future.</p>" +
            "<p>Here you'll be able to:</p>" +
            "<ul style='text-align: left; display: inline-block;'>" +
            "<li>View available fitness classes</li>" +
            "<li>Register for classes</li>" +
            "<li>View your enrolled classes</li>" +
            "<li>See class schedules</li>" +
            "</ul>" +
            "</div></html>",
            SwingConstants.CENTER
        );
        placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        classesPanel.add(placeholderLabel, BorderLayout.CENTER);
        
        return classesPanel;
    }

    private JPanel createCreateClassTab() {
        JPanel createClassPanel = new JPanel(new BorderLayout());
        createClassPanel.setBackground(BACKGROUND_COLOR);
        createClassPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Center panel with button to create class
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel titleLabel = new JLabel("Create New Class");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(BAYLOR_GREEN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 30, 20);
        centerPanel.add(titleLabel, gbc);
        
        JButton createClassButton = new JButton("Create Class");
        createClassButton.setBackground(BAYLOR_GREEN);
        createClassButton.setForeground(Color.WHITE);
        createClassButton.setOpaque(true);
        createClassButton.setBorderPainted(false);
        createClassButton.setFocusPainted(false);
        createClassButton.setFont(new Font("Arial", Font.BOLD, 16));
        createClassButton.setPreferredSize(new Dimension(200, 50));
        createClassButton.addActionListener(e -> {
            // Open CreateClass window
            SwingUtilities.invokeLater(() -> {
                CreateClass.CreateAndShowGUI();
            });
        });
        
        // Add hover effect
        createClassButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                createClassButton.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                createClassButton.setBackground(BAYLOR_GREEN);
            }
        });
        
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 20, 20);
        centerPanel.add(createClassButton, gbc);
        
        JLabel infoLabel = new JLabel(
            "<html><div style='text-align: center;'>" +
            "<p>Click the button above to create a new fitness class.</p>" +
            "<p>You'll be able to set:</p>" +
            "<ul style='text-align: left; display: inline-block;'>" +
            "<li>Class type and description</li>" +
            "<li>Start and end times</li>" +
            "<li>Maximum participants</li>" +
            "<li>Cost</li>" +
            "</ul>" +
            "</div></html>"
        );
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        centerPanel.add(infoLabel, gbc);
        
        createClassPanel.add(centerPanel, BorderLayout.CENTER);
        
        return createClassPanel;
    }

    private JPanel createAchievementsTab() {
        JPanel achievementsPanel = new JPanel(new BorderLayout());
        achievementsPanel.setBackground(BACKGROUND_COLOR);
        achievementsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel placeholderLabel = new JLabel(
            "<html><div style='text-align: center;'>" +
            "<h2>Achievements</h2>" +
            "<p>This feature will be implemented in the future.</p>" +
            "<p>Here you'll be able to:</p>" +
            "<ul style='text-align: left; display: inline-block;'>" +
            "<li>View your earned achievements</li>" +
            "<li>Track progress toward goals</li>" +
            "<li>See achievement badges</li>" +
            "<li>Compare with friends</li>" +
            "</ul>" +
            "</div></html>",
            SwingConstants.CENTER
        );
        placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        achievementsPanel.add(placeholderLabel, BorderLayout.CENTER);
        
        return achievementsPanel;
    }
    
    private boolean isTrainer() {
        return userType != null && userType.equalsIgnoreCase("trainer");
    }
    

    private void loadUserData() {
        if (userId == -1) {
            // User not found in database
            caloriesLabel.setText("Calories Consumed: N/A");
            burnedLabel.setText("Calories Burned: N/A");
            weightLabel.setText("Weight: N/A");
            sleepLabel.setText("Sleep: N/A");
            return;
        }

        double[] data = dbManager.getLatestUserDataDouble(userId);
        if (data != null && data.length == 4) {
            int caloriesConsumed = (int)data[0];
            double weight = data[1];
            double sleepHours = data[2];
            int totalCaloriesBurned = (int)data[3];
            
            caloriesLabel.setText("Calories Consumed: " + caloriesConsumed + " kcal");
            burnedLabel.setText("Calories Burned: " + totalCaloriesBurned + " kcal");
            weightLabel.setText("Weight: " + String.format("%.1f", weight) + " lbs");
            sleepLabel.setText("Sleep: " + String.format("%.1f", sleepHours) + " hrs");
        } else {
            // No data found
            caloriesLabel.setText("Calories Consumed: No data");
            burnedLabel.setText("Calories Burned: No data");
            weightLabel.setText("Weight: No data");
            sleepLabel.setText("Sleep: No data");
        }
    }

    private void checkForReminders() {
        if (userId == -1) {
            messageLabel.setText("User not found in database.");
            messageLabel.setForeground(Color.RED);
            return;
        }

        boolean hasRecentData = dbManager.hasRecentData(userId);
        
        if (!hasRecentData) {
            messageLabel.setText("No entries for the last 7 days. Add today's data to get back on track!");
            messageLabel.setForeground(new Color(200, 0, 0));
            addQuickLogButtons();
        } else {
            messageLabel.setText("Progress data updated successfully");
            messageLabel.setForeground(new Color(0, 128, 64));
        }
    }

    private void addQuickLogButtons() {
        JPanel quickPanel = new JPanel(new FlowLayout());
        quickPanel.setBackground(BACKGROUND_COLOR);
        
        JButton quickCalories = new JButton("Quick Add Calories");
        JButton quickWorkout = new JButton("Quick Add Workout");
        
        // Style buttons with Baylor green
        quickCalories.setBackground(BAYLOR_GREEN);
        quickCalories.setForeground(Color.WHITE);
        quickCalories.setOpaque(true);
        quickCalories.setBorderPainted(false);
        quickCalories.setFocusPainted(false);
        
        quickWorkout.setBackground(BAYLOR_GREEN);
        quickWorkout.setForeground(Color.WHITE);
        quickWorkout.setOpaque(true);
        quickWorkout.setBorderPainted(false);
        quickWorkout.setFocusPainted(false);
        
        // Add hover effects
        quickCalories.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                quickCalories.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                quickCalories.setBackground(BAYLOR_GREEN);
            }
        });
        
        quickWorkout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                quickWorkout.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                quickWorkout.setBackground(BAYLOR_GREEN);
            }
        });
        
        quickPanel.add(quickCalories);
        quickPanel.add(quickWorkout);
        
        // Add to a panel that can be placed in the data tab
        // For now, we'll add it to the message area or create a separate panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.add(messageLabel, BorderLayout.CENTER);
        bottomPanel.add(quickPanel, BorderLayout.SOUTH);
        
        // Replace the message label area
        remove(messageLabel);
        add(bottomPanel, BorderLayout.SOUTH);
        validate();
    }

    @Override
    public void dispose() {
        if (dbManager != null) {
            dbManager.closeConnection();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        // Test main - in production, DashboardUI is called from LoginPage with actual first name
        SwingUtilities.invokeLater(() -> new DashboardUI("Lademi"));
    }
}
