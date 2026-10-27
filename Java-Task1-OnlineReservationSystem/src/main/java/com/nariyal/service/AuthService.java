package com.nariyal.service;

import com.nariyal.dao.UserDAO;
import com.nariyal.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.UUID;

public class AuthService {


    public User register(String username, String password) throws SQLException {

        if (username != null && password != null && !username.isBlank() && !password.isBlank()) {
            UserDAO userDAO = new UserDAO();
            if (userDAO.findUserByUserName(username) == null) {
                String uniqueId = UUID.randomUUID().toString();
                User user = new User();
                user.setUserName(username);
                user.setUserId(uniqueId);
                user.setPassword(PasswordHasher.hashPassword(password));
                var res = userDAO.saveUser(user);
                if (res) {
                    return user;
                } else {
                    throw new SQLException("Failed to register user");
                }
            } else {
                throw new IllegalArgumentException("Username already registered");
            }

        } else {
            throw new IllegalArgumentException("Username and password cannot be blank");
        }

    }


    public User login(String username, String password) throws SQLException {

        if (username != null && password != null && !username.isBlank() && !password.isBlank()) {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.findUserByUserName(username);
            if (user != null) {

                if (PasswordHasher.verifyPassword(password, user.getPassword())) {
                    return user;
                } else {
                    throw new IllegalArgumentException("Wrong Password");
                }

            } else {
                throw new IllegalArgumentException("Username not found in the database");
            }
        } else {
            throw new IllegalArgumentException("Username and password cannot be blank");
        }
    }
}
