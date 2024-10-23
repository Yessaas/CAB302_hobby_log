package com.example.demoplswork.events;

import java.util.List;
/**
 * StartEvent class represents a log event where a new project is started.
 * It extends the LogEvent class to inherit common log event properties.
 * It has fields for the project name, comments, and likes.
 * It has a constructor to initialize these fields along with the inherited fields.
 * It overrides the getDescription method to return the project name.
 * It overrides the getComments method to return the list of comments.
 * It overrides the setDescription method to return a formatted description of the event.
 */
public class StartEvent extends LogEvent {
    private String projectName;
    private List<String> comments;
    private List<Integer> likes;

    public StartEvent(int id, int userId, int logId, String description, List<String> comments, List<Integer> likes) {
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
        return "started logging \"" + projectName + "\"";
    }

}
