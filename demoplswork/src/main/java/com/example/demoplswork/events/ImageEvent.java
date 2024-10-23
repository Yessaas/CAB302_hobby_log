package com.example.demoplswork.events;

import com.example.demoplswork.model.LogsDAO;

import java.util.List;

public class ImageEvent extends LogEvent {
    private String imagePath;
    private List<String> comments;
    private List<Integer> likes;

    public ImageEvent(int id, int userId, int logId, String description, List<String> comments, List<Integer> likes) {
        super(id, logId, userId, description, comments, likes);
        this.imagePath = description;
        this.comments = comments;
        this.likes = likes;
    }

    @Override
    public String getDescription() {
        return imagePath;
    }

    @Override
    public List<String> getComments(){
        return comments;
    }

    @Override
    public String setDescription() {
        return "added new media: " + imagePath;
    }

    public String getImagePath(String description) {
        String imagePath = "";

        // Check if the description contains the expected format
        if (description.startsWith("added new media: ")) {
            // Extract the image file name from the description
            imagePath = description.substring("added new media: ".length()).trim();
        } else {
            System.out.println("Invalid description format. No image found.");
        }

        return imagePath;
    }
}


