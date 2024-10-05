package com.example.demoplswork.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseDAO {
    protected Connection connection;

    public BaseDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:contacts.db");
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to initialize the database and create tables
    private void initializeDatabase() {
        createProfileTable();  // Create the user_profiles table
    }


    // Method to create the user_profiles table
    private void createProfileTable() {
        String query = "CREATE TABLE IF NOT EXISTS user_profiles ("
                + "user_id INTEGER PRIMARY KEY, "
                + "bio TEXT, "
                + "photo TEXT, "
                + "FOREIGN KEY(user_id) REFERENCES users(id)"
                + ")";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
