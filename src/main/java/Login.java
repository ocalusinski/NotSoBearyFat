import javax.swing.*;
import java.awt.*;

public class Login {

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);

        // Colors
        Color background = new Color(190, 220, 250);
        Color buttonColor = new Color(140, 180, 230);
        Color textColor = new Color(30, 60, 100);

        // Other Stuff
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(background);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("Not So Beary Fat - Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(textColor);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton forgotButton = new JButton("Forgot Password");
        JButton signupButton = new JButton("Sign Up");

        // Button styles
        loginButton.setBackground(buttonColor);
        forgotButton.setBackground(buttonColor);
        signupButton.setBackground(buttonColor);
        loginButton.setFocusPainted(false);
        forgotButton.setFocusPainted(false);
        signupButton.setFocusPainted(false);

        // Gridbag Layout
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        gbc.gridy = 4;
        panel.add(forgotButton, gbc);

        gbc.gridy = 5;
        panel.add(signupButton, gbc);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

        // Button Actions
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter both username and password.",
                        "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (username.equalsIgnoreCase("Jonah") && password.equals("password123")) {
                JOptionPane.showMessageDialog(frame,
                        "Login successful! Redirecting to dashboard...",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                showDashboard("General User");
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Incorrect username or password. Please try again.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        });

        forgotButton.addActionListener(e ->
                JOptionPane.showMessageDialog(frame,
                        "Password reset link sent to your email.",
                        "Password Recovery", JOptionPane.INFORMATION_MESSAGE)
        );

        signupButton.addActionListener(e ->
                JOptionPane.showMessageDialog(frame,
                        "Redirecting to Sign Up page (not implemented).",
                        "Sign Up", JOptionPane.INFORMATION_MESSAGE)
        );
    }

    // Temporary showDashboard
    // TODO: Dashboard Use Cases
    private void showDashboard(String role) {
        JFrame dash = new JFrame(role + " Dashboard");
        dash.setSize(400, 300);
        dash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel welcome = new JLabel("Welcome to your " + role + " dashboard!");
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        dash.add(welcome);
        dash.setLocationRelativeTo(null);
        dash.setVisible(true);
    }

    public static void main(String[] args) {
        Login app = new Login();
        app.createAndShowGUI();
    }
}
