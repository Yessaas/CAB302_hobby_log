package com.example.demoplswork.events;

import java.util.List;
/**
 * MaterialEvent class represents a log event where a new material is added.
 * It extends the LogEvent class to inherit common log event properties.
 * It has fields for the material name, comments, and likes.
 * It has a constructor to initialize these fields along with the inherited fields.
 * It overrides the getDescription method to return the material name.
 * It overrides the getComments method to return the list of comments.
 * It overrides the setDescription method to return a formatted description of the event.
 */
public class MaterialEvent extends LogEvent {
    private String materialName;
    private List<String> comments;
    private List<Integer> likes;

    public MaterialEvent(int id, int userId, int logId, String description, List<String> comments, List<Integer> likes) {
        super(id, logId, userId, description, comments, likes);
        this.materialName = description;
        this.comments = comments;
        this.likes = likes;
    }

    @Override
    public String getDescription() {
        return materialName;
    }

    @Override
    public List<String> getComments(){
        return comments;
    }

    @Override
    public String setDescription() {
        return "added new material: \"" + materialName + "\"";
    }
}

