package com.example.demoplswork;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LogsCreatorView {
    private HelloApplication app;

    @FXML
    private ImageView imageView;

    @FXML
    private Label videoLabel;

    @FXML
    public void setApplication(HelloApplication app) {
        this.app = app;
    }

    @FXML
    public void goToHome() throws IOException {
        if (app != null) {
            app.showHomeView();  // Navigate to Home view
        }
    }

    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();
        }
    }

    @FXML
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();  // Navigate to Explore view
        }
    }

    @FXML
    public void addStep() throws IOException {

    }

    @FXML
    public void removeStep() throws IOException {

    }

    @FXML
    public void addMaterial() throws IOException {

    }

    @FXML
    public void removeMaterial() throws IOException {

    }

    @FXML
    public void createLog() throws IOException {

    }

    @FXML
    public void RemoveMedia() throws IOException {
        imageView.setImage(null);
        imageView.setVisible(false);

        videoLabel.setText("No video selected");
    }

    @FXML
    public void uploadMedia() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Media");
        File file = fileChooser.showOpenDialog(imageView.getScene().getWindow());

        if (file != null) {
            String filePath = file.toURI().toString();
            String fileName = file.getName();
            if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                imageView.setImage(new Image(filePath));
                imageView.setVisible(true);
                videoLabel.setText("No video selected");
            } else if (fileName.endsWith(".mp4") || fileName.endsWith(".avi")) {
                videoLabel.setText(fileName);
                imageView.setVisible(false);
            } else {
                videoLabel.setText("Unsupported file type");
            }
        }
    }
}
