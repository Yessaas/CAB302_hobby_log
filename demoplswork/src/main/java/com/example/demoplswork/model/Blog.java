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

    /**
     * Constructor to initialize the fields of the Blog object.
     * @param intro the introduction of the blog post
     * @param description the description of the blog post
     * @param category the category of the blog post
     * @param imagePath the image path of the blog post
     */
    public Blog(String intro, String description, String category, String imagePath) {
        this.intro = intro;
        this.description = description;
        this.category = category;
        this.imagePath = imagePath;
    }

    /**
     * Getter method to retrieve the introduction of the blog post.
     * @return the introduction of the blog post
     */
    public String getIntro() {
        return intro;
    }

    /**
     * Getter method to retrieve the description of the blog post.
     * @return the description of the blog post
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter method to retrieve the category of the blog post.
     * @return the category of the blog post
     */
    public String getCategory() {
        return category;
    }

    /**
     * Getter method to retrieve the image path of the blog post.
     * @return the image path of the blog post
     */
    public String getImagePath() {
        return imagePath;
    }
}
