package myPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.List;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

import static myPackage.Constants.*;

/**
 * Allows trainers to modify the classes they have created
 */
public final class ModifyClass {

    private ModifyClass() {
        // static utility class prevent instantiation
    }

    /**
     * Opens the Modify Class window
     */
    public static void openModifyClassPage(String trainerUsername,
                                           Runnable refreshCallback) {

        JFrame classFrame = new JFrame("Modify Class");
        classFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        classFrame.setSize(700, 850);

        JPanel menuBar = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        menuBar.setBackground(baylorGold);
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(700, 60));

        JLabel titleLabel = new JLabel("Modify Class");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));
        g.gridx = 0;
        g.gridy = 0;
        g.insets = new Insets(10, 10, 10, 10);
        menuBar.add(titleLabel, g);

        // Main panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(baylorGreen);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Retrieve classes for the trainer
        List<WorkoutClass> trainerClasses = DB_MANAGER.getClassesForTrainer(trainerUsername);

        // If no classes exist inform the trainer and exit early
        if (trainerClasses.isEmpty()) {
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.setBackground(baylorGreen);
            JLabel info = new JLabel("You haven't created any classes yet.");
            info.setFont(new Font("Arial", Font.ITALIC, 14));
            info.setForeground(Color.WHITE);
            emptyPanel.add(info, new GridBagConstraints());

            classFrame.add(menuBar, BorderLayout.NORTH);
            classFrame.getContentPane().add(emptyPanel);
            classFrame.pack();
            classFrame.setLocationRelativeTo(null);
            classFrame.setVisible(true);
            return;
        }

        // Labels
        JLabel selectLabel = new JLabel("Select Class:");
        JLabel typeLabel = new JLabel("Class Type:");
        JLabel descLabel = new JLabel("Description:");
        JLabel startLabel = new JLabel("Start Time:");
        JLabel endLabel = new JLabel("End Time:");
        JLabel maxLabel = new JLabel("Max Participants:");
        JLabel costLabel = new JLabel("Cost ($):");

        Dimension labelSize = new Dimension(150, 25);
        selectLabel.setPreferredSize(labelSize);
        typeLabel.setPreferredSize(labelSize);
        descLabel.setPreferredSize(labelSize);
        startLabel.setPreferredSize(labelSize);
        endLabel.setPreferredSize(labelSize);
        maxLabel.setPreferredSize(labelSize);
        costLabel.setPreferredSize(labelSize);

        // White text on green background
        for (JLabel lbl : new JLabel[]{selectLabel, typeLabel, descLabel, startLabel, endLabel, maxLabel, costLabel}) {
            lbl.setForeground(Color.WHITE);
        }

        // Components
        JComboBox<WorkoutClass> classSelector =
                new JComboBox<>(trainerClasses.toArray(new WorkoutClass[0]));
        classSelector.setPreferredSize(new Dimension(200, 30));

        JTextField typeField = new JTextField(25);
        typeField.setPreferredSize(new Dimension(200, 30));

        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(200, 60));

        // Start date & time pickers
        TimePicker startTimePicker = new TimePicker();
        startTimePicker.setPreferredSize(new Dimension(200, 30));
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPreferredSize(new Dimension(200, 30));

        // End date & time pickers
        TimePicker endTimePicker = new TimePicker();
        endTimePicker.setPreferredSize(new Dimension(200, 30));
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPreferredSize(new Dimension(200, 30));

        JSpinner maxSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        maxSpinner.setPreferredSize(new Dimension(200, 30));

        JTextField costField = new JTextField(25);
        costField.setPreferredSize(new Dimension(200, 30));

        JButton saveButton = new JButton("Save Changes");
        saveButton.setPreferredSize(new Dimension(120, 35));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 35));


        // Row 0: Select Class
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 5, 10);
        gbc.weightx = 0.0;
        panel.add(selectLabel, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(10, 10, 5, 20);
        gbc.weightx = 1.0;
        panel.add(classSelector, gbc);

        // Row 1: Class Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 20, 5, 10);
        gbc.weightx = 0.0;
        panel.add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(5, 10, 15, 20);
        gbc.weightx = 1.0;
        panel.add(typeField, gbc);

        // Row 2: Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 20, 5, 10);
        gbc.weightx = 0.0;
        panel.add(descLabel, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(5, 10, 15, 20);
        gbc.weightx = 1.0;
        panel.add(descScroll, gbc);

        // Row 3 & 4: Start time (time then date)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 20, 5, 10);
        gbc.weightx = 0.0;
        panel.add(startLabel, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(5, 10, 5, 20);
        gbc.weightx = 1.0;
        panel.add(startTimePicker, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(5, 10, 15, 20);
        panel.add(startDatePicker, gbc);

        // Row 5 & 6: End time (time then date)
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 20, 5, 10);
        gbc.weightx = 0.0;
        panel.add(endLabel, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(5, 10, 5, 20);
        gbc.weightx = 1.0;
        panel.add(endTimePicker, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(5, 10, 15, 20);
        panel.add(endDatePicker, gbc);

        // Row 7: Max participants
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 20, 5, 10);
        gbc.weightx = 0.0;
        panel.add(maxLabel, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(5, 10, 15, 20);
        gbc.weightx = 1.0;
        panel.add(maxSpinner, gbc);

        // Row 8: Cost
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.insets = new Insets(5, 20, 5, 10);
        gbc.weightx = 0.0;
        panel.add(costLabel, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(5, 10, 20, 20);
        gbc.weightx = 1.0;
        panel.add(costField, gbc);

        // Row 9: Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(saveButton);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);


        // populate form with first class details initially
        fillFieldsFromClass((WorkoutClass) classSelector.getSelectedItem(), typeField, descArea,
                startTimePicker, startDatePicker, endTimePicker, endDatePicker, maxSpinner, costField);

        //when a different class is selected update fields accordingly
        classSelector.addActionListener(e -> fillFieldsFromClass(
                (WorkoutClass) classSelector.getSelectedItem(), typeField, descArea,
                startTimePicker, startDatePicker, endTimePicker, endDatePicker, maxSpinner, costField));

        //cancel closes window
        cancelButton.addActionListener(e -> classFrame.dispose());

        //save applies changes to the selected class
        saveButton.addActionListener((ActionEvent e) -> {
            WorkoutClass selected = (WorkoutClass) classSelector.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(classFrame, "Please select a class to modify.",
                        "No Class Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String classType = typeField.getText().trim();
            String description = descArea.getText().trim();
            String costText = costField.getText().trim();
            if (classType.isEmpty() || description.isEmpty() || costText.isEmpty()) {
                JOptionPane.showMessageDialog(classFrame, "Please complete all fields.",
                        "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int maxParticipants = (Integer) maxSpinner.getValue();
            double cost;
            try {
                cost = Double.parseDouble(costText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(classFrame, "Cost must be a valid number.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDateTime startLdt = LocalDateTime.of(startDatePicker.getDate(), startTimePicker.getTime());
            LocalDateTime endLdt = LocalDateTime.of(endDatePicker.getDate(), endTimePicker.getTime());
            if (endLdt.isBefore(startLdt)) {
                JOptionPane.showMessageDialog(classFrame, "End time must be after start time.",
                        "Invalid Time", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Persist changes
            boolean success = DB_MANAGER.updateClass(
                    selected.getId(), classType, description,
                    startLdt, endLdt, maxParticipants, cost);

            if (success) {
                JOptionPane.showMessageDialog(classFrame, "Class updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
                classFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(classFrame,
                        "There was an error updating the class.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        classFrame.add(menuBar, BorderLayout.NORTH);
        classFrame.getContentPane().add(panel);
        classFrame.setLocationRelativeTo(null);
        classFrame.setVisible(true);
    }

    /**
     * Fills the provided form fields with values from WorkoutClass
     */
    private static void fillFieldsFromClass(WorkoutClass wc,
                                            JTextField typeField,
                                            JTextArea descArea,
                                            TimePicker startTimePicker,
                                            DatePicker startDatePicker,
                                            TimePicker endTimePicker,
                                            DatePicker endDatePicker,
                                            JSpinner maxSpinner,
                                            JTextField costField) {
        if (wc == null) {
            return;
        }
        typeField.setText(wc.getClassType());
        descArea.setText(wc.getDescription());
        try {
            LocalDateTime startLdt = wc.getStartTime();
            LocalDateTime endLdt = wc.getEndTime();
            startDatePicker.setDate(startLdt.toLocalDate());
            startTimePicker.setTime(startLdt.toLocalTime());
            endDatePicker.setDate(endLdt.toLocalDate());
            endTimePicker.setTime(endLdt.toLocalTime());
        } catch (Exception ignore) {
            // fallback to existing values
        }
        maxSpinner.setValue(wc.getMaxParticipants());
        costField.setText(String.format("%.2f", wc.getCost()));
    }
}
