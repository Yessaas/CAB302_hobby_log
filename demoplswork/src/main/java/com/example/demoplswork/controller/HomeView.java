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

/**
 * HomeView class is the controller for the Home view.
 * It handles the logic for the Home page of the application.
 * It has methods to initialize the view, load logs for the user, and handle user interactions.
 * It has a setApplication method to set the application instance.
 * It has a goToExplore method to navigate to the Explore view.
 * It has a goToLogs method to navigate to the Logs view.
 * It has a showAccountMenu method to display the account menu.
 * It has a goToAccount method to navigate to the Account view.
 * It has an onLogout method to log out the user.
 * It has a goToUpdateLogs method to navigate to the Update Logs view.
 * It has a loadLogsForUser method to load logs for the logged-in user.
 * It has a getRandomLog method to get a random log from the list of logs.
 * It has a getFirstIncompleteToDoItem method to get the first incomplete to-do item from the log.
 * It has a displayFeaturedLog method to display the log in the featured log section.
 */
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

    @FXML
    private ListView<String> listView1;  // Total Spent
    @FXML
    private ListView<String> listView2;  // Tasks Completed
    @FXML
    private ListView<String> listView3;  // Projects Completed
    @FXML
    private ListView<String> listView4;  // Materials Used
    @FXML
    private ListView<String> listView5;  // Likes
    @FXML
    private ListView<String> listView6;  // Followers

    /**
     * Sets the application instance.
     *
     * @param app the application instance
     */
    public void setApplication(HelloApplication app) {
        this.app = app;
        loadLogsForUser();
    }

    /**
     * Constructor for HomeView.
     *
     * @throws SQLException if a database access error occurs
     */
    public HomeView() throws SQLException {
        logsDAO = new LogsDAO();
    }

    /**
     * Initializes the Home view.
     */
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

        // Adding items to listView1
        listView1.getItems().add("Camera: $1450.00");
        listView1.getItems().add("Mic: $250.00");
        listView1.getItems().add("Hard Disk: $150.00");
        listView1.getItems().add("Tripod: $75.00");

        // Adding items to listView2
        listView2.getItems().add("Tasks Completed: 5");
        listView2.getItems().add("Tasks Pending: 2");
        listView2.getItems().add("Tasks in Progress: 1");

        // Adding items to listView3
        listView3.getItems().add("Projects Completed: 2");
        listView3.getItems().add("Ongoing Projects: 3");
        listView3.getItems().add("Upcoming Projects: 1");

        // Adding items to listView4
        listView4.getItems().add("Materials Used: Camera, Laptop");
        listView4.getItems().add("Materials Used: Mic, Tripod");
        listView4.getItems().add("Materials Used: Hard Disk, Light");

        // Adding items to listView5
        listView5.getItems().add("Likes: 290");
        listView5.getItems().add("Comments: 45");
        listView5.getItems().add("Shares: 30");

        // Adding items to listView6
        listView6.getItems().add("Followers: 2940");
        listView6.getItems().add("Following: 520");
        listView6.getItems().add("New Followers Today: 12");
        listView6.getItems().add("Unfollowers Today: 3");
    }

    /**
     * Loads logs for the logged-in user.
     */
    private void loadLogsForUser() {
        int userID = app.getLoggedInUserID();

        // Fetch logs from the database for the specific user
        List<Object[]> logs = logsDAO.getLogsForUser(userID);

        if (!logs.isEmpty()) {
            // Pick a random log
            Object[] randomLog = getRandomLog(logs);

            // Display the selected log in the featured log section
            displayFeaturedLog(randomLog);
        } else {
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

    /**
     * Gets a random log from the list of logs.
     *
     * @param logs the list of logs
     * @return a random log
     */
    private Object[] getRandomLog(List<Object[]> logs) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(logs.size());
        return logs.get(randomIndex);
    }

    /**
     * Gets the first incomplete to-do item from the log.
     *
     * @param log the log
     * @return the first incomplete to-do item, or null if all items are completed
     */
    private String getFirstIncompleteToDoItem(Logs log) {
        List<Pair<String, Boolean>> toDoItems = log.getToDoItems(); // Assuming you have this method
        for (Pair<String, Boolean> item : toDoItems) {
            if (!item.getValue()) { // Check if it's not completed
                return item.getKey(); // Return the first incomplete item
            }
        }
        return null; // or handle the case where all items are completed
    }

    /**
     * Displays the log in the featured log section.
     *
     * @param logData the log data
     */
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
        } else {
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

    /**
     * Navigates to the Explore view.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();  // Navigate to Explore view
        }
    }

    /**
     * Navigates to the Logs view.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();
        }
    }

    /**
     * Shows the account menu.
     *
     * @param event the action event
     */
    @FXML
    private void showAccountMenu(ActionEvent event) {
        accountMenu.show(accountButton, Side.BOTTOM, 0, 0);
    }

    /**
     * Navigates to the Account view.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToAccount() throws IOException {
        if (app != null) {
            app.showAccountView();
        }
    }

    /**
     * Logs out the user and navigates to the Login view.
     */
    @FXML
    private void onLogout() {
        try {
            app.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the Update Logs view.
     *
     * @param id  the log ID
     * @param log the log
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToUpdateLogs(int id, Logs log) throws IOException {
        if (app != null) {
            app.showLogsUpdateView(id, log);  // Navigate to Explore view
        }
    }
}