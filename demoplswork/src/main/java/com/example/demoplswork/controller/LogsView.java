package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.events.LogEvent;
import com.example.demoplswork.events.LogEventDAO;
import com.example.demoplswork.events.ProgressLog;
import com.example.demoplswork.events.StartEvent;
import com.example.demoplswork.model.Logs;
import com.example.demoplswork.model.LogsDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogsView {
    private HelloApplication app;

    @FXML
    private GridPane projectsGrid;

    @FXML
    private Label logNameText;

    private int currentRow = 0;
    private int currentColumn = 0;
    private static final int LOGS_PER_ROW = 3;
    private ContextMenu accountMenu;

    @FXML
    private Button accountButton;

    private LogsDAO logsDAO;
    private ProgressLog progressLog;
    private LogEventDAO logEventDAO;

    public LogsView() throws SQLException {
        this.progressLog = new ProgressLog();
        logsDAO = new LogsDAO();
        logEventDAO = new LogEventDAO();
    }

    public void setApplication(HelloApplication app) {
        this.app = app;
        loadLogsForUser();
    }

    @FXML
    public void initialize() {
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
            app.showHomeView();
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
            app.showLogsUpdateView(id, log);
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
    public void handleAddNewProject() {
        createNewLog();
    }

    private void loadLogsForUser() {
        int userID = app.getLoggedInUserID();
        List<Object[]> logsList = logsDAO.getLogsForUser(userID);

        for (Object[] logData : logsList) {
            int logID = (int) logData[0];
            Logs log = (Logs) logData[1];
            VBox logModule = createLogModuleFromDatabase(log, logID);
            projectsGrid.add(logModule, currentColumn, currentRow);
            currentColumn++;
            if (currentColumn >= LOGS_PER_ROW) {
                currentColumn = 0;
                currentRow++;
            }
        }
    }

    private VBox createLogModuleFromDatabase(Logs log, int logID) {
        VBox logModule = new VBox();
        logModule.setSpacing(0);
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

        InputStream imageStream = getClass().getResourceAsStream(relativePath);
        if (imageStream != null) {
            Image image = new Image(imageStream);
            imageView.setImage(image);
        }

        imageContainer.getChildren().addAll(imageBackground, imageView);

        StackPane titleContainer = new StackPane();
        Rectangle titleBackground = new Rectangle(250, 40);
        titleBackground.setFill(javafx.scene.paint.Color.web("#ffee00"));
        titleBackground.setStroke(javafx.scene.paint.Color.BLACK);

        logNameText = new Label(log.getLogName());
        logNameText.setStyle("-fx-font-size: 14px; -fx-font-family: 'Roboto'; -fx-text-fill: black;");
        titleContainer.getChildren().addAll(titleBackground, logNameText);

        ProgressBar progressBar = new ProgressBar(log.getProgress() / 100.0);
        progressBar.setPrefWidth(250);
        progressBar.setPrefHeight(40);
        progressBar.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        StackPane buttonContainer = new StackPane();
        Rectangle buttonBackground = new Rectangle(250, 100);
        buttonBackground.setFill(javafx.scene.paint.Color.BLACK);
        buttonBackground.setStroke(javafx.scene.paint.Color.BLACK);

        VBox buttonsVBox = new VBox(10);
        buttonsVBox.setAlignment(Pos.CENTER);
        Button viewButton = new Button("View Log");
        viewButton.setPrefWidth(190);
        viewButton.setPrefHeight(35);
        viewButton.setStyle("-fx-background-color: #d3d3d3;");
        viewButton.setOnAction(actionEvent -> {
            try {
                goToUpdateLogs(logID, log);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        HBox buttonsHBox = new HBox(10);
        buttonsHBox.setAlignment(Pos.CENTER);

        Button editButton = new Button("Rename Log");
        editButton.setOnAction(event -> renameLog(logID, log));
        editButton.setPrefWidth(90);
        editButton.setPrefHeight(35);
        editButton.setStyle("-fx-background-color: #ffffff;");

        Button deleteButton = new Button("Delete Log");
        deleteButton.setPrefWidth(90);
        deleteButton.setPrefHeight(35);
        deleteButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");

        buttonsHBox.getChildren().addAll(editButton, deleteButton);
        buttonsVBox.getChildren().addAll(viewButton, buttonsHBox);
        buttonContainer.getChildren().addAll(buttonBackground, buttonsVBox);
        logModule.getChildren().addAll(imageContainer, titleContainer, progressBar, buttonContainer);

        deleteButton.setOnAction(event -> handleDeleteLog(logID, log, logModule));

        return logModule;
    }

    private void renameLog(int currentLogId, Logs log) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename Log");
        dialog.setHeaderText("Rename Log");
        dialog.setContentText("New log name:");
        dialog.getEditor().setText(log.getLogName());

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newLogName = result.get().trim();

            if (newLogName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Log name cannot be empty!");
                alert.showAndWait();
                return;
            }

            try {
                logsDAO.updateLogName(currentLogId, newLogName);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Log name updated successfully!");
                logNameText.setText(newLogName);
                log.setLogName(newLogName);
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update log name. Please try again.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void handleDeleteLog(int logID, Logs selectedLog, VBox logModule) {
        if (selectedLog != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Log");
            confirmationAlert.setHeaderText("Are you sure you want to delete this log?");
            confirmationAlert.setContentText("Log: " + selectedLog.getLogName());

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteLogFromDatabase(logID);
                projectsGrid.getChildren().remove(logModule);
                rearrangeGrid();
            }
        } else {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("No Selection");
            warningAlert.setHeaderText("No Log Selected");
            warningAlert.setContentText("Please select a log to delete.");
            warningAlert.showAndWait();
        }
    }

    private void deleteLogFromDatabase(int logId) {
        LogsDAO logsDAO = new LogsDAO();
        logsDAO.deleteLog(logId);
    }

    private void rearrangeGrid() {
        List<Node> remainingLogs = new ArrayList<>(projectsGrid.getChildren());
        projectsGrid.getChildren().clear();
        currentRow = 0;
        currentColumn = 0;

        for (Node logNode : remainingLogs) {
            projectsGrid.add(logNode, currentColumn, currentRow);
            currentColumn++;
            if (currentColumn >= LOGS_PER_ROW) {
                currentColumn = 0;
                currentRow++;
            }
        }
    }

    private void createNewLog() {
        TextInputDialog dialog = new TextInputDialog("New Project");
        dialog.setTitle("Create New Project");
        dialog.setHeaderText("Enter the name of your new project:");
        dialog.setContentText("Project Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(projectName -> {
            int userID = app.getLoggedInUserID();
            Logs newLog = new Logs(projectName, List.of(), List.of(), List.of());
            int logID = -1;
            try {
                logID = logsDAO.insertLog(userID, newLog);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (logID > 0) {
                try {
                    LogEvent startEvent = new StartEvent(0, userID, logID, projectName, new ArrayList<>(), new ArrayList<>());
                    addEventToProgressLog(startEvent);
                    goToUpdateLogs(logID, newLog);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addEventToProgressLog(LogEvent event) {
        progressLog.addEvent(event);
        try {
            logEventDAO.insertLogEvent(event);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String log = progressLog.getLog();
        System.out.println(log);
    }

    public String getProgressLog() {
        return progressLog.getLog();
    }
}
