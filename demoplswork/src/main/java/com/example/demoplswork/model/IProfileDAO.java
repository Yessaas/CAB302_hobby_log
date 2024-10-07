package com.example.demoplswork.model;

public interface IProfileDAO {
    void insertProfile(int userId, String bio, String photo);
    void getProfileByUserId(Contact contact, int userId);
    void updateProfile(int userId, String bio, String photo);

}

