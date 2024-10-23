package com.example.demoplswork.model;
/**
 * Blog class represents a blog post with an introduction, description, category, and image path.
 * It has fields for the intro, description, category, and image path.
 * It has a constructor to initialize these fields.
 * It has getter methods to retrieve the values of these fields.
 */
public class Blog {
    private String intro;
    private String description;
    private String category;
    private String imagePath;

    public Blog(String intro, String description, String category, String imagePath) {
        this.intro = intro;
        this.description = description;
        this.category = category;
        this.imagePath = imagePath;
    }

    public String getIntro() {
        return intro;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImagePath() {
        return imagePath;
    }
}
