package com.example.demoplswork.model;

public interface ProfileDAO {
    void insertProfile(int userId, String bio, String photo);
    void getProfileByUserId(Contact contact, int userId);
    void updateProfile(int userId, String bio, String photo);
    void deleteProfile(int userId);
}

