import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

/**
 * SignUpPage - GUI for user registration (Client and Trainer)
 */
public class SignUpPage extends JFrame {
    private DatabaseManager dbManager;
    
    public SignUpPage(String userType) {
        dbManager = new DatabaseManager();
        
        setTitle("Sign Up as " + userType);
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color baylorGreen = new Color(0, 71, 56);
        Color baylorGold = new Color(255, 199, 44);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(baylorGreen);

        // Header panel
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(baylorGold);
        headerPanel.setPreferredSize(new Dimension(600, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel title = new JLabel("🐻 Sign Up as " + userType + " 🐻");
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
        gbc.gridy = 6;
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
                if (dbManager.usernameExists(username)) {
                    JOptionPane.showMessageDialog(SignUpPage.this, 
                        "Username already exists. Please choose a different one.", 
                        "Username Taken", 
                        JOptionPane.ERROR_MESSAGE);
                    usernameField.setText("");
                    return;
                }

                if (dbManager.emailExists(email)) {
                    JOptionPane.showMessageDialog(SignUpPage.this, 
                        "Email already registered. Please use a different email.", 
                        "Email Taken", 
                        JOptionPane.ERROR_MESSAGE);
                    emailField.setText("");
                    return;
                }

                // Register user
                boolean success = dbManager.registerUser(username, password, email, 
                                                         userType, firstName, lastName);
                
                if (success) {
                    JOptionPane.showMessageDialog(SignUpPage.this, 
                        "Account created successfully! Logging you in...", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Automatically log the user in after successful signup
                    User newUser = dbManager.loginUser(username, password);
                    dbManager.closeConnection();
                    dispose();
                    
                    if (newUser != null) {
                        // Open dashboard for the newly registered user
                        SwingUtilities.invokeLater(() -> new DashboardUI(newUser.getFirstName()));
                    } else {
                        // If auto-login fails, go to homepage (shouldn't happen)
                        HomePage.main(null);
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
                dbManager.closeConnection();
                dispose();
                HomePage.main(null);
            }
        });

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        // For testing
        SwingUtilities.invokeLater(() -> new SignUpPage("Client"));
    }
}

