import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginPage - GUI for user login (Client and Trainer)
 */
public class LoginPage extends JFrame {
    private DatabaseManager dbManager;
    
    public LoginPage(String userType) {
        dbManager = new DatabaseManager();
        
        setTitle("Login as " + userType);
        setSize(500, 500);
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
        headerPanel.setPreferredSize(new Dimension(500, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel title = new JLabel("🐻 Login as " + userType + " 🐻");
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

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setForeground(baylorGold);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 10, 20);
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setForeground(baylorGold);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 20, 10, 20);
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(passwordField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(baylorGreen);
        buttonPanel.setOpaque(true);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(baylorGold);
        loginButton.setForeground(baylorGreen);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setOpaque(true);
        loginButton.setPreferredSize(new Dimension(100, 40));
        loginButton.setBorderPainted(false);

        JButton backButton = new JButton("Back");
        backButton.setBackground(baylorGold);
        backButton.setForeground(baylorGreen);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setOpaque(true);
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setBorderPainted(false);

        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(buttonPanel, gbc);

        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPage.this, 
                        "Please enter both username and password", 
                        "Missing Information", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                User user = dbManager.loginUser(username, password);
                if (user != null) {
                    if (!user.getUserType().equalsIgnoreCase(userType)) {
                        JOptionPane.showMessageDialog(LoginPage.this, 
                            "This account is not a " + userType + " account", 
                            "Invalid Account Type", 
                            JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(LoginPage.this, 
                            "Welcome back, " + user.getFirstName() + "!", 
                            "Login Successful", 
                            JOptionPane.INFORMATION_MESSAGE);
                        dbManager.closeConnection();
                        dispose();
                        // Open user dashboard
                        SwingUtilities.invokeLater(() -> new DashboardUI(user.getFirstName(), user.getUserType()));
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginPage.this, 
                        "Invalid username or password", 
                        "Login Failed", 
                        JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
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
        SwingUtilities.invokeLater(() -> new LoginPage("Client"));
    }
}

