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

    int insertLog(int userId, Logs log) throws SQLException;

    void addToDoItem(int logId, String toDoItem, boolean isChecked) throws SQLException;

    void addImage(int logId, String imagePath) throws SQLException;

    void addMaterial(int logId, Material material);

    List<Object[]> getLogsForUser(int userId);

    double updateToDoItemStatus(int logId, String task, boolean isChecked);


    void updateLogName(int logId, String newLogName) throws SQLException;

    void deleteLog(int logId);
}

