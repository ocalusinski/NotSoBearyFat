package myPackage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import static myPackage.Constants.*;

/**
 * SignUpPage - GUI for user registration (Client and Trainer)
 */
public class SignUpPage extends JFrame {
    private JRadioButton clientRadio;
    
    /**
     * Constructs a new SignUpPage GUI.
     * @author Owen Chipman
     */
    public SignUpPage(boolean admin) {
        setTitle("Sign Up");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Constants.baylorGreen);

        // Header panel
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(baylorGold);
        headerPanel.setPreferredSize(new Dimension(600, 100));

        GridBagConstraints gbc = new GridBagConstraints();

        String titleTitle = "🐻 Sign Up 🐻";
        if(admin) {
           titleTitle = "Admin Sign Up";
        }
        JLabel title = new JLabel(titleTitle);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(baylorGreen);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        headerPanel.add(title, gbc);

        // Center panel with form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(baylorGreen);
        gbc = new GridBagConstraints();

        // First Name field
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        firstNameLabel.setForeground(baylorGold);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 20, 5, 20);
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(firstNameLabel, gbc);

        JTextField firstNameField = new JTextField(20);
        firstNameField.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(firstNameField, gbc);

        // Last Name field
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        lastNameLabel.setForeground(baylorGold);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 5, 20);
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(lastNameLabel, gbc);

        JTextField lastNameField = new JTextField(20);
        lastNameField.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(lastNameField, gbc);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(baylorGold);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 20, 5, 20);
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(usernameField, gbc);

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(baylorGold);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 20, 5, 20);
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(emailField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(baylorGold);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 20, 5, 20);
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(passwordField, gbc);

        // Confirm Password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordLabel.setForeground(baylorGold);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 20, 5, 20);
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(confirmPasswordField, gbc);

        // User Type Selection (Radio Buttons)
        if(!admin) {
            JLabel userTypeLabel = new JLabel("Account Type:");
            userTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            userTypeLabel.setForeground(baylorGold);
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.insets = new Insets(15, 20, 5, 20);
            gbc.anchor = GridBagConstraints.EAST;
            centerPanel.add(userTypeLabel, gbc);

            JPanel userTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            userTypePanel.setBackground(baylorGreen);
            userTypePanel.setOpaque(false);

            clientRadio = new JRadioButton("Client", true);
            clientRadio.setForeground(baylorGold);
            clientRadio.setBackground(baylorGreen);
            clientRadio.setOpaque(false);
            clientRadio.setFont(new Font("Arial", Font.PLAIN, 14));

            JRadioButton trainerRadio = new JRadioButton("Trainer", false);
            trainerRadio.setForeground(baylorGold);
            trainerRadio.setBackground(baylorGreen);
            trainerRadio.setOpaque(false);
            trainerRadio.setFont(new Font("Arial", Font.PLAIN, 14));

            ButtonGroup userTypeGroup = new ButtonGroup();
            userTypeGroup.add(clientRadio);
            userTypeGroup.add(trainerRadio);

            userTypePanel.add(clientRadio);
            userTypePanel.add(trainerRadio);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            centerPanel.add(userTypePanel, gbc);
        }

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(baylorGreen);
        buttonPanel.setOpaque(true);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(baylorGold);
        signUpButton.setForeground(baylorGreen);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.setOpaque(true);
        signUpButton.setPreferredSize(new Dimension(100, 40));
        signUpButton.setBorderPainted(false);

        JButton backButton = new JButton("Back");
        backButton.setBackground(baylorGold);
        backButton.setForeground(baylorGreen);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setOpaque(true);
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setBorderPainted(false);

        buttonPanel.add(backButton);
        buttonPanel.add(signUpButton);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(buttonPanel, gbc);

        // Sign Up button action
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Validation
                if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || 
                    email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(SignUpPage.this, 
                        "Please fill in all fields", 
                        "Missing Information", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(SignUpPage.this, 
                        "Passwords do not match", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                    return;
                }

                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(SignUpPage.this, 
                        "Password must be at least 6 characters long", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                    return;
                }

                // Simple email validation
                String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
                if (!Pattern.matches(emailPattern, email)) {
                    JOptionPane.showMessageDialog(SignUpPage.this, 
                        "Please enter a valid email address", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if username or email already exists
                if (DB_MANAGER.usernameExists(username)) {
                    JOptionPane.showMessageDialog(SignUpPage.this, 
                        "Username already exists. Please choose a different one.", 
                        "Username Taken", 
                        JOptionPane.ERROR_MESSAGE);
                    usernameField.setText("");
                    return;
                }

                if (DB_MANAGER.emailExists(email)) {
                    JOptionPane.showMessageDialog(SignUpPage.this, 
                        "Email already registered. Please use a different email.", 
                        "Email Taken", 
                        JOptionPane.ERROR_MESSAGE);
                    emailField.setText("");
                    return;
                }

                // Get selected user type
                String selectedUserType = "Admin";
                if(!admin) {
                    selectedUserType = clientRadio.isSelected() ? "Client" : "Trainer";
                }
                // Register user
                boolean success = DB_MANAGER.registerUser(username, password, email,
                                                         selectedUserType, firstName, lastName);
                StringBuilder b = new StringBuilder();
                b.append("Account created successfully!");
                if(!admin){
                    b.append(" Logging you in...\n");
                }
                String message = b.toString();
                if (success) {
                    JOptionPane.showMessageDialog(SignUpPage.this, 
                        message,
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Automatically log the user in after successful signup
                    if(!admin) {
                        User newUser = DB_MANAGER.loginUser(username, password);
                        dispose();


                        if (newUser != null) {
                            // Open dashboard for the newly registered user
                            SwingUtilities.invokeLater(() -> new DashboardUI(newUser.getFirstName(), newUser.getUserType()));
                        } else {
                            // If auto-login fails, go to homepage (shouldn't happen)
                            HomePage.main(null);
                        }
                    }
                    //return to Admin Page
                    else{
                        dispose();
                        UserManagement.main(null);
                    }
                } else {
                    JOptionPane.showMessageDialog(SignUpPage.this, 
                        "Registration failed. Please try again.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Back button action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if(!admin) {
                    HomePage.main(null);
                }
                else {
                    SwingUtilities.invokeLater(() -> new UserManagement().main(null));
                }
            }
        });

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        setVisible(true);
    }

    /**
     * Main method to run the SignUpPage application for testing purposes.
     * @author zachtaylorcsc
     */
    public static void main(String[] args) {
        // For testing
        SwingUtilities.invokeLater(() -> new SignUpPage(false));
    }
}

