package com.example.demoplswork.events;

import java.util.List;

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

