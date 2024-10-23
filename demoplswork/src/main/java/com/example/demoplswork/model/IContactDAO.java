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


    boolean createAccount(String firstName, String lastName, String email, String password);

    boolean authenticateUser(String email, String password);

    boolean updateUser(int id, String firstName, String lastName, String email, String password); //implement later


    boolean deleteUser(int id);

    List<Contact> getAllContacts();
}