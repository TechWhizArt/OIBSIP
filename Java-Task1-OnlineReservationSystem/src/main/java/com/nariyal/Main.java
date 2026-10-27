package com.nariyal;

import com.nariyal.database.DatabaseInitializer;
import com.nariyal.ui.LoginFrame;
import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        System.out.printf("Hello Guys! Welcome to my project walkthrough video");
        try {
            DatabaseInitializer.initialize();
            DatabaseInitializer.verifyColumns();

            System.out.println("Database and tables initialized successfully!");
            SwingUtilities.invokeLater(() -> new LoginFrame());

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to initialize the database:\n" + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}