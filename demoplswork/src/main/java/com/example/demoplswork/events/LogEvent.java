package com.example.demoplswork.events;

import com.example.demoplswork.model.LogsDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * LogEvent class is an abstract class representing a log event.
 * It has fields for the timestamp, user ID, log ID, comments, likes, and description.
 * It has a constructor to initialize these fields.
 * It has getter and setter methods for the timestamp, user ID, log ID, comments, likes, and ID.
 * It has an abstract method getDescription to be implemented by subclasses.
 * It has a toString method to return a string representation of the log event.
 * It has a getLogName method to get the log name by log ID.
 * It has a getCommentsFromDatabase method to retrieve comments for the event from the database.
 */
public abstract class LogEvent {
    protected String timestamp;
    protected int userId;
    protected int logId;
    private List<String> comments; // New field to store comments
    private List<Integer> likes; // New field to store user IDs who liked the event
    private LogsDAO logsDAO;
    private LogEventDAO logEventDAO;
    private int id;
    private String description;


    public LogEvent(int id, int logId, int userId, String description, List<String> comments, List<Integer> likes) {
        this.id = id;
        this.logId = logId;
        this.userId = userId;
        this.description = description;
        this.comments = comments != null ? comments : new ArrayList<>();
        this.likes = likes != null ? likes : new ArrayList<>();
        this.timestamp = java.time.LocalDate.now().toString();
        this.logsDAO = new LogsDAO();
        try {
            this.logEventDAO = new LogEventDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public int getLogId() {
        return logId;
    }

    // Abstract method for event description, to be implemented by subclasses
    public abstract String getDescription();

    @Override
    public String toString() {
        return timestamp + " - User" + userId + " " + setDescription() + " (Log ID: " + logId + ")";
    }

    public abstract String setDescription();


    public String getLogName(int logID) {
        return logsDAO.getLogNameById(logID);
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<Integer> getLikes() {
        return likes;
    }

    public void setLikes(List<Integer> likes) {
        this.likes = likes;
    }

    public int getId (){
        return id;
    }

    public void setId (int id){
        this.id = id;
    }
    
    public List<String> getCommentsFromDatabase() {
        return new ArrayList<>(logEventDAO.getCommentsForEvent(this.id));
    }


}
