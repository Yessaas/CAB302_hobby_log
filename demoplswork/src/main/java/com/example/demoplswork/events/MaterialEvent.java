package com.example.demoplswork.events;

import java.util.List;

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

