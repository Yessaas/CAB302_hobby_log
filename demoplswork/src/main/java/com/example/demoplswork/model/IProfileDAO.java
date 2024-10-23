package com.example.demoplswork.model;

import java.sql.SQLException;
/**
 * IProfileDAO interface defines the contract for managing profiles in the database.
 * It has a method to insert a profile for a user.
 * It has a method to retrieve a profile by user ID.
 * It has a method to update a profile.
 */
public interface IProfileDAO {
    /**
     * Insert a profile for a user.
     * @param userId The user ID.
     * @param bio The bio of the user.
     * @param photo The photo of the user.
     * @throws SQLException
     */
    void insertProfile(int userId, String bio, String photo) throws SQLException;

    /**
     * Retrieve a profile by user ID.
     * @param contact The contact object to store the profile.
     * @param userId The user ID.
     */
    void getProfileByUserId(Contact contact, int userId);

    /**
     * Update a profile.
     * @param userId The user ID.
     * @param bio The bio of the user.
     * @param photo The photo of the user.
     * @throws SQLException
     */
    void updateProfile(int userId, String bio, String photo) throws SQLException;

}

