package com.example.demoplswork.events;

import java.util.List;
/**
 * MediaEvent class represents a log event where media is added.
 * It extends the LogEvent class to inherit common log event properties.
 * It has a field for the description of the media.
 * It has a constructor to initialize the media name, tags, and likes along with the inherited fields.
 * It overrides the getDescription method to return the media name.
 * It overrides the setDescription method to return the media name.
 */
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
