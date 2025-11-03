import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Date;


// Main class for creating workout classes
// Trainers use this to create new classes for clients to sign up for
public class CreateClass{
    
    // Creates the initial GUI with a button to start creating a class
    private static void CreateAndShowGUI(){
        JFrame frame = new JFrame("Create Class");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
                CreateClassPage newPage = new CreateClassPage();
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

    // Inner class that contains the form for creating a class
    // This is where trainers input all the class details
    static class CreateClassPage extends JFrame{

        public CreateClassPage(){
            // Using arrays to store values because inner classes can't modify local variables
            final String[] classType = new String[1];
            final String[] description = new String[1];
            final LocalDateTime[] startTime = new LocalDateTime[1];
            final LocalDateTime[] endTime = new LocalDateTime[1];
            final int[] maxParticipants = new int[1];
            final double[] cost = new double[1];

            JFrame classFrame = new JFrame("Create Class");
            classFrame.setSize(600, 800);
            classFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            JLabel typeLabel = new JLabel("Class Type");
            JLabel descLabel = new JLabel("Description");
            JLabel startLabel = new JLabel("Start Time");
            JLabel endLabel = new JLabel("End Time");
            JLabel maxLabel = new JLabel("Max Participants");
            JLabel costLabel = new JLabel("Cost ($)");
            JTextField typeField = new JTextField(20);
            JTextField descField = new JTextField(20);
            
            // Date and time pickers using JSpinner
            // Users can click the arrows to pick a date/time
            JSpinner startSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startSpinner, "MM-dd-yyyy HH:mm");
            startSpinner.setEditor(startEditor);
            
            JSpinner endSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endSpinner, "MM-dd-yyyy HH:mm");
            endSpinner.setEditor(endEditor);
            
            JTextField maxField = new JTextField(20);
            JTextField costField = new JTextField(20);
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");
            
            // Action listeners to store field values when user types
            typeField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    classType[0] = typeField.getText();
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
                    Date startDate = (Date) startSpinner.getValue();
                    Date endDate = (Date) endSpinner.getValue();
                    startTime[0] = java.time.LocalDateTime.ofInstant(startDate.toInstant(), java.time.ZoneId.systemDefault());
                    endTime[0] = java.time.LocalDateTime.ofInstant(endDate.toInstant(), java.time.ZoneId.systemDefault());
                    areYouSure("Cancel", classFrame, classType[0], description[0], startTime[0], endTime[0], maxParticipants[0], cost[0]);
                }
            });
            
            // Save button: validates all fields and checks for scheduling conflicts
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Convert JSpinner date values to LocalDateTime
                    classType[0] = typeField.getText();
                    description[0] = descField.getText();
                    maxParticipants[0] = Integer.parseInt(maxField.getText());
                    cost[0] = Double.parseDouble(costField.getText());
                    Date startDate = (Date) startSpinner.getValue();
                    Date endDate = (Date) endSpinner.getValue();
                    startTime[0] = java.time.LocalDateTime.ofInstant(startDate.toInstant(), java.time.ZoneId.systemDefault());
                    endTime[0] = java.time.LocalDateTime.ofInstant(endDate.toInstant(), java.time.ZoneId.systemDefault());

                    
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
                        areYouSure("Save", classFrame, classType[0], description[0], startTime[0], endTime[0], maxParticipants[0], cost[0]);
                    }
                }
            });

            JPanel menuBar = new JPanel();
            menuBar.setLayout(new GridBagLayout());
            menuBar.setPreferredSize(new Dimension(600, 300));
            GridBagConstraints g = new GridBagConstraints();
            menuBar.setBackground(new Color(255, 199, 44));
            menuBar.setOpaque(true);
            menuBar.setPreferredSize(new Dimension(100, 50));
            JLabel item = new JLabel("Create Class");
            menuBar.add(item, g);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(typeLabel, gbc);
            gbc.gridy = 1;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(typeField,gbc);
            gbc.gridy = 2;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(descLabel, gbc);
            gbc.gridy = 3;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(descField, gbc);
            gbc.gridy = 4;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(startLabel, gbc);
            gbc.gridy = 5;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(startSpinner, gbc);
            gbc.gridy = 6;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(endLabel, gbc);
            gbc.gridy = 7;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(endSpinner, gbc);
            gbc.gridy = 8;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(maxLabel, gbc);
            gbc.gridy = 9;
            gbc.insets = new Insets(5, 5, 20, 5);
            panel.add(maxField, gbc);
            gbc.gridy = 10;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(costLabel, gbc);
            gbc.gridy = 11;
            gbc.insets = new Insets(5, 5, 1, 5);
            panel.add(costField, gbc);
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.gridy = 15;
            panel.add(cancelButton, gbc);
            gbc.gridx = 2;
            gbc.gridy = 15;
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
    private static void areYouSure(String message, JFrame prevFrame,
                                   String classType, String description, LocalDateTime startTime, 
                                   LocalDateTime endTime, int maxParticipants, double cost){
        JFrame frame = new JFrame(message);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                    JOptionPane.showMessageDialog(frame, 
                        "Class successfully created!", 
                        "Confirmation", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                CreateAndShowGUI();
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
