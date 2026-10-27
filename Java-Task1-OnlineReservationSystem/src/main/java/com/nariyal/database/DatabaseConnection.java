
package com.nariyal.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatabaseConnection {

    private static final Path DB_PATH =
            Path.of("reservation.db")
                    .toAbsolutePath()
                    .normalize();

    private static final String URL =
            "jdbc:sqlite:" + DB_PATH;

    public static Connection getConnection() throws SQLException {
        try {
            Files.createDirectories(DB_PATH.getParent());
        } catch (java.io.IOException e) {
            throw new SQLException("Could not create database directory", e);
        }

        Connection connection = DriverManager.getConnection(URL);

        try (var statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            connection.close();
            throw e;
        }

        return connection;
    }
}
