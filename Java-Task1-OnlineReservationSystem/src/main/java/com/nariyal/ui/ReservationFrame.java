
package com.nariyal.ui;

import com.nariyal.model.User;
import com.nariyal.model.Reservation;
import com.nariyal.model.Train;
import com.nariyal.service.ReservationService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ReservationFrame extends JFrame {

    private final User user;
    private JTextField departureTimeField;
    private JTextField arrivalTimeField;

    public ReservationFrame(User user) {
        this.user = user;

        arrivalTimeField = new JTextField(10);
        arrivalTimeField.setEditable(false);

        departureTimeField = new JTextField(10);
        departureTimeField.setEditable(false);



        setTitle("Book Reservation");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));

        JLabel passengerNameLabel = new JLabel("Passenger Name:");
        JTextField passengerNameField = new JTextField();

        JLabel trainNumberLabel = new JLabel("Train Number:");
        JComboBox<Train> trainNumberBox = new JComboBox<>();

        JLabel trainNameLabel = new JLabel("Train Name:");
        JTextField trainNameField = new JTextField();
        trainNameField.setEditable(false);

        JLabel journeyDateLabel = new JLabel("Journey Date:");

        SpinnerDateModel dateModel = new SpinnerDateModel(
                new Date(), null, null, java.util.Calendar.DAY_OF_MONTH
        );
        JSpinner journeyDateSpinner = new JSpinner(dateModel);
        journeyDateSpinner.setEditor(
                new JSpinner.DateEditor(journeyDateSpinner, "yyyy-MM-dd")
        );

        JLabel sourceLabel = new JLabel("Source:");
        JTextField sourceField = new JTextField();
        sourceField.setEditable(false);

        JLabel destinationLabel = new JLabel("Destination:");
        JTextField destinationField = new JTextField();
        destinationField.setEditable(false);



        JLabel classLabel = new JLabel("Train Class:");
        JComboBox<String> classBox = new JComboBox<>(
                new String[]{
                        "Sleeper",
                        "AC 3 Tier",
                        "AC 2 Tier",
                        "AC First Class"
                }
        );

        panel.add(passengerNameLabel);
        panel.add(passengerNameField);

        panel.add(trainNumberLabel);
        panel.add(trainNumberBox);

        panel.add(trainNameLabel);
        panel.add(trainNameField);

        panel.add(journeyDateLabel);
        panel.add(journeyDateSpinner);

        panel.add(sourceLabel);
        panel.add(sourceField);

        panel.add(destinationLabel);
        panel.add(destinationField);

        panel.add(classLabel);
        panel.add(classBox);

        panel.add(new JLabel("Departure Time:"));
        panel.add(departureTimeField);


        panel.add(new JLabel("Arrival Time:"));
        panel.add(arrivalTimeField);




        ReservationService reservationService = new ReservationService();

        // Load all available trains from the database.
        try {
            List<Train> trains = reservationService.getAllTrains();

            for (Train train : trains) {
                trainNumberBox.addItem(train);
            }

        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load trains: " + ex.getMessage(),
                    "Train Loading Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        trainNumberBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                if (value instanceof Train train) {
                    setText(String.valueOf(train.getTrainNumber()));
                }

                return this;
            }
        });

        // fills the train details when train number added

        trainNumberBox.addActionListener(e -> {
            Train selectedTrain =
                    (Train) trainNumberBox.getSelectedItem();

            if (selectedTrain != null) {
                trainNameField.setText(selectedTrain.getTrainName());
                sourceField.setText(selectedTrain.getTrainSource());
                destinationField.setText(
                        selectedTrain.getTrainDestination()
                );

                updateTrainSchedule(selectedTrain, journeyDateSpinner);



            } else {
                trainNameField.setText("");
                sourceField.setText("");
                destinationField.setText("");
                departureTimeField.setText("");
                arrivalTimeField.setText("");
            }
        });

        journeyDateSpinner.addChangeListener(e -> {
            Train selectedTrain =
                    (Train) trainNumberBox.getSelectedItem();

            updateTrainSchedule(selectedTrain, journeyDateSpinner);
        });


        JButton bookButton = new JButton("Book Reservation");
        JButton backButton = new JButton("Back to Dashboard");

        panel.add(bookButton);
        panel.add(backButton);

        bookButton.addActionListener(e -> {
            try {
                String passengerName =
                        passengerNameField.getText().trim();

                if (passengerName.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Passenger name is required."
                    );
                }

                Train selectedTrain =
                        (Train) trainNumberBox.getSelectedItem();

                if (selectedTrain == null) {
                    throw new IllegalArgumentException(
                            "Please select a train."
                    );
                }

                Date selectedDate =
                        (Date) journeyDateSpinner.getValue();

                LocalDate journeyDate = selectedDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                Reservation reservation = new Reservation();

                reservation.setUserId(user.getUserId());
                reservation.setPassengerName(passengerName);
                reservation.setTrainNumber(
                        selectedTrain.getTrainNumber()
                );
                reservation.setTrainClass(
                        (String) classBox.getSelectedItem()
                );
                reservation.setTrainDate(journeyDate);
                reservation.setTrainSource(
                        selectedTrain.getTrainSource()
                );
                reservation.setTrainDestination(
                        selectedTrain.getTrainDestination()
                );

                long pnr =
                        reservationService.bookReservation(reservation);

                JOptionPane.showMessageDialog(
                        this,
                        "Reservation booked successfully!\nPNR: " + pnr,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Booking Failed",
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
            new DashboardFrame(user);
            dispose();
        });

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 40, 25, 40)
        );
        outerPanel.add(panel, BorderLayout.CENTER);

        add(outerPanel);
        setVisible(true);
    }

    private void updateTrainSchedule(
            Train selectedTrain,
            JSpinner journeyDateSpinner
    ) {
        if (selectedTrain == null) {
            departureTimeField.setText("");
            arrivalTimeField.setText("");
            return;
        }

        Date selectedDate = (Date) journeyDateSpinner.getValue();

        LocalDate journeyDate = selectedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        departureTimeField.setText(
                journeyDate + " " + selectedTrain.getTrainStartTime()
        );

        arrivalTimeField.setText(
                journeyDate + " " + selectedTrain.getTrainEndTime()
        );
    }

}
