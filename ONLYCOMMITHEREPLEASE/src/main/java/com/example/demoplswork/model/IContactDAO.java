package com.example.demoplswork.model;

import com.example.demoplswork.Contact;

import java.util.List;

public interface IContactDAO {

    boolean createAccount(String firstName, String lastName, String email, String password);

    boolean authenticateUser(String email, String password);

    boolean updateUser(int id, String firstName, String lastName, String email, String password); //implement later


    boolean deleteUser(int id);

    List<Contact> getAllContacts();
}
