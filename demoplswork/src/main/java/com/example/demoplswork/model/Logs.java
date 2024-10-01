package com.example.demoplswork.model;

import javafx.util.Pair;

import java.util.List;

public class Logs {
    private int id;
    private String logName;
    private List<Pair<String, Boolean>> toDoItems;
    private double progress; // Progress (in percentage)
    private List<String> images; // List of image file paths
    private List<Material> materials; // List of materials

    // Constructor
    public Logs(String logName, List<Pair<String, Boolean>> toDoItems, List<String> images, List<Material> materials) {
        this.logName = logName;
        this.toDoItems = toDoItems;
        this.images = images;
        this.materials = materials;
    }

    // Initialize all to-do items as not completed (false)
    private List<Boolean> initializeCompletionStatus(int size) {
        List<Boolean> completionStatus = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            completionStatus.add(false);
        }
        return completionStatus;
    }


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

    public List<Pair<String, Boolean>> getToDoItems() {
        return toDoItems;
    }

    public void setToDoItems(List<Pair<String, Boolean>> toDoItems) {
        this.toDoItems = toDoItems;
    }


    // Getters
    public String getLogName() {
        return logName;
    }



    public List<String> getImages() {
        return images;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public double getProgress() {
        return progress;
    }

    // Setters
    public void setLogName(String logName) {
        this.logName = logName;
    }



    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
