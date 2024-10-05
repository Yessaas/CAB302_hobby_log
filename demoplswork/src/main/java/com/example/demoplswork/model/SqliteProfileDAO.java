package com.example.demoplswork.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteProfileDAO extends BaseDAO implements ProfileDAO {


    @Override
    public void insertProfile(int userId, String bio, String photo) {
        if (!profileExists(userId)) {  // Check if profile exists
            String query = "INSERT INTO user_profiles (user_id, bio, photo) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                statement.setString(2, bio != null ? bio : "");  // Use default empty string if bio is null
                statement.setString(3, photo != null ? photo : "");  // Use default empty string if photo is null
                statement.executeUpdate();
                System.out.println("New blank profile created for user ID: " + userId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Profile found for user ID: " + userId);
        }
    }
    // Method to check if a profile exists for the given user ID
    private boolean profileExists(int userId) {
        String query = "SELECT COUNT(*) FROM user_profiles WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Returns true if the profile exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void getProfileByUserId(Contact contact, int userId) {
        String query = "SELECT bio, photo FROM user_profiles WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String bio = rs.getString("bio");
                String photo = rs.getString("photo");

                // Update the existing Contact object
                contact.setBio(bio);
                contact.setPhoto(photo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProfile(int userId, String bio, String photo) {
        // Build the query dynamically based on which fields are non-null
        StringBuilder queryBuilder = new StringBuilder("UPDATE user_profiles SET ");
        boolean addComma = false;

        if (bio != null) {
            queryBuilder.append("bio = ?");
            addComma = true;
        }

        if (photo != null) {
            if (addComma) {
                queryBuilder.append(", ");
            }
            queryBuilder.append("photo = ?");
        }

        queryBuilder.append(" WHERE user_id = ?");

        // Prepare the statement and set parameters dynamically
        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
            int paramIndex = 1;

            if (bio != null) {
                statement.setString(paramIndex++, bio);  // Set the bio if it's not null
            }

            if (photo != null) {
                statement.setString(paramIndex++, photo);  // Set the photo if it's not null
            }

            statement.setInt(paramIndex, userId);  // Set the user ID

            // Execute the update only if there are changes
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteProfile(int userId) {
        String query = "DELETE FROM user_profiles WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

