package com.example.demoplswork.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {
    private static Connection instance = null;



    public SqliteConnection() {
        String url = "jdbc:sqlite:contacts.db";
        try {
            instance = DriverManager.getConnection(url);
            System.out.println("Database connection opened.");
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        } else {
            try {
                // Check if the connection is still open
                if (instance.isClosed()) {
                    System.out.println("Database connection was closed. Reopening connection.");
                    new SqliteConnection();
                } else {
                    System.out.println("Database connection is still open.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static Connection getConnection() {
        return getInstance();
    }
}
