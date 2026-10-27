package com.nariyal.ui;

import com.nariyal.model.User;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private final User user;

    public DashboardFrame(User user) {
        this.user = user;

        setTitle("Reservation Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton bookButton = new JButton("Book Reservation");
        JButton cancelButton = new JButton("Cancel Reservation");
        JButton reservationButton = new JButton("My Reservations");
        JButton logoutButton = new JButton("Logout");
        panel.add(bookButton);
        panel.add(cancelButton);
        panel.add(reservationButton);
        panel.add(logoutButton);


        bookButton.addActionListener(e -> {
            new ReservationFrame(user);
            dispose();
        });

        cancelButton.addActionListener(e -> {
            new CancellationFrame(user);
            dispose();
        });

        reservationButton.addActionListener(e->{
            new MyReservationsFrame(user);
            dispose();
        });
        logoutButton.addActionListener(e -> {
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