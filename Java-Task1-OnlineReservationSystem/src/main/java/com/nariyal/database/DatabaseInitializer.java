
package com.nariyal.database;

import java.sql.*;

public class DatabaseInitializer {

    public static void initialize() throws SQLException {

        String usersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id TEXT PRIMARY KEY,
                    username TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL
                )
                """;

        String trainsTable = """
                CREATE TABLE IF NOT EXISTS trains (
                    train_number INTEGER PRIMARY KEY,
                    train_name TEXT NOT NULL,
                    source TEXT NOT NULL,
                    destination TEXT NOT NULL,
                    start_time TEXT,
                    end_time TEXT
                )
                """;

        String reservationsTable = """
                CREATE TABLE IF NOT EXISTS reservations (
                    pnr INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id TEXT NOT NULL,
                    passenger_name TEXT NOT NULL,
                    train_number INTEGER NOT NULL,
                    train_class TEXT NOT NULL,
                    journey_date TEXT NOT NULL,
                    source TEXT NOT NULL,
                    destination TEXT NOT NULL,
                    departure_time TEXT NOT NULL,
                    arrival_time TEXT NOT NULL,

                    FOREIGN KEY (user_id)
                        REFERENCES users(user_id),

                    FOREIGN KEY (train_number)
                        REFERENCES trains(train_number)
                )
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                Statement statement =
                        connection.createStatement()
        ) {
            statement.execute(usersTable);
            statement.execute(trainsTable);
            statement.execute(reservationsTable);
        }
    }



    public static void insertSampleTrains() throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM trains";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement checkStatement = connection.createStatement();
             ResultSet resultSet = checkStatement.executeQuery(checkSql)) {

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return; // Sample trains have already been added
            }
        }

        String sql = """
            INSERT OR IGNORE INTO trains
            (train_number, train_name, source, destination, start_time, end_time)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        Object[][] trains = {
                {12001, "Meerut Mumbai Superfast Express", "Meerut", "Mumbai", "06:00", "14:00"},
                {12002, "Mumbai Meerut Superfast Express", "Mumbai", "Meerut", "16:30", "08:00"},
                {12003, "Meerut Haridwar Intercity Express", "Meerut", "Haridwar", "07:00", "12:00"},
                {12004, "Haridwar Meerut Intercity Express", "Haridwar", "Meerut", "08:00", "14:00"},
                {12005, "Meerut Ghaziabad Passenger Express", "Meerut", "Ghaziabad", "20:00", "22:00"},
                {12006, "Ghaziabad Meerut Passenger Express", "Ghaziabad", "Meerut", "20:00", "22:00"},
                {12007, "Meerut Bengaluru Express", "Meerut", "Bengaluru", "18:00", "10:00"},
                {12008, "Bengaluru Meerut Express", "Bengaluru", "Meerut", "18:00", "10:00"},
                {12009, "Meerut Delhi Intercity Express", "Meerut", "Delhi", "09:00", "12:00"},
                {12010, "Delhi Meerut Intercity Express", "Delhi", "Meerut", "17:00", "20:00"}
        };

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Object[] train : trains) {
                statement.setInt(1, (Integer) train[0]);
                statement.setString(2, (String) train[1]);
                statement.setString(3, (String) train[2]);
                statement.setString(4, (String) train[3]);
                statement.setString(5, (String) train[4]);
                statement.setString(6, (String) train[5]);
                statement.executeUpdate();
            }
        }
    }



    public static void verifyColumns() throws SQLException {

        String[] tables = {"users", "trains", "reservations"};

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            for (String table : tables) {
                String sql = "PRAGMA table_info(" + table + ")";

                try (var result = statement.executeQuery(sql)) {
                    System.out.println("\nColumns in table: " + table);

                    while (result.next()) {
                        System.out.println(
                                "Column: " + result.getString("name")
                                        + " | Type: " + result.getString("type")
                                        + " | Not Null: " + result.getInt("notnull")
                                        + " | Primary Key: " + result.getInt("pk")
                        );
                    }
                }
            }
        }
    }

    public static void dropUsersTable() throws SQLException {
        String sql = "DROP TABLE IF EXISTS users";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    public static void dropTrainsTable() throws SQLException {
        String sql = "DROP TABLE IF EXISTS trains";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    public static void dropReservationsTable() throws SQLException {
        String sql = "DROP TABLE IF EXISTS reservations";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

}

