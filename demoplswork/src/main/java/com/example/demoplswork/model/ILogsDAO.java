package com.example.demoplswork.model;

import java.util.List;

public interface ILogsDAO {

    int insertLog(int userId, Logs log);

    void addToDoItem(int logId, String toDoItem, boolean isChecked);

    void addImage(int logId, String imagePath);

    void addMaterial(int logId, Material material);

    List<Object[]> getLogsForUser(int userId);

    double updateToDoItemStatus(int logId, String task, boolean isChecked);


}

