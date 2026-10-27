package com.nariyal.dao;

import com.nariyal.database.DatabaseConnection;
import com.nariyal.model.Train;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TrainDAO {



    public Train findTrainByNumber (int trainNumber) throws SQLException {
        String sql = """
                SELECT * FROM trains
                WHERE train_number = ?;
                """;

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setInt(1, trainNumber);

            try(var result = statement.executeQuery()) {
                if (result.next()) {
                    Train train = new Train(result.getInt("train_number"),
                            result.getString("train_name"),
                            result.getString("source"),
                            result.getString("destination")
                    );
                    String startTime = result.getString("start_time");
                    String endTime = result.getString("end_time");
                    if (startTime != null) {
                        train.setTrainStartTime(LocalTime.parse(startTime));
                    }
                    if(endTime != null){
                        train.setTrainEndTime(LocalTime.parse(endTime));
                    }
                    return train;
                } else {
                    return null;
                }
            }

        }

    }


public List<Train> getAllTrains() throws SQLException{
        String sql = """
                SELECT * FROM trains;
                """;

        try(Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
        ){
            List<Train> list = new ArrayList<>();
            var result = statement.executeQuery(sql);
            while(result.next()){
                Train train = new Train(result.getInt("train_number"),
                        result.getString("train_name"),
                        result.getString("source"),
                        result.getString("destination")
                );
                String startTime = result.getString("start_time");
                String endTime = result.getString("end_time");
                if (startTime != null) {
                    train.setTrainStartTime(LocalTime.parse(startTime));
                }
                if(endTime != null){
                    train.setTrainEndTime(LocalTime.parse(endTime));
                }
                list.add(train);

            }
            return list;
        }
    }
}
