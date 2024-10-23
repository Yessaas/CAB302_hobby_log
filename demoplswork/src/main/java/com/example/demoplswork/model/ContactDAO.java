package com.example.demoplswork.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * ContactDAO class is responsible for managing contacts in the database for login and register pages.
 * It extends the BaseDAO class and implements the IContactDAO interface.
 * It has a method to create a new user account.
 * It has a method to authenticate a user.
 * It has a method to update user information.
 * It has a method to delete a user.
 * It has a method to retrieve all contacts.
 * It has a method to get a user ID by email.
 * It has a method to get a contact by user ID.
 */
public class ContactDAO extends BaseDAO implements IContactDAO  {
    /**
     * Constructor for the ContactDAO class.
     * It calls the constructor of the BaseDAO class to establish a connection to the database.
     */
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
    /**
     * Method to authenticate a user.
     * It takes an email and password as input and checks if the user exists in the database.
     * It returns true if the user is authenticated, otherwise false.
     */
    @Override
    public boolean authenticateUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  // Returns true if a match is found, otherwise false
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Method to update user information.
     * It takes the user ID, first name, last name, email, and password as input.
     * It updates the user information in the database.
     * It returns true if the update is successful, otherwise false.
     */
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
    /**
     * Method to delete a user.
     * It takes the user ID as input and deletes the user from the database.
     * It returns true if the deletion is successful, otherwise false.
     */
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
    /**
     * Method to retrieve all contacts.
     * It returns a list of all contacts in the database.
     */
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
    /**
     * Method to get a user ID by email.
     * It takes an email as input and returns the user ID.
     * If no user is found with the given email, it returns -1.
     */
    public int getUserIDByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

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

    /**
     * Method to get a contact by user ID.
     * It takes a user ID as input and returns the contact information.
     * If no contact is found with the given user ID, it returns null.
     */
    public Contact getContactById(int userId) {
        Contact contact = null;
        String query = "SELECT firstName, lastName FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                contact = new Contact(firstName, lastName, " ", " ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contact;
    }



}

