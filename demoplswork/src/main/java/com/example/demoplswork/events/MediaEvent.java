package com.example.demoplswork.events;

import java.util.List;

public class MediaEvent extends LogEvent {
    private String description;

    public MediaEvent(int id, int userId, int logId, String mediaName, List<String> tags, List<Integer> likes) {
        super(id, logId, userId, mediaName, tags, likes);
        this.description = mediaName; 
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public String setDescription() {
        return description;
    }
}
