
package com.nariyal.ui;

import com.nariyal.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("Register Account");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));

        JLabel heading = new JLabel("Create Your Account", SwingConstants.CENTER);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");

        panel.add(heading);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);
        panel.add(registerButton);
        panel.add(backButton);

        AuthService authService = new AuthService();

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword =
                    new String(confirmPasswordField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Passwords do not match.",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                authService.register(username, password);

                JOptionPane.showMessageDialog(
                        this,
                        "Account created successfully! Please log in.",
                        "Registration Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                new LoginFrame();
                dispose();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Database error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        outerPanel.add(panel, BorderLayout.CENTER);

        add(outerPanel);
        setVisible(true);
    }
}
