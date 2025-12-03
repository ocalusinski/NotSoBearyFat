package myPackage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import static myPackage.Constants.*;

public class UserManagement {
    private static void CreateAndShowAdminPortal(){
        JFrame frame = new JFrame("Admin Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.getContentPane().setBackground(new Color(0, 71, 56));
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
                System.out.println("Change Password");
            }
        });
        buttonPanel.add(b);
        b = new JButton("Remove User");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Remove User");
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
        buttonPanel.setBackground(new Color(0, 71, 56));
        frame.setLayout(new GridBagLayout());
        frame.add(buttonPanel, new GridBagConstraints());
        frame.setVisible(true);
    }
    private static void editUsernameAndPassword(){
        JFrame frame = new JFrame("Edit Username/Password");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.getContentPane().setBackground(new Color(0, 71, 56));
        //make a three sized box layout and place the flowLayout in the middle!
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setSize(420, 120);
        //email field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(baylorGold);
    }
    public static void main(String[] args) {
        CreateAndShowAdminPortal();
    }
}
