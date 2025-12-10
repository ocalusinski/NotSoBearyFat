package myPackage;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static myPackage.Constants.*;

public class DashboardUI extends JFrame {
    private JLabel caloriesLabel;
    private JLabel weightLabel;
    private JLabel sleepLabel;
    private JLabel burnedLabel;
    private int userId;
    private String username;
    private String userType;
    private ClassSearchParams csp = new ClassSearchParams();
    private GoalManager goalManager;
    private JProgressBar calorieProgressBar;
    private JLabel goalStatusLabel;
    
    // Redesigned data tab components
    private JPanel dataTabPanel;
    private JLabel caloriesValueLabel;
    private JLabel caloriesTrendLabel;
    private JLabel burnedValueLabel;
    private JLabel burnedTrendLabel;
    private JLabel weightValueLabel;
    private JLabel weightTrendLabel;
    private JLabel sleepValueLabel;
    private JLabel sleepTrendLabel;
    private JLabel netCaloriesLabel;
    private JLabel netCaloriesValueLabel;
    private JProgressBar enhancedCalorieProgressBar;
    private JLabel weeklyAvgLabel;
    private DefaultListModel<Goal> goalListModel;
    private JList<Goal> goalList;
    private int selectedGoalIndex = -1;
    private SelfPacedPlanManager selfPacedPlanManager;
    private DefaultListModel<SelfPacedPlan> libraryPlanListModel;
    private JList<SelfPacedPlan> libraryPlanList;
    private int selectedPlanIndex = -1;
    private SelfPacedPlan selectedPlan = null;
    private DefaultListModel<SelfPacedPlan> planListModel;
    private JList<SelfPacedPlan> planList;
    // Goal form field
    private JTextField goalNameField;
    private JComboBox<String> objectiveField;
    private JTextField caloriesField;
    private JTextField exerciseField;
    private JTextField frequencyField;
    private JTextField intensityField;
    private JTextField durationField;
    private JTextArea descriptionArea;


    // References to Classes tab components for refreshing
    private DefaultListModel<WorkoutClass> classListModel;
    private JList<WorkoutClass> classList;
    private JTabbedPane tabbedPane;
    
    // Calendar view state
    private java.time.LocalDate calendarCurrentMonth = java.time.LocalDate.now();
    private JPanel calendarViewPanel;
    private JLabel calendarMonthLabel;
    private JPanel calendarGridContainer;
    private JList<String> sidebarList;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JSplitPane mainSplitPane;
    private JPanel sidebarPanel;
    private boolean sidebarVisible = true;
    
    // Graph tab references for refreshing
    private JTabbedPane graphTabbedPane;
    private JPanel historicalTabPanel;
    
    // Baylor green color scheme
    private static final Color BAYLOR_GREEN = new Color(0, 71, 56);
    private static final Color LIGHT_GREEN = new Color(0, 100, 80);
    private static final Color BACKGROUND_COLOR = new Color(240, 255, 250);

    /**
     * Constructor for the DashboardUI with a specified username.
     * @author zachtaylorcsc
     */
    public DashboardUI(String username) {
        this(username, null);
    }
    
