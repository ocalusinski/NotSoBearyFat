package myPackage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Allows trainers to modify the classes they have created
 */
public final class ModifyClass {
    // Color constants reused
    private static final Color BAYLOR_GREEN = new Color(0, 104, 55);
    private static final Color LIGHT_GREEN = new Color(33, 166, 81);

    private ModifyClass() {
        // static utility class prevent instantiation
    }

    /**
     * Opens the Modify Class window
     */
    public static void openModifyClassPage(String trainerUsername,
                                           DatabaseManager dbManager,
                                           Runnable refreshCallback) {
        // Build GUI components
        JFrame frame = new JFrame("Modify Existing Class");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Modify Your Class");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        // Retrieve classes for the trainer
        List<WorkoutClass> trainerClasses = dbManager.getClassesForTrainer(trainerUsername);

        // If no classes exist inform the trainer and exit early
        if (trainerClasses.isEmpty()) {
            JLabel info = new JLabel("You haven't created any classes yet.");
            info.setFont(new Font("Arial", Font.ITALIC, 14));
            info.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(info);
            frame.setContentPane(container);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            return;
        }

        // Class selector
        JComboBox<WorkoutClass> classSelector = new JComboBox<>(trainerClasses.toArray(new WorkoutClass[0]));
        classSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        container.add(labeledComponent("Select Class", classSelector));

        // Class type field
        JTextField typeField = new JTextField();
        container.add(labeledComponent("Class Type", typeField));

        // Description area
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        container.add(labeledComponent("Description", descScroll));

        // Start and end time spinners
        JSpinner startSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startSpinner, "yyyy-MM-dd HH:mm");
        startSpinner.setEditor(startEditor);
        container.add(labeledComponent("Start Time", startSpinner));

        JSpinner endSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endSpinner, "yyyy-MM-dd HH:mm");
        endSpinner.setEditor(endEditor);
        container.add(labeledComponent("End Time", endSpinner));

        // Maximum participants spinner
        JSpinner maxSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        container.add(labeledComponent("Max Participants", maxSpinner));

        // Cost field
        JTextField costField = new JTextField();
        container.add(labeledComponent("Cost ($)", costField));

        container.add(Box.createRigidArea(new Dimension(0, 10)));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton saveButton = new JButton("Save Changes");
        styleSecondaryButton(saveButton);
        JButton cancelButton = new JButton("Cancel");
        styleSecondaryButton(cancelButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(saveButton);

        container.add(buttonPanel);

        // Populate form with first class details initially
        fillFieldsFromClass((WorkoutClass) classSelector.getSelectedItem(), typeField, descArea,
                startSpinner, endSpinner, maxSpinner, costField);

        // When a different class is selected, update fields accordingly
        classSelector.addActionListener(e -> fillFieldsFromClass(
                (WorkoutClass) classSelector.getSelectedItem(), typeField, descArea,
                startSpinner, endSpinner, maxSpinner, costField));

        // Cancel closes the window
        cancelButton.addActionListener(e -> frame.dispose());

        // Save applies changes to the selected class
        saveButton.addActionListener((ActionEvent e) -> {
            WorkoutClass selected = (WorkoutClass) classSelector.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(frame, "Please select a class to modify.",
                        "No Class Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Validate fields
            String classType = typeField.getText().trim();
            String description = descArea.getText().trim();
            String costText = costField.getText().trim();
            if (classType.isEmpty() || description.isEmpty() || costText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please complete all fields.",
                        "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Parse numbers
            int maxParticipants = (Integer) maxSpinner.getValue();
            double cost;
            try {
                cost = Double.parseDouble(costText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Cost must be a valid number.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Get start and end times
            Date startDate = (Date) startSpinner.getValue();
            Date endDate = (Date) endSpinner.getValue();
            LocalDateTime startLdt = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
            LocalDateTime endLdt = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
            if (endLdt.isBefore(startLdt)) {
                JOptionPane.showMessageDialog(frame, "End time must be after start time.",
                        "Invalid Time", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Persist changes
            boolean success = dbManager.updateClass(
                    selected.getId(), classType, description,
                    startLdt.toString(), endLdt.toString(), maxParticipants, cost);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Class updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                // Refresh lists in dashboard if requested
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "There was an error updating the class.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setContentPane(container);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Creates a panel containing a label and component aligned on a horizontal axis
     */
    private static JPanel labeledComponent(String labelText, JComponent comp) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(comp);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    /**
     * Applies the primary button styling used throughout the application
     */
    private static void stylePrimaryButton(JButton button) {
        button.setBackground(BAYLOR_GREEN);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(140, 40));
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(LIGHT_GREEN);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(BAYLOR_GREEN);
            }
        });
    }

    /**
     * Applies a secondary button style for less prominent actions like cancelling
     */
    private static void styleSecondaryButton(JButton button) {
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(100, 40));
    }

    /**
     * Fills the provided form fields with values from WorkoutClass
     */
    private static void fillFieldsFromClass(WorkoutClass wc,
                                            JTextField typeField,
                                            JTextArea descArea,
                                            JSpinner startSpinner,
                                            JSpinner endSpinner,
                                            JSpinner maxSpinner,
                                            JTextField costField) {
        if (wc == null) {
            return;
        }
        typeField.setText(wc.getClassType());
        descArea.setText(wc.getDescription());
        // Parse stored ISO strings into local date time
        try {
            LocalDateTime startLdt = LocalDateTime.parse(wc.getStartTime());
            LocalDateTime endLdt = LocalDateTime.parse(wc.getEndTime());
            startSpinner.setValue(Date.from(startLdt.atZone(ZoneId.systemDefault()).toInstant()));
            endSpinner.setValue(Date.from(endLdt.atZone(ZoneId.systemDefault()).toInstant()));
        } catch (Exception ignore) {
            // Fallback to current spinner values if parsing fails
        }
        maxSpinner.setValue(wc.getMaxParticipants());
        costField.setText(String.format("%.2f", wc.getCost()));
    }
}
