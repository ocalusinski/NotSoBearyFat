package myPackage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import static myPackage.Constants.*;

public class UserManagement {
    private static DatabaseManager dbMan = new DatabaseManager();
    private static void CreateAndShowAdminPortal(){
        JFrame frame = new JFrame("Admin Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.getContentPane().setBackground(baylorGreen);
        //make a three sized box layout and place the flowLayout in the middle!
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setSize(420, 120);

        JButton b = new JButton("Create New Admin");
        b.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              System.out.println("Create New Admin");
              frame.dispose();
              SwingUtilities.invokeLater(() -> new SignUpPage(true));
          }
        });
        buttonPanel.add(b);
        b = new JButton("Change Password/username");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                editUsernameAndPassword();
            }
        });
        buttonPanel.add(b);
        b = new JButton("Remove User");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeUser();
            }
        });
        buttonPanel.add(b);

        b = new JButton("Return to Main Page");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Return to Main Page");
                frame.dispose();
                new HomePage();
            }
        });
        buttonPanel.add(b);
        buttonPanel.setBackground(baylorGreen);
        frame.setLayout(new GridBagLayout());
        frame.add(buttonPanel, new GridBagConstraints());
        frame.setVisible(true);
    }
    private static void editUsernameAndPassword(){
        JFrame frame = new JFrame("Edit Username/Password");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.getContentPane().setBackground(baylorGreen);
        //boxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setSize(420, 120);
        panel.setBackground(baylorGreen);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));

        //my title text field
        JLabel title = new JLabel("Edit Username/Password\n\n");
        title.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        title.setForeground(baylorGold);
        panel.add(title);

        //email label
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(baylorGold);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(emailLabel);

        //email field
        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 12));
        emailField.setBackground(baylorGold);
        emailField.setMaximumSize(new Dimension(800, 20));
        panel.add(emailField);

        //username label
        JLabel usernameLabel = new JLabel("new username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(baylorGold);
        panel.add(usernameLabel);
        //username field
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField.setBackground(baylorGold);
        usernameField.setMaximumSize(new Dimension(800, 20));
        panel.add(usernameField);

        //password label
        JLabel passwordLabel = new JLabel("new password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(baylorGold);
        panel.add(passwordLabel);
        //password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setBackground(baylorGold);
        passwordField.setMaximumSize(new Dimension(800, 20));
        panel.add(passwordField);

        //password label
        JLabel cpasswordLabel = new JLabel("confirm new password:");
        cpasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cpasswordLabel.setForeground(baylorGold);
        panel.add(cpasswordLabel);
        //password field
        JPasswordField cpasswordField = new JPasswordField();
        cpasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
        cpasswordField.setBackground(baylorGold);
        cpasswordField.setMaximumSize(new Dimension(800, 20));
        panel.add(cpasswordField);

        //submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setBackground(baylorGold);
        submitButton.setForeground(baylorGreen);
        submitButton.setOpaque(true);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(cpasswordField.getPassword());

                // Validation
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please fill in all fields",
                            "Missing Information",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(frame,
                            "Passwords do not match",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                    cpasswordField.setText("");
                    return;
                }

                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(frame,
                            "Password must be at least 6 characters long",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                    cpasswordField.setText("");
                    return;
                }

                String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
                if (!Pattern.matches(emailPattern, email)) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid email address",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(dbMan.emailExists(email)){
                    if(dbMan.updateUser(username, password, email)){
                        JOptionPane.showMessageDialog(frame, "User has been updated successfully",
                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        CreateAndShowAdminPortal();
                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "Username already exists, please select a different username",
                                "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(frame, "Email does not exist. Try creating a new user",
                            "Error!", JOptionPane.ERROR_MESSAGE);
                    //route to add user
                }
            }
        });
        panel.add(submitButton);


        //Back button
        JButton backButton = new JButton("Back");
        backButton.setBackground(baylorGold);
        backButton.setForeground(baylorGreen);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setOpaque(true);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                CreateAndShowAdminPortal();
            }
        });
        panel.add(backButton);

        //add panel to frame
        frame.add(panel);
        frame.setVisible(true);
    }
    private static void removeUser(){
        JFrame frame = new JFrame("Remove User");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.getContentPane().setBackground(baylorGreen);
        //boxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setSize(420, 120);
        panel.setBackground(baylorGreen);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));

        //my title text field
        JLabel title = new JLabel("Remove User\n\n");
        title.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        title.setForeground(baylorGold);
        panel.add(title);

        //username label
        JLabel usernameLabel = new JLabel("username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(baylorGold);
        panel.add(usernameLabel);
        //username field
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField.setBackground(baylorGold);
        usernameField.setMaximumSize(new Dimension(800, 20));
        panel.add(usernameField);

        //password label
        JLabel passwordLabel = new JLabel("password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(baylorGold);
        panel.add(passwordLabel);
        //password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setBackground(baylorGold);
        passwordField.setMaximumSize(new Dimension(800, 20));
        panel.add(passwordField);

        //submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setBackground(baylorGold);
        submitButton.setForeground(baylorGreen);
        submitButton.setOpaque(true);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                // Validation
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please fill in all fields",
                            "Missing Information",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }


                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(frame,
                            "Password must be at least 6 characters long",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                    return;
                }

                if(dbMan.usernameExists(username)){
                    if(dbMan.removeUser(username, password)){
                        JOptionPane.showMessageDialog(frame, "User has been successfully removed",
                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        CreateAndShowAdminPortal();
                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "Error occurred. Please try again",
                                "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(frame, "Username/password combo does not exist. Try resetting username and password first",
                            "Error!", JOptionPane.ERROR_MESSAGE);
                    //route to add user
                }
            }
        });
        panel.add(submitButton);


        //Back button
        JButton backButton = new JButton("Back");
        backButton.setBackground(baylorGold);
        backButton.setForeground(baylorGreen);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setOpaque(true);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                CreateAndShowAdminPortal();
            }
        });
        panel.add(backButton);

        //add panel to frame
        frame.add(panel);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        CreateAndShowAdminPortal();
    }
}
