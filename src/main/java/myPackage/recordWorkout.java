package myPackage;
import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static myPackage.Constants.DB_MANAGER;

public class recordWorkout {

    static String[] workoutOptions = {"Calisthenics", "HIIT", "Lifting", "Running", "Walking", "Yoga", "Other"};
    static JTextField dateField = new JTextField(20);
    static JComboBox<String> workoutDropDown = new JComboBox<>(workoutOptions);
    static JTextField durationField = new JTextField(20);
    static JTextField caloriesBurntField = new JTextField(20);

    public static void openRecordWorkout(int userId, Runnable refreshCallBack){
        SwingUtilities.invokeLater(() -> {
            recordWorkoutPage rwp =  new recordWorkoutPage(userId, refreshCallBack);
            rwp.setVisible(true);
        });
    }

    static class recordWorkoutPage extends JFrame {
        private int userId;
        private Runnable refreshCallback;

        public recordWorkoutPage(int userId) {
            this.userId = userId;
            this.refreshCallback = null;
        }

        public recordWorkoutPage(int userId, Runnable refreshCallback) {
            this.userId = userId;
            this.refreshCallback = refreshCallback;
            JFrame frame = new JFrame("RecordWorkout");
            // Don't exit the whole program when closing this window
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            frame.setSize(1200, 800);
            frame.setVisible(true);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            durationField.setText("");
            caloriesBurntField.setText("");

            final LocalDate date[] = new LocalDate[1];
            final String workoutType[] = new String[1];
            final int exerciseMinutes[] = new int[1];
            final int caloriesBurnt[] = new int[1];


            JLabel dateLabel = new JLabel("Date (MM-dd-yyyy)");
            JLabel workoutTypeLabel = new JLabel("Workout Type");
            JLabel exerciseMinutesLabel = new JLabel("Duration (in minutes)");
            JLabel caloriesBurntLabel = new JLabel("Calories burnt");
            JLabel optionalLabel = new JLabel("(Optional)");
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");

            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("M-d-yyyy"); // Allow single digit month/day
            DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // Allow slashes
            DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("M/d/yyyy"); // Single digit with slashes

            // Set today's date as default/placeholder
            dateField.setText(LocalDate.now().format(formatter1));
            date[0] = LocalDate.now();

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(dateLabel, gbc);
            gbc.gridy = 1;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(dateField, gbc);
            gbc.gridy = 2;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(workoutTypeLabel, gbc);
            gbc.gridy = 3;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(workoutDropDown, gbc);
            gbc.gridy = 4;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(exerciseMinutesLabel, gbc);
            gbc.gridy = 5;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(durationField, gbc);
            gbc.gridy = 6;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(caloriesBurntLabel, gbc);
            gbc.gridy = 7;
            gbc.insets = new Insets(5, 5, 1, 5);
            panel.add(caloriesBurntField, gbc);
            gbc.gridy = 8;
            gbc.insets = new Insets(1, 1, 1, 1);
            panel.add(optionalLabel, gbc);
            gbc.gridx = 0;
            gbc.gridy = 15;
            gbc.insets = new Insets(10, 10, 10, 10);
            panel.add(cancelButton, gbc);
            gbc.gridx = 2;
            panel.add(saveButton, gbc);
            panel.setBackground(new Color(240, 255, 250));


            JPanel menuBar = new JPanel();
            menuBar.setLayout(new GridBagLayout());
            menuBar.setPreferredSize(new Dimension(600, 300));
            GridBagConstraints g = new GridBagConstraints();
            menuBar.setBackground(new Color(0, 71, 56));
            menuBar.setOpaque(true);
            menuBar.setPreferredSize(new Dimension(100, 50));
            JLabel item = new JLabel("Add Data");
            item.setForeground(Color.WHITE);
            menuBar.add(item, g);

            frame.add(menuBar, BorderLayout.NORTH);
            frame.getContentPane().add(panel);
            frame.getContentPane().setBackground(new Color(240, 255, 250));
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            dateField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parseDateField(dateField, date, formatter, formatter2, formatter3, formatter4);
                }
            });

            // Also parse when field loses focus
            dateField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    parseDateField(dateField, date, formatter, formatter2, formatter3, formatter4);
                }
            });

            durationField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = e.getActionCommand();
                    if(s.matches("\\d+")){
                        exerciseMinutes[0] = Integer.parseInt(s);
                    }
                    else {
                        tempMessage(durationField, "Invalid input, must be an integer");
                    }
                }
            });

            durationField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    validIntegerField(durationField, exerciseMinutes);
                }
            });

            caloriesBurntField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = e.getActionCommand();
                    if(s.matches("\\d+")){
                        caloriesBurnt[0] = Integer.parseInt(s);
                    }
                    else{
                        tempMessage(caloriesBurntField, "Invalid input, must be an integer");
                    }
                }
            });

            caloriesBurntField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    validIntegerField(caloriesBurntField, caloriesBurnt);
                }
            });


            // Parse workout type from dropdown when it changes
            workoutDropDown.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    workoutType[0] = (String) workoutDropDown.getSelectedItem();
                }
            });
            // Initialize workout type
            workoutType[0] = (String) workoutDropDown.getSelectedItem();

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    areYouSure("Cancel", frame, date[0], workoutType[0], exerciseMinutes[0], caloriesBurnt[0], 
                              userId, refreshCallback);
                }
            });
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Parse date field when Save is clicked (in case user didn't press Enter)
                    if (!parseDateField(dateField, date, formatter1, formatter2, formatter3, formatter4)) {
                        JOptionPane.showMessageDialog(frame,
                            "Please enter a valid date.\nAccepted formats: MM-dd-yyyy, M-d-yyyy, MM/dd/yyyy, or M/d/yyyy\nExample: 12-03-2024 or 12/03/2024",
                            "Invalid Date Format",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    // Validate required fields before showing confirmation
                    if (date[0] == null) {
                        date[0] = LocalDate.now();
                    }
                    
                    // Parse calories from field if not already parsed
                    try {
                        String calText = caloriesBurntField.getText().trim();
                        if (!calText.isEmpty()) {
                            caloriesBurnt[0] = Integer.parseInt(calText);
                        }
                    } catch (NumberFormatException ex) {
                        // Will be handled in checkFields
                    }
                    
                    // Parse workout type
                    workoutType[0] = (String) workoutDropDown.getSelectedItem();
                    
                    boolean fieldsFull = checkFields();
                    if(!fieldsFull){
                        fieldsNotFull();
                    }
                    else {
                        areYouSure("Save", frame, date[0], workoutType[0], exerciseMinutes[0], caloriesBurnt[0],
                                  userId, refreshCallback);
                    }
                }
            });
        }

    }

    private static void tempMessage(JTextField field, String message){
        field.setText(message);
        field.setForeground(Color.RED);

        new javax.swing.Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.setText("");
                field.setForeground(Color.BLACK);
            }
        }) {{
            setRepeats(false);
            start();
        }};
    }

    private static void areYouSure(String message, JFrame prevFrame,
                                   LocalDate date, String workoutType, int exerciseMinutes, int caloriesBurnt,
                                   int userId, Runnable refreshCallback){
        JFrame frame = new JFrame(message);
        // Don't exit the whole program when closing this window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(0, 100, 80));

        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        JLabel label = new JLabel("Are you sure?");
        label.setForeground(Color.WHITE);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBackground(new Color(0, 100, 80));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 20, 5);
        panel.add(label, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(noButton, gbc);
        gbc.gridx = 2;
        panel.add(yesButton, gbc);
        frame.getContentPane().add(panel);

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(message.equals("Save")){
                    // Save to database if userId and dbManager are available
                    if (userId != -1 && DB_MANAGER != null && date != null && workoutType != null) {
                        String dateStr = date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                        // Use caloriesBurnt if provided, otherwise use 0 (optional field)
                        int caloriesToSave = caloriesBurnt > 0 ? caloriesBurnt : 0;
                        
                        boolean success = DB_MANAGER.saveWorkoutData(userId, dateStr, workoutType, exerciseMinutes, caloriesToSave);
                        if (success) {
                            JOptionPane.showMessageDialog(frame,
                                "Workout saved successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                            // Call refresh callback if provided (to refresh dashboard)
                            SwingUtilities.invokeLater(() -> {
                                if (refreshCallback != null) {
                                    refreshCallback.run();
                                }
                            });
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                "Error saving workout. Please try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Fallback: just create workoutData object (for standalone use)
                        workoutData data = new workoutData(date, workoutType, exerciseMinutes, caloriesBurnt);
                        JOptionPane.showMessageDialog(frame,
                            "Workout object created (not saved to database).",
                            "Note",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                frame.dispose();
                prevFrame.dispose();
            }
        });

        frame.setSize(300, 200);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private static boolean checkFields(){
        boolean fieldsFull = true;
        if(dateField.getText().isEmpty()){
            fieldsFull = false;
        }
        else if(workoutDropDown.getSelectedIndex() == -1){
            fieldsFull = false;
        }
        else if(durationField.getText().isEmpty()){
            fieldsFull = false;
        }

            return fieldsFull;
    }

    private static void fieldsNotFull(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 100, 80));

        JLabel label = new JLabel("One or more fields are missing input");
        label.setForeground(Color.WHITE);
        JButton okayButton = new JButton("Okay");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(label, gbc);
        gbc.gridy = 1;
        panel.add(okayButton, gbc);

        okayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            frame.dispose();
        }
        });

        frame.getContentPane().add(panel);
        frame.setSize(300, 200);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private static boolean parseDateField(JTextField dateField, LocalDate[] date,
                                          DateTimeFormatter... formatters) {
        String dateText = dateField.getText().trim();
        if (dateText.isEmpty()) {
            date[0] = LocalDate.now(); // Default to today
            return true;
        }

        // Try each format
        for (DateTimeFormatter fmt : formatters) {
            try {
                date[0] = LocalDate.parse(dateText, fmt);
                dateField.setForeground(Color.BLACK);
                return true;
            } catch (DateTimeParseException ex) {
                // Try next format
            }
        }

        // If all formats failed, show error
        dateField.setForeground(Color.RED);
        tempMessage(dateField, "Invalid date format");
        return false;
    }

    private static boolean validIntegerField(JTextField textField, int[] input){
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            textField.setForeground(Color.BLACK);
            return false;
        }

        try{
            int val =  Integer.parseInt(text);
            if(val < 0){
                tempMessage(textField, "Must be a positive integer");
                return false;
            }
            textField.setForeground(Color.BLACK);
            input[0] = val;
            return true;
        }catch (NumberFormatException ex){
            tempMessage(textField, "Invalid integer format");
            return false;
        }
    }

}