    /**
     * Constructor for the DashboardUI with a specified username and user type.
     * @author zachtaylorcsc
     */
    public DashboardUI(String username, String userType) {
        this.username = username;
        this.userType = userType;
        this.goalManager = new GoalManager();
        this.selfPacedPlanManager = new SelfPacedPlanManager();
        
        // Try to get user ID by username first, then by first name (for backward compatibility)
        this.userId = DB_MANAGER.getUserIdByUsername(username);
        if (this.userId == -1) {
            this.userId = DB_MANAGER.getUserIdByFirstName(username);
        }
        
        // If userType not provided, try to get it from database
        if (this.userType == null && this.userId != -1) {
            this.userType = DB_MANAGER.getUserType(this.userId);
        }


        // Record login for streak tracking
        if (this.userId != -1) {
            DB_MANAGER.recordLogin(this.userId);
            // Streak is displayed in the header and streak tab
        }

        setTitle("Dashboard - Not So Beary Fat");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Header with logout button and streak
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BAYLOR_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Left side: Welcome message and streak
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(BAYLOR_GREEN);
        leftPanel.setOpaque(true);

        JLabel header = new JLabel("Welcome back, " + username + "!");
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        leftPanel.add(header);

        // Streak display
        int currentStreak = userId != -1 ? DB_MANAGER.getCurrentStreak(userId) : 0;
        JLabel streakLabel = new JLabel(currentStreak + " day streak");
        streakLabel.setForeground(new Color(255, 199, 44)); // Baylor gold
        streakLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(streakLabel);

        headerPanel.add(leftPanel, BorderLayout.CENTER);
        
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
            resetUserInfo();
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

        // Create sidebar navigation panel
        sidebarPanel = new JPanel(new BorderLayout());
        sidebarPanel.setBackground(BAYLOR_GREEN);
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create toggle button for sidebar
        JButton toggleButton = new JButton("◀");
        toggleButton.setFont(new Font("Arial", Font.BOLD, 16));
        toggleButton.setBackground(LIGHT_GREEN);
        toggleButton.setForeground(Color.WHITE);
        toggleButton.setBorderPainted(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setOpaque(true);
        toggleButton.setPreferredSize(new Dimension(30, 30));
        toggleButton.setMargin(new Insets(0, 0, 0, 0));
        
        // Add hover effect
        toggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                toggleButton.setBackground(new Color(0, 120, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                toggleButton.setBackground(LIGHT_GREEN);
            }
        });
        
        // Create sidebar list with all navigation items
        DefaultListModel<String> sidebarModel = new DefaultListModel<>();
        sidebarModel.addElement("Graphs");
        sidebarModel.addElement("Data");
        sidebarModel.addElement("Goals");
        sidebarModel.addElement("Friends");
        sidebarModel.addElement("Classes");
        if (isTrainer()) {
            sidebarModel.addElement("New Class");
            sidebarModel.addElement("My Plans");
        }
        
        sidebarModel.addElement("Library");
        sidebarModel.addElement("Streak");
        
        sidebarList = new JList<>(sidebarModel);
        sidebarList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sidebarList.setBackground(BAYLOR_GREEN);
        sidebarList.setForeground(Color.WHITE);
        sidebarList.setFont(new Font("Arial", Font.PLAIN, 14));
        sidebarList.setSelectedIndex(1); // Select Data by default
        
        // Custom cell renderer for sidebar items
        sidebarList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    c.setBackground(LIGHT_GREEN);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(BAYLOR_GREEN);
                    c.setForeground(Color.WHITE);
                }
                setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                return c;
            }
        });
        
        JScrollPane sidebarScroll = new JScrollPane(sidebarList);
        sidebarScroll.setBorder(null);
        sidebarScroll.setBackground(BAYLOR_GREEN);
        sidebarPanel.add(sidebarScroll, BorderLayout.CENTER);
        
        // Add toggle button to top of sidebar
        JPanel togglePanel = new JPanel(new BorderLayout());
        togglePanel.setBackground(BAYLOR_GREEN);
        togglePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        togglePanel.add(toggleButton, BorderLayout.EAST);
        sidebarPanel.add(togglePanel, BorderLayout.NORTH);
        
        // Create content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Create all tab panels and add them to card layout
        JPanel historicalTab = createHistoricalTab();
        contentPanel.add(historicalTab, "Graphs");
        
        JPanel dataTab = createDataTab();
        contentPanel.add(dataTab, "Data");
        
        JPanel goalsTab = createGoalsTab();
        contentPanel.add(goalsTab, "Goals");
        
        JPanel friendsTab = createFriendsTab();
        contentPanel.add(friendsTab, "Friends");
        
        if (isTrainer()) {
            JPanel classesTab = createClassesTab();
            contentPanel.add(classesTab, "Classes");
            
            JPanel createClassTab = createCreateClassTab();
            contentPanel.add(createClassTab, "New Class");
            
            JPanel selfPacedTab = createSelfPacedPlansTab();
            contentPanel.add(selfPacedTab, "My Plans");
        }
        //if client
        else{
            JPanel classesTab = createClientClassesTab();
            contentPanel.add(classesTab, "Classes");
        }
        
        JPanel libraryTab = createPlanLibraryTab();
        contentPanel.add(libraryTab, "Library");
        
        JPanel streakTab = createStreakTab();
        contentPanel.add(streakTab, "Streak");
        
        // Add listener to sidebar list to switch content
        sidebarList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = sidebarList.getSelectedValue();
                if (selected != null) {
                    cardLayout.show(contentPanel, selected);
                    
                    // Handle refresh logic
                    if ("Classes".equals(selected)) {
                        refreshCalendarGrid();
                    } else if ("Library".equals(selected)) {
                        refreshPlanLibraryList();
                        if (!isTrainer() && availablePlansModel != null) {
                            refreshAvailablePlans();
                        }
                    } else if ("Graphs".equals(selected)) {
                        // Refresh graphs when switching to Graphs tab
                        refreshGraphs();
                    }
                }
            }
        });
        
        // Create wrapper panel for content with toggle button when sidebar is hidden
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(BACKGROUND_COLOR);
        
        // Create a small toggle button panel for when sidebar is hidden (initially hidden)
        JPanel hiddenTogglePanel = new JPanel(new BorderLayout());
        hiddenTogglePanel.setBackground(BACKGROUND_COLOR);
        hiddenTogglePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        hiddenTogglePanel.setVisible(false);
        JButton hiddenToggleButton = new JButton("▶");
        hiddenToggleButton.setFont(new Font("Arial", Font.BOLD, 16));
        hiddenToggleButton.setBackground(LIGHT_GREEN);
        hiddenToggleButton.setForeground(Color.WHITE);
        hiddenToggleButton.setBorderPainted(false);
        hiddenToggleButton.setFocusPainted(false);
        hiddenToggleButton.setOpaque(true);
        hiddenToggleButton.setPreferredSize(new Dimension(30, 30));
        hiddenToggleButton.setMargin(new Insets(0, 0, 0, 0));
        hiddenToggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hiddenToggleButton.setBackground(new Color(0, 120, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hiddenToggleButton.setBackground(LIGHT_GREEN);
            }
        });
        hiddenTogglePanel.add(hiddenToggleButton, BorderLayout.WEST);
        contentWrapper.add(hiddenTogglePanel, BorderLayout.NORTH);
        contentWrapper.add(contentPanel, BorderLayout.CENTER);
        
        // Create main container with sidebar and content
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, contentWrapper);
        mainSplitPane.setDividerLocation(200);
        mainSplitPane.setDividerSize(5);
        mainSplitPane.setResizeWeight(0);
        mainSplitPane.setBorder(null);
        
        // Toggle button action
        toggleButton.addActionListener(e -> {
            if (sidebarVisible) {
                // Hide sidebar
                mainSplitPane.setDividerLocation(0);
                mainSplitPane.setDividerSize(0);
                toggleButton.setText("▶");
                hiddenTogglePanel.setVisible(true);
                sidebarVisible = false;
            } else {
                // Show sidebar
                mainSplitPane.setDividerSize(5);
                mainSplitPane.setDividerLocation(200);
                toggleButton.setText("◀");
                hiddenTogglePanel.setVisible(false);
                sidebarVisible = true;
            }
        });
        
        // Hidden toggle button action (shows sidebar)
        hiddenToggleButton.addActionListener(e -> {
            mainSplitPane.setDividerSize(5);
            mainSplitPane.setDividerLocation(200);
            toggleButton.setText("◀");
            hiddenTogglePanel.setVisible(false);
            sidebarVisible = true;
        });
        
        add(mainSplitPane, BorderLayout.CENTER);
        
        // Show Data by default
        cardLayout.show(contentPanel, "Data");

        // Load and display real data
        loadUserData();
        
        // Data is already selected by default in sidebar list
        
        setVisible(true);
    }
    //added to db allows for multiple logins
    /**
     * Resets the user information, typically upon logout.
     * @author Owen Chipman
     */
    private void resetUserInfo() {
        userId = -1;
        username = null;
        userType = null;
    }

    /**
     * Creates and returns the data tab panel for the dashboard.
     * @author zachtaylorcsc
     * @return A JPanel representing the data tab.
     */
    private JPanel createDataTab() {
        dataTabPanel = new JPanel(new BorderLayout());
        dataTabPanel.setBackground(BACKGROUND_COLOR);
        dataTabPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top section: Quick summary and net calories
        JPanel topSection = createTopSummarySection();
        dataTabPanel.add(topSection, BorderLayout.NORTH);

        // Center section: Main metric cards in a grid
        JPanel centerSection = new JPanel(new GridLayout(2, 2, 20, 20));
        centerSection.setBackground(BACKGROUND_COLOR);
        centerSection.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Create metric cards
        centerSection.add(createMetricCard("", "Calories Consumed", "--", "kcal", Color.decode("#FF6B6B")));
        centerSection.add(createMetricCard("", "Calories Burned", "--", "kcal", Color.decode("#4ECDC4")));
        centerSection.add(createMetricCard("", "Weight", "--", "lbs", Color.decode("#95E1D3")));
        centerSection.add(createMetricCard("", "Sleep", "--", "hrs", Color.decode("#A8DADC")));

        dataTabPanel.add(centerSection, BorderLayout.CENTER);

        // Bottom section: Goal progress and weekly stats
        JPanel bottomSection = createBottomSection();
        dataTabPanel.add(bottomSection, BorderLayout.SOUTH);

        return dataTabPanel;
    }

    /**
     * Creates and returns the top summary section for the data tab, including net calories and a quick add button.
     * @author zachtaylorcsc
     * @return A JPanel representing the top summary section.
     */
    private JPanel createTopSummarySection() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Net calories card (prominent)
        JPanel netCaloriesCard = createStyledCard();
        netCaloriesCard.setLayout(new BorderLayout());
        netCaloriesCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BAYLOR_GREEN, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel netTitle = new JLabel("Net Calories Today", SwingConstants.CENTER);
        netTitle.setFont(new Font("Arial", Font.BOLD, 16));
        netTitle.setForeground(BAYLOR_GREEN);
        netCaloriesCard.add(netTitle, BorderLayout.NORTH);

        netCaloriesValueLabel = new JLabel("--", SwingConstants.CENTER);
        netCaloriesValueLabel.setFont(new Font("Arial", Font.BOLD, 40));
        netCaloriesValueLabel.setForeground(BAYLOR_GREEN);
        netCaloriesValueLabel.setVerticalAlignment(SwingConstants.CENTER);
        netCaloriesCard.add(netCaloriesValueLabel, BorderLayout.CENTER);

        netCaloriesLabel = new JLabel("kcal", SwingConstants.CENTER);
        netCaloriesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        netCaloriesLabel.setForeground(Color.GRAY);
        netCaloriesCard.add(netCaloriesLabel, BorderLayout.SOUTH);

        // Quick Add Data button
        JButton quickAddButton = new JButton("Quick Add Data");
        quickAddButton.setBackground(BAYLOR_GREEN);
        quickAddButton.setForeground(Color.WHITE);
        quickAddButton.setOpaque(true);
        quickAddButton.setBorderPainted(false);
        quickAddButton.setFocusPainted(false);
        quickAddButton.setFont(new Font("Arial", Font.BOLD, 14));
        quickAddButton.setPreferredSize(new Dimension(180, 40));
        quickAddButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                quickAddButton.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                quickAddButton.setBackground(BAYLOR_GREEN);
            }
        });
        quickAddButton.addActionListener(e -> {
            if (userId != -1 && DB_MANAGER != null) {
                AddData.openAddDataPage(userId, () -> {
                    // Refresh data tab after adding data
                    loadUserData();
                });
            } else {
                JOptionPane.showMessageDialog(this,
                    "Unable to add data: user not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Container for net calories card and button
        JPanel centerContainer = new JPanel(new BorderLayout(20, 0));
        centerContainer.setBackground(BACKGROUND_COLOR);
        centerContainer.add(netCaloriesCard, BorderLayout.CENTER);
        centerContainer.add(quickAddButton, BorderLayout.EAST);

        topPanel.add(centerContainer, BorderLayout.CENTER);

        return topPanel;
    }

    /**
     * Creates a styled JPanel to display a single metric (e.g., calories, weight).
     * @author zachtaylorcsc
     * @return A JPanel representing a metric card.
     */
    private JPanel createMetricCard(String icon, String title, String value, String unit, Color accentColor) {
        JPanel card = createStyledCard();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Top: Icon and title (only show title if no icon, or show both if icon provided)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setOpaque(false);
        if (!icon.isEmpty()) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
            topPanel.add(iconLabel);
        }
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.GRAY);
        topPanel.add(titleLabel);
        card.add(topPanel, BorderLayout.NORTH);

        // Center: Value with proper spacing
        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setOpaque(false);
        valuePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel valueLabel = new JLabel(value, SwingConstants.LEFT);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(BAYLOR_GREEN);
        valueLabel.setVerticalAlignment(SwingConstants.CENTER);
        valuePanel.add(valueLabel, BorderLayout.CENTER);
        card.add(valuePanel, BorderLayout.CENTER);

        // Bottom: Unit and trend
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        JLabel unitLabel = new JLabel(unit);
        unitLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        unitLabel.setForeground(Color.GRAY);
        bottomPanel.add(unitLabel, BorderLayout.WEST);

        JLabel trendLabel = new JLabel("", SwingConstants.RIGHT);
        trendLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        bottomPanel.add(trendLabel, BorderLayout.EAST);

        card.add(bottomPanel, BorderLayout.SOUTH);

        // Store references based on metric type
        switch (title) {
            case "Calories Consumed":
                caloriesValueLabel = valueLabel;
                caloriesTrendLabel = trendLabel;
                break;
            case "Calories Burned":
                burnedValueLabel = valueLabel;
                burnedTrendLabel = trendLabel;
                break;
            case "Weight":
                weightValueLabel = valueLabel;
                weightTrendLabel = trendLabel;
                break;
            case "Sleep":
                sleepValueLabel = valueLabel;
                sleepTrendLabel = trendLabel;
                break;
        }

        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(card.getBackground().getRed() - 5,
                    card.getBackground().getGreen() - 5,
                    card.getBackground().getBlue() - 5));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    /**
     * Creates and returns the bottom section for the data tab, including goal progress and weekly averages.
     * @author zachtaylorcsc
     * @return A JPanel representing the bottom section.
     */
    private JPanel createBottomSection() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Left: Goal progress card
        JPanel goalCard = createStyledCard();
        goalCard.setLayout(new BorderLayout());
        goalCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel goalTitle = new JLabel("Daily Calorie Goal", SwingConstants.LEFT);
        goalTitle.setFont(new Font("Arial", Font.BOLD, 16));
        goalTitle.setForeground(BAYLOR_GREEN);
        goalCard.add(goalTitle, BorderLayout.NORTH);

        enhancedCalorieProgressBar = new JProgressBar(0, 100);
        enhancedCalorieProgressBar.setStringPainted(true);
        enhancedCalorieProgressBar.setFont(new Font("Arial", Font.BOLD, 12));
        enhancedCalorieProgressBar.setForeground(Color.WHITE);
        enhancedCalorieProgressBar.setBackground(new Color(220, 220, 220));
        enhancedCalorieProgressBar.setValue(0);
        enhancedCalorieProgressBar.setString("No goal set");
        enhancedCalorieProgressBar.setPreferredSize(new Dimension(0, 40));
        goalCard.add(enhancedCalorieProgressBar, BorderLayout.CENTER);

        goalStatusLabel = new JLabel("Set a goal in the Goals tab to start tracking.");
        goalStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        goalStatusLabel.setForeground(Color.GRAY);
        goalCard.add(goalStatusLabel, BorderLayout.SOUTH);

        // Right: Weekly average card
        JPanel weeklyCard = createStyledCard();
        weeklyCard.setLayout(new BorderLayout());
        weeklyCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel weeklyTitle = new JLabel("Weekly Average", SwingConstants.LEFT);
        weeklyTitle.setFont(new Font("Arial", Font.BOLD, 16));
        weeklyTitle.setForeground(BAYLOR_GREEN);
        weeklyCard.add(weeklyTitle, BorderLayout.NORTH);

        weeklyAvgLabel = new JLabel("<html><div style='text-align: left;'>" +
            "Calories: --<br>" +
            "Burned: --<br>" +
            "Sleep: -- hrs</div></html>");
        weeklyAvgLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        weeklyAvgLabel.setForeground(Color.GRAY);
        weeklyCard.add(weeklyAvgLabel, BorderLayout.CENTER);

        // Use GridLayout for side-by-side
        JPanel cardsContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsContainer.setBackground(BACKGROUND_COLOR);
        cardsContainer.add(goalCard);
        cardsContainer.add(weeklyCard);

        bottomPanel.add(cardsContainer, BorderLayout.CENTER);

        // Keep old progress bar reference for compatibility
        calorieProgressBar = enhancedCalorieProgressBar;

        return bottomPanel;
    }

    /**
     * Creates a styled JPanel with a white background and borders, used as a card component.
     * @author zachtaylorcsc
     * @return A JPanel styled as a card.
     */
    private JPanel createStyledCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return card;
    }

    /**
     * Creates and returns the friends tab panel, including user search, friend requests, and friends list.
     * @author zachtaylorcsc
     * @return A JPanel representing the friends tab.
     */
    private JPanel createFriendsTab() {
        JPanel friendsPanel = new JPanel(new BorderLayout());
        friendsPanel.setBackground(BACKGROUND_COLOR);
        friendsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create main content panel with search and lists
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BACKGROUND_COLOR);

        // Top panel: Search for users
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search for Users"));

        JPanel searchInputPanel = new JPanel(new BorderLayout());
        searchInputPanel.setBackground(BACKGROUND_COLOR);
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(BAYLOR_GREEN);
        searchButton.setForeground(Color.WHITE);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(BAYLOR_GREEN);
            }
        });

        searchInputPanel.add(searchField, BorderLayout.CENTER);
        searchInputPanel.add(searchButton, BorderLayout.EAST);
        searchPanel.add(searchInputPanel, BorderLayout.CENTER);

        // Search results list with send request button
        JPanel searchResultsPanel = new JPanel(new BorderLayout());
        searchResultsPanel.setBackground(BACKGROUND_COLOR);

        DefaultListModel<User> searchResultsModel = new DefaultListModel<>();
        JList<User> searchResultsList = new JList<>(searchResultsModel);
        searchResultsList.setCellRenderer(new UserListCellRenderer());
        JScrollPane searchScroll = new JScrollPane(searchResultsList);
        searchScroll.setPreferredSize(new Dimension(0, 120));
        searchResultsPanel.add(searchScroll, BorderLayout.CENTER);

        JButton sendRequestButton = new JButton("Send Friend Request");
        sendRequestButton.setBackground(BAYLOR_GREEN);
        sendRequestButton.setForeground(Color.WHITE);
        sendRequestButton.setOpaque(true);
        sendRequestButton.setBorderPainted(false);
        sendRequestButton.setFocusPainted(false);
        sendRequestButton.setEnabled(false);
        sendRequestButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (sendRequestButton.isEnabled()) {
                    sendRequestButton.setBackground(LIGHT_GREEN);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sendRequestButton.setBackground(BAYLOR_GREEN);
            }
        });

        sendRequestButton.addActionListener(e -> {
            User selected = searchResultsList.getSelectedValue();
            if (selected != null) {
                if (DB_MANAGER.sendFriendRequest(userId, selected.getId())) {
                    JOptionPane.showMessageDialog(friendsPanel,
                        "Friend request sent to " + selected.getUsername() + "!",
                        "Request Sent",
                        JOptionPane.INFORMATION_MESSAGE);
                    // Refresh outgoing requests
                    refreshAllFriendData();
                } else {
                    JOptionPane.showMessageDialog(friendsPanel,
                        "Failed to send friend request. You may already have a pending request with this user.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        searchResultsList.addListSelectionListener(e -> {
            sendRequestButton.setEnabled(searchResultsList.getSelectedValue() != null);
        });

        searchResultsPanel.add(sendRequestButton, BorderLayout.SOUTH);
        searchPanel.add(searchResultsPanel, BorderLayout.SOUTH);

        // Search button action
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                searchResultsModel.clear();
                List<User> results = DB_MANAGER.searchUsersByUsername(searchTerm, userId);
                for (User user : results) {
                    searchResultsModel.addElement(user);
                }
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(friendsPanel,
                        "No users found matching: " + searchTerm,
                        "No Results",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Allow Enter key to search
        searchField.addActionListener(e -> searchButton.doClick());

        // Center panel: Tabbed pane for friend requests and friends list
        JTabbedPane friendsTabbedPane = new JTabbedPane();
        friendsTabbedPane.setBackground(BACKGROUND_COLOR);
        friendsTabbedPane.setForeground(BAYLOR_GREEN);

        // Pending requests tab
        JPanel requestsPanel = createFriendRequestsPanel();
        friendsTabbedPane.addTab("Friend Requests", requestsPanel);

        // Friends list tab
        JPanel friendsListPanel = createFriendsListPanel();
        friendsTabbedPane.addTab("My Friends", friendsListPanel);

        // Refresh when switching tabs
        friendsTabbedPane.addChangeListener(e -> {
            int selectedIndex = friendsTabbedPane.getSelectedIndex();
            if (selectedIndex == 0) {
                // Refresh requests
                refreshAllFriendData();
            } else if (selectedIndex == 1) {
                // Refresh friends list
                refreshAllFriendData();
            }
        });

        mainContent.add(searchPanel, BorderLayout.NORTH);
        mainContent.add(friendsTabbedPane, BorderLayout.CENTER);

        friendsPanel.add(mainContent, BorderLayout.CENTER);

        return friendsPanel;
    }

    /**
     * Creates and returns the panel for friend requests, including incoming and outgoing requests.
     * @author zachtaylorcsc
     * @return A JPanel representing the friend requests section.
     */
    private JPanel createFriendRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Split into incoming and outgoing requests
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        splitPanel.setBackground(BACKGROUND_COLOR);

        // Incoming requests
        JPanel incomingPanel = new JPanel(new BorderLayout());
        incomingPanel.setBackground(BACKGROUND_COLOR);
        incomingPanel.setBorder(BorderFactory.createTitledBorder("Incoming Requests"));

        DefaultListModel<User> incomingModel = new DefaultListModel<>();
        JList<User> incomingList = new JList<>(incomingModel);
        incomingList.setCellRenderer(new UserListCellRenderer());
        JScrollPane incomingScroll = new JScrollPane(incomingList);
        incomingPanel.add(incomingScroll, BorderLayout.CENTER);

        JPanel incomingButtons = new JPanel(new FlowLayout());
        incomingButtons.setBackground(BACKGROUND_COLOR);
        JButton acceptButton = new JButton("Accept");
        acceptButton.setBackground(BAYLOR_GREEN);
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setOpaque(true);
        acceptButton.setBorderPainted(false);
        acceptButton.setFocusPainted(false);
        acceptButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                acceptButton.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                acceptButton.setBackground(BAYLOR_GREEN);
            }
        });

        JButton rejectButton = new JButton("Reject");
        rejectButton.setBackground(new Color(200, 0, 0));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setOpaque(true);
        rejectButton.setBorderPainted(false);
        rejectButton.setFocusPainted(false);
        rejectButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                rejectButton.setBackground(new Color(220, 0, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rejectButton.setBackground(new Color(200, 0, 0));
            }
        });

        acceptButton.addActionListener(e -> {
            User selected = incomingList.getSelectedValue();
            if (selected != null) {
                if (DB_MANAGER.acceptFriendRequest(selected.getId(), userId)) {
                    JOptionPane.showMessageDialog(panel,
                        "Friend request accepted!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshAllFriendData();
                } else {
                    JOptionPane.showMessageDialog(panel,
                        "Failed to accept friend request.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        rejectButton.addActionListener(e -> {
            User selected = incomingList.getSelectedValue();
            if (selected != null) {
                if (DB_MANAGER.rejectFriendRequest(selected.getId(), userId)) {
                    refreshAllFriendData();
                } else {
                    JOptionPane.showMessageDialog(panel,
                        "Failed to reject friend request.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        incomingButtons.add(acceptButton);
        incomingButtons.add(rejectButton);
        incomingPanel.add(incomingButtons, BorderLayout.SOUTH);

        // Outgoing requests
        JPanel outgoingPanel = new JPanel(new BorderLayout());
        outgoingPanel.setBackground(BACKGROUND_COLOR);
        outgoingPanel.setBorder(BorderFactory.createTitledBorder("Outgoing Requests"));

        DefaultListModel<User> outgoingModel = new DefaultListModel<>();
        JList<User> outgoingList = new JList<>(outgoingModel);
        outgoingList.setCellRenderer(new UserListCellRenderer());
        JScrollPane outgoingScroll = new JScrollPane(outgoingList);
        outgoingPanel.add(outgoingScroll, BorderLayout.CENTER);

        JPanel outgoingButtons = new JPanel(new FlowLayout());
        outgoingButtons.setBackground(BACKGROUND_COLOR);
        JButton cancelButton = new JButton("Cancel Request");
        cancelButton.setBackground(new Color(200, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setOpaque(true);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(220, 0, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(200, 0, 0));
            }
        });

        cancelButton.addActionListener(e -> {
            User selected = outgoingList.getSelectedValue();
            if (selected != null) {
                if (DB_MANAGER.rejectFriendRequest(userId, selected.getId())) {
                    refreshAllFriendData();
                } else {
                    JOptionPane.showMessageDialog(panel,
                        "Failed to cancel friend request.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        outgoingButtons.add(cancelButton);
        outgoingPanel.add(outgoingButtons, BorderLayout.SOUTH);

        splitPanel.add(incomingPanel);
        splitPanel.add(outgoingPanel);
        panel.add(splitPanel, BorderLayout.CENTER);

        // Store references for refreshing
        incomingRequestsModelRef = incomingModel;
        outgoingRequestsModelRef = outgoingModel;

        // Initial load
        refreshAllFriendData();

        return panel;
    }

    /**
     * Creates and returns the panel for displaying the user's friends list.
     * @author zachtaylorcsc
     * @return A JPanel representing the friends list section.
     */
    private JPanel createFriendsListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Split into friends list and classes view
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        splitPanel.setBackground(BACKGROUND_COLOR);

        // Friends list
        JPanel friendsListPanel = new JPanel(new BorderLayout());
        friendsListPanel.setBackground(BACKGROUND_COLOR);
        friendsListPanel.setBorder(BorderFactory.createTitledBorder("Friends"));

        DefaultListModel<User> friendsModel = new DefaultListModel<>();
        JList<User> friendsList = new JList<>(friendsModel);
        friendsList.setCellRenderer(new UserListCellRenderer());
        JScrollPane friendsScroll = new JScrollPane(friendsList);
        friendsListPanel.add(friendsScroll, BorderLayout.CENTER);

        // Friend's classes view
        JPanel classesPanel = new JPanel(new BorderLayout());
        classesPanel.setBackground(BACKGROUND_COLOR);
        classesPanel.setBorder(BorderFactory.createTitledBorder("Friend's Enrolled Classes"));

        DefaultListModel<WorkoutClass> friendClassesModel = new DefaultListModel<>();
        JList<WorkoutClass> friendClassesList = new JList<>(friendClassesModel);
        friendClassesList.setCellRenderer(new FriendClassListCellRenderer());
        JScrollPane classesScroll = new JScrollPane(friendClassesList);
        classesPanel.add(classesScroll, BorderLayout.CENTER);

        // When a friend is selected, show their classes
        friendsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                friendClassesModel.clear();
                User selected = friendsList.getSelectedValue();
                if (selected != null) {
                    List<WorkoutClass> classes = DB_MANAGER.getFriendEnrolledClasses(selected.getId());
                    if (classes.isEmpty()) {
                        // Add a placeholder
                        friendClassesModel.addElement(null);
                    } else {
                        for (WorkoutClass wc : classes) {
                            friendClassesModel.addElement(wc);
                        }
                    }
                } else {
                    friendClassesModel.clear();
                }
            }
        });

        splitPanel.add(friendsListPanel);
        splitPanel.add(classesPanel);
        panel.add(splitPanel, BorderLayout.CENTER);

        // Store reference for refreshing
        friendsModelRef = friendsModel;

        // Initial load
        refreshAllFriendData();

        return panel;
    }


    // Store references to models for refreshing
    private DefaultListModel<User> friendsModelRef;
    private DefaultListModel<User> incomingRequestsModelRef;
    private DefaultListModel<User> outgoingRequestsModelRef;

    /**
     * Refreshes all friend-related data, including incoming requests, outgoing requests, and the friends list.
     * @author zachtaylorcsc
     */
    private void refreshAllFriendData() {
        // Refresh friend requests
        if (incomingRequestsModelRef != null) {
            incomingRequestsModelRef.clear();
            List<User> incoming = DB_MANAGER.getPendingFriendRequests(userId);
            for (User user : incoming) {
                incomingRequestsModelRef.addElement(user);
            }
        }

        if (outgoingRequestsModelRef != null) {
            outgoingRequestsModelRef.clear();
            List<User> outgoing = DB_MANAGER.getSentFriendRequests(userId);
            for (User user : outgoing) {
                outgoingRequestsModelRef.addElement(user);
            }
        }
        
        // Refresh friends list
        if (friendsModelRef != null) {
            friendsModelRef.clear();
            List<User> friends = DB_MANAGER.getFriends(userId);
            for (User friend : friends) {
                friendsModelRef.addElement(friend);
            }
        }
    }

    // Custom cell renderer for user list
    private class UserListCellRenderer extends DefaultListCellRenderer {
        /**
         * Returns a component that has been configured to display the specified value.
         * @author zachtaylorcsc
         * @return A component that has been configured to display the specified value.
         */
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                     boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof User) {
                User user = (User) value;
                String displayName = user.getUsername();
                if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                    displayName = user.getFirstName();
                    if (user.getLastName() != null && !user.getLastName().isEmpty()) {
                        displayName += " " + user.getLastName();
                    }
                    displayName += " (" + user.getUsername() + ")";
                }
                setText(displayName);
            }
            return this;
        }
    }

    // Custom cell renderer for friend's classes list (handles null)
    private class FriendClassListCellRenderer extends DefaultListCellRenderer {
        /**
         * Returns a component that has been configured to display the specified value, handling null values for classes.
         * @author zachtaylorcsc
         * @return A component that has been configured to display the specified value.
         */
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                     boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value == null) {
                setText("No classes enrolled");
                setForeground(Color.GRAY);
            } else if (value instanceof WorkoutClass) {
                WorkoutClass wc = (WorkoutClass) value;
                int currentEnrolled = DB_MANAGER.getCurrentEnrollmentCount(wc.getId());
                int spotsAvailable = wc.getMaxParticipants() - currentEnrolled;
                String text = wc.getClassType() + " - " + wc.getStartTime() +
                             " ($" + String.format("%.2f", wc.getCost()) + ")";
                setText(text);
                setForeground(Color.BLACK);
            }
            return this;
        }
    }

    /**
     * Creates and returns the goals tab panel, allowing users to set, view, and manage fitness goals.
     * @author Oluwalademi Aromolaran
     * @return A JPanel representing the goals tab.
     */
    private JPanel createGoalsTab() {
        JPanel goalsPanel = new JPanel(new BorderLayout());
        goalsPanel.setBackground(BACKGROUND_COLOR);
        goalsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "<h2>Goals</h2>" +
                        "<p>Set personal fitness goals and track your progress.</p>" +
                        "<p>Use the list on the left to select and update existing goals, " +
                        "or create a new goal using the form on the right.</p>" +
                        "</div></html>",
                SwingConstants.CENTER
        );
        headerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        goalsPanel.add(headerLabel, BorderLayout.NORTH);

        // Center split
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(BACKGROUND_COLOR);

        // Left: Goal list
        goalListModel = new DefaultListModel<>();
        goalList = new JList<>(goalListModel);
        goalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Load persisted goals
        if (goalManager != null && userId != -1) {
            java.util.List<Goal> goals = goalManager.loadGoals(userId);
            for (Goal g : goals) {
                goalListModel.addElement(g);
            }
        }

        JScrollPane listScroll = new JScrollPane(goalList);
        listScroll.setBorder(BorderFactory.createTitledBorder("My Goals"));

        JButton newGoalButton = new JButton("New Goal");
        newGoalButton.addActionListener(e -> {
            goalList.clearSelection();
            selectedGoalIndex = -1;
            // Clear form fields
            goalNameField.setText("");
            objectiveField.setSelectedIndex(0);
            caloriesField.setText("");
            exerciseField.setText("");
            frequencyField.setText("");
            intensityField.setText("");
            durationField.setText("");
            descriptionArea.setText("");
        });

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(listScroll, BorderLayout.CENTER);
        leftPanel.add(newGoalButton, BorderLayout.SOUTH);

        // Right: Goal form
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createTitledBorder("Goal Details"));

        goalNameField = new JTextField();
        objectiveField = new JComboBox<>(new String[]{
                "Weight Loss", "Build Strength", "Endurance", "General Health"
        });
        caloriesField = new JTextField();
        exerciseField = new JTextField();
        frequencyField = new JTextField();
        intensityField = new JTextField();
        durationField = new JTextField();
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        formPanel.add(new JLabel("Goal Name:"));
        formPanel.add(goalNameField);
        formPanel.add(new JLabel("Overall Objective:"));
        formPanel.add(objectiveField);
        formPanel.add(new JLabel("Daily Calorie Target:"));
        formPanel.add(caloriesField);
        formPanel.add(new JLabel("Exercise Type:"));
        formPanel.add(exerciseField);
        formPanel.add(new JLabel("Frequency (e.g., 3/week):"));
        formPanel.add(frequencyField);
        formPanel.add(new JLabel("Intensity (e.g., Moderate):"));
        formPanel.add(intensityField);
        formPanel.add(new JLabel("Duration (e.g., 3 months):"));
        formPanel.add(durationField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(new JScrollPane(descriptionArea));

        // When a goal is selected from the list, load it into the form
        goalList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Goal selected = goalList.getSelectedValue();
                if (selected != null) {
                    selectedGoalIndex = goalList.getSelectedIndex();
                    goalNameField.setText(selected.getGoalName());
                    if (selected.getFitnessObjective() != null) {
                        objectiveField.setSelectedItem(selected.getFitnessObjective());
                    }
                    caloriesField.setText(
                            selected.getCalories() != null ? selected.getCalories().toString() : ""
                    );
                    exerciseField.setText(selected.getExerciseType());
                    frequencyField.setText(selected.getFrequency());
                    intensityField.setText(selected.getIntensity());
                    durationField.setText(selected.getDuration());
                    descriptionArea.setText(selected.getDescription());

                    if (goalManager != null) {
                        goalManager.setCurrentGoal(selected);
                        loadUserData();
                    }
                }
            }
        });

        centerPanel.add(leftPanel);
        centerPanel.add(formPanel);
        goalsPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom: Save button + status
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);

        JLabel statusLabel = new JLabel(" ", SwingConstants.LEFT);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        JButton saveButton = new JButton("Save Goal");
        saveButton.setBackground(BAYLOR_GREEN);
        saveButton.setForeground(Color.WHITE);
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setPreferredSize(new Dimension(140, 35));

        saveButton.addActionListener(e -> {
            if (userId == -1) {
                JOptionPane.showMessageDialog(
                        DashboardUI.this,
                        "Unable to save goals: user not found.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            Integer calories = null;
            String caloriesText = caloriesField.getText().trim();
            if (!caloriesText.isEmpty()) {
                try {
                    calories = Integer.parseInt(caloriesText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            DashboardUI.this,
                            "Please enter a valid number for Daily Calorie Target.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE
                    );
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

            // If editing an existing goal in the list
            if (selectedGoalIndex >= 0 && selectedGoalIndex < goalListModel.size()) {
                Goal existing = goalListModel.get(selectedGoalIndex);
                // Keep the same ID so DB knows to update
                newGoal.setId(existing.getId());
                existing.updateFrom(newGoal);
                goalListModel.set(selectedGoalIndex, existing);

                if (goalManager != null) {
                    goalManager.setCurrentGoal(existing);
                    goalManager.saveGoals(userId, existing);
                }
            } else {
                // New goal
                goalListModel.addElement(newGoal);
                selectedGoalIndex = goalListModel.size() - 1;
                goalList.setSelectedIndex(selectedGoalIndex);
                if (goalManager != null) {
                    goalManager.setCurrentGoal(newGoal);
                    goalManager.saveGoals(userId, newGoal);
                }
            }

            statusLabel.setText("Goal saved successfully.");
            statusLabel.setForeground(new Color(0, 128, 64));

            loadUserData(); // update Data tab if you're showing goal-based info
        });

        // Delete Goal logic
        JButton deleteButton = new JButton("Delete Goal");
        deleteButton.setBackground(new Color(200, 0, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setPreferredSize(new Dimension(140, 35));

        deleteButton.addActionListener(e -> {
            Goal selected = goalList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(
                        DashboardUI.this,
                        "Please select a goal to delete.",
                        "No Goal Selected",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // If goal was never saved to DB, just remove from list
            if (selected.getId() == null) {
                goalListModel.removeElement(selected);
                selectedGoalIndex = -1;
                goalNameField.setText("");
                objectiveField.setSelectedIndex(0);
                caloriesField.setText("");
                exerciseField.setText("");
                frequencyField.setText("");
                intensityField.setText("");
                durationField.setText("");
                descriptionArea.setText("");

                statusLabel.setText("Goal removed (not yet saved in the database).");
                statusLabel.setForeground(new Color(200, 0, 0));
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    DashboardUI.this,
                    "Are you sure you want to delete this goal?",
                    "Delete Goal",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            boolean ok = DB_MANAGER.deleteGoal(selected.getId(), userId);
            if (ok) {
                goalListModel.removeElement(selected);
                selectedGoalIndex = -1;

                goalNameField.setText("");
                objectiveField.setSelectedIndex(0);
                caloriesField.setText("");
                exerciseField.setText("");
                frequencyField.setText("");
                intensityField.setText("");
                durationField.setText("");
                descriptionArea.setText("");

                statusLabel.setText("Goal deleted.");
                statusLabel.setForeground(new Color(200, 0, 0));
                // refresh Data tab based on current goal
                goalManager.setCurrentGoal(null);
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(
                        DashboardUI.this,
                        "Could not delete the goal. Please try again.",
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        bottomPanel.add(statusLabel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(saveButton);

        bottomPanel.add(buttonsPanel, BorderLayout.EAST);


        goalsPanel.add(bottomPanel, BorderLayout.SOUTH);

        return goalsPanel;
    }



    /**
     * Updates the goal progress display based on the user's calorie consumption.
     * @author Oluwalademi Aromolaran
     */
    private void updateGoalProgress(Integer caloriesConsumed) {
        // Use enhanced progress bar if available, otherwise fall back to old one
        JProgressBar progressBar = (enhancedCalorieProgressBar != null) ? enhancedCalorieProgressBar : calorieProgressBar;
        
        if (progressBar == null || goalStatusLabel == null) {
            return; // Data tab not built yet
        }

        // If you don't have GoalManager hooked up yet, this will just show "No goal set"
        Goal goal = (goalManager != null) ? goalManager.getCurrentGoal() : null;

        // No goal set at all
        if (goal == null || goal.getCalories() == null) {
            progressBar.setValue(0);
            progressBar.setString("No goal set");
            progressBar.setForeground(Color.GRAY);
            goalStatusLabel.setText("Set a goal in the Goals tab to start tracking.");
            goalStatusLabel.setForeground(Color.DARK_GRAY);
            return;
        }

        int target = goal.getCalories();
        if (target <= 0) {
            progressBar.setValue(0);
            progressBar.setString("Invalid goal");
            progressBar.setForeground(Color.RED);
            goalStatusLabel.setText("Calorie goal must be greater than 0.");
            goalStatusLabel.setForeground(Color.RED);
            return;
        }

        // No recent data
        if (caloriesConsumed == null) {
            progressBar.setValue(0);
            progressBar.setString("0% of " + String.format("%,d", target) + " kcal");
            progressBar.setForeground(BAYLOR_GREEN);
            goalStatusLabel.setText("No recent data. Log today's calories to start tracking.");
            goalStatusLabel.setForeground(new Color(128, 64, 0));
            return;
        }

        // Compute % of goal reached
        double ratio = (double) caloriesConsumed / target;
        int percent = (int) Math.round(ratio * 100);
        percent = Math.max(0, Math.min(percent, 200));

        progressBar.setValue(Math.min(percent, 100));
        progressBar.setString(percent + "% (" + String.format("%,d", caloriesConsumed) + " / " + String.format("%,d", target) + " kcal)");

        // Color code progress bar based on progress
        if (percent >= 100) {
            progressBar.setForeground(new Color(0, 180, 0)); // Green for goal reached
            goalStatusLabel.setText("You reached your calorie goal today!");
            goalStatusLabel.setForeground(new Color(0, 128, 64));
        } else if (percent >= 75) {
            progressBar.setForeground(Color.decode("#4ECDC4")); // Teal for close
            goalStatusLabel.setText("Almost there! " + String.format("%,d", (target - caloriesConsumed)) + " kcal to go.");
            goalStatusLabel.setForeground(new Color(0, 102, 204));
        } else if (percent >= 50) {
            progressBar.setForeground(Color.decode("#95E1D3")); // Light teal for halfway
            goalStatusLabel.setText("You have " + String.format("%,d", (target - caloriesConsumed)) + " kcal remaining.");
            goalStatusLabel.setForeground(new Color(0, 102, 204));
        } else {
            progressBar.setForeground(BAYLOR_GREEN); // Baylor green for early progress
            goalStatusLabel.setText("You have " + String.format("%,d", (target - caloriesConsumed)) + " kcal remaining.");
            goalStatusLabel.setForeground(new Color(128, 64, 0));
        }
    }

    /**
     * Creates and returns the self-paced plans tab panel, allowing trainers to create and manage plans.
     * @author Oluwalademi Aromolaran
     * @return A JPanel representing the self-paced plans tab.
     */
    private JPanel createSelfPacedPlansTab() {
        JPanel plansPanel = new JPanel(new BorderLayout());
        plansPanel.setBackground(BACKGROUND_COLOR);
        plansPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel headerLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "<h2>Self-Paced Exercise Plans</h2>" +
                        "<p>Trainers can create structured plans that users follow on their own.</p>" +
                        "<p>Select a plan on the left to edit it, or create a new one on the right.</p>" +
                        "</div></html>",
                SwingConstants.CENTER
        );
        headerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        plansPanel.add(headerLabel, BorderLayout.NORTH);

        // Center: left = list, right = form
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(BACKGROUND_COLOR);

        // Left: list of this trainer's plans
        planListModel = new DefaultListModel<>();
        planList = new JList<>(planListModel);
        planList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScroll = new JScrollPane(planList);
        listScroll.setBorder(BorderFactory.createTitledBorder("My Self-Paced Plans"));

        JButton newPlanButton = new JButton("New Plan");

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(listScroll, BorderLayout.CENTER);
        leftPanel.add(newPlanButton, BorderLayout.SOUTH);

        // Right: plan details form
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createTitledBorder("Plan Details"));

        JTextField titleField = new JTextField();
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JComboBox<String> fitnessLevelField = new JComboBox<>(new String[]{
                "Beginner", "Intermediate", "Advanced", "All Levels"
        });
        JTextField equipmentField = new JTextField();
        JTextField lengthField = new JTextField();    // session length
        JTextField frequencyField = new JTextField();

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(new JScrollPane(descriptionArea));
        formPanel.add(new JLabel("Fitness Level:"));
        formPanel.add(fitnessLevelField);
        formPanel.add(new JLabel("Equipment:"));
        formPanel.add(equipmentField);
        formPanel.add(new JLabel("Session Length:"));
        formPanel.add(lengthField);
        formPanel.add(new JLabel("Frequency:"));
        formPanel.add(frequencyField);

        // When a plan is selected, load it into the form
        planList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedPlanIndex = planList.getSelectedIndex();
                selectedPlan = planList.getSelectedValue();
                if (selectedPlan != null) {
                    titleField.setText(selectedPlan.getTitle());
                    descriptionArea.setText(selectedPlan.getDescription());
                    fitnessLevelField.setSelectedItem(selectedPlan.getFitnessLevel());
                    equipmentField.setText(selectedPlan.getEquipment());
                    lengthField.setText(selectedPlan.getSessionLength());
                    frequencyField.setText(selectedPlan.getFrequency());
                }
            }
        });

        // "New Plan" clears selection + form
        newPlanButton.addActionListener(e -> {
            planList.clearSelection();
            selectedPlanIndex = -1;
            selectedPlan = null;
            titleField.setText("");
            descriptionArea.setText("");
            fitnessLevelField.setSelectedIndex(0);
            equipmentField.setText("");
            lengthField.setText("");
            frequencyField.setText("");
        });

        centerPanel.add(leftPanel);
        centerPanel.add(formPanel);
        plansPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom: Save button and status label
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);

        JLabel statusLabel = new JLabel(" ", SwingConstants.LEFT);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        JButton saveButton = new JButton("Save Plan");
        saveButton.setBackground(BAYLOR_GREEN);
        saveButton.setForeground(Color.WHITE);
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setPreferredSize(new Dimension(140, 35));

        saveButton.addActionListener(e -> {
            if (userId == -1) {
                JOptionPane.showMessageDialog(
                        DashboardUI.this,
                        "Unable to save plan: trainer not found.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Build plan object from form
            SelfPacedPlan planToSave;
            if (selectedPlan != null) {
                // editing existing plan
                planToSave = new SelfPacedPlan(
                        selectedPlan.getId(),
                        selectedPlan.getTrainerId(),
                        titleField.getText().trim(),
                        descriptionArea.getText().trim(),
                        (String) fitnessLevelField.getSelectedItem(),
                        equipmentField.getText().trim(),
                        lengthField.getText().trim(),
                        frequencyField.getText().trim()
                );
            } else {
                // new plan
                planToSave = new SelfPacedPlan(
                        titleField.getText().trim(),
                        descriptionArea.getText().trim(),
                        (String) fitnessLevelField.getSelectedItem(),
                        equipmentField.getText().trim(),
                        lengthField.getText().trim(),
                        frequencyField.getText().trim()
                );
            }

            // Validation: missing required fields
            java.util.List<String> missing = new java.util.ArrayList<>();
            boolean hasMissing = selfPacedPlanManager.hasMissingRequiredFields(planToSave, missing);
            if (hasMissing) {
                JOptionPane.showMessageDialog(
                        DashboardUI.this,
                        "Please fill in the required fields: " + String.join(", ", missing),
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );
                statusLabel.setText("Missing required fields: " + String.join(", ", missing));
                statusLabel.setForeground(Color.RED);
                return;
            }

            // savePlan(trainerID, planDetails)
            boolean ok = selfPacedPlanManager.savePlan(userId, planToSave);
            if (!ok) {
                JOptionPane.showMessageDialog(
                        DashboardUI.this,
                        "Unable to save your plan right now. Please try again later.",
                        "System Error",
                        JOptionPane.ERROR_MESSAGE
                );
                statusLabel.setText("Unable to save your plan right now. Please try again later.");
                statusLabel.setForeground(Color.RED);
                return;
            }

            // Update list model (new or edited)
            if (selectedPlanIndex >= 0 && selectedPlanIndex < planListModel.size()) {
                planListModel.set(selectedPlanIndex, planToSave);
            } else {
                planListModel.addElement(planToSave);
                selectedPlanIndex = planListModel.size() - 1;
                planList.setSelectedIndex(selectedPlanIndex);
                selectedPlan = planToSave;
            }

            // Also refresh library so users can see it
            refreshTrainerPlansList();
            refreshPlanLibraryList();

            statusLabel.setText("Plan saved and published to Plan Library.");
            statusLabel.setForeground(new Color(0, 128, 64));
        });

        // Delete Plan logic
        JButton deletePlanButton = new JButton("Delete Plan");
        deletePlanButton.setBackground(new Color(200, 0, 0));
        deletePlanButton.setForeground(Color.WHITE);
        deletePlanButton.setOpaque(true);
        deletePlanButton.setBorderPainted(false);
        deletePlanButton.setFocusPainted(false);
        deletePlanButton.setFont(new Font("Arial", Font.BOLD, 14));
        deletePlanButton.setPreferredSize(new Dimension(140, 35));

        deletePlanButton.addActionListener(e -> {
            SelfPacedPlan selected = planList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(
                        DashboardUI.this,
                        "Please select a plan to delete.",
                        "No Plan Selected",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (selected.getId() == 0) {
                // Not yet saved to DB: just remove from UI
                planListModel.removeElement(selected);
                titleField.setText("");
                descriptionArea.setText("");
                fitnessLevelField.setSelectedIndex(0);
                equipmentField.setText("");
                lengthField.setText("");
                frequencyField.setText("");
                statusLabel.setText("Plan removed (not yet saved in the database).");
                statusLabel.setForeground(new Color(200, 0, 0));
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    DashboardUI.this,
                    "Are you sure you want to delete this plan?",
                    "Delete Plan",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            boolean ok = selfPacedPlanManager.deletePlan(selected.getId());

            if (ok) {
                refreshTrainerPlansList();
                refreshPlanLibraryList();

                titleField.setText("");
                descriptionArea.setText("");
                fitnessLevelField.setSelectedIndex(0);
                equipmentField.setText("");
                lengthField.setText("");
                frequencyField.setText("");


                statusLabel.setText("Plan deleted successfully.");
                statusLabel.setForeground(new Color(200, 0, 0));
            } else {
                JOptionPane.showMessageDialog(
                        DashboardUI.this,
                        "Could not delete the plan.",
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });



        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        buttonsPanel.add(deletePlanButton);
        buttonsPanel.add(saveButton);

        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(buttonsPanel, BorderLayout.EAST);
        plansPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Initial load of trainer's plans
        refreshTrainerPlansList();

        return plansPanel;
    }




    /**
     * Refreshes the list of self-paced plans created by the current trainer.
     * @author Oluwalademi Aromolaran
     */
    private void refreshTrainerPlansList() {
        if (selfPacedPlanManager == null || planListModel == null || userId == -1) {
            return;
        }
        planListModel.clear();
        for (SelfPacedPlan p : selfPacedPlanManager.getPlansForTrainer(userId)) {
            planListModel.addElement(p);
        }
    }

    /**
     * Creates and returns the plan library tab panel, where users can browse and interact with self-paced plans.
     * @author Oluwalademi Aromolaran
     * @return A JPanel representing the plan library tab.
     */
    private JPanel createPlanLibraryTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // For clients, create a tabbed pane with "Available Plans" and "My Plans"
        if (!isTrainer()) {
            JTabbedPane clientTabbedPane = new JTabbedPane();
            clientTabbedPane.setBackground(BACKGROUND_COLOR);
            clientTabbedPane.setForeground(BAYLOR_GREEN);

            // Available Plans tab
            JPanel availablePanel = createAvailablePlansPanel();
            clientTabbedPane.addTab("Available Plans", availablePanel);

            // My Plans tab (enrolled plans)
            JPanel enrolledPanel = createEnrolledPlansPanel();
            clientTabbedPane.addTab("My Plans", enrolledPanel);

            // Refresh when switching tabs
            clientTabbedPane.addChangeListener(e -> {
                int selectedIndex = clientTabbedPane.getSelectedIndex();
                if (selectedIndex == 0) {
                    refreshPlanLibraryList();
                } else if (selectedIndex == 1) {
                    refreshEnrolledPlans();
                }
            });

            mainPanel.add(clientTabbedPane, BorderLayout.CENTER);
            return mainPanel;
        }

        // Trainer view: simple browse view
        JLabel header = new JLabel(
                "<html><h2>Plan Library</h2>" +
                        "<p>Browse self-paced plans created by trainers.</p></html>",
                SwingConstants.LEFT
        );
        mainPanel.add(header, BorderLayout.NORTH);

        // List of all plans
        libraryPlanListModel = new DefaultListModel<>();
        libraryPlanList = new JList<>(libraryPlanListModel);
        libraryPlanList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScroll = new JScrollPane(libraryPlanList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Available Self-Paced Plans"));

        // Details panel
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setBackground(BACKGROUND_COLOR);
        detailsArea.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Plan Details"));
        detailsScroll.setPreferredSize(new Dimension(0, 150));

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                listScroll,
                detailsScroll
        );
        splitPane.setResizeWeight(0.6);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // When user selects a plan, show its details
        libraryPlanList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SelfPacedPlan selected = libraryPlanList.getSelectedValue();
                if (selected != null) {
                    String text = "Title: " + selected.getTitle() + "\n\n" +
                            "Fitness Level: " + selected.getFitnessLevel() + "\n" +
                            "Equipment: " + selected.getEquipment() + "\n" +
                            "Session Length: " + selected.getSessionLength() + "\n" +
                            "Frequency: " + selected.getFrequency() + "\n\n" +
                            "Description:\n" +
                            (selected.getDescription() != null ? selected.getDescription() : "None");
                    detailsArea.setText(text);
                } else {
                    detailsArea.setText("");
                }
            }
        });

        // Initial fill
        refreshPlanLibraryList();

        return mainPanel;
    }



    /**
     * Refreshes the list of all available self-paced plans in the library.
     * @author Oluwalademi Aromolaran
     */
    private void refreshPlanLibraryList() {
        if (selfPacedPlanManager == null || libraryPlanListModel == null) {
            return;
        }
        libraryPlanListModel.clear();
        for (SelfPacedPlan p : selfPacedPlanManager.getAllPlans()) {
            libraryPlanListModel.addElement(p);
        }
    }

    // Panel showing all available plans with sign up buttons (for clients)
    private DefaultListModel<SelfPacedPlan> availablePlansModel;
    private JList<SelfPacedPlan> availablePlansList;

    /**
     * Creates and returns a panel displaying available self-paced plans for clients to sign up for.
     * @author zachtaylorcsc
     * @return A JPanel displaying available plans.
     */
    private JPanel createAvailablePlansPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel header = new JLabel(
                "<html><h2>Available Self-Paced Plans</h2>" +
                        "<p>Browse and sign up for self-paced plans created by trainers.</p></html>",
                SwingConstants.LEFT
        );
        panel.add(header, BorderLayout.NORTH);

        availablePlansModel = new DefaultListModel<>();
        availablePlansList = new JList<>(availablePlansModel);
        availablePlansList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availablePlansList.setVisibleRowCount(10);

        JScrollPane scrollPane = new JScrollPane(availablePlansList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Select a plan to sign up"));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Details and sign up button panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(BACKGROUND_COLOR);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsArea.setBackground(BACKGROUND_COLOR);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setText("Select a plan to see details and sign up.");
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setPreferredSize(new Dimension(0, 150));
        detailsPanel.add(detailsScroll, BorderLayout.CENTER);

        JButton signUpButton = new JButton("Sign Up for Selected Plan");
        signUpButton.setBackground(BAYLOR_GREEN);
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setOpaque(true);
        signUpButton.setBorderPainted(false);
        signUpButton.setFocusPainted(false);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton.setPreferredSize(new Dimension(0, 40));
        signUpButton.setEnabled(false);
        signUpButton.addActionListener(e -> {
            SelfPacedPlan selected = availablePlansList.getSelectedValue();
            if (selected != null) {
                signUpForPlan(selected, detailsArea);
            }
        });
        signUpButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (signUpButton.isEnabled()) {
                    signUpButton.setBackground(LIGHT_GREEN);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                signUpButton.setBackground(BAYLOR_GREEN);
            }
        });
        detailsPanel.add(signUpButton, BorderLayout.SOUTH);

        panel.add(detailsPanel, BorderLayout.SOUTH);

        // Update details and button state when selection changes
        availablePlansList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SelfPacedPlan selected = availablePlansList.getSelectedValue();
                if (selected != null) {
                    String text = "Title: " + selected.getTitle() + "\n\n" +
                            "Fitness Level: " + selected.getFitnessLevel() + "\n" +
                            "Equipment: " + selected.getEquipment() + "\n" +
                            "Session Length: " + selected.getSessionLength() + "\n" +
                            "Frequency: " + selected.getFrequency() + "\n\n" +
                            "Description:\n" +
                            (selected.getDescription() != null ? selected.getDescription() : "None");

                    boolean alreadyEnrolled = userId != -1 && 
                            selfPacedPlanManager.isUserEnrolledInPlan(userId, selected.getId());
                    if (alreadyEnrolled) {
                        text += "\n\n⚠ You are already enrolled in this plan.";
                    }

                    detailsArea.setText(text);
                    signUpButton.setEnabled(!alreadyEnrolled);
                } else {
                    detailsArea.setText("Select a plan to see details and sign up.");
                    signUpButton.setEnabled(false);
                }
            }
        });

        // Initial fill
        refreshAvailablePlans();

        return panel;
    }

    // Panel showing user's enrolled plans (for clients)
    private DefaultListModel<SelfPacedPlan> enrolledPlansModel;
    private JList<SelfPacedPlan> enrolledPlansList;

    private JPanel createEnrolledPlansPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel header = new JLabel("My Enrolled Plans", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(BAYLOR_GREEN);
        panel.add(header, BorderLayout.NORTH);

        enrolledPlansModel = new DefaultListModel<>();
        enrolledPlansList = new JList<>(enrolledPlansModel);
        enrolledPlansList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrolledPlansList.setVisibleRowCount(15);

        JScrollPane scrollPane = new JScrollPane(enrolledPlansList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Plans You're Following"));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Details panel
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsArea.setBackground(BACKGROUND_COLOR);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);

        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Plan Details"));
        detailsScroll.setPreferredSize(new Dimension(0, 150));
        panel.add(detailsScroll, BorderLayout.SOUTH);

        // Show details when plan is selected
        enrolledPlansList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SelfPacedPlan selected = enrolledPlansList.getSelectedValue();
                if (selected != null) {
                    String text = "Title: " + selected.getTitle() + "\n\n" +
                            "Fitness Level: " + selected.getFitnessLevel() + "\n" +
                            "Equipment: " + selected.getEquipment() + "\n" +
                            "Session Length: " + selected.getSessionLength() + "\n" +
                            "Frequency: " + selected.getFrequency() + "\n\n" +
                            "Description:\n" +
                            (selected.getDescription() != null ? selected.getDescription() : "None");
                    detailsArea.setText(text);
                } else {
                    detailsArea.setText("");
                }
            }
        });

        // Load enrolled plans
        refreshEnrolledPlans();

        return panel;
    }

    /**
     * Refreshes the list of available self-paced plans displayed in the UI.
     * @author zachtaylorcsc
     */
    private void refreshAvailablePlans() {
        if (availablePlansModel != null && selfPacedPlanManager != null) {
            availablePlansModel.clear();
            for (SelfPacedPlan p : selfPacedPlanManager.getAllPlans()) {
                availablePlansModel.addElement(p);
            }
        }
    }

    /**
     * Refreshes the list of self-paced plans that the current user is enrolled in.
     * @author zachtaylorcsc
     */
    private void refreshEnrolledPlans() {
        if (enrolledPlansModel != null && selfPacedPlanManager != null && userId != -1) {
            enrolledPlansModel.clear();
            List<SelfPacedPlan> plans = selfPacedPlanManager.getEnrolledPlansForUser(userId);
            for (SelfPacedPlan p : plans) {
                enrolledPlansModel.addElement(p);
            }
        }
    }

    /**
     * Handles the logic for a user to sign up for a self-paced plan.
     * @author zachtaylorcsc
     */
    private void signUpForPlan(SelfPacedPlan plan, JTextArea detailsArea) {
        if (userId == -1) {
            JOptionPane.showMessageDialog(this,
                "Unable to sign up: User not found.",
                "Sign Up Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if already enrolled
        if (selfPacedPlanManager.isUserEnrolledInPlan(userId, plan.getId())) {
            JOptionPane.showMessageDialog(this,
                "You are already enrolled in this plan.",
                "Already Enrolled",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Attempt enrollment
        boolean success = selfPacedPlanManager.enrollUserInPlan(userId, plan.getId());
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Successfully signed up for: " + plan.getTitle(),
                "Sign Up Successful",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh both lists
            refreshAvailablePlans();
            refreshEnrolledPlans();
            
            // Update details area
            String text = detailsArea.getText();
            if (!text.contains("⚠ You are already enrolled")) {
                detailsArea.setText(text + "\n\n✅ You are now enrolled in this plan!");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to sign up. You may already be enrolled in this plan.",
                "Sign Up Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }



    /**
     * Creates and returns the classes tab panel, displaying either available classes for clients or a calendar view for trainers.
     * @author zachtaylorcsc
     * @return A JPanel representing the classes tab.
     */
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
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel header = new JLabel("All Classes (for all trainers)", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(BAYLOR_GREEN);
        headerPanel.add(header, BorderLayout.CENTER);
        
        // Refresh button with proper sizing
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(BAYLOR_GREEN);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setOpaque(true);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshButton.setPreferredSize(new Dimension(100, 30));
        refreshButton.setMinimumSize(new Dimension(100, 30));
        refreshButton.setMaximumSize(new Dimension(100, 30));
        refreshButton.addActionListener(e -> refreshCalendarGrid());
        
        // Add hover effect
        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(BAYLOR_GREEN);
            }
        });
        
        // Wrap button in a panel to ensure it doesn't get compressed
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(refreshButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Calendar view panel (only view now)
        calendarViewPanel = createCalendarView();
        mainPanel.add(calendarViewPanel, BorderLayout.CENTER);
        
        // Update refresh button to refresh calendar
        refreshButton.removeActionListener(refreshButton.getActionListeners()[0]);
        refreshButton.addActionListener(e -> refreshCalendarGrid());

        return mainPanel;
    }
    
    /**
     * Creates a monthly calendar view showing classes as events
     */
    /**
     * Creates and returns a monthly calendar view panel showing classes as events.
     * @author zachtaylorcsc
     * @return A JPanel representing the calendar view.
     */
    private JPanel createCalendarView() {
        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBackground(BACKGROUND_COLOR);
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Calendar header with month/year and view buttons
        JPanel calendarHeader = new JPanel(new BorderLayout());
        calendarHeader.setBackground(BACKGROUND_COLOR);
        calendarHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        calendarHeader.setPreferredSize(new Dimension(0, 60)); // Ensure header has enough height
        
        // Use a single panel with BoxLayout to control spacing better
        JPanel headerContent = new JPanel();
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.X_AXIS));
        headerContent.setBackground(BACKGROUND_COLOR);
        headerContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Left side: Previous month button
        JButton prevButton = new JButton("Prev");
        prevButton.setFont(new Font("Arial", Font.BOLD, 12));
        prevButton.setBackground(LIGHT_GREEN);
        prevButton.setForeground(Color.WHITE);
        prevButton.setOpaque(true);
        prevButton.setBorderPainted(false);
        prevButton.setFocusPainted(false);
        prevButton.setPreferredSize(new Dimension(70, 35));
        prevButton.setMinimumSize(new Dimension(70, 35));
        prevButton.setMaximumSize(new Dimension(70, 35));
        prevButton.setMargin(new Insets(5, 10, 5, 10));
        prevButton.addActionListener(e -> {
            calendarCurrentMonth = calendarCurrentMonth.minusMonths(1);
            refreshCalendarGrid();
        });
        
        // Wrap prev button in a panel to enforce size
        JPanel prevWrapper = new JPanel(new BorderLayout());
        prevWrapper.setBackground(BACKGROUND_COLOR);
        prevWrapper.setPreferredSize(new Dimension(70, 35));
        prevWrapper.add(prevButton, BorderLayout.CENTER);
        
        headerContent.add(prevWrapper);
        headerContent.add(Box.createHorizontalStrut(15)); // Small gap before month
        
        // Center: Month/year label
        java.time.format.DateTimeFormatter monthFormatter = java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy");
        calendarMonthLabel = new JLabel(calendarCurrentMonth.format(monthFormatter).toUpperCase());
        calendarMonthLabel.setFont(new Font("Arial", Font.BOLD, 20));
        calendarMonthLabel.setForeground(BAYLOR_GREEN);
        calendarMonthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        calendarMonthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add glue before and after month label to center it
        headerContent.add(Box.createHorizontalGlue());
        headerContent.add(calendarMonthLabel);
        headerContent.add(Box.createHorizontalGlue());
        
        headerContent.add(Box.createHorizontalStrut(15)); // Small gap after month
        
        // Right side: Next button
        JButton nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 12));
        nextButton.setBackground(LIGHT_GREEN);
        nextButton.setForeground(Color.WHITE);
        nextButton.setOpaque(true);
        nextButton.setBorderPainted(false);
        nextButton.setFocusPainted(false);
        nextButton.setPreferredSize(new Dimension(70, 35));
        nextButton.setMinimumSize(new Dimension(70, 35));
        nextButton.setMaximumSize(new Dimension(70, 35));
        nextButton.setMargin(new Insets(5, 10, 5, 10));
        nextButton.addActionListener(e -> {
            calendarCurrentMonth = calendarCurrentMonth.plusMonths(1);
            refreshCalendarGrid();
        });
        
        // Wrap next button in a panel to enforce size
        JPanel nextWrapper = new JPanel(new BorderLayout());
        nextWrapper.setBackground(BACKGROUND_COLOR);
        nextWrapper.setPreferredSize(new Dimension(70, 35));
        nextWrapper.add(nextButton, BorderLayout.CENTER);
        
        headerContent.add(nextWrapper);
        headerContent.add(Box.createHorizontalStrut(10)); // Gap before Today button
        
        // Today button
        JButton todayButton = new JButton("Today");
        todayButton.setBackground(BACKGROUND_COLOR);
        todayButton.setForeground(BAYLOR_GREEN);
        todayButton.setOpaque(true);
        todayButton.setBorderPainted(false);
        todayButton.setFont(new Font("Arial", Font.PLAIN, 12));
        todayButton.setPreferredSize(new Dimension(80, 30));
        todayButton.setMinimumSize(new Dimension(80, 30));
        todayButton.setMaximumSize(new Dimension(80, 30));
        todayButton.setMargin(new Insets(5, 10, 5, 10));
        todayButton.addActionListener(e -> {
            calendarCurrentMonth = java.time.LocalDate.now();
            refreshCalendarGrid();
        });
        
        // Wrap today button in a panel to enforce size
        JPanel todayWrapper = new JPanel(new BorderLayout());
        todayWrapper.setBackground(BACKGROUND_COLOR);
        todayWrapper.setPreferredSize(new Dimension(80, 30));
        todayWrapper.add(todayButton, BorderLayout.CENTER);
        
        headerContent.add(todayWrapper);
        
        calendarHeader.add(headerContent, BorderLayout.CENTER);
        
        calendarPanel.add(calendarHeader, BorderLayout.NORTH);
        
        // Monthly calendar grid container
        calendarGridContainer = new JPanel(new BorderLayout());
        calendarGridContainer.setBackground(BACKGROUND_COLOR);
        calendarGridContainer.setPreferredSize(new Dimension(0, 600)); // Prevent shrinking
        JPanel monthCalendar = createMonthlyCalendarGrid();
        calendarGridContainer.add(monthCalendar, BorderLayout.CENTER);
        calendarPanel.add(calendarGridContainer, BorderLayout.CENTER);
        
        return calendarPanel;
    }
    
    /**
     * Refreshes only the calendar grid and month label without recreating the entire panel
     */
    /**
     * Refreshes only the calendar grid and month label without recreating the entire panel.
     * @author zachtaylorcsc
     */
    private void refreshCalendarGrid() {
        if (calendarMonthLabel != null && calendarGridContainer != null) {
            // Update month label
            java.time.format.DateTimeFormatter monthFormatter = java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy");
            calendarMonthLabel.setText(calendarCurrentMonth.format(monthFormatter).toUpperCase());
            
            // Update calendar grid
            calendarGridContainer.removeAll();
            JPanel monthCalendar = createMonthlyCalendarGrid();
            calendarGridContainer.add(monthCalendar, BorderLayout.CENTER);
            calendarGridContainer.revalidate();
            calendarGridContainer.repaint();
        }
    }
    
    /**
     * Creates the monthly calendar grid with classes displayed as events
     */
    /**
     * Creates the monthly calendar grid with classes displayed as events.
     * @author zachtaylorcsc
     * @return A JPanel representing the monthly calendar grid.
     */
    private JPanel createMonthlyCalendarGrid() {
        JPanel monthPanel = new JPanel(new BorderLayout());
        monthPanel.setBackground(BACKGROUND_COLOR);
        monthPanel.setPreferredSize(new Dimension(0, 600)); // Set preferred height to prevent shrinking
        
        // Get first day of month and first Monday of the calendar
        java.time.LocalDate firstDayOfMonth = calendarCurrentMonth.withDayOfMonth(1);
        java.time.DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();
        java.time.LocalDate firstMonday = firstDayOfMonth.minusDays(firstDayOfWeek.getValue() - 1);
        
        // Days of week header
        JPanel daysHeader = new JPanel(new GridLayout(1, 7));
        daysHeader.setBackground(BACKGROUND_COLOR);
        daysHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BAYLOR_GREEN));
        
        String[] dayNames = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
        
        for (int i = 0; i < 7; i++) {
            JPanel dayHeaderPanel = new JPanel(new BorderLayout());
            dayHeaderPanel.setBackground(new Color(BAYLOR_GREEN.getRed(), BAYLOR_GREEN.getGreen(), BAYLOR_GREEN.getBlue(), 50));
            dayHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            
            JLabel dayNameLabel = new JLabel(dayNames[i], SwingConstants.CENTER);
            dayNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
            dayNameLabel.setForeground(BAYLOR_GREEN);
            dayHeaderPanel.add(dayNameLabel, BorderLayout.CENTER);
            
            daysHeader.add(dayHeaderPanel);
        }
        
        monthPanel.add(daysHeader, BorderLayout.NORTH);
        
        // Calendar grid for events (6 weeks to cover full month)
        JPanel calendarGrid = new JPanel(new GridLayout(6, 7, 2, 2));
        calendarGrid.setBackground(BACKGROUND_COLOR);
        calendarGrid.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        // Get all classes
        List<WorkoutClass> allClasses = DB_MANAGER != null ? DB_MANAGER.getAllClasses(csp) : new ArrayList<>();
        
        // Create a day panel for each day (6 weeks * 7 days = 42 days)
        java.time.LocalDate currentDay = firstMonday;
        
        for (int week = 0; week < 6; week++) {
            for (int day = 0; day < 7; day++) {
                JPanel dayPanel = new JPanel(new BorderLayout());
                dayPanel.setBackground(Color.WHITE);
                dayPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
                
                // Day number label
                JLabel dayNumberLabel = new JLabel(String.valueOf(currentDay.getDayOfMonth()), SwingConstants.LEFT);
                dayNumberLabel.setFont(new Font("Arial", Font.BOLD, 12));
                dayNumberLabel.setBorder(BorderFactory.createEmptyBorder(3, 5, 0, 0));
                
                // Gray out days not in current month
                if (currentDay.getMonth() != calendarCurrentMonth.getMonth()) {
                    dayPanel.setBackground(new Color(245, 245, 245));
                    dayNumberLabel.setForeground(new Color(180, 180, 180));
                } else {
                    dayNumberLabel.setForeground(BAYLOR_GREEN);
                    // Highlight today
                    if (currentDay.equals(java.time.LocalDate.now())) {
                        dayPanel.setBackground(new Color(240, 255, 250));
                        dayNumberLabel.setForeground(LIGHT_GREEN);
                        dayNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    }
                }
                
                dayPanel.add(dayNumberLabel, BorderLayout.NORTH);
                
                // Events panel
                JPanel eventsPanel = new JPanel();
                eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
                eventsPanel.setOpaque(false);
                eventsPanel.setBorder(BorderFactory.createEmptyBorder(20, 2, 2, 2));
                
                // Find classes for this day
                final java.time.LocalDate dayDate = currentDay;
                List<WorkoutClass> dayClasses = allClasses.stream()
                    .filter(wc -> wc.getStartTime().toLocalDate().equals(dayDate))
                    .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
                    .collect(Collectors.toList());
                
                // Add class blocks (limit to 3 visible, show "+X more" if needed)
                int maxVisible = 3;
                for (int i = 0; i < Math.min(dayClasses.size(), maxVisible); i++) {
                    WorkoutClass wc = dayClasses.get(i);
                    JPanel classBlock = createClassBlock(wc);
                    eventsPanel.add(classBlock);
                }
                
                if (dayClasses.size() > maxVisible) {
                    JLabel moreLabel = new JLabel("+" + (dayClasses.size() - maxVisible) + " more");
                    moreLabel.setFont(new Font("Arial", Font.PLAIN, 9));
                    moreLabel.setForeground(BAYLOR_GREEN);
                    eventsPanel.add(moreLabel);
                }
                
                dayPanel.add(eventsPanel, BorderLayout.CENTER);
                
                calendarGrid.add(dayPanel);
                currentDay = currentDay.plusDays(1);
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(calendarGrid);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        monthPanel.add(scrollPane, BorderLayout.CENTER);
        
        return monthPanel;
    }
    
    /**
     * Creates a visual block representing a class event (compact for monthly view)
     */
    /**
     * Creates a visual block representing a class event for the calendar view.
     * @author zachtaylorcsc
     * @return A JPanel representing a class event block.
     */
    private JPanel createClassBlock(WorkoutClass wc) {
        JPanel block = new JPanel(new BorderLayout());
        block.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 3));
        block.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        
        // Color based on class type (using hash of class type name for consistency)
        Color blockColor = getColorForClassType(wc.getClassType());
        block.setBackground(blockColor);
        
        // Class name and time (compact)
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("h:mm a");
        String displayText = wc.getClassType() + " " + wc.getStartTime().format(timeFormatter);
        JLabel nameLabel = new JLabel(displayText);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 9));
        nameLabel.setForeground(Color.WHITE);
        block.add(nameLabel, BorderLayout.CENTER);
        
        // Tooltip with full details
        String tooltip = String.format("%s\nTrainer: %s\n%s\nTime: %s - %s\nCost: $%.2f",
            wc.getClassType(),
            wc.getTrainerUsername(),
            wc.getDescription(),
            wc.getStartTime().format(timeFormatter),
            wc.getEndTime().format(timeFormatter),
            wc.getCost());
        block.setToolTipText(tooltip);
        
        // Make it clickable
        block.setCursor(new Cursor(Cursor.HAND_CURSOR));
        block.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Get enrolled users for this class
                List<String> enrolledUsers = DB_MANAGER != null ? DB_MANAGER.getUsersForClass(wc.getId()) : new ArrayList<>();
                
                // Build message with class details and enrolled users
                StringBuilder message = new StringBuilder();
                message.append(String.format("Class: %s\n", wc.getClassType()));
                message.append(String.format("Trainer: %s\n", wc.getTrainerUsername()));
                message.append(String.format("Description: %s\n", wc.getDescription()));
                message.append(String.format("Time: %s - %s\n", 
                    wc.getStartTime().format(timeFormatter),
                    wc.getEndTime().format(timeFormatter)));
                message.append(String.format("Cost: $%.2f\n", wc.getCost()));
                message.append(String.format("Max Participants: %d\n", wc.getMaxParticipants()));
                message.append(String.format("Current Enrollment: %d\n\n", enrolledUsers.size()));
                
                if (enrolledUsers.isEmpty()) {
                    message.append("No users enrolled yet.");
                } else {
                    message.append("Enrolled Users:\n");
                    for (int i = 0; i < enrolledUsers.size(); i++) {
                        message.append(String.format("%d. %s\n", i + 1, enrolledUsers.get(i)));
                    }
                }
                
                JOptionPane.showMessageDialog(block, message.toString(), "Class Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        return block;
    }
    
    /**
     * Gets a consistent color for a class type
     */
    /**
     * Gets a consistent color for a class type based on its hash code.
     * @author zachtaylorcsc
     * @return A Color object for the given class type.
     */
    private Color getColorForClassType(String classType) {
        // Use hash of class type to get consistent colors
        int hash = classType.hashCode();
        int r = Math.abs(hash % 100) + 100;
        int g = Math.abs((hash / 100) % 100) + 100;
        int b = Math.abs((hash / 10000) % 100) + 100;
        return new Color(Math.min(r, 200), Math.min(g, 200), Math.min(b, 200));
    }
    
    // Method to refresh the classes list from the database
    /**
     * Refreshes the list of all classes displayed in the UI.
     * @author zachtaylorcsc
     */
    private void refreshClassesList() {
        if (classListModel != null && DB_MANAGER != null) {
            classListModel.clear();
            List<WorkoutClass> classes = DB_MANAGER.getAllClasses(csp);
            for (WorkoutClass wc : classes) {
                classListModel.addElement(wc);
            }
        }
    }

    // Client view for Classes tab - shows available classes and allows registration
    /**
     * Creates and returns the client's view of the classes tab, showing available and enrolled classes.
     * @author zachtaylorcsc
     * @return A JPanel representing the client's classes tab.
     */
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

        //My Classes tab (classes Search)
        JPanel searchPanel = createClassesSearchPanel();
        clientTabbedPane.addTab("Search", searchPanel);

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

    /**
     * Creates and returns a panel displaying available classes for clients to register for.
     * @author zachtaylorcsc
     * @return A JPanel displaying available classes.
     */
    private JPanel createAvailableClassesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

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
                    int currentEnrolled = DB_MANAGER.getCurrentEnrollmentCount(selected.getId());
                    int spotsAvailable = selected.getMaxParticipants() - currentEnrolled;
                    boolean alreadyEnrolled = userId != -1 && DB_MANAGER.isUserEnrolled(userId, selected.getId());
                    
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

    /**
     * Creates and returns a panel displaying classes the user is currently enrolled in.
     * @author zachtaylorcsc
     * @return A JPanel displaying enrolled classes.
     */
    private JPanel createEnrolledClassesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

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

    /**
     * Creates and returns a panel for searching classes.
     * @author Owen Chipman
     * @return A JPanel representing the class search functionality.
     */
    private JPanel createClassesSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new GridLayout(2,2));

        //initializing buttons
        //class type box
        List<String> classTypes = ClassType.getClassTypes();
        JComboBox<String> classTypeBox = new JComboBox<>(classTypes.toArray(new String[classTypes.size()]));
        centerPanel.add(classTypeBox);
        //available trainers box
        List<String> trainers = DB_MANAGER.getAllTrainers();
        trainers.add(0, "Trainer--");
        JComboBox<String> trainerBox = new JComboBox<>(trainers.toArray(new String[trainers.size()]));
        centerPanel.add(trainerBox);
        //Duration Box
        String[] durationOptions = {"Duration--", "30 min", "1 Hour", "1.5 Hours", "2 Hours", "2+ Hours"};
        JComboBox<String> durationBox = new JComboBox<>(durationOptions);
        centerPanel.add(durationBox);
        //Time of day box
        String[] timeOfDayOptions = {"Time of Day--", "Early Morning", "Morning", "Afternoon", "Evening", "Night", "The Witching Hour"};
        JComboBox<String> timeOfDayBox = new JComboBox<>(timeOfDayOptions);
        centerPanel.add(timeOfDayBox);

        JButton apply = new JButton("Apply");
        apply.addActionListener(e -> {
            String classType = (String) classTypeBox.getSelectedItem();
            String trainer = (String) trainerBox.getSelectedItem();
            String duration = (String) durationBox.getSelectedItem();
            String timeOfDay = (String) timeOfDayBox.getSelectedItem();


            csp.assignVals(classType, trainer, duration, timeOfDay);
            //DB_MANAGER.selectValidClasses(classType, trainer, duration, timeOfDay);
        });
        panel.add(apply, BorderLayout.PAGE_END);
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel header = new JLabel("Classes Search", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(BAYLOR_GREEN);
        panel.add(header, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);


        // Load enrolled classes
        refreshEnrolledClasses();

        return panel;
    }

    /**
     * Refreshes the list of available classes displayed in the UI.
     * @author zachtaylorcsc
     */
    private void refreshAvailableClasses() {
        if (availableClassesModel != null && DB_MANAGER != null) {
            availableClassesModel.clear();
            List<WorkoutClass> classes = DB_MANAGER.getAllClasses(csp);
            for (WorkoutClass wc : classes) {
                availableClassesModel.addElement(wc);
            }
        }
    }

    /**
     * Refreshes the list of classes the user is currently enrolled in.
     * @author zachtaylorcsc
     */
    private void refreshEnrolledClasses() {
        if (enrolledClassesModel != null && DB_MANAGER != null && userId != -1) {
            enrolledClassesModel.clear();
            List<WorkoutClass> classes = DB_MANAGER.getUserEnrolledClasses(userId);
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

    /**
     * Handles the logic for a user to register for a workout class.
     * @author zachtaylorcsc
     */
    private void registerForClass(WorkoutClass workoutClass, JTextArea detailsArea) {
        if (userId == -1) {
            JOptionPane.showMessageDialog(this,
                "Unable to register: User not found.",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if already enrolled
        if (DB_MANAGER.isUserEnrolled(userId, workoutClass.getId())) {
            JOptionPane.showMessageDialog(this,
                "You are already enrolled in this class.",
                "Already Enrolled",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Check if class is full
        int currentEnrolled = DB_MANAGER.getCurrentEnrollmentCount(workoutClass.getId());
        if (currentEnrolled >= workoutClass.getMaxParticipants()) {
            JOptionPane.showMessageDialog(this,
                "This class is full. Please select another class.",
                "Class Full",
                JOptionPane.WARNING_MESSAGE);
            refreshAvailableClasses();
            return;
        }

        // Attempt enrollment
        boolean success = DB_MANAGER.enrollUserInClass(userId, workoutClass.getId());
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
        /**
         * Returns a component that has been configured to display the specified value, representing a WorkoutClass.
         * @author zachtaylorcsc
         * @return A component that has been configured to display the specified value.
         */
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                     boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof WorkoutClass) {
                WorkoutClass wc = (WorkoutClass) value;
                int currentEnrolled = DB_MANAGER.getCurrentEnrollmentCount(wc.getId());
                int spotsAvailable = wc.getMaxParticipants() - currentEnrolled;
                String text = wc.getClassType() + " - " + wc.getStartTime() + 
                             " ($" + String.format("%.2f", wc.getCost()) + ") - " +
                             spotsAvailable + " spots available";
                setText(text);
            }
            return this;
        }
    }

    /**
     * Creates and returns the "Create Class" tab panel for trainers to create new classes.
     * @author zachtaylorcsc
     * @return A JPanel representing the "Create Class" tab.
     */
    private JPanel createCreateClassTab() {
        JPanel createClassPanel = new JPanel(new BorderLayout());
        createClassPanel.setBackground(BACKGROUND_COLOR);
        createClassPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Center panel with button to create class
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;

        JLabel titleLabel = new JLabel("Create New Class");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(BAYLOR_GREEN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 30, 20);
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(titleLabel, gbc);
        
        JButton createClassButton = new JButton("Create Class");
        createClassButton.setBackground(BAYLOR_GREEN);
        createClassButton.setForeground(Color.WHITE);
        createClassButton.setOpaque(true);
        createClassButton.setBorderPainted(false);
        createClassButton.setFocusPainted(false);
        createClassButton.setFont(new Font("Arial", Font.BOLD, 16));
        createClassButton.setPreferredSize(new Dimension(250, 50));
        createClassButton.addActionListener(e -> {
            // Open CreateClass window, passing the current trainer's username
            // and reusing the existing DatabaseManager connection
            SwingUtilities.invokeLater(() -> {
                CreateClass.CreateAndShowGUI(username);
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
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
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
        modifyClassButton.setPreferredSize(new Dimension(250, 50));

        modifyClassButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                // Open ModifyClass window and refresh lists after successful update
                ModifyClass.openModifyClassPage(username,  () -> {
                    refreshCalendarGrid();    // Trainer "Classes" tab (calendar view)
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
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(modifyClassButton, gbc);
        
        JTextArea infoTextArea = new JTextArea(
            "Click the button above to create a new fitness class.\n\n" +
            "You'll be able to set:\n" +
            "• Class type and description\n" +
            "• Start and end times\n" +
            "• Maximum participants\n" +
            "• Cost"
        );
        infoTextArea.setEditable(false);
        infoTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        infoTextArea.setBackground(BACKGROUND_COLOR);
        infoTextArea.setForeground(BAYLOR_GREEN);
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoTextArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 40, 20, 40);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(infoTextArea, gbc);
        
        createClassPanel.add(centerPanel, BorderLayout.CENTER);
        
        return createClassPanel;
    }

    /**
     * Creates and returns the login streak tab panel.
     * @author zachtaylorcsc
     * @return A JPanel representing the login streak tab.
     */
    private JPanel createStreakTab() {
        JPanel streakPanel = new JPanel(new BorderLayout());
        streakPanel.setBackground(BACKGROUND_COLOR);
        streakPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Main content panel
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);

        // Title
        JLabel titleLabel = new JLabel("Login Streak");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(BAYLOR_GREEN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainContent.add(titleLabel, gbc);

        // Get streak data
        int currentStreak = userId != -1 ? DB_MANAGER.getCurrentStreak(userId) : 0;
        int longestStreak = userId != -1 ? DB_MANAGER.getLongestStreak(userId) : 0;
        int totalLogins = userId != -1 ? DB_MANAGER.getTotalLoginDays(userId) : 0;

        // Current Streak Display
        JPanel currentStreakPanel = new JPanel(new BorderLayout());
        currentStreakPanel.setBackground(new Color(255, 199, 44));
        currentStreakPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BAYLOR_GREEN, 3),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel currentStreakLabel = new JLabel("Current Streak", SwingConstants.CENTER);
        currentStreakLabel.setFont(new Font("Arial", Font.BOLD, 18));
        currentStreakLabel.setForeground(BAYLOR_GREEN);
        currentStreakPanel.add(currentStreakLabel, BorderLayout.NORTH);
        
        JLabel currentStreakValue = new JLabel(String.valueOf(currentStreak), SwingConstants.CENTER);
        currentStreakValue.setFont(new Font("Arial", Font.BOLD, 48));
        currentStreakValue.setForeground(BAYLOR_GREEN);
        currentStreakPanel.add(currentStreakValue, BorderLayout.CENTER);

        JLabel daysLabel = new JLabel("day" + (currentStreak != 1 ? "s" : ""), SwingConstants.CENTER);
        daysLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        daysLabel.setForeground(BAYLOR_GREEN);
        currentStreakPanel.add(daysLabel, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainContent.add(currentStreakPanel, gbc);

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBackground(BACKGROUND_COLOR);

        // Longest Streak
        JPanel longestStreakPanel = new JPanel(new BorderLayout());
        longestStreakPanel.setBackground(BACKGROUND_COLOR);
        longestStreakPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BAYLOR_GREEN, 2),
            "Longest Streak"
        ));

        JLabel longestStreakValue = new JLabel(String.valueOf(longestStreak), SwingConstants.CENTER);
        longestStreakValue.setFont(new Font("Arial", Font.BOLD, 36));
        longestStreakValue.setForeground(BAYLOR_GREEN);
        longestStreakPanel.add(longestStreakValue, BorderLayout.CENTER);

        // Total Logins
        JPanel totalLoginsPanel = new JPanel(new BorderLayout());
        totalLoginsPanel.setBackground(BACKGROUND_COLOR);
        totalLoginsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BAYLOR_GREEN, 2),
            "Total Login Days"
        ));

        JLabel totalLoginsValue = new JLabel(String.valueOf(totalLogins), SwingConstants.CENTER);
        totalLoginsValue.setFont(new Font("Arial", Font.BOLD, 36));
        totalLoginsValue.setForeground(BAYLOR_GREEN);
        totalLoginsPanel.add(totalLoginsValue, BorderLayout.CENTER);

        statsPanel.add(longestStreakPanel);
        statsPanel.add(totalLoginsPanel);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        mainContent.add(statsPanel, gbc);

        // Rewards/Milestones section
        JPanel rewardsPanel = new JPanel(new BorderLayout());
        rewardsPanel.setBackground(BACKGROUND_COLOR);
        rewardsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BAYLOR_GREEN, 2),
            "Streak Milestones & Rewards"
        ));

        JTextArea rewardsText = new JTextArea();
        rewardsText.setEditable(false);
        rewardsText.setFont(new Font("Arial", Font.PLAIN, 14));
        rewardsText.setBackground(BACKGROUND_COLOR);
        rewardsText.setForeground(BAYLOR_GREEN);
        rewardsText.setLineWrap(true);
        rewardsText.setWrapStyleWord(true);

        StringBuilder rewards = new StringBuilder();
        rewards.append("Streak Milestones:\n\n");
        rewards.append("• 7 days: Week Warrior\n");
        rewards.append("• 14 days: Two Week Champion\n");
        rewards.append("• 30 days: Monthly Master\n");
        rewards.append("• 60 days: Two Month Legend\n");
        rewards.append("• 100 days: Centurion\n\n");
        rewards.append("Tip: Log in every day to maintain your streak. Your streak resets if you miss a day.");

        // Check if user has reached any milestones
        if (currentStreak >= 100) {
            rewards.append("\n\nCongratulations! You've reached 100 days!");
        } else if (currentStreak >= 60) {
            rewards.append("\n\nAmazing! You're a Two Month Legend!");
        } else if (currentStreak >= 30) {
            rewards.append("\n\nGreat job! You're a Monthly Master!");
        } else if (currentStreak >= 14) {
            rewards.append("\n\nWell done! You're a Two Week Champion!");
        } else if (currentStreak >= 7) {
            rewards.append("\n\nKeep it up! You're a Week Warrior!");
        } else if (currentStreak > 0) {
            rewards.append("\n\nYou're building your streak! Keep logging in daily!");
        }

        rewardsText.setText(rewards.toString());
        JScrollPane rewardsScroll = new JScrollPane(rewardsText);
        rewardsScroll.setBorder(null);
        rewardsPanel.add(rewardsScroll, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        mainContent.add(rewardsPanel, gbc);

        streakPanel.add(mainContent, BorderLayout.CENTER);

        return streakPanel;
    }

    /**
     * Creates and returns the historical data tab panel, containing various graphs.
     * @author Owen Chipman
     * @return A JPanel representing the historical data tab.
     */
    private JPanel createHistoricalTab() {
        historicalTabPanel = new JPanel(new BorderLayout());
        historicalTabPanel.setBackground(BACKGROUND_COLOR);

        // Create a tabbed pane for different graph pages
        graphTabbedPane = new JTabbedPane();
        graphTabbedPane.setBackground(BACKGROUND_COLOR);
        graphTabbedPane.setForeground(BAYLOR_GREEN);

        // First page: Main data graphs (calories, weight, sleep, total calories burnt)
        JPanel mainGraphsPanel = createMainGraphsPanel();
        graphTabbedPane.addTab("Health Data", mainGraphsPanel);

        // Second page: Workout graphs
        JPanel workoutGraphsPanel = createWorkoutGraphsPanel();
        graphTabbedPane.addTab("Workout Data", workoutGraphsPanel);

        // Add refresh button panel at the top
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshPanel.setBackground(BACKGROUND_COLOR);
        JButton refreshButton = new JButton("Refresh Graphs");
        refreshButton.setBackground(BAYLOR_GREEN);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setOpaque(true);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(BAYLOR_GREEN);
            }
        });
        refreshButton.addActionListener(e -> refreshGraphs());
        refreshPanel.add(refreshButton);

        historicalTabPanel.add(refreshPanel, BorderLayout.NORTH);
        historicalTabPanel.add(graphTabbedPane, BorderLayout.CENTER);

        return historicalTabPanel;
    }
    
    /**
     * Creates and returns a panel containing the main health data graphs (calories, weight, sleep, total calories burnt).
     * @author zachtaylorcsc
     * @return A JPanel displaying the main health data graphs.
     */
    private JPanel createMainGraphsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Create graphs with user data
        int days = 0; // 0 means all data, can be changed based on time filter buttons
        drawCaloriesConsumedGraph calorieGraph = new drawCaloriesConsumedGraph(userId, days);
        drawWeightGraph weightGraph = new drawWeightGraph(userId, days);
        drawSleepGraph sleepGraph = new drawSleepGraph(userId, days);
        drawTotalCaloriesBurntGraph burntGraph = new drawTotalCaloriesBurntGraph(userId, days);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(calorieGraph, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(burntGraph, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(weightGraph, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(sleepGraph, gbc);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        wrapper.setBackground(BACKGROUND_COLOR);

        return wrapper;
    }

    /**
     * Creates and returns a panel containing the workout data graphs (workout type, minutes of exercise, active calories burnt).
     * @author zachtaylorcsc
     * @return A JPanel displaying the workout data graphs.
     */
    private JPanel createWorkoutGraphsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        int days = 0; // 0 means all data
        drawWorkoutTypeGraph workoutTypeGraph = new drawWorkoutTypeGraph(userId, days);
        drawMinutesOfExerciseGraph minutesGraph = new drawMinutesOfExerciseGraph(userId,  days);
        drawActiveCaloriesBurntGraph activeCaloriesGraph = new drawActiveCaloriesBurntGraph(userId, days);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 10, 5);
        panel.add(workoutTypeGraph, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(minutesGraph, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(activeCaloriesGraph, gbc);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Add button panel at the top
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton quickAddWorkoutButton = new JButton("Quick Add Workout");
        quickAddWorkoutButton.setBackground(BAYLOR_GREEN);
        quickAddWorkoutButton.setForeground(Color.WHITE);
        quickAddWorkoutButton.setOpaque(true);
        quickAddWorkoutButton.setBorderPainted(false);
        quickAddWorkoutButton.setFocusPainted(false);
        quickAddWorkoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        quickAddWorkoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                quickAddWorkoutButton.setBackground(LIGHT_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                quickAddWorkoutButton.setBackground(BAYLOR_GREEN);
            }
        });
        quickAddWorkoutButton.addActionListener(e -> {
            if (userId == -1) {
                JOptionPane.showMessageDialog(DashboardUI.this,
                        "Unable to add workout: User not found",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            new recordWorkout.recordWorkoutPage(userId, () -> {
                loadUserData();
                refreshGraphs();
            });
        });
        buttonPanel.add(quickAddWorkoutButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(buttonPanel, BorderLayout.NORTH);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        wrapper.setBackground(BACKGROUND_COLOR);

        return wrapper;
    }

    /**
     * Checks if the current user has a "trainer" user type.
     * @author zachtaylorcsc
     * @return True if the user is a trainer, false otherwise.
     */
    private boolean isTrainer() {
        return userType != null && userType.equalsIgnoreCase("trainer");
    }
    
    /**
     * Refreshes all graphs displayed in the historical data tab.
     * @author zachtaylorcsc
     */
    private void refreshGraphs() {
        if (graphTabbedPane == null) {
            return;
        }
        
        System.out.println("refreshGraphs: Refreshing all graphs...");
        
        // Remove existing tabs
        graphTabbedPane.removeAll();
        
        // Recreate graph panels
        JPanel mainGraphsPanel = createMainGraphsPanel();
        graphTabbedPane.addTab("Health Data", mainGraphsPanel);
        
        JPanel workoutGraphsPanel = createWorkoutGraphsPanel();
        graphTabbedPane.addTab("Workout Data", workoutGraphsPanel);
        
        // Refresh the panel to show updated graphs
        historicalTabPanel.revalidate();
        historicalTabPanel.repaint();
        
        System.out.println("refreshGraphs: Graphs refreshed successfully");
    }

    /**
     * Loads and updates the user's fitness data displayed on the dashboard.
     * @author zachtaylorcsc
     */
    private void loadUserData() {
        if (userId == -1) {
            // User not found in database
            updateDataTabWithNoData();
            return;
        }

        // Get today's data
        double[] todayData = DB_MANAGER.getLatestUserDataDouble(userId);
        
        // Get historical data for trends and weekly averages
        java.util.List<Object[]> historicalData = DB_MANAGER.getHistoricalUserData(userId, 7);
        
        // Get yesterday's data for trend comparison
        double[] yesterdayData = null;
        if (historicalData != null && historicalData.size() >= 2) {
            // Get second most recent entry (yesterday)
            Object[] yesterdayEntry = historicalData.get(historicalData.size() - 2);
            if (yesterdayEntry != null && yesterdayEntry.length >= 5) {
                Integer cal = (Integer) yesterdayEntry[1];
                Double wt = (Double) yesterdayEntry[2];
                Double slp = (Double) yesterdayEntry[3];
                Integer brn = (Integer) yesterdayEntry[4];
                if (cal != null && wt != null && slp != null && brn != null) {
                    yesterdayData = new double[]{cal, wt, slp, brn};
                }
            }
        }

        if (todayData != null && todayData.length == 4) {
            int caloriesConsumed = (int)todayData[0];
            double weight = todayData[1];
            double sleepHours = todayData[2];
            int totalCaloriesBurned = (int)todayData[3];
            
            // Update new components if they exist
            if (caloriesValueLabel != null) {
                caloriesValueLabel.setText(String.format("%,d", caloriesConsumed));
                updateTrendLabel(caloriesTrendLabel, caloriesConsumed, 
                    yesterdayData != null ? (int)yesterdayData[0] : -1, true);
            }
            
            if (burnedValueLabel != null) {
                burnedValueLabel.setText(String.format("%,d", totalCaloriesBurned));
                updateTrendLabel(burnedTrendLabel, totalCaloriesBurned, 
                    yesterdayData != null ? (int)yesterdayData[3] : -1, true);
            }
            
            if (weightValueLabel != null) {
                weightValueLabel.setText(String.format("%.1f", weight));
                updateTrendLabel(weightTrendLabel, weight, 
                    yesterdayData != null ? yesterdayData[1] : -1, false);
            }
            
            if (sleepValueLabel != null) {
                sleepValueLabel.setText(String.format("%.1f", sleepHours));
                updateTrendLabel(sleepTrendLabel, sleepHours, 
                    yesterdayData != null ? yesterdayData[2] : -1, false);
            }
            
            // Calculate and display net calories
            if (netCaloriesValueLabel != null) {
                int netCalories = caloriesConsumed - totalCaloriesBurned;
                netCaloriesValueLabel.setText(String.format("%,d", netCalories));
                // Color code net calories
                if (netCalories < 0) {
                    netCaloriesValueLabel.setForeground(Color.decode("#FF6B6B")); // Red for deficit
                } else if (netCalories < 500) {
                    netCaloriesValueLabel.setForeground(Color.decode("#FFA500")); // Orange for small surplus
                } else {
                    netCaloriesValueLabel.setForeground(Color.decode("#4ECDC4")); // Teal for larger surplus
                }
            }
            
            // Calculate weekly averages
            if (weeklyAvgLabel != null && historicalData != null && historicalData.size() > 0) {
                updateWeeklyAverages(historicalData);
            }
            
            // Update goal progress
            updateGoalProgress(caloriesConsumed);
            
            // Keep old labels updated for backward compatibility
            if (caloriesLabel != null) {
                caloriesLabel.setText("Calories Consumed: " + caloriesConsumed + " kcal");
            }
            if (burnedLabel != null) {
                burnedLabel.setText("Calories Burned: " + totalCaloriesBurned + " kcal");
            }
            if (weightLabel != null) {
                weightLabel.setText("Weight: " + String.format("%.1f", weight) + " lbs");
            }
            if (sleepLabel != null) {
                sleepLabel.setText("Sleep: " + String.format("%.1f", sleepHours) + " hrs");
            }
        } else {
            // No data found
            updateDataTabWithNoData();
            updateGoalProgress(null);
        }
    }

    /**
     * Updates a trend label to show the change and percentage change from yesterday's value.
     * @author zachtaylorcsc
     */
    private void updateTrendLabel(JLabel trendLabel, double todayValue, double yesterdayValue, boolean isInteger) {
        if (trendLabel == null) return;
        
        if (yesterdayValue < 0) {
            trendLabel.setText("");
            trendLabel.setForeground(Color.GRAY);
            return;
        }
        
        double change = todayValue - yesterdayValue;
        double percentChange = yesterdayValue != 0 ? (change / yesterdayValue) * 100 : 0;
        
        String arrow;
        Color color;
        if (change > 0) {
            arrow = "↑";
            color = Color.decode("#4ECDC4"); // Teal for increase
        } else if (change < 0) {
            arrow = "↓";
            color = Color.decode("#FF6B6B"); // Red for decrease
        } else {
            arrow = "→";
            color = Color.GRAY;
        }
        
        String changeText;
        if (isInteger) {
            changeText = String.format("%s %d (%.1f%%)", arrow, Math.abs((int)change), Math.abs(percentChange));
        } else {
            changeText = String.format("%s %.1f (%.1f%%)", arrow, Math.abs(change), Math.abs(percentChange));
        }
        
        trendLabel.setText(changeText);
        trendLabel.setForeground(color);
    }

    /**
     * Calculates and displays the weekly averages for calories, burned, and sleep.
     * @author zachtaylorcsc
     */
    private void updateWeeklyAverages(java.util.List<Object[]> historicalData) {
        if (weeklyAvgLabel == null || historicalData == null || historicalData.isEmpty()) {
            return;
        }
        
        int count = 0;
        double totalCalories = 0;
        double totalBurned = 0;
        double totalSleep = 0;
        
        for (Object[] entry : historicalData) {
            if (entry.length >= 5) {
                Integer cal = (Integer) entry[1];
                Double slp = (Double) entry[3];
                Integer brn = (Integer) entry[4];
                
                if (cal != null) totalCalories += cal;
                if (brn != null) totalBurned += brn;
                if (slp != null) totalSleep += slp;
                count++;
            }
        }
        
        if (count > 0) {
            int avgCalories = (int)(totalCalories / count);
            int avgBurned = (int)(totalBurned / count);
            double avgSleep = totalSleep / count;
            
            weeklyAvgLabel.setText(String.format(
                "<html><div style='text-align: left;'>" +
                "Calories: %,d<br>" +
                "Burned: %,d<br>" +
                "Sleep: %.1f hrs</div></html>",
                avgCalories, avgBurned, avgSleep
            ));
        } else {
            weeklyAvgLabel.setText("<html><div style='text-align: left;'>" +
                "Calories: --<br>" +
                "Burned: --<br>" +
                "Sleep: -- hrs</div></html>");
        }
    }

    /**
     * Updates the data tab UI to reflect that no data is available.
     * @author zachtaylorcsc
     */
    private void updateDataTabWithNoData() {
        if (caloriesValueLabel != null) caloriesValueLabel.setText("--");
        if (burnedValueLabel != null) burnedValueLabel.setText("--");
        if (weightValueLabel != null) weightValueLabel.setText("--");
        if (sleepValueLabel != null) sleepValueLabel.setText("--");
        if (netCaloriesValueLabel != null) {
            netCaloriesValueLabel.setText("--");
            netCaloriesValueLabel.setForeground(Color.GRAY);
        }
        if (caloriesTrendLabel != null) caloriesTrendLabel.setText("");
        if (burnedTrendLabel != null) burnedTrendLabel.setText("");
        if (weightTrendLabel != null) weightTrendLabel.setText("");
        if (sleepTrendLabel != null) sleepTrendLabel.setText("");
        if (weeklyAvgLabel != null) {
            weeklyAvgLabel.setText("<html><div style='text-align: left;'>" +
                "Calories: --<br>" +
                "Burned: --<br>" +
                "Sleep: -- hrs</div></html>");
        }
        
        // Keep old labels updated for backward compatibility
        if (caloriesLabel != null) caloriesLabel.setText("Calories Consumed: No data");
        if (burnedLabel != null) burnedLabel.setText("Calories Burned: No data");
        if (weightLabel != null) weightLabel.setText("Weight: No data");
        if (sleepLabel != null) sleepLabel.setText("Sleep: No data");
    }



    /**
     * Disposes of the DashboardUI frame, ensuring database connections are properly closed.
     * @author zachtaylorcsc
     */
    @Override
    public void dispose() {
        if (DB_MANAGER != null) {
        }
        super.dispose();
    }

    /**
     * Main method to run the DashboardUI application for testing purposes.
     * @author zachtaylorcsc
     */
    public static void main(String[] args) {
        // Test main - in production, DashboardUI is called from LoginPage with actual first name
        SwingUtilities.invokeLater(() -> new DashboardUI("mowen"));
    }
}
