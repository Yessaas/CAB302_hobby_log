package com.example.demoplswork.model;

import java.sql.SQLException;
import java.util.List;

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

