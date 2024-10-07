package com.example.demoplswork.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseDAO {
    public Connection connection;

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
        createLogsTable();
        createContactsTable();
    }


    // Method to create the user_profiles table
    public void createProfileTable() {
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

    public void createLogsTable() {
        String query = "CREATE TABLE IF NOT EXISTS logs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "log_name TEXT NOT NULL," +
                "to_do_items TEXT," +
                "images TEXT," +
                "progress REAL," +
                "materials TEXT," +
                "FOREIGN KEY (user_id) REFERENCES users(id))";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create the users table in the database
    private void createContactsTable() {
        String query = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "firstName VARCHAR NOT NULL, "
                + "lastName VARCHAR NOT NULL, "
                + "email VARCHAR NOT NULL UNIQUE, "
                + "password VARCHAR NOT NULL"
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
