package com.nariyal.dao;

import com.nariyal.database.DatabaseConnection;
import com.nariyal.model.User;

import java.sql.*;

public class UserDAO {


    public boolean saveUser(User user) throws SQLException{


        String userId = user.getUserId();
        String userName = user.getUserName();
        String passwordHash = user.getPassword();


        String savingUser = """
                INSERT INTO users (user_id, username, password_hash)
                VALUES(?, ?, ?);
                """;

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(savingUser);
        ){
            statement.setString(1, userId);
            statement.setString(2, userName);
            statement.setString(3, passwordHash);

            var result = statement.executeUpdate();
//            System.out.println(result);
            return result > 0;
        }
    }

    public User findUserByUserName(String uname)throws SQLException{

        String findByName = """
                SELECT * FROM users
                WHERE username = ?;
                """;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(findByName);
        ){
            statement.setString(1, uname);
            var result = statement.executeQuery();

            if(result.next()){
                User user = new User();
                user.setUserId(result.getString("user_id"));
                user.setUserName(result.getString("username"));
                user.setPassword(result.getString("password_hash"));
                return user;
            } else {
                return null;
            }
        }
    }



    public User findUserById(String userId) throws SQLException{
        String findById = """
                SELECT * FROM users
                WHERE user_id = ?;
                """;

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(findById);
        ){
            statement.setString(1, userId);
            var result = statement.executeQuery();



            if(result.next()) {
                User user = new User();
                user.setUserId(result.getString("user_id"));
                user.setUserName(result.getString("username"));
                user.setPassword(result.getString("password_hash"));
                return user;
            } else {
                return null;
            }
        }
    }












}



