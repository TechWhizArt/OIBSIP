package com.nariyal.ui;
import com.nariyal.model.User;
import com.nariyal.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


public class LoginFrame extends JFrame{

    public LoginFrame() {

        setTitle("Online Reservation System");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JLabel heading = new JLabel("Online Reservation System");
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        panel.add(heading);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        JButton registerButton = new JButton("Create Account");
        panel.add(registerButton);
        registerButton.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });

        AuthService authService = new AuthService();
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            try {
                User user = authService.login(username, password);


                new DashboardFrame(user);
                dispose();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Login Failed",
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

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        outerPanel.add(panel, BorderLayout.CENTER);

        add(outerPanel);


        setVisible(true);
    }







}
