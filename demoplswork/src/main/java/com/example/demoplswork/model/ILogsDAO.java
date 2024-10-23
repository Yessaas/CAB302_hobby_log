package com.example.demoplswork.model;

import java.sql.SQLException;
import java.util.List;
/**
 * ILogsDAO interface defines the contract for managing logs in the database.
 * It has a method to insert a log for a user.
 * It has a method to add a to-do item to a log.
 * It has a method to add an image to a log.
 * It has a method to add material to a log.
 * It has a method to retrieve logs for a user.
 * It has a method to update the status of a to-do item in a log.
 * It has a method to update the name of a log.
 * It has a method to delete a log.
 */
public interface ILogsDAO {

    /**
     * Inserts a log for a user.
     * @param userId The user id.
     * @param log The log to be inserted.
     * @return The id of the inserted log.
     * @throws SQLException
     */
    int insertLog(int userId, Logs log) throws SQLException;

    /**
     * Adds a to-do item to a log.
     * @param logId The id of the log.
     * @param toDoItem The to-do item to be added.
     * @param isChecked The status of the to-do item.
     * @throws SQLException
     */
    void addToDoItem(int logId, String toDoItem, boolean isChecked) throws SQLException;
    /**
     * Adds an image to a log.
     * @param logId The id of the log.
     * @param imagePath The path of the image to be added.
     * @throws SQLException
     */
    void addImage(int logId, String imagePath) throws SQLException;
    /**
     * Adds material to a log.
     * @param logId The id of the log.
     * @param material The material to be added.
     * @throws SQLException
     */
    void addMaterial(int logId, Material material);
    /**
     * Retrieves logs for a user.
     * @param userId The user id.
     * @return A list of logs for the user.
     */
    List<Object[]> getLogsForUser(int userId);
    /**
     * Updates the status of a to-do item in a log.
     * @param logId The id of the log.
     * @param task The to-do item.
     * @param isChecked The status of the to-do item.
     * @return The number of rows affected.
     */
    double updateToDoItemStatus(int logId, String task, boolean isChecked);

    /**
     * Updates the name of a log.
     * @param logId The id of the log.
     * @param newLogName The new name of the log.
     * @throws SQLException
     */
    void updateLogName(int logId, String newLogName) throws SQLException;
    /**
     * Deletes a log.
     * @param logId The id of the log.
     */
    void deleteLog(int logId);
}

