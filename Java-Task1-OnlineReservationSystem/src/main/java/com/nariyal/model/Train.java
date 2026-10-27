package com.nariyal.model;


import java.time.LocalTime;

public class Train {

    private int trainNumber;
    private String trainName;
    private String trainSource;
    private String trainDestination;
    private LocalTime trainStartTime;
    private LocalTime trainEndTime;


    public LocalTime getTrainStartTime() {
        return trainStartTime;
    }

    public void setTrainStartTime(LocalTime trainStartTime) {
        this.trainStartTime = trainStartTime;
    }

    public LocalTime getTrainEndTime() {
        return trainEndTime;
    }

    public void setTrainEndTime(LocalTime trainEndTime) {
        this.trainEndTime = trainEndTime;
    }

    public Train(int trainNumber, String trainName, String trainSource, String trainDestination) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.trainSource = trainSource;
        this.trainDestination = trainDestination;
    }


    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainSource() {
        return trainSource;
    }

    public void setTrainSource(String trainSource) {
        this.trainSource = trainSource;
    }

    public String getTrainDestination() {
        return trainDestination;
    }

    public void setTrainDestination(String trainDestination) {
        this.trainDestination = trainDestination;
    }

}
