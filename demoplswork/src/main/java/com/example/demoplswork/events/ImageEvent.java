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

    public String getImagePath() {
        LogsDAO logDAO = new LogsDAO();
        String firstImagePath;

        // Call the method to retrieve the images
        System.out.println(logId);
        List<String> images = logDAO.getImagesForLog(logId);

        // Display or use the retrieved images
        if (!images.isEmpty()) {
            for (String imageName : images) {
                System.out.println("Image: " + imageName);
            }
        } else {
            System.out.println("No images found for the log with ID: " + logId);
        }

        firstImagePath = images.getFirst();
        return firstImagePath;
    }
}


