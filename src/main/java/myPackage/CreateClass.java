package myPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import static myPackage.Constants.*;


// Main class for creating workout classes
// Trainers use this to create new classes for clients to sign up for
public class CreateClass{
    
    // Creates the initial GUI with a button to start creating a class
    // trainerUsername is used to track which trainer created the class
    // dbManager is passed in so we share one connection with the dashboard
    public static void CreateAndShowGUI(String trainerUsername, DatabaseManager dbManager){
        JFrame frame = new JFrame("Create Class");
        // When closing this window, only dispose this frame instead of exiting the app
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setSize(600, 800);
        frame.setVisible(true);

        JButton createClassButton = new JButton("Create Class");
        createClassButton.setBackground(new Color(255, 199, 44));
        createClassButton.setForeground(new Color(0, 71, 56));
        createClassButton.setOpaque(true);
        createClassButton.setPreferredSize(new Dimension(100, 50));
        createClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                CreateClassPage newPage = new CreateClassPage(trainerUsername, dbManager);
                newPage.setVisible(true);
                frame.dispose();
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(createClassButton, new GridBagConstraints());
        panel.setBackground(new Color(0, 71, 56)); // Baylor green color
        frame.getContentPane().add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Convenience overload for testing without a real trainer user
    public static void CreateAndShowGUI(){
        CreateAndShowGUI("trainer", DB_MANAGER);
    }

    // Inner class that contains the form for creating a class
    // This is where trainers input all the class details
    static class CreateClassPage extends JFrame{

        private final String trainerUsername;
        private final DatabaseManager dbManager;

        public CreateClassPage(String trainerUsername, DatabaseManager dbManager){
            this.trainerUsername = trainerUsername;
            this.dbManager = dbManager;
            // Using arrays to store values because inner classes can't modify local variables
            final String[] classType = new String[1];
            final String[] description = new String[1];
            final LocalDateTime[] startTime = new LocalDateTime[1];
            final LocalDateTime[] endTime = new LocalDateTime[1];
            final int[] maxParticipants = new int[1];
            final double[] cost = new double[1];

            JFrame classFrame = new JFrame("Create Class");
            classFrame.setSize(700, 850);
            // Do not exit the whole program when this window is closed
            classFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JLabel typeLabel = new JLabel("Class Type:");
            JLabel descLabel = new JLabel("Description:");
            JLabel startLabel = new JLabel("Start Time:");
            JLabel endLabel = new JLabel("End Time:");
            JLabel maxLabel = new JLabel("Max Participants:");
            JLabel costLabel = new JLabel("Cost ($):");
            
            // Set preferred sizes for labels to prevent text cutoff
            typeLabel.setPreferredSize(new Dimension(150, 25));
            descLabel.setPreferredSize(new Dimension(150, 25));
            startLabel.setPreferredSize(new Dimension(150, 25));
            endLabel.setPreferredSize(new Dimension(150, 25));
            maxLabel.setPreferredSize(new Dimension(150, 25));
            costLabel.setPreferredSize(new Dimension(150, 25));
            
            JComboBox<String> typeField = new JComboBox<>(ClassType.getClassTypes());
            typeField.setPreferredSize(new Dimension(200, 30));
            JTextField descField = new JTextField(25);
            descField.setPreferredSize(new Dimension(200, 30));

            //start date & time pickers
            TimePicker startTimePicker = new TimePicker();
            startTimePicker.setTime(LocalTime.now());
            startTimePicker.setPreferredSize(new Dimension(200, 30));
            DatePicker startDatePicker = new DatePicker();
            startDatePicker.setDate(LocalDate.now());
            startDatePicker.setPreferredSize(new Dimension(200, 30));

            //end date & time pickers
            TimePicker endTimePicker = new TimePicker();
            endTimePicker.setTime(LocalTime.now().plusHours(1));
            endTimePicker.setPreferredSize(new Dimension(200, 30));
            DatePicker endDatePicker = new DatePicker();
            endDatePicker.setDate(LocalDate.now());
            endDatePicker.setPreferredSize(new Dimension(200, 30));

            JTextField maxField = new JTextField(25);
            maxField.setPreferredSize(new Dimension(200, 30));
            JTextField costField = new JTextField(25);
            costField.setPreferredSize(new Dimension(200, 30));
            JButton saveButton = new JButton("Save");
            saveButton.setPreferredSize(new Dimension(100, 35));
            JButton cancelButton = new JButton("Cancel");
            cancelButton.setPreferredSize(new Dimension(100, 35));
            
            // Action listeners to store field values when user types
            typeField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    classType[0] = typeField.getSelectedItem().toString();
                }
            });
            
