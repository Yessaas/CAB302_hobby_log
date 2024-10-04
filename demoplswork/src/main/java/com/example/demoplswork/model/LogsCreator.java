package com.example.demoplswork.model;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class LogsCreator {
    private ImageView mediaView;
    private Label mediaLabel;

    public LogsCreator(ImageView mediaView, Label mediaLabel) {
        this.mediaView = mediaView;
        this.mediaLabel = mediaLabel;
    }

    // Method to select and display an image or video file
    public void selectMediaFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String filePath = selectedFile.toURI().toString();
            if (isImageFile(selectedFile)) {
                displayImage(filePath);
            } else if (isVideoFile(selectedFile)) {
                displayVideo(filePath);
            } else {
                mediaLabel.setText("Unsupported file type.");
            }
        }
    }

    // Method to display image files
    private void displayImage(String filePath) {
        Image image = new Image(filePath);
        mediaView.setImage(image);
        mediaLabel.setText("Image loaded: " + filePath);
    }

    // Method to handle video display (stub for video implementation)
    private void displayVideo(String filePath) {
        // Video playback logic should be implemented here
        mediaLabel.setText("Video loaded: " + filePath);
        // Since ImageView cannot display videos, you may need a MediaView for actual video playback
    }

    // Utility methods to check file types
    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".gif");
    }

    private boolean isVideoFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".mkv");
    }

    // Method to remove the selected media
    public void removeMediaFile() {
        mediaView.setImage(null);
        mediaLabel.setText("No media selected.");
    }
}

