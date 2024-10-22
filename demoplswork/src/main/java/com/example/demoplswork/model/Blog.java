package com.example.demoplswork.model;

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
