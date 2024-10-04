package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.model.Logs;
import com.example.demoplswork.model.LogsDAO;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class HomeView {
    private HelloApplication app;

    private ContextMenu accountMenu;

    private LogsDAO logsDAO;

    @FXML
    private Button accountButton;

    @FXML
    private ImageView featuredLogImageView;
    @FXML
    private Label featuredLogTitle;
    @FXML
    private ProgressBar featuredLogProgress;
    @FXML
    private CheckBox featuredLogToDo;
    @FXML
    private Button featuredLogButton;



    public void setApplication(HelloApplication app) {
        this.app = app;
        loadLogsForUser();
    }

    public HomeView() throws SQLException {
        logsDAO = new LogsDAO();
    }

    @FXML
    public void initialize() {
        // Create the dropdown menu
        accountMenu = new ContextMenu();

        MenuItem viewProfile = new MenuItem("View Profile");
        viewProfile.setOnAction(event -> {
            try {
                goToAccount();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        MenuItem logout = new MenuItem("Log Out");
        logout.setOnAction(event -> onLogout());

        accountMenu.getItems().addAll(viewProfile, logout);


    }

    private void loadLogsForUser(){

        int userID = app.getLoggedInUserID();

        // Fetch logs from the database for the specific user
        List<Object[]> logs = logsDAO.getLogsForUser(userID);



        if (!logs.isEmpty()) {
            // Pick a random log
            Object[] randomLog = getRandomLog(logs);



            // Display the selected log in the featured log section
            displayFeaturedLog(randomLog);
        }
        else {
            // If there are no logs, show a message indicating no logs exist
            featuredLogTitle.setText("You don't have any logs.");
            featuredLogProgress.setProgress(0.0); // Set progress to 0
            featuredLogImageView.setScaleX(0.4); // Scale to 50% of the original size
            featuredLogImageView.setScaleY(0.4); // Scale to 50% of the original size
            featuredLogImageView.setPreserveRatio(true);

            // Update the button text and action to navigate to the LogsView
            featuredLogButton.setText("My Logs");
            featuredLogButton.setOnAction(event -> {
                try {
                    goToLogs(); // Navigate to LogsView
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;

        }
    }

    // Method to get a random log
    private Object[] getRandomLog(List<Object[]> logs) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(logs.size());
        return logs.get(randomIndex);
    }

    private String getFirstIncompleteToDoItem(Logs log) {
        List<Pair<String, Boolean>> toDoItems = log.getToDoItems(); // Assuming you have this method
        for (Pair<String, Boolean> item : toDoItems) {
            if (!item.getValue()) { // Check if it's not completed
                return item.getKey(); // Return the first incomplete item
            }
        }
        return null; // or handle the case where all items are completed
    }

    // Method to display the log in the featured log section
    private void displayFeaturedLog(Object[] logData) {
        int logID = (int) logData[0]; // Extract the log ID
        Logs log = (Logs) logData[1]; // Extract the Logs object
        // Set log title
        featuredLogTitle.setText(log.getLogName());

        // Set log progress
        featuredLogProgress.setProgress(log.getProgress() / 100.0);

        // Set the image
        String imageName = log.getImages().getFirst();
        String relativePath = "/images/" + imageName;
        InputStream imageStream = getClass().getResourceAsStream(relativePath);

        if (imageStream != null) {
            Image image = new Image(imageStream);
            featuredLogImageView.setImage(image);
        }
        else{
            featuredLogImageView.setImage(null);
        }
        // Find the first incomplete to-do item
        String incompleteToDo = getFirstIncompleteToDoItem(log);
        if (incompleteToDo != null) {
            featuredLogToDo.setText(incompleteToDo);

        }

        // Set up the "View Log" button
        featuredLogButton.setOnAction(event -> {
            try {
                goToUpdateLogs(logID, log);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();  // Navigate to Explore view
        }
    }

    @FXML
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();
        }
    }

    @FXML
    private void showAccountMenu(ActionEvent event) {
        accountMenu.show(accountButton, Side.BOTTOM, 0, 0);
    }

    @FXML
    public void goToAccount() throws IOException {
        if (app != null) {
            app.showAccountView();
        }
    }
    @FXML
    private void onLogout() {
        try {
            app.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToUpdateLogs(int id, Logs log) throws IOException {
        if (app != null) {
            app.showLogsUpdateView(id, log);  // Navigate to Explore view
        }
    }

}