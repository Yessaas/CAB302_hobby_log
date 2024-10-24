package com.example.demoplswork.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * The base abstract DAO class for creating and accessing the database connection. Creates database tables if not exist
 */
public abstract class BaseDAO {
    protected static Connection connection;


    public BaseDAO() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:contacts.db");
                initializeDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void setConnection(Connection conn) {
        connection = conn;
    }



    // Method to initialize the database and create tables
    private void initializeDatabase() {
        createProfileTable();  // Create the user_profiles table
        createLogsTable();
        createContactsTable();
        createLogEventsTable();
        createBlogsTable();
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

    // Create the 'log_events' table if it doesn't exist
    public void createLogEventsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS log_events ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "timestamp TEXT NOT NULL, "
                + "description TEXT NOT NULL, "
                + "user_id INTEGER NOT NULL, "
                + "log_id INTEGER NOT NULL, "
                + "event_type TEXT NOT NULL"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e){
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

    public void createBlogsTable() {
        String query = "CREATE TABLE IF NOT EXISTS blogs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "title TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "image_path TEXT," +
                "tag TEXT," +  // Add the 'tag' column
                "FOREIGN KEY (user_id) REFERENCES users(id))";
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
