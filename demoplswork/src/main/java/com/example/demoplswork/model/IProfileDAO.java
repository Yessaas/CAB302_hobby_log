package com.example.demoplswork.model;

import java.sql.SQLException;
/**
 * IProfileDAO interface defines the contract for managing profiles in the database.
 * It has a method to insert a profile for a user.
 * It has a method to retrieve a profile by user ID.
 * It has a method to update a profile.
 */
public interface IProfileDAO {
    void insertProfile(int userId, String bio, String photo) throws SQLException;
    void getProfileByUserId(Contact contact, int userId);
    void updateProfile(int userId, String bio, String photo) throws SQLException;

}

