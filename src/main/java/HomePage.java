import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage {
    private static void CreateAndShowGUI(){
        JFrame frame = new JFrame("Not So Beary Fat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 1000);

        Color baylorGreen = new Color(0, 71, 56);
        Color baylorGold = new Color(255, 199, 44);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(baylorGreen);

        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(baylorGold);
        headerPanel.setPreferredSize(new Dimension(800, 150));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("🐻💪 Not So Beary Fat 💪🐻");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(baylorGreen);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 10, 20);
        headerPanel.add(title, gbc);

        JLabel subtitle = new JLabel("Your Journey to Fitness Starts Here");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(baylorGreen);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 20, 20, 20);
        headerPanel.add(subtitle, gbc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(baylorGreen);
        GridBagConstraints gbcCenter = new GridBagConstraints();

        JLabel pawPrint = new JLabel("🐾 🐾 🐾");
        pawPrint.setFont(new Font("Arial", Font.PLAIN, 35));
        pawPrint.setForeground(baylorGold);
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 0;
        gbcCenter.insets = new Insets(30, 20, 20, 20);
        centerPanel.add(pawPrint, gbcCenter);

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setForeground(baylorGold);
        gbcCenter.gridy = 1;
        gbcCenter.insets = new Insets(20, 20, 20, 20);
        centerPanel.add(loginLabel, gbcCenter);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(baylorGold);
        loginButton.setForeground(baylorGreen);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setOpaque(true);
        loginButton.setPreferredSize(new Dimension(250, 50));
        loginButton.setBorderPainted(false);
        gbcCenter.gridy = 2;
        gbcCenter.insets = new Insets(10, 20, 20, 20);
        centerPanel.add(loginButton, gbcCenter);

        JLabel spacer = new JLabel("or");
        spacer.setFont(new Font("Arial", Font.PLAIN, 16));
        spacer.setForeground(Color.WHITE);
        gbcCenter.gridy = 3;
        gbcCenter.insets = new Insets(20, 20, 20, 20);
        centerPanel.add(spacer, gbcCenter);

        JLabel signUpLabel = new JLabel("New Here? Sign Up!");
        signUpLabel.setFont(new Font("Arial", Font.BOLD, 24));
        signUpLabel.setForeground(baylorGold);
        gbcCenter.gridy = 4;
        gbcCenter.insets = new Insets(20, 20, 20, 20);
        centerPanel.add(signUpLabel, gbcCenter);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(baylorGold);
        signUpButton.setForeground(baylorGreen);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.setOpaque(true);
        signUpButton.setPreferredSize(new Dimension(250, 50));
        signUpButton.setBorderPainted(false);
        gbcCenter.gridy = 5;
        gbcCenter.insets = new Insets(10, 20, 30, 20);
        centerPanel.add(signUpButton, gbcCenter);

        JLabel bottomPaws = new JLabel("🐾 🐾 🐾");
        bottomPaws.setFont(new Font("Arial", Font.PLAIN, 35));
        bottomPaws.setForeground(baylorGold);
        gbcCenter.gridy = 6;
        gbcCenter.insets = new Insets(10, 20, 30, 20);
        centerPanel.add(bottomPaws, gbcCenter);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                frame.dispose();
                new LoginPage();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                frame.dispose();
                new SignUpPage();
            }
        });

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        frame.getContentPane().add(mainPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        CreateAndShowGUI();
    }
}
