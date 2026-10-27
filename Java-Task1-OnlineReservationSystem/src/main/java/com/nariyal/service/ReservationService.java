package com.nariyal.service;

import com.nariyal.dao.ReservationDAO;
import com.nariyal.dao.TrainDAO;
import com.nariyal.model.Reservation;
import com.nariyal.model.Train;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

public class ReservationService {

    public long bookReservation(Reservation reservation) throws SQLException {


        if (reservation == null || reservation.getTrainDate() == null || reservation.getPassengerName() == null
                || reservation.getUserId() == null || reservation.getTrainClass() == null
                || reservation.getUserId().isBlank() || reservation.getPassengerName().isBlank()
                || reservation.getTrainClass().isBlank()) {
            throw new IllegalArgumentException("Revervation data not complete");
        }

        int trainnumber = reservation.getTrainNumber();
        TrainDAO traindao = new TrainDAO();
        ReservationDAO reservationDAO = new ReservationDAO();

        Train train = traindao.findTrainByNumber(trainnumber);
        if (train != null) { //train is valid

            reservation.setDeparture(train.getTrainStartTime());
            reservation.setArrival(train.getTrainEndTime());
            LocalTime depTime = train.getTrainStartTime();
            LocalDate resDate = reservation.getTrainDate();
            if (depTime == null) {
                throw new IllegalArgumentException("No depTime found");
            }
            if (resDate.isEqual(LocalDate.now())) {
                if (depTime.isAfter(LocalTime.now())) {
                    long pnr = reservationDAO.saveReservation(reservation);
                    return pnr;
                } else {
                    throw new IllegalArgumentException("Train has departured");
                }
            } else if (!resDate.isBefore(LocalDate.now())) {
                long pnr = reservationDAO.saveReservation(reservation);
                return pnr;
            } else {
                throw new IllegalArgumentException("Train has departured");
            }
        } else {
            throw new IllegalArgumentException("No train found");
        }
    }

    public boolean cancelReservation(long pnr)throws  SQLException{

        if(pnr <= 0){
            throw new IllegalArgumentException("Enter a valid pnr");
        }

        ReservationDAO reservationDao = new ReservationDAO();
        Reservation reservation = reservationDao.findReservationByPNR(pnr);
        if(reservation != null){
            boolean success = reservationDao.deleteReservationByPNR(pnr);
            return success;
        }else{
            throw new IllegalArgumentException("No reservation found with this pnr");
        }

    }



    public Reservation getReservationDetails(long pnr) throws SQLException {

        if(pnr <= 0){
            throw new IllegalArgumentException("Enter a valid pnr");
        }

        ReservationDAO reservationDao = new ReservationDAO();
        Reservation reservation = reservationDao.findReservationByPNR(pnr);
        if(reservation != null){
            return reservation;
        }else{
            throw new IllegalArgumentException("Reservation not found");
        }

    }

    public List<Train> getAllTrains() throws SQLException{

        TrainDAO trainDao = new TrainDAO();
        List<Train> list = trainDao.getAllTrains();
        if(list != null && !list.isEmpty()){
            return list;
        }else{
            throw new IllegalArgumentException("No trains found");
        }

    }

    public List<Reservation> getAllReservationsByUser(String username) throws SQLException{

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }

        ReservationDAO reservationDAO = new ReservationDAO();
        return reservationDAO.getAllReservationByUser(username);

    }








}
