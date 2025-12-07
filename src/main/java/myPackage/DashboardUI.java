package myPackage;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

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
    private GoalManager goalManager;
    private JProgressBar calorieProgressBar;
    private JLabel goalStatusLabel;
    
    // References to Classes tab components for refreshing
    private DefaultListModel<WorkoutClass> classListModel;
    private JList<WorkoutClass> classList;
    private JTabbedPane tabbedPane;
    
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
        this.goalManager = new GoalManager();
        
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
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(BAYLOR_GREEN);
        
        // Data Tab (main dashboard with real data)
        JPanel dataTab = createDataTab();
        tabbedPane.addTab("Data", dataTab);
        
        // Friends Tab (placeholder)
        JPanel friendsTab = createFriendsTab();
        tabbedPane.addTab("Friends", friendsTab);

        // Goals Tab
        JPanel goalsTab = createGoalsTab();
        tabbedPane.addTab("Goals", goalsTab);
        
        // Classes Tab (placeholder)
        JPanel classesTab = createClassesTab();
        tabbedPane.addTab("Classes", classesTab);

        //historical Tab
        JPanel historicalTab = createHistoricalTab();
        tabbedPane.addTab("Historical", historicalTab);
        
        // Create Class Tab (only for trainers)
        if (isTrainer()) {
            JPanel createClassTab = createCreateClassTab();
            tabbedPane.addTab("Create Class", createClassTab);
        }
        
        // Achievements Tab (placeholder)
        JPanel achievementsTab = createAchievementsTab();
        tabbedPane.addTab("Achievements", achievementsTab);
        
        // Add listener to refresh Classes tab when it becomes visible
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                String selectedTitle = tabbedPane.getTitleAt(selectedIndex);
                if ("Classes".equals(selectedTitle) && isTrainer() && classListModel != null) {
                    refreshClassesList();
                }
            }
        });
        
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

        // Calorie goal progress bar
        mainPanel.add(new JLabel("Calorie Goal Progress:"));
        calorieProgressBar = new JProgressBar(0, 100);
        calorieProgressBar.setStringPainted(true);
        calorieProgressBar.setValue(0);
        calorieProgressBar.setString("No goal set");
        mainPanel.add(calorieProgressBar);

        // Statue text for how close the user is to goal
        mainPanel.add(new JLabel("Goal Status:"));
        goalStatusLabel = new JLabel("Set a goal in the Goals tab to start tracking.");
        mainPanel.add(goalStatusLabel);
        
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

    private JPanel createGoalsTab() {
        JPanel goalsPanel = new JPanel(new BorderLayout());
        goalsPanel.setBackground(BACKGROUND_COLOR);
        goalsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel header = new JLabel("Set Goals and Track Progress", SwingConstants.CENTER);
        goalsPanel.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBackground(BACKGROUND_COLOR);

        // Form fields
        JTextField goalNameField = new JTextField();
        JComboBox<String> objectiveField = new JComboBox<>(new String[] {
                "Weight Loss", "Build Strength", "Endurance", "General Health"
        });
        JTextField caloriesField = new JTextField();
        JTextField exerciseField = new JTextField();
        JTextField frequencyField = new JTextField();
        JTextField intensityField = new JTextField();
        JTextField durationField = new JTextField();
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);

        // Add rows
        form.add(new JLabel("Goal Name:"));
        form.add(goalNameField);
        form.add(new JLabel("Overall Objective:"));
        form.add(objectiveField);
        form.add(new JLabel("Daily Calorie Target:"));
        form.add(caloriesField);
        form.add(new JLabel("Exercise Type:"));
        form.add(exerciseField);
        form.add(new JLabel("Frequency:"));
        form.add(frequencyField);
        form.add(new JLabel("Intensity:"));
        form.add(intensityField);
        form.add(new JLabel("Duration:"));
        form.add(durationField);
        form.add(new JLabel("Description:"));
        form.add(new JScrollPane(descriptionArea));

        // Pre-fill if the goal exists
        Goal existing = goalManager.getCurrentGoal();
        if (existing != null) {
            goalNameField.setText(existing.getGoalName());
            objectiveField.setSelectedItem(existing.getFitnessObjective());
            caloriesField.setText(existing.getCalories() != null ? existing.getCalories().toString() : "");
            exerciseField.setText(existing.getExerciseType());
            frequencyField.setText(existing.getFrequency());
            intensityField.setText(existing.getIntensity());
            durationField.setText(existing.getDuration());
            descriptionArea.setText(existing.getDescription());
        }

        goalsPanel.add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save Goals");
        saveBtn.setBackground(BAYLOR_GREEN);
        saveBtn.setForeground(Color.BLACK);

        JLabel statusLabel = new JLabel(" ", SwingConstants.LEFT);

        saveBtn.addActionListener(e -> {
            Integer calories = null;
            if (!caloriesField.getText().trim().isEmpty()) {
                try {
                    calories = Integer.parseInt(caloriesField.getText().trim());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Calories must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Goal newGoal = new Goal(
                    goalNameField.getText().trim(),
                    (String) objectiveField.getSelectedItem(),
                    calories,
                    exerciseField.getText().trim(),
                    frequencyField.getText().trim(),
                    intensityField.getText().trim(),
                    durationField.getText().trim(),
                    descriptionArea.getText().trim()
            );

            boolean ok = goalManager.saveGoals(userId, newGoal);

            if (ok) {
                statusLabel.setText("Goals saved successfully.");
                statusLabel.setForeground(new Color(0, 128, 164));
            }
            else {
                statusLabel.setText("Failed to save goals.");
                statusLabel.setForeground(Color.RED);
            }
        });

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(BACKGROUND_COLOR);
        bottom.add(statusLabel, BorderLayout.CENTER);
        bottom.add(saveBtn, BorderLayout.EAST);

        goalsPanel.add(bottom, BorderLayout.SOUTH);

        return goalsPanel;
    }

    private void updateGoalProgress(Integer caloriesConsumed) {
        if (calorieProgressBar == null || goalStatusLabel == null) {
            return; // Data tab not built yet
        }

        // If you don't have GoalManager hooked up yet, this will just show "No goal set"
        Goal goal = (goalManager != null) ? goalManager.getCurrentGoal() : null;

        // No goal set at all
        if (goal == null || goal.getCalories() == null) {
            calorieProgressBar.setValue(0);
            calorieProgressBar.setString("No goal set");
            goalStatusLabel.setText("Set a goal in the Goals tab to start tracking.");
            goalStatusLabel.setForeground(Color.DARK_GRAY);
            return;
        }

        int target = goal.getCalories();
        if (target <= 0) {
            calorieProgressBar.setValue(0);
            calorieProgressBar.setString("Invalid goal");
            goalStatusLabel.setText("Calorie goal must be greater than 0.");
            goalStatusLabel.setForeground(Color.RED);
            return;
        }

        // No recent data
        if (caloriesConsumed == null) {
            calorieProgressBar.setValue(0);
            calorieProgressBar.setString("0% of " + target + " kcal");
            goalStatusLabel.setText("No recent data. Log today's calories to start tracking.");
            goalStatusLabel.setForeground(new Color(128, 64, 0));
            return;
        }

        // Compute % of goal reached
        double ratio = (double) caloriesConsumed / target;
        int percent = (int) Math.round(ratio * 100);
        percent = Math.max(0, Math.min(percent, 200));

        calorieProgressBar.setValue(Math.min(percent, 100));
        calorieProgressBar.setString(percent + "% of " + target + " kcal");

        if (percent >= 100) {
            goalStatusLabel.setText("🎉 You reached your calorie goal today!");
            goalStatusLabel.setForeground(new Color(0, 128, 64));
        } else if (percent >= 75) {
            goalStatusLabel.setText("Almost there! " + (target - caloriesConsumed) + " kcal to go.");
            goalStatusLabel.setForeground(new Color(0, 102, 204));
        } else {
            goalStatusLabel.setText("You have " + (target - caloriesConsumed) + " kcal remaining.");
            goalStatusLabel.setForeground(new Color(128, 64, 0));
        }
    }


    private JPanel createClassesTab() {
        // Client view: show available classes and allow registration
        if (!isTrainer()) {
            return createClientClassesTab();
        }

        // Trainer view: list of all classes and enrolled users for the selected class
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel with title and refresh button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel header = new JLabel("All Classes (for all trainers)", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(BAYLOR_GREEN);
        headerPanel.add(header, BorderLayout.CENTER);
        
        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(BAYLOR_GREEN);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setOpaque(true);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshButton.setPreferredSize(new Dimension(80, 30));
        refreshButton.addActionListener(e -> refreshClassesList());
        
        // Add hover effect
        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(BAYLOR_GREEN);
            }
        });
        
        headerPanel.add(refreshButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(BACKGROUND_COLOR);

        // Left: classes list
        classListModel = new DefaultListModel<>();
        classList = new JList<>(classListModel);
        classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        classList.setVisibleRowCount(12);

        // Load all classes from database
        refreshClassesList();

        JScrollPane classScroll = new JScrollPane(classList);
        classScroll.setBorder(BorderFactory.createTitledBorder("Classes"));

        // Right: enrolled users for selected class
        DefaultListModel<String> userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        userList.setVisibleRowCount(12);
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setBorder(BorderFactory.createTitledBorder("Enrolled Users"));

        // When a class is selected, load its enrolled users
        classList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    userListModel.clear();
                    WorkoutClass selected = classList.getSelectedValue();
                    if (selected != null) {
                        List<String> users = dbManager.getUsersForClass(selected.getId());
                        if (users.isEmpty()) {
                            userListModel.addElement("No users enrolled yet.");
                        } else {
                            for (String u : users) {
                                userListModel.addElement(u);
                            }
                        }
                    }
                }
            }
        });

        centerPanel.add(classScroll);
        centerPanel.add(userScroll);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        return mainPanel;
    }
    
    // Method to refresh the classes list from the database
    private void refreshClassesList() {
        if (classListModel != null && dbManager != null) {
            classListModel.clear();
            List<WorkoutClass> classes = dbManager.getAllClasses();
            for (WorkoutClass wc : classes) {
                classListModel.addElement(wc);
            }
        }
    }

    // Client view for Classes tab - shows available classes and allows registration
    private JPanel createClientClassesTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create tabbed pane for Available Classes and My Classes
        JTabbedPane clientTabbedPane = new JTabbedPane();
        clientTabbedPane.setBackground(BACKGROUND_COLOR);
        clientTabbedPane.setForeground(BAYLOR_GREEN);

        // Available Classes tab
        JPanel availablePanel = createAvailableClassesPanel();
        clientTabbedPane.addTab("Available Classes", availablePanel);

        // My Classes tab (enrolled classes)
        JPanel enrolledPanel = createEnrolledClassesPanel();
        clientTabbedPane.addTab("My Classes", enrolledPanel);

        mainPanel.add(clientTabbedPane, BorderLayout.CENTER);

        // Refresh when switching tabs
        clientTabbedPane.addChangeListener(e -> {
            int selectedIndex = clientTabbedPane.getSelectedIndex();
            if (selectedIndex == 0) {
                // Refresh available classes
                refreshAvailableClasses();
            } else if (selectedIndex == 1) {
                // Refresh enrolled classes
                refreshEnrolledClasses();
            }
        });

        return mainPanel;
    }

    // Panel showing all available classes with register buttons
    private DefaultListModel<WorkoutClass> availableClassesModel;
    private JList<WorkoutClass> availableClassesList;

    private JPanel createAvailableClassesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with refresh button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel header = new JLabel("Available Classes", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(BAYLOR_GREEN);
        headerPanel.add(header, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(BAYLOR_GREEN);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setOpaque(true);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshButton.setPreferredSize(new Dimension(80, 30));
        refreshButton.addActionListener(e -> refreshAvailableClasses());
        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(BAYLOR_GREEN);
            }
        });
        headerPanel.add(refreshButton, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Classes list
        availableClassesModel = new DefaultListModel<>();
        availableClassesList = new JList<>(availableClassesModel);
        availableClassesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableClassesList.setCellRenderer(new ClassListCellRenderer());
        availableClassesList.setVisibleRowCount(10);

        JScrollPane scrollPane = new JScrollPane(availableClassesList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Select a class to register"));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Details and register button panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(BACKGROUND_COLOR);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsArea.setBackground(BACKGROUND_COLOR);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setText("Select a class to see details and register.");
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setPreferredSize(new Dimension(0, 150));
        detailsPanel.add(detailsScroll, BorderLayout.CENTER);

        JButton registerButton = new JButton("Register for Selected Class");
        registerButton.setBackground(BAYLOR_GREEN);
        registerButton.setForeground(Color.WHITE);
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setPreferredSize(new Dimension(0, 40));
        registerButton.setEnabled(false);
        registerButton.addActionListener(e -> {
            WorkoutClass selected = availableClassesList.getSelectedValue();
            if (selected != null) {
                registerForClass(selected, detailsArea);
            }
        });
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (registerButton.isEnabled()) {
                    registerButton.setBackground(LIGHT_GREEN);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(BAYLOR_GREEN);
            }
        });
        detailsPanel.add(registerButton, BorderLayout.SOUTH);

        // Update details when class is selected
        availableClassesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                WorkoutClass selected = availableClassesList.getSelectedValue();
                if (selected != null) {
                    int currentEnrolled = dbManager.getCurrentEnrollmentCount(selected.getId());
                    int spotsAvailable = selected.getMaxParticipants() - currentEnrolled;
                    boolean alreadyEnrolled = userId != -1 && dbManager.isUserEnrolled(userId, selected.getId());
                    
                    String details = "Class Type: " + selected.getClassType() + "\n" +
                                    "Description: " + (selected.getDescription() != null ? selected.getDescription() : "N/A") + "\n" +
                                    "Trainer: " + selected.getTrainerUsername() + "\n" +
                                    "Start Time: " + selected.getStartTime() + "\n" +
                                    "End Time: " + selected.getEndTime() + "\n" +
                                    "Cost: $" + String.format("%.2f", selected.getCost()) + "\n" +
                                    "Spots Available: " + spotsAvailable + " / " + selected.getMaxParticipants() + "\n" +
                                    (alreadyEnrolled ? "\n⚠ You are already enrolled in this class." : "");
                    
                    detailsArea.setText(details);
                    registerButton.setEnabled(!alreadyEnrolled && spotsAvailable > 0);
                } else {
                    detailsArea.setText("Select a class to see details and register.");
                    registerButton.setEnabled(false);
                }
            }
        });

        panel.add(detailsPanel, BorderLayout.SOUTH);

        // Load classes
        refreshAvailableClasses();

        return panel;
    }

    // Panel showing user's enrolled classes
    private DefaultListModel<WorkoutClass> enrolledClassesModel;
    private JList<WorkoutClass> enrolledClassesList;

    private JPanel createEnrolledClassesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("My Enrolled Classes", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(BAYLOR_GREEN);
        panel.add(header, BorderLayout.NORTH);

        enrolledClassesModel = new DefaultListModel<>();
        enrolledClassesList = new JList<>(enrolledClassesModel);
        enrolledClassesList.setCellRenderer(new ClassListCellRenderer());
        enrolledClassesList.setVisibleRowCount(15);

        JScrollPane scrollPane = new JScrollPane(enrolledClassesList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Classes you are registered for"));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load enrolled classes
        refreshEnrolledClasses();

        return panel;
    }

    private void refreshAvailableClasses() {
        if (availableClassesModel != null && dbManager != null) {
            availableClassesModel.clear();
            List<WorkoutClass> classes = dbManager.getAllClasses();
            for (WorkoutClass wc : classes) {
                availableClassesModel.addElement(wc);
            }
        }
    }

    private void refreshEnrolledClasses() {
        if (enrolledClassesModel != null && dbManager != null && userId != -1) {
            enrolledClassesModel.clear();
            List<WorkoutClass> classes = dbManager.getUserEnrolledClasses(userId);
            if (classes.isEmpty()) {
                // Add a placeholder message
                // We'll handle this differently - maybe show a label
            } else {
                for (WorkoutClass wc : classes) {
                    enrolledClassesModel.addElement(wc);
                }
            }
        }
    }

    private void registerForClass(WorkoutClass workoutClass, JTextArea detailsArea) {
        if (userId == -1) {
            JOptionPane.showMessageDialog(this,
                "Unable to register: User not found.",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if already enrolled
        if (dbManager.isUserEnrolled(userId, workoutClass.getId())) {
            JOptionPane.showMessageDialog(this,
                "You are already enrolled in this class.",
                "Already Enrolled",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Check if class is full
        int currentEnrolled = dbManager.getCurrentEnrollmentCount(workoutClass.getId());
        if (currentEnrolled >= workoutClass.getMaxParticipants()) {
            JOptionPane.showMessageDialog(this,
                "This class is full. Please select another class.",
                "Class Full",
                JOptionPane.WARNING_MESSAGE);
            refreshAvailableClasses();
            return;
        }

        // Attempt enrollment
        boolean success = dbManager.enrollUserInClass(userId, workoutClass.getId());
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Successfully registered for " + workoutClass.getClassType() + "!",
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE);
            // Refresh both lists
            refreshAvailableClasses();
            refreshEnrolledClasses();
            // Update details area
            availableClassesList.clearSelection();
            detailsArea.setText("Select a class to see details and register.");
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to register. The class may be full or you may already be enrolled.",
                "Registration Failed",
                JOptionPane.ERROR_MESSAGE);
            refreshAvailableClasses();
        }
    }

    // Custom cell renderer for class list to show more info
    private class ClassListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                     boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof WorkoutClass) {
                WorkoutClass wc = (WorkoutClass) value;
                int currentEnrolled = dbManager.getCurrentEnrollmentCount(wc.getId());
                int spotsAvailable = wc.getMaxParticipants() - currentEnrolled;
                String text = wc.getClassType() + " - " + wc.getStartTime() + 
                             " ($" + String.format("%.2f", wc.getCost()) + ") - " +
                             spotsAvailable + " spots available";
                setText(text);
            }
            return this;
        }
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
            // Open CreateClass window, passing the current trainer's username
            // and reusing the existing DatabaseManager connection
            SwingUtilities.invokeLater(() -> {
                CreateClass.CreateAndShowGUI(username, dbManager);
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

        /**
         * Modify Existing Class button
          */
        JButton modifyClassButton = new JButton("Modify Existing Class");
        modifyClassButton.setBackground(BAYLOR_GREEN);
        modifyClassButton.setForeground(Color.WHITE);
        modifyClassButton.setOpaque(true);
        modifyClassButton.setBorderPainted(false);
        modifyClassButton.setFocusPainted(false);
        modifyClassButton.setFont(new Font("Arial", Font.BOLD, 16));
        modifyClassButton.setPreferredSize(new Dimension(200, 50));

        modifyClassButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                // Open ModifyClass window and refresh lists after successful update
                ModifyClass.openModifyClassPage(username, dbManager, () -> {
                    refreshClassesList();     // Trainer “Classes” tab
                    refreshAvailableClasses(); // Client “Available Classes” tab
                    refreshEnrolledClasses();  // Client “My Classes” tab
                });
            });
        });

        // Hover effect
        modifyClassButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                modifyClassButton.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                modifyClassButton.setBackground(BAYLOR_GREEN);
            }
        });

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        centerPanel.add(modifyClassButton, gbc);
        
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
        gbc.gridy = 3;
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

    private JPanel createHistoricalTab() {
        JPanel historicalPanel = new JPanel(new BorderLayout());
        JPanel optionsPanel = new JPanel(new GridLayout(3, 1));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JButton daysButton = new JButton("Days");
        JButton weeksButton = new JButton("Weeks");
        JButton monthsButton = new JButton("Months");
        //adding buttons yo
        optionsPanel.add(daysButton);
        optionsPanel.add(weeksButton);
        optionsPanel.add(monthsButton);

        //JPanel
        historicalPanel.setBackground(BACKGROUND_COLOR);
        historicalPanel.add(optionsPanel, BorderLayout.LINE_START);
        return historicalPanel;
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
            updateGoalProgress(caloriesConsumed);
        } else {
            // No data found
            caloriesLabel.setText("Calories Consumed: No data");
            burnedLabel.setText("Calories Burned: No data");
            weightLabel.setText("Weight: No data");
            sleepLabel.setText("Sleep: No data");
            updateGoalProgress(null);
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
        
        // Add action listener to Quick Add Calories button
        quickCalories.addActionListener(e -> {
            if (userId == -1) {
                JOptionPane.showMessageDialog(DashboardUI.this,
                    "Unable to add data: User not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Open AddData page with current user's ID and database manager
            // Pass a callback to refresh dashboard data after saving
            AddData.openAddDataPage(userId, dbManager, () -> {
                loadUserData();
                checkForReminders();
            });
        });
        
        // TODO: Add action listener for Quick Add Workout button when implemented
        
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
        SwingUtilities.invokeLater(() -> new DashboardUI("hippowenc"));
    }
}
