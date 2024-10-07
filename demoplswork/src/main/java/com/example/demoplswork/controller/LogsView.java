package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.model.Logs;
import com.example.demoplswork.model.LogsDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;



public class LogsView {
    private HelloApplication app;

    @FXML
    private GridPane projectsGrid;  // The GridPane where logs will be added

    private TableView<Logs> logsTable;  // TableView for logs


    private int currentRow = 0;  // Track the current row in the grid
    private int currentColumn = 0;  // Track the current column in the grid

    private static final int LOGS_PER_ROW = 3;  // Number of logs per row

    private ContextMenu accountMenu;


    @FXML
    private Button accountButton;

    private LogsDAO logsDAO;

    // Constructor
    public LogsView() throws SQLException {
        logsDAO = new LogsDAO();
    }

    public void setApplication(HelloApplication app) {
        this.app = app;

        // Load logs from the database for the current user
        loadLogsForUser();
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
    public void goToUpdateLogs(int id, Logs log) throws IOException {
        if (app != null) {
            app.showLogsUpdateView(id, log);  // Navigate to Explore view
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
    // Method to handle creating a new log
    @FXML
    public void handleAddNewProject() {
        createLogModule();
    }

    private void loadLogsForUser() {
        // Retrieve the userID from the main application class
        int userID = app.getLoggedInUserID();

        // Fetch logs from the database for the specific user
        List<Object[]> logsList = logsDAO.getLogsForUser(userID);

        for (Object[] logData : logsList) {
            int logID = (int) logData[0]; // Extract the log ID
            Logs log = (Logs) logData[1]; // Extract the Logs object

            // Create a log module for each log
            VBox logModule = createLogModuleFromDatabase(log, logID);

            // Add the log to the GridPane
            projectsGrid.add(logModule, currentColumn, currentRow);

            // Update the column and row for the next log
            currentColumn++;
            if (currentColumn >= LOGS_PER_ROW) {
                currentColumn = 0;
                currentRow++;
            }
        }
    }


    private VBox createLogModuleFromDatabase(Logs log, int logID) {
        VBox logModule = new VBox();
        logModule.setSpacing(0); // Adds spacing between elements

        // Image container with a white background and black border
        StackPane imageContainer = new StackPane();
        Rectangle imageBackground = new Rectangle(250, 200);
        imageBackground.setFill(javafx.scene.paint.Color.WHITE);
        imageBackground.setStroke(javafx.scene.paint.Color.BLACK);

        String imageName = log.getImages().getFirst();
        String relativePath = "/images/" + imageName;

        ImageView imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(false);

        // Load the image using the image name
        InputStream imageStream = getClass().getResourceAsStream(relativePath);
        if (imageStream != null) {
            Image image = new Image(imageStream);
            imageView.setImage(image);
        }


        imageContainer.getChildren().addAll(imageBackground, imageView);

        // Title container with a yellow background
        StackPane titleContainer = new StackPane();
        Rectangle titleBackground = new Rectangle(250, 40);
        titleBackground.setFill(javafx.scene.paint.Color.web("#ffee00"));
        titleBackground.setStroke(javafx.scene.paint.Color.BLACK);

        Label projectTitle = new Label(log.getLogName());
        projectTitle.setStyle("-fx-font-size: 14px; -fx-font-family: 'Roboto'; -fx-text-fill: black;");
        titleContainer.getChildren().addAll(titleBackground, projectTitle);

        // Progress bar
        System.out.println("Creating log module: " + ". Progress: " + log.getProgress());
        ProgressBar progressBar = new ProgressBar(log.getProgress() / 100.0);
        progressBar.setPrefWidth(250);
        progressBar.setPrefHeight(40);
        progressBar.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        // Button container with a black rectangle background
        StackPane buttonContainer = new StackPane();
        Rectangle buttonBackground = new Rectangle(250, 100);
        buttonBackground.setFill(javafx.scene.paint.Color.BLACK); // Set to transparent if no fill needed
        buttonBackground.setStroke(javafx.scene.paint.Color.BLACK);

        // Buttons within a VBox
        VBox buttonsVBox = new VBox(10);
        buttonsVBox.setAlignment(Pos.CENTER); // Center-align the buttons
        Button viewButton = new Button("View Log");
        viewButton.setPrefWidth(190);
        viewButton.setPrefHeight(35);
        viewButton.setStyle("-fx-background-color: #d3d3d3;");
        viewButton.setOnAction(actionEvent -> {
            try {
                goToUpdateLogs(logID, log);  // Use logID here to open the correct log
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Horizontal box for Edit and Delete buttons
        HBox buttonsHBox = new HBox(10);
        buttonsHBox.setAlignment(Pos.CENTER);

        Button editButton = new Button("Edit Log");
        editButton.setPrefWidth(90);
        editButton.setPrefHeight(35);
        editButton.setStyle("-fx-background-color: #ffffff;");

        Button deleteButton = new Button("Delete Log");
        deleteButton.setPrefWidth(90);
        deleteButton.setPrefHeight(35);
        deleteButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");

        // Add edit and delete buttons to the HBox
        buttonsHBox.getChildren().addAll(editButton, deleteButton);

        // Add view button and buttonsHBox to the VBox
        buttonsVBox.getChildren().addAll(viewButton, buttonsHBox);

        // Add the buttonVBox and buttonBackground to the buttonContainer StackPane
        buttonContainer.getChildren().addAll(buttonBackground, buttonsVBox);

        // Add all elements to the main log module VBox
        logModule.getChildren().addAll(imageContainer, titleContainer, progressBar, buttonContainer);

        return logModule; // Return the complete log module
    }


    // Method to create a log module
    private void createLogModule() {
        // Create a dialog to get the project name
        TextInputDialog dialog = new TextInputDialog("New Project");
        dialog.setTitle("Create New Project");
        dialog.setHeaderText("Enter the name of your new project:");
        dialog.setContentText("Project Name:");

        // Show the dialog and capture the result
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(projectName -> {
            // Retrieve the userID from the main application class
            int userID = app.getLoggedInUserID();

            // Create a new Logs object with the captured name (no content for now)
            Logs newLog = new Logs(projectName, List.of(), List.of(), List.of());

            // Insert the new log into the database with the correct userID
            int logID = logsDAO.insertLog(userID, newLog);

            // Redirect the user to the logs update view
            try {
                goToUpdateLogs(logID, newLog);  // This will let the user input log information
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }




}
