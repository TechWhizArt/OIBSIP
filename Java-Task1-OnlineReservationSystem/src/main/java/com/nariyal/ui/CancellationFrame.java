
package com.nariyal.ui;

import com.nariyal.model.Reservation;
import com.nariyal.model.User;
import com.nariyal.service.ReservationService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CancellationFrame extends JFrame {

    public CancellationFrame(User user) {
        setTitle("Cancel Reservation");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));





        JLabel pnrLabel = new JLabel("Enter PNR:");
        JTextField pnrField = new JTextField();

        JButton searchButton = new JButton("Find Reservation");

        JLabel passengerLabel = new JLabel("Passenger:");
        JTextField passengerField = new JTextField();
        passengerField.setEditable(false);

        JLabel trainLabel = new JLabel("Train Number:");
        JTextField trainField = new JTextField();
        trainField.setEditable(false);

        JLabel dateLabel = new JLabel("Journey Date:");
        JTextField dateField = new JTextField();
        dateField.setEditable(false);

        JButton cancelButton = new JButton("Cancel Reservation");
        cancelButton.setEnabled(false);

        panel.add(pnrLabel);
        panel.add(pnrField);
        panel.add(new JLabel(""));
        panel.add(searchButton);

        panel.add(passengerLabel);
        panel.add(passengerField);
        panel.add(trainLabel);
        panel.add(trainField);
        panel.add(dateLabel);
        panel.add(dateField);

        panel.add(new JLabel(""));
        panel.add(cancelButton);

        JButton backButton = new JButton("Back to Dashboard");
        panel.add(new JLabel(""));
        panel.add(backButton);

        backButton.addActionListener(e -> {
            new DashboardFrame(user);
            dispose();
        });

        ReservationService reservationService = new ReservationService();

        searchButton.addActionListener(e -> {
            cancelButton.setEnabled(false);
            passengerField.setText("");
            trainField.setText("");
            dateField.setText("");
            try {
                long pnr = Long.parseLong(pnrField.getText().trim());

                Reservation reservation =
                        reservationService.getReservationDetails(pnr);

                passengerField.setText(reservation.getPassengerName());
                trainField.setText(String.valueOf(reservation.getTrainNumber()));
                dateField.setText(reservation.getTrainDate().toString());

                cancelButton.setEnabled(true);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid numeric PNR.",
                        "Invalid PNR",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (IllegalArgumentException ex) {
                passengerField.setText("");
                trainField.setText("");
                dateField.setText("");
                cancelButton.setEnabled(false);

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Reservation Not Found",
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


        cancelButton.addActionListener(e -> {
            try {
                long pnr = Long.parseLong(pnrField.getText().trim());

                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to cancel this reservation?",
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (choice == JOptionPane.YES_OPTION) {
                    boolean cancelled = reservationService.cancelReservation(pnr);

                    if (cancelled) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Reservation cancelled successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        passengerField.setText("");
                        trainField.setText("");
                        dateField.setText("");
                        pnrField.setText("");
                        cancelButton.setEnabled(false);
                    }
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Cancellation Failed",
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
