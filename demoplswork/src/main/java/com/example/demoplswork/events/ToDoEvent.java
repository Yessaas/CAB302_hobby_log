package com.example.demoplswork.events;

import java.util.List;
/**
 * ToDoEvent class represents a log event where a to-do item is completed.
 * It extends the LogEvent class to inherit common log event properties.
 * It has fields for the to-do item, comments, and likes.
 * It has a constructor to initialize these fields along with the inherited fields.
 * It overrides the getDescription method to return the to-do item.
 * It overrides the getComments method to return the list of comments.
 * It overrides the setDescription method to return a formatted description of the event.
 */
public class ToDoEvent extends LogEvent {
    private String toDoItem;
    private List<String> comments;
    private List<Integer> likes;

    public ToDoEvent(int id, int userId, int logId, String description, List<String> comments, List<Integer> likes) {
        super(id, logId, userId, description, comments, likes);
        this.toDoItem = description;
        this.comments = comments;
        this.likes = likes;
    }

    @Override
    public List<String> getComments(){
        return comments;
    }

    @Override
    public String getDescription() {
        return toDoItem;
    }

    @Override
    public String setDescription() {
        return "completed To-Do \"" + toDoItem + "\"";
    }
}

