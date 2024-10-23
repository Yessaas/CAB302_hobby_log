package com.example.demoplswork.model;

import java.util.List;
/**
 * IContactDAO interface defines the contract for managing contacts in the database.
 * It has a method to create a new user account.
 * It has a method to authenticate a user.
 * It has a method to update user information.
 * It has a method to delete a user.
 * It has a method to retrieve all contacts.
 */
public interface IContactDAO {

    /**
     * Create a new user account.
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @return true if the account is created successfully, false otherwise.
     */
    boolean createAccount(String firstName, String lastName, String email, String password);

    /**
     * Authenticate a user.
     * @param email
     * @param password
     * @return true if the user is authenticated successfully, false otherwise.
     */
    boolean authenticateUser(String email, String password);

    /**
     * Update user information.
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @return true if the user information is updated successfully, false otherwise.
     */
    boolean updateUser(int id, String firstName, String lastName, String email, String password); //implement later

    /**
     * Delete a user.
     * @param id
     * @return true if the user is deleted successfully, false otherwise.
     */
    boolean deleteUser(int id);
    /**
     * Retrieve all contacts.
     * @return a list of all contacts.
     */
    List<Contact> getAllContacts();
}