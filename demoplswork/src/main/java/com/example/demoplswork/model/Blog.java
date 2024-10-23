package com.example.demoplswork.model;

public class Blog {
    private int id;
    private int userId;
    private String title;
    private String description;
    private String imagePath;
    private String tag;  // New field for the tag

    public Blog(int userId, String title, String description, String imagePath, String tag) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.tag = tag;
    }

    public Blog(int id, int userId, String title, String description, String imagePath, String tag) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.tag = tag;
    }

    // Getter for tag
    public String getTag() {
        return tag;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}

