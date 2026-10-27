package com.nariyal.dao;

import com.nariyal.database.DatabaseConnection;
import com.nariyal.model.Reservation;
import com.nariyal.model.User;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {


    public long saveReservation(Reservation reservation) throws SQLException {
        String sql = """
                INSERT INTO reservations(user_id, passenger_name, train_number, train_class, journey_date, source, destination, departure_time, arrival_time)
                VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            String userId = reservation.getUserId();
            String passengerName = reservation.getPassengerName();
            int trainNumber = reservation.getTrainNumber();
            String trainClass = reservation.getTrainClass();
            String journeyDate = reservation.getTrainDate().toString();
            String source = reservation.getTrainSource();
            String destination = reservation.getTrainDestination();
            String departure = String.valueOf(reservation.getDeparture());
            String arrival = String.valueOf(reservation.getArrival());

            statement.setString(1, userId);
            statement.setString(2, passengerName);
            statement.setInt(3, trainNumber);
            statement.setString(4, trainClass);
            statement.setString(5, journeyDate);
            statement.setString(6, source);
            statement.setString(7, destination);
            statement.setString(8, departure);
            statement.setString(9, arrival);
            var result = statement.executeUpdate();
            if (result == 1) {
                try (var keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getLong(1);

                    } else {
                        throw new SQLException("Reservation saved, but PNR could not be retrieved.");
                    }
                }
            } else {
                throw new SQLException("Failed to save reservation");
            }
        }
    }


    public Reservation findReservationByPNR(long pnrnumber) throws SQLException{
        String sql = """
                SELECT * FROM reservations 
                WHERE pnr = ?;
                """;

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setLong(1, pnrnumber);
            try(var result = statement.executeQuery()) {
                if (result.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setPnrNumber(result.getLong("pnr"));
                    reservation.setPassengerName(result.getString("passenger_name"));
                    reservation.setTrainNumber(result.getInt("train_number"));
                    reservation.setTrainClass(result.getString("train_class"));
                    reservation.setUserId(result.getString("user_id"));
                    reservation.setTrainDate(LocalDate.parse(result.getString("journey_date")));
                    reservation.setTrainSource(result.getString("source"));
                    reservation.setTrainDestination(result.getString("destination"));
                    reservation.setDeparture(LocalTime.parse(result.getString("departure_time")));
                    reservation.setArrival(LocalTime.parse(result.getString("arrival_time")));
                    return reservation;
                } else {
                    throw new SQLException("Reservation not found");
                }
            }
        }
    }

    public boolean deleteReservationByPNR(long pnrnumber) throws SQLException{
        String sql = """
                DELETE FROM reservations 
                WHERE pnr = ?;
                """;

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setLong(1, pnrnumber);
            var result = statement.executeUpdate();
            return result > 0;
        }

    }



    public List<Reservation> getAllReservationByUser(String uname) throws SQLException{
        String sql = """
                SELECT *
                FROM reservations r
                WHERE user_id = ?
                ORDER BY journey_date DESC, pnr DESC
                """;

        List<Reservation> list = new ArrayList<>();

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            UserDAO userDao = new UserDAO();

            User user = userDao.findUserByUserName(uname);

            if (user == null) {
                return list;
            }
            String userId = user.getUserId();

            statement.setString(1, userId);
            try(var result = statement.executeQuery()) {
                while (result.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setUserId(result.getString("user_id"));
                    reservation.setPnrNumber(result.getLong("pnr"));
                    reservation.setPassengerName(result.getString("passenger_name"));
                    reservation.setTrainNumber(result.getInt("train_number"));
                    reservation.setTrainClass(result.getString("train_class"));
                    reservation.setTrainDate(
                            LocalDate.parse(result.getString("journey_date"))
                    );
                    reservation.setTrainSource(result.getString("source"));
                    reservation.setTrainDestination(result.getString("destination"));
                    reservation.setDeparture(LocalTime.parse(result.getString("departure_time")));
                    reservation.setArrival(LocalTime.parse(result.getString("arrival_time")));


                    list.add(reservation);

                }
            }

            return list;

        }
    }

}
