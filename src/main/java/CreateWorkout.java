package org.example;
import javax.swing.*;
import java.awt.*;

public class CreateWorkout {
    private static void CreateAndShowAdminPortal(){
       JFrame frame = new JFrame("Admin Portal");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(500, 500);
       frame.getContentPane().setBackground(Color.PINK);

       JPanel buttonPanel = new JPanel();
       buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
       buttonPanel.setSize(420, 120);
       String[] buttons = {"Add User", "Change Password", "Remove User"};
       for (String button : buttons) {
           JButton b = new JButton(button);
           buttonPanel.add(b);
       }
       buttonPanel.setBackground(Color.PINK);
       frame.add(buttonPanel);
       frame.setVisible(true);
    }
    public static void main(String[] args) {
        CreateAndShowAdminPortal();
    }
}