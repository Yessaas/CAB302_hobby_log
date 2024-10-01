package com.example.demoplswork.model;

import com.example.demoplswork.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteContactDAO implements IContactDAO {

    private static final String DB_URL = "jdbc:sqlite:contacts.db";
    private Connection connection;

    public SqliteContactDAO() {
        connection = SqliteConnection.getInstance();
        createTable(); // Create the users table if it doesn't exist
    }

    // Create the users table in the database
    private void createTable() {
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

    @Override
    public boolean createAccount(String firstName, String lastName, String email, String password) {
        String query = "INSERT INTO users (firstName, lastName, email, password) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, password);  // Password should be hashed in production
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if account creation fails (e.g., email already exists)
        }
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);  // Password should be hashed and compared in production
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  // Returns true if a match is found, otherwise false
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(int id, String firstName, String lastName, String email, String password) {
        String query = "UPDATE users SET firstName = ?, lastName = ?, email = ?, password = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, password);  // Password should be hashed
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM users";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String email = rs.getString("email");
                String password = rs.getString("password"); // You might want to omit password here
                contacts.add(new Contact(firstName, lastName, email, password)); // Adjust according to your Contact class
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public int getUserIDByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = SqliteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1; // No user found with the given email
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }


    // Close the database connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
                connection = null; // Set instance to null after closing
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



}

