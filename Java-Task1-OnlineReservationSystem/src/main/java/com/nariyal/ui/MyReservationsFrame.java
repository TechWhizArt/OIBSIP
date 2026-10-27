
package com.nariyal.ui;

import com.nariyal.model.Reservation;
import com.nariyal.model.User;
import com.nariyal.service.ReservationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MyReservationsFrame extends JFrame {

    private final User user;
    private final ReservationService reservationService;

    private final DefaultTableModel tableModel;
    private final JTable reservationTable;


    public MyReservationsFrame(User user) {
        this.user = user;
        this.reservationService = new ReservationService();

        setTitle("My Reservations");
        setSize(900, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {
                "PNR", "Train Number", "Passenger Name",
                "Journey Date", "Source", "Destination","Departure",
                "Arrival", "Class"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservationTable = new JTable(tableModel);
        reservationTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        reservationTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(reservationTable);

        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Dashboard");

        refreshButton.addActionListener(e -> loadReservations());

        backButton.addActionListener(e -> {
            dispose();
            new DashboardFrame(user).setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);


        setLayout(new BorderLayout(10, 10));
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadReservations();
        setVisible(true);
    }



    private void loadReservations() {
        tableModel.setRowCount(0);

        try {
            List<Reservation> reservations =
                    reservationService.getAllReservationsByUser(
                            user.getUserName()
                    );

            for (Reservation reservation : reservations) {
                tableModel.addRow(new Object[]{
                        reservation.getPnrNumber(),
                        reservation.getTrainNumber(),
                        reservation.getPassengerName(),
                        reservation.getTrainDate(),
                        reservation.getTrainSource(),
                        reservation.getTrainDestination(),
                        reservation.getDeparture(),
                        reservation.getArrival(),
                        reservation.getTrainClass()
                });
            }

            if (reservations.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "You have no reservations yet.",
                        "My Reservations",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load reservations: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
