package com.example.demoplswork.model;

import java.sql.SQLException;

public interface IProfileDAO {
    void insertProfile(int userId, String bio, String photo) throws SQLException;
    void getProfileByUserId(Contact contact, int userId);
    void updateProfile(int userId, String bio, String photo) throws SQLException;

}