            descField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    description[0] = descField.getText();
                }
            });
            
            // Validation: checks if max participants is a valid integer
            maxField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = e.getActionCommand();
                    if(s.matches("\\d+")){
                        maxParticipants[0] = Integer.parseInt(s);
                    }
                    else{
                        tempMessage(maxField, "Invalid input, must be an integer");
                    }
                }
            });
            
            // Validation: checks if cost is a valid number (integer or decimal)
            costField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = e.getActionCommand();
                    if(s.matches("\\d+(\\.\\d+)?")){
                        cost[0] = Double.parseDouble(s);
                    }
                    else{
                        tempMessage(costField, "Invalid input, must be a number");
                    }
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LocalDateTime startDate = LocalDateTime.of(startDatePicker.getDate(), startTimePicker.getTime());
                    LocalDateTime endDate = LocalDateTime.of(endDatePicker.getDate(), endTimePicker.getTime());
                    startTime[0] = startDate;
                    endTime[0] = endDate;
                    areYouSure("Cancel", classFrame, trainerUsername, classType[0], description[0],
                               startTime[0], endTime[0], maxParticipants[0], cost[0], dbManager);
                }
            });
            
            // Save button: validates all fields and checks for scheduling conflicts
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Convert JSpinner date values to LocalDateTime
                    classType[0] = typeField.getSelectedItem().toString();
                    description[0] = descField.getText();
                    maxParticipants[0] = Integer.parseInt(maxField.getText());
                    cost[0] = Double.parseDouble(costField.getText());

                    //this just creates LocalDateTime r-values instead of creating my own variables
                    startTime[0] = LocalDateTime.of(startDatePicker.getDate(), startTimePicker.getTime());
                    endTime[0] = LocalDateTime.of(endDatePicker.getDate(), endTimePicker.getTime());
                    
                    // Validation: checks if all required fields are filled
                    if(classType[0] == null || description[0] == null || 
                       maxParticipants[0] == 0 || cost[0] == 0){
                        JOptionPane.showMessageDialog(classFrame, 
                            "Please complete all required fields", 
                            "Missing Information", 
                            JOptionPane.WARNING_MESSAGE);
                    }
                    // Validation: checks if end time is after start time
                    else if(endTime[0].isBefore(startTime[0])){
                        JOptionPane.showMessageDialog(classFrame, 
                            "End time must be after start time", 
                            "Invalid Time", 
                            JOptionPane.WARNING_MESSAGE);
                    }
                    else{
                        areYouSure("Save", classFrame, trainerUsername, classType[0], description[0],
                                   startTime[0], endTime[0], maxParticipants[0], cost[0], dbManager);
                    }
                }
            });

            JPanel menuBar = new JPanel();
            menuBar.setLayout(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            menuBar.setBackground(new Color(255, 199, 44));
            menuBar.setOpaque(true);
            menuBar.setPreferredSize(new Dimension(700, 60));
            JLabel item = new JLabel("Create Class");
            item.setFont(new Font(item.getFont().getName(), Font.BOLD, 18));
            g.gridx = 0;
            g.gridy = 0;
            g.insets = new Insets(10, 10, 10, 10);
            menuBar.add(item, g);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 20, 5, 10);
            gbc.weightx = 0.0;
            panel.add(typeLabel, gbc);
            gbc.gridx = 1;
            gbc.insets = new Insets(10, 10, 5, 20);
            gbc.weightx = 1.0;
            panel.add(typeField, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.insets = new Insets(5, 20, 5, 10);
            gbc.weightx = 0.0;
            panel.add(descLabel, gbc);
            gbc.gridx = 1;
            gbc.insets = new Insets(5, 10, 15, 20);
            gbc.weightx = 1.0;
            panel.add(descField, gbc);

            //start date stuff
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.insets = new Insets(5, 20, 5, 10);
            gbc.weightx = 0.0;
            panel.add(startLabel, gbc);
            gbc.gridx = 1;
            gbc.insets = new Insets(5, 10, 5, 20);
            gbc.weightx = 1.0;
            panel.add(startTimePicker, gbc);
            gbc.gridy = 3;
            gbc.insets = new Insets(5, 10, 15, 20);
            panel.add(startDatePicker, gbc);

            //end date stuff
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.insets = new Insets(5, 20, 5, 10);
            gbc.weightx = 0.0;
            panel.add(endLabel, gbc);
            gbc.gridx = 1;
            gbc.insets = new Insets(5, 10, 5, 20);
            gbc.weightx = 1.0;
            panel.add(endTimePicker, gbc);
            gbc.gridy = 5;
            gbc.insets = new Insets(5, 10, 15, 20);
            panel.add(endDatePicker, gbc);

            //everything else
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.insets = new Insets(5, 20, 5, 10);
            gbc.weightx = 0.0;
            panel.add(maxLabel, gbc);
            gbc.gridx = 1;
            gbc.insets = new Insets(5, 10, 15, 20);
            gbc.weightx = 1.0;
            panel.add(maxField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbc.insets = new Insets(5, 20, 5, 10);
            gbc.weightx = 0.0;
            panel.add(costLabel, gbc);
            gbc.gridx = 1;
            gbc.insets = new Insets(5, 10, 20, 20);
            gbc.weightx = 1.0;
            panel.add(costField, gbc);
            
            // Buttons
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.insets = new Insets(20, 20, 20, 10);
            gbc.weightx = 0.5;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(cancelButton, gbc);
            gbc.gridx = 1;
            gbc.insets = new Insets(20, 10, 20, 20);
            gbc.weightx = 0.5;
            panel.add(saveButton, gbc);
            panel.setBackground(new Color(0, 71, 56));

            classFrame.add(menuBar, BorderLayout.NORTH);
            classFrame.getContentPane().add(panel);
            classFrame.setVisible(true);
            classFrame.setLocationRelativeTo(null);

        }
        
        // Shows a temporary error message in a text field
        // The message appears in red and disappears after 2 seconds
        private void tempMessage(JTextField field, String message){
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
    }
    
    // Confirmation dialog that asks "Are you sure?" before saving or canceling
    private static void areYouSure(String message, JFrame prevFrame, String trainerUsername,
                                   String classType, String description, LocalDateTime startTime,
                                   LocalDateTime endTime, int maxParticipants, double cost,
                                   DatabaseManager dbManager){
        JFrame frame = new JFrame(message);
        // Only close this confirmation window when the user exits it
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(0, 71, 56));

        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        JLabel label = new JLabel("Are you sure?");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBackground(new Color(0, 71, 56));
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

        // If user clicks Yes, show confirmation and close windows
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(message.equals("Save")){
                    boolean success = dbManager.saveClass(
                        trainerUsername,
                        classType != null ? classType : "",
                        description != null ? description : "",
                        startTime,
                        endTime,
                        maxParticipants,
                        cost
                    );
                    if (success) {
                        JOptionPane.showMessageDialog(frame, 
                            "Class successfully created!", 
                            "Confirmation", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, 
                            "There was an error saving the class.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
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


    // Main method to start the application
    public static void main(String[] args){
        CreateAndShowGUI();
    }
}
