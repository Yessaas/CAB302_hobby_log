package com.example.demoplswork.events;

import java.util.List;

public class EndEvent extends LogEvent {
    private String projectName;
    private List<String> comments;
    private List<Integer> likes;

    public EndEvent(int id, int userId, int logId, String description, List<String> comments, List<Integer> likes) {
        super(id, logId, userId, description, comments, likes);
        this.projectName = description;
        this.comments = comments;
        this.likes = likes;
    }

    @Override
    public List<String> getComments(){
        return comments;
    }

    @Override
    public String getDescription() {
        return projectName;
    }
    @Override
    public String setDescription() {
        return "completed the log! \"" + projectName + "\"";
    }

}

