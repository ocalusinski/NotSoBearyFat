package myPackage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import static myPackage.Constants.*;


public class AddData{
    static JTextField dateField = new JTextField(20);
    static JTextField calIntake = new JTextField(20);
    static JTextField weightField = new JTextField(20);
    static JTextField sleepField = new JTextField(20);
    static JTextField totCalBurn = new JTextField(20);
    // Static method to open AddDataPage directly (for use from DashboardUI)
    // refreshCallback can be null - if provided, it will be called after successful save
    public static void openAddDataPage(int userId, DatabaseManager dbManager, Runnable refreshCallback) {
        SwingUtilities.invokeLater(() -> {
            AddDataPage newPage = new AddDataPage(userId, dbManager, refreshCallback);
            newPage.setVisible(true);
        });
    }
    
    // Overload without refresh callback for backward compatibility
    public static void openAddDataPage(int userId, DatabaseManager dbManager) {
        openAddDataPage(userId, dbManager, null);
    }

    static class AddDataPage extends JFrame{
        private int userId;
        private DatabaseManager dbManager;
        private Runnable refreshCallback;

        // Constructor without refresh callback for backward compatibility
        public AddDataPage(int userId, DatabaseManager dbManager){
            this(userId, dbManager, null);
        }

        public AddDataPage(int userId, DatabaseManager dbManager, Runnable refreshCallback){
            this.userId = userId;
            this.dbManager = dbManager;
            this.refreshCallback = refreshCallback;
            
            final LocalDate[] date = new LocalDate[1];
            final int[] cal = new int[1];
            final double[] weight = new double[1];
            final double[] sleep = new double[1];
            final int[] totalCal = new int[1];

            JFrame dataFrame = new JFrame("Add Data");
            dataFrame.setSize(1200, 800);
            // Don't exit the whole program when closing this window
            dataFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            JLabel dateLabel = new JLabel("Date (MM-dd-yyyy)");
            JLabel calLabel = new JLabel("Calories Consumed (kcal)");
            JLabel weightLabel = new JLabel("Weight (lbs)");
            JLabel sleepLabel = new JLabel("Sleep (hrs)");
            JLabel totCalLabel = new JLabel("Total Calories Burned (kcal)");
            JLabel optional = new JLabel("(Optional)");
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("M-d-yyyy"); // Allow single digit month/day
            DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // Allow slashes
            DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("M/d/yyyy"); // Single digit with slashes
            
            // Set today's date as default/placeholder
            dateField.setText(LocalDate.now().format(formatter));
            date[0] = LocalDate.now();
            
            // Optional: parse date when user types (but don't require it)
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

            calIntake.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = e.getActionCommand();
                    if(s.matches("\\d+")){
                        cal[0] = Integer.parseInt(s);
                    }
                    else{
                        tempMessage(calIntake, "Invalid input, must be an integer");
                    }
                }
            });

            calIntake.addFocusListener(new java .awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    validIntegerField(calIntake, cal);
                }
            });

            weightField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = e.getActionCommand();
                    if(s.matches("\\d+")){
                        weight[0] = Double.parseDouble(s);
                    }
                    else{
                    weightField.setText("Invalid input, must be an integer");
                        tempMessage(weightField, "Invalid input, must be an integer");
                    }
                }
            });

            weightField.addFocusListener(new java .awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    validDoubleField(weightField, weight);
                }
            });

            sleepField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = e.getActionCommand();
                    if(s.matches("\\d+")){
                        sleep[0] = Double.parseDouble(s);
                    }
                    else{
                        tempMessage(sleepField, "Invalid input, must be an integer");

                    }
                }
            });

            sleepField.addFocusListener(new java .awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    validDoubleField(sleepField, sleep);
                }
            });

            totCalBurn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = e.getActionCommand();
                    if(s.matches("\\d+")){
                        totalCal[0] = Integer.parseInt(s);
                    }
                    else{
                        tempMessage(totCalBurn, "Invalid input, must be an integer");
                    }
                }
            });

            totCalBurn.addFocusListener(new java .awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    validIntegerField(totCalBurn, totalCal);
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    areYouSure("Cancel", dataFrame, date[0], cal[0], sleep[0], weight[0], totalCal[0], userId, dbManager, refreshCallback);
                }
            });
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Parse date field when Save is clicked (in case user didn't press Enter)
                    if (!parseDateField(dateField, date, formatter, formatter2, formatter3, formatter4)) {
                        // If parsing failed, show error and don't proceed
                        JOptionPane.showMessageDialog(dataFrame,
                            "Please enter a valid date.\nAccepted formats: MM-dd-yyyy, M-d-yyyy, MM/dd/yyyy, or M/d/yyyy\nExample: 12-03-2024 or 12/03/2024",
                            "Invalid Date Format",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    // Validate required fields before showing confirmation
                    if (date[0] == null) {
                        // Default to today if still null
                        date[0] = LocalDate.now();
                    }
                    
                    // Parse calories from field if not already parsed
                    try {
                        String calText = calIntake.getText().trim();
                        if (!calText.isEmpty()) {
                            cal[0] = Integer.parseInt(calText);
                        }
                    } catch (NumberFormatException ex) {
                        // Will be handled below
                    }

                    boolean fieldsFull = checkFields();
                    if(!fieldsFull){
                        fieldsNotFull();
                    }
                    else {
                        areYouSure("Save", dataFrame, date[0], cal[0], sleep[0], weight[0], totalCal[0], userId, dbManager, refreshCallback);
                    }
                }
            });


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

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(dateLabel, gbc);
            gbc.gridy = 1;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(dateField,gbc);
            gbc.gridy = 2;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(calLabel, gbc);
            gbc.gridy = 3;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(calIntake, gbc);
            gbc.gridy = 4;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(weightLabel, gbc);
            gbc.gridy = 5;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(weightField, gbc);
            gbc.gridy = 6;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(sleepLabel, gbc);
            gbc.gridy = 7;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(sleepField, gbc);
            gbc.gridy = 8;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(totCalLabel, gbc);
            gbc.gridy = 9;
            gbc.insets = new Insets(5, 5, 1, 5);
            panel.add(totCalBurn, gbc);
            gbc.gridy = 10;
            gbc.insets = new Insets(1, 1, 1, 1);
            panel.add(optional, gbc);
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.gridy = 15;
            panel.add(cancelButton, gbc);
            gbc.gridx = 2;
            gbc.gridy = 15;
            panel.add(saveButton, gbc);
            panel.setBackground(new Color(240, 255, 250));

            dataFrame.add(menuBar, BorderLayout.NORTH);
            dataFrame.getContentPane().add(panel);
            dataFrame.setVisible(true);
            dataFrame.setLocationRelativeTo(null);

        }
    }
    // Helper method to parse date field with multiple format support
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
                                   LocalDate date, int cal, double sleep, double weight, int totalCal,
                                   int userId, DatabaseManager dbManager, Runnable refreshCallback){
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
                    if (userId != -1 && dbManager != null && date != null) {
                        String dateStr = date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                        boolean success = dbManager.saveUserData(
                            userId,
                            dateStr,
                            cal,
                            weight,
                            sleep,
                            totalCal
                        );
                        if (success) {
                            JOptionPane.showMessageDialog(frame,
                                "Data saved successfully!",
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
                                "Error saving data. Please try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Fallback: just create Data object (for standalone use)
                        Data data = new Data(date, cal, weight, sleep);
                        JOptionPane.showMessageDialog(frame,
                            "Data object created (not saved to database).",
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

    //checks to make sure all fields have data inputed
    private static boolean checkFields(){
        boolean fieldsFull = true;
        if(dateField.getText().isEmpty()){
            fieldsFull = false;
        }
        else if(calIntake.getText().isEmpty()){
            fieldsFull = false;
        }
        else if(weightField.getText().isEmpty()){
            fieldsFull = false;
        }
        else if(sleepField.getText().isEmpty()){
            fieldsFull = false;
        }

        return fieldsFull;
    }

    //popup to signify fields are not full
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
        frame.getRootPane().setBackground(new Color(0, 100, 80));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.toFront();
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

    private static boolean validDoubleField(JTextField textField, double[] input){
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            textField.setForeground(Color.BLACK);
            return false;
        }

        try{
            double val =  Integer.parseInt(text);
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
