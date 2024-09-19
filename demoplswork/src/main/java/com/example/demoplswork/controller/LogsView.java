package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import javafx.fxml.FXML;
import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;



public class LogsView {
    private HelloApplication app;

    @FXML
    private GridPane projectsGrid;  // The GridPane where logs will be added

    private int currentRow = 0;  // Track the current row in the grid
    private int currentColumn = 0;  // Track the current column in the grid

    private static final int LOGS_PER_ROW = 3;  // Number of logs per row

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
    public void goToUpdateLogs() throws IOException {
        if (app != null) {
            app.showLogsUpdateView();  // Navigate to Explore view
        }
    }
    @FXML
    public void goToAccount() throws IOException {
        if (app != null) {
            app.showAccountView();
        }
    }
    // Method to handle creating a new log
    @FXML
    public void handleAddNewProject() {
        // Create the log UI elements
        VBox logModule = createLogModule();

        // Add the log to the GridPane
        projectsGrid.add(logModule, currentColumn, currentRow);

        // Update the column and row for the next log
        currentColumn++;
        if (currentColumn >= LOGS_PER_ROW) {
            currentColumn = 0;
            currentRow++;
        }
    }

    // Method to create a log module
    private VBox createLogModule() {
        // Create a dialog to get the project name
        TextInputDialog dialog = new TextInputDialog("New Project");
        dialog.setTitle("Create New Project");
        dialog.setHeaderText("Enter the name of your new project:");
        dialog.setContentText("Project Name:");

        // Show the dialog and capture the result
        Optional<String> result = dialog.showAndWait();
        String projectName = result.orElse("New Project");

        VBox logModule = new VBox();
        logModule.setSpacing(0); // Adds spacing between elements

        // Image container with a white background and black border
        StackPane imageContainer = new StackPane();
        Rectangle imageBackground = new Rectangle(250, 200);
        imageBackground.setFill(javafx.scene.paint.Color.WHITE);
        imageBackground.setStroke(javafx.scene.paint.Color.BLACK);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);

        imageContainer.getChildren().addAll(imageBackground, imageView);

        // Title container with a yellow background
        StackPane titleContainer = new StackPane();
        Rectangle titleBackground = new Rectangle(250, 40);
        titleBackground.setFill(javafx.scene.paint.Color.web("#ffee00"));
        titleBackground.setStroke(javafx.scene.paint.Color.BLACK);

        Label projectTitle = new Label(projectName);
        projectTitle.setStyle("-fx-font-size: 14px; -fx-font-family: 'Roboto'; -fx-text-fill: black;");
        titleContainer.getChildren().addAll(titleBackground, projectTitle);

        // Progress bar
        ProgressBar progressBar = new ProgressBar(0.5);
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
                goToUpdateLogs();
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




}
