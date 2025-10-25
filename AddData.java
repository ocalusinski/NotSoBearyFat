import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;


public class AddData{
    private static void CreateAndShowGUI(){
        JFrame frame = new JFrame("Add Data");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(600, 800);
        frame.setVisible(true);

        JButton addDataButton = new JButton("Add Data");
        addDataButton.setBackground(new Color(186, 140, 167));
        addDataButton.setForeground(new Color(186, 140, 167));
        addDataButton.setOpaque(true);
        addDataButton.setPreferredSize(new Dimension(100, 50));
        addDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                AddDataPage newPage = new AddDataPage();
                newPage.setVisible(true);
                frame.dispose();
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(addDataButton, new GridBagConstraints());
        panel.setBackground(new Color(227, 172, 204));
        frame.getContentPane().add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class AddDataPage extends JFrame{

        public AddDataPage(){
            final LocalDate[] date = new LocalDate[1];
            final int[] cal = new int[1];
            final double[] weight = new double[1];
            final double[] sleep = new double[1];
            final int[] totalCal = new int[1];

            JFrame dataFrame = new JFrame("Add Data");
            dataFrame.setSize(600, 800);
            dataFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            JLabel dateLabel = new JLabel("Date (MM-dd-yyyy)");
            JLabel calLabel = new JLabel("Calories Consumed (kcal)");
            JLabel weightLabel = new JLabel("Weight (lbs)");
            JLabel sleepLabel = new JLabel("Sleep (hrs)");
            JLabel totCalLabel = new JLabel("Total Calories Burned (kcal)");
            JLabel optional = new JLabel("(Optional)");
            JTextField dateField = new JTextField(20);
            JTextField calIntake = new JTextField(20);
            JTextField weightField = new JTextField(20);
            JTextField sleepField = new JTextField(20);
            JTextField totCalBurn = new JTextField(20);
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");

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

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    areYouSure("Cancel", dataFrame, date[0], cal[0], sleep[0], weight[0], totalCal[0]);
                }
            });
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    areYouSure("Save", dataFrame, date[0], cal[0], sleep[0], weight[0], totalCal[0]);
                }
            });


            JPanel menuBar = new JPanel();
            menuBar.setLayout(new GridBagLayout());
            menuBar.setPreferredSize(new Dimension(600, 300));
            GridBagConstraints g = new GridBagConstraints();
            menuBar.setBackground(new Color(186, 140, 167));
            menuBar.setOpaque(true);
            menuBar.setPreferredSize(new Dimension(100, 50));
            JLabel item = new JLabel("Add Data");
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
            panel.setBackground(new Color(227, 172, 204));

            dataFrame.add(menuBar, BorderLayout.NORTH);
            dataFrame.getContentPane().add(panel);
            dataFrame.setVisible(true);
            dataFrame.setLocationRelativeTo(null);

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
                                   LocalDate date, int cal, double sleep, double weight, int totalCal){
        JFrame frame = new JFrame(message);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(227, 172, 204));

        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        JLabel label = new JLabel("Are you sure?");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBackground(new Color(227, 172, 204));
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
                    Data data = new Data(date, cal, weight, sleep);
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


    public static void main(String[] args){
        CreateAndShowGUI();
    }
}
