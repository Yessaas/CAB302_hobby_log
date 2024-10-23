package com.example.demoplswork.model;

import javafx.util.Pair;

import java.util.List;
/**
 * Logs class represents a log with a log name, list of to-do items, list of image paths, and list of materials.
 * It has fields for the ID, log name, to-do items, progress, images, and materials.
 * It has a constructor to initialize these fields.
 * It has getter and setter methods to retrieve and update the values of these fields.
 * It has a method to update the status of a to-do item.
 * It has a method to add a material to the materials list.
 */
public class Logs {
    private int id;
    private String logName;
    private List<Pair<String, Boolean>> toDoItems;
    private double progress; // Progress (in percentage)
    private List<String> images; // List of image file paths
    private List<Material> materials; // List of materials

    /**
     * Constructor for Logs class.
     */
    // Constructor
    public Logs(String logName, List<Pair<String, Boolean>> toDoItems, List<String> images, List<Material> materials) {
        this.logName = logName;
        this.toDoItems = toDoItems;
        this.images = images;
        this.materials = materials;
    }

    /**
     * Initializes the completion status of to-do items.
     */
    // Initialize all to-do items as not completed (false)
    private List<Boolean> initializeCompletionStatus(int size) {
        List<Boolean> completionStatus = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            completionStatus.add(false);
        }
        return completionStatus;
    }

    /**
     * Updates the status of a to-do item.
     */
    // Method to update the status of a to-do item
    public void updateToDoItemStatus(String task, boolean isChecked) {
        for (Pair<String, Boolean> toDoItem : toDoItems) {
            if (toDoItem.getKey().equals(task)) {
                // Update the checked state
                toDoItem = new Pair<>(task, isChecked);
                break;
            }
        }
    }

    /**
     * Adds a material to the materials list.
     */
    // Method to add a material to the materials list
    public void addMaterial(Material material) {
        if (material != null) {
            materials.add(material); // Add the material to the list
        } else {
            System.err.println("Cannot add null material.");
        }
    }

    /**
     * Adds a to-do item to the to-do items list.
     */
    public List<Pair<String, Boolean>> getToDoItems() {
        return toDoItems;
    }
    /**
     * Sets the to-do items list.
     */
    public void setToDoItems(List<Pair<String, Boolean>> toDoItems) {
        this.toDoItems = toDoItems;
    }

    /**
     * Gets the name of the log.
     */
    // Getters
    public String getLogName() {
        return logName;
    }


    /**
     * Gets the list of image paths.
     */
    public List<String> getImages() {
        return images;
    }

    /**
     * Gets the list of materials.
     */
    public List<Material> getMaterials() {
        return materials;
    }
    /**
     * Gets the progress of the log.
     */
    public double getProgress() {
        return progress;
    }

    /**
     * Sets the name of the log.
     */
    // Setters
    public void setLogName(String logName) {
        this.logName = logName;
    }


    /**
     * Sets the list of image paths.
     */
    public void setImages(List<String> images) {
        this.images = images;
    }

    /**
     * Sets the list of materials.
     */
    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
    /**
     * Sets the progress of the log.
     */
    public void setProgress(double progress) {
        this.progress = progress;
    }
}
