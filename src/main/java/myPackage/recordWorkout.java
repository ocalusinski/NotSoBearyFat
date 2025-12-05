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

public class recordWorkout {
{
    static String[] workoutOptions = {"Walking", "Running", "Lifting", "HIIT", "Other"};
    static JTextField dateField = new JTextField(20);
    static JComboBox<String> workoutDropDown = new JComboBox<>(workoutOptions);
    static JTextField durationField = new JTextField(20);
    static JTextField caloriesBurntField = new JTextField(20);

    private static void createAndShowGUI(){
        JFrame frame = new JFrame("RecordWorkout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(600, 800);
        frame.setVisible(true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

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


        frame.add(panel);
        frame.setVisible(true);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        dateField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    date[0] = LocalDate.parse(dateField.getText(), formatter);
                }
                catch(DateTimeParseException ex){
                    tempMessage(dateField, "Must be in MM-dd-yyyy form");
                }
            }
        });

        durationField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = e.getActionCommand();
                if(s.matches("\\d+")){
                    exerciseMinutes[0] = Integer.parseInt(s);
                }
                else{
                    tempMessage(durationField, "Invalid input, must be an integer");
                }
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


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                areYouSure("Cancel", frame, date[0], workoutType[0], exerciseMinutes[0], caloriesBurnt[0]);
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean fieldsFull = checkFields();
                if(!fieldsFull){
                    fieldsNotFull();
                }
                else {
                    areYouSure("Save", frame, date[0], workoutType[0], exerciseMinutes[0], caloriesBurnt[0]);
                }
            }
        });
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
                                   LocalDate date, String workoutType, int exerciseMinutes, int caloriesBurnt){
        JFrame frame = new JFrame(message);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.getContentPane().setBackground(new Color(227, 172, 204));

        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        JLabel label = new JLabel("Are you sure?");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        //panel.setBackground(new Color(227, 172, 204));
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
                    workoutData data = new workoutData(date, workoutType, exerciseMinutes, caloriesBurnt );
                }
                createAndShowGUI();
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

        JLabel label = new JLabel("One or more fields are missing input");
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



    public static void main(String[] args) {
        createAndShowGUI();
    }
}
