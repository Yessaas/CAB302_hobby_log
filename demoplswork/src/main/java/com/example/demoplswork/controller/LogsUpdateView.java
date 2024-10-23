package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.events.*;
import com.example.demoplswork.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * LogsUpdateView class is the controller for viewing and updating logs.
 * It handles the UI for viewing and updating logs.
 * It has methods to set the application instance, set the log ID, set the log, and initialize the view.
 * It has methods to navigate to different views such as Home, Explore, Logs, and Account.
 * It has methods to handle user interactions such as adding to-do items, adding media, and adding materials.
 * It has methods to populate log details, load images from the log, and display log events.
 * It has methods to handle next and previous media navigation.
 * It has a showAlert method to display alerts with specified titles and messages.
 * It has a getTotalCost method to calculate the total cost of materials.
 * It has a getContactForUserId method to get the contact information for a user ID.
 */
public class LogsUpdateView {

    private HelloApplication app;
    private ContextMenu accountMenu;
    private int logId;
    private LogsDAO logsDAO = new LogsDAO();
    private LogEventDAO logEventDAO;
    private ContactDAO contactDAO;

    @FXML
    private VBox mediaviewholder;

    @FXML
    private VBox logEventsBox;
    @FXML
    private ListView<LogEvent> logEventsListView;

    @FXML
    private Button accountButton;

    @FXML
    private VBox toDoListVBox;

    private ImageView mediaImageView;
    private MediaView mediaView;

    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;

    @FXML
    private Label logTitleLabel;

    @FXML
    private Label totalCostLabel;

    @FXML
    private ProgressBar progressBar;

    private ObservableList<Image> images = FXCollections.observableArrayList();
    private ObservableList<String> videos = FXCollections.observableArrayList();
    private int currentIndex = -1;

    @FXML
    private TableView<Material> materialsTable;

    @FXML
    private TableColumn<Material, String> materialNameCol;
    @FXML
    private TableColumn<Material, Integer> quantityCol;
    @FXML
    private TableColumn<Material, Double> priceCol;
    private Logs log;

    private Connection connection;

    /**
     * Constructor for LogsUpdateView
     * @throws SQLException
     */
    public LogsUpdateView() throws SQLException {
        logEventDAO = new LogEventDAO();
        logEventsBox = new VBox();
        logEventsListView = new ListView<>();
    }

    /**
     * Set the application
     * @param app
     */
    @FXML
    public void setApplication(HelloApplication app) {
        this.app = app;
    }

    /**
     * Set the log id
     * @param logId
     */
    public void setLogId(int logId) {
        this.logId = logId;
    }

    /**
     * Set the log
     * @param log
     */
    public void setLog(Logs log) {
        this.log = log;
        populateLogDetails();
    }

    /**
     * Initialize the LogsUpdateView
     */
    @FXML
    public void initialize() {
        System.out.println("version 3.0");

        mediaImageView = new ImageView();
        mediaImageView.setFitWidth(300);
        mediaImageView.setFitHeight(200);
        mediaImageView.setPreserveRatio(true);

        mediaView = new MediaView();
        mediaView.setFitWidth(300);
        mediaView.setFitHeight(200);
        mediaView.setPreserveRatio(true);
        mediaView.setVisible(false);

        materialNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

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

    /**
     * Go to home view
     * @throws IOException
     */
    @FXML
    public void goToHome() throws IOException {
        if (app != null) {
            app.showHomeView();
        }
    }

    /**
     * Go to explore view
     * @throws IOException
     */
    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();
        }
    }

    /**
     * Go to logs view
     * @throws IOException
     */
    @FXML
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();
        }
    }

    /**
     * Show account menu
     * @throws IOException
     */
    @FXML
    private void showAccountMenu(ActionEvent event) {
        accountMenu.show(accountButton, Side.BOTTOM, 0, 0);
    }

    /**
     * Go to account view
     * @throws IOException
     */
    @FXML
    public void goToAccount() throws IOException {
        if (app != null) {
            app.showAccountView();
        }
    }

    /**
     * Log out of the account
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
     * Populate log details with tasks, materials, and images/videos
     */
    private void populateLogDetails() {
        logTitleLabel.setText(log.getLogName());
        progressBar.setProgress(log.getProgress() / 100);

        toDoListVBox.getChildren().clear();
        materialsTable.getItems().clear();

        for (Pair<String, Boolean> toDoItem : log.getToDoItems()) {
            String task = toDoItem.getKey();
            Boolean isChecked = toDoItem.getValue();

            CheckBox checkBox = new CheckBox(task);
            checkBox.setSelected(isChecked);
            toDoListVBox.getChildren().add(checkBox);

            checkBox.setOnAction(event -> {
                boolean wasChecked = checkBox.isSelected();
                log.updateToDoItemStatus(task, checkBox.isSelected());
                double progress = logsDAO.updateToDoItemStatus(logId, task, checkBox.isSelected());

                LogsView logsView;
                try {
                    logsView = new LogsView();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (wasChecked) {
                    LogEvent newEvent = new ToDoEvent(0, app.getLoggedInUserID(), logId, task, new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(newEvent);
                }

                progressBar.setProgress(progress / 100);

                if (progress / 100 == 1) {
                    LogEvent endEvent = new EndEvent(0, app.getLoggedInUserID(), logId, log.getLogName(), new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(endEvent);
                }
            });
        }

        materialsTable.getItems().addAll(log.getMaterials());

        loadImagesFromLog();

        getTotalCost(log.getMaterials());

        displayLogEvents(logId);
    }

    /**
     * Get the total cost of materials
     * @param materials
     */
    public void getTotalCost(List<Material> materials) {
        double totalCost = materials.stream()
                .mapToDouble(material -> material.getPrice() * material.getQuantity())
                .sum();

        totalCostLabel.setText(String.format("Total Cost: $%.2f", totalCost));
    }

    /**
     * Handle adding a to-do item
     */
    public void handleAddToDo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add To-Do Item");
        dialog.setHeaderText("Add a new task");
        dialog.setContentText("Enter your task:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(task -> {
            CheckBox newTask = new CheckBox(task);
            boolean isChecked = newTask.isSelected();
            toDoListVBox.getChildren().add(newTask);

            try {
                logsDAO.addToDoItem(logId, task, isChecked);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            double newProgress = logsDAO.updateToDoItemStatus(logId, task, newTask.isSelected());
            progressBar.setProgress(newProgress / 100);

            newTask.setOnAction(event -> {
                boolean wasChecked = newTask.isSelected();
                log.updateToDoItemStatus(task, newTask.isSelected());
                double progress = logsDAO.updateToDoItemStatus(logId, task, newTask.isSelected());

                LogsView logsView;
                try {
                    logsView = new LogsView();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (wasChecked) {
                    LogEvent newEvent = new ToDoEvent(0, app.getLoggedInUserID(), logId, task, new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(newEvent);
                }

                progressBar.setProgress(progress / 100);

                if (progress / 100 == 1) {
                    LogEvent endEvent = new EndEvent(0, app.getLoggedInUserID(), logId, log.getLogName(), new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(endEvent);
                }
            });
        });
    }

    /**
     * Load images from log
     */
    private void loadImagesFromLog() {
        images.clear();
        List<String> imageFileNames = log.getImages();

        if (imageFileNames != null && !imageFileNames.isEmpty()) {
            for (String imageName : imageFileNames) {
                String imagePath = "/images/" + imageName;
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream != null) {
                    Image image = new Image(imageStream);
                    images.add(image);
                }
            }
        }
    }

    /**
     * Handle adding media, includes videos and images
     */
    @FXML
    public void handleAddMedia() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Media File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mov", "*.avi")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String projectDirectory = System.getProperty("user.dir");
            String targetDirectoryImages = projectDirectory + "/src/main/resources/images";
            String targetDirectoryVideos = projectDirectory + "/src/main/resources/media";
            String targetDirectory;
            boolean isImage = false;

            if (selectedFile.getName().endsWith(".png") || selectedFile.getName().endsWith(".jpg") || selectedFile.getName().endsWith(".jpeg")) {
                targetDirectory = targetDirectoryImages;
                isImage = true;
            } else if (selectedFile.getName().endsWith(".mp4") || selectedFile.getName().endsWith(".mov") || selectedFile.getName().endsWith(".avi")) {
                targetDirectory = targetDirectoryVideos;
            } else {
                System.out.println("Unsupported file type.");
                return;
            }

            File targetFile = new File(targetDirectory, selectedFile.getName());

            try {
                Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Media copied to " + targetFile.getAbsolutePath());

                if (isImage) {
                    Image image = new Image(targetFile.toURI().toString());
                    images.add(image);
                    currentIndex = images.size() - 1;
                    mediaImageView.setImage(images.get(currentIndex));
                    mediaView.setVisible(false);
                    mediaviewholder.getChildren().remove(mediaView);
                    mediaviewholder.getChildren().add(mediaImageView);
                    mediaImageView.setVisible(true);
                } else {
                    videos.add(targetFile.toURI().toString());
                    currentIndex = videos.size() - 1;
                    MediaPlayer mediaPlayer = new MediaPlayer(new Media(videos.get(currentIndex)));
                    mediaPlayer.setAutoPlay(true);
                    mediaView.setMediaPlayer(mediaPlayer);
                    mediaPlayer.play();
                    mediaView.setVisible(true);
                    mediaImageView.setVisible(false);
                    mediaviewholder.getChildren().remove(mediaImageView);
                    mediaviewholder.getChildren().add(mediaView);
                }

                String mediaName = selectedFile.getName();
                logsDAO.addMedia(logId, mediaName);

                LogsView logsView = new LogsView();
                LogEvent event = new MediaEvent(0, app.getLoggedInUserID(), logId, mediaName, new ArrayList<>(), new ArrayList<>());
                logsView.addEventToProgressLog(event);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to add media");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No file selected.");
        }
    }

    /**
     * Handle next media
     */
    @FXML
    public void handleNext() {
        if (currentIndex < images.size() - 1) {
            currentIndex++;
            mediaImageView.setImage(images.get(currentIndex));
            mediaView.setVisible(false);
            mediaImageView.setVisible(true);
        } else if (currentIndex < videos.size() - 1) {
            currentIndex++;
            MediaPlayer mediaPlayer = new MediaPlayer(new Media(videos.get(currentIndex)));
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
            mediaView.setVisible(true);
            mediaImageView.setVisible(false);
        }
        updateButtonState();
    }

    /**
     * Handle previous media
     */
    @FXML
    public void handleBack() {
        if (currentIndex > 0) {
            currentIndex--;
            if (currentIndex < images.size()) {
                mediaImageView.setImage(images.get(currentIndex));
                mediaView.setVisible(false);
                mediaImageView.setVisible(true);
            } else {
                MediaPlayer mediaPlayer = new MediaPlayer(new Media(videos.get(currentIndex)));
                mediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();
                mediaView.setVisible(true);
                mediaImageView.setVisible(false);
            }
        }
        updateButtonState();
    }

    /**
     * Update button state
     */
    private void updateButtonState() {
        backButton.setDisable(currentIndex == 0);
        nextButton.setDisable(currentIndex == images.size() - 1 && currentIndex == videos.size() - 1);
    }

    /**
     * Handle adding a material including name, quantity, and price
     */
    @FXML
    public void handleAddMaterial() {
        Dialog<Pair<String, Pair<Integer, Double>>> dialog = new Dialog<>();
        dialog.setTitle("Add Material");
        dialog.setHeaderText("Enter material details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField materialNameField = new TextField();
        materialNameField.setPromptText("Material Name");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        TextField costField = new TextField();
        costField.setPromptText("Price (Each)");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Material Name:"), 0, 0);
        grid.add(materialNameField, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(new Label("Price (Each):"), 0, 2);
        grid.add(costField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        materialNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || quantityField.getText().trim().isEmpty() || costField.getText().trim().isEmpty());
        });
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || materialNameField.getText().trim().isEmpty() || costField.getText().trim().isEmpty());
        });
        costField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || materialNameField.getText().trim().isEmpty() || quantityField.getText().trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String materialName = materialNameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    double cost = Double.parseDouble(costField.getText());
                    return new Pair<>(materialName, new Pair<>(quantity, cost));
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid numeric values for Quantity and Price.");
                    return null;
                }
            }
            return null;
        });

        Optional<Pair<String, Pair<Integer, Double>>> result = dialog.showAndWait();

        result.ifPresent(materialData -> {
            String materialName = materialData.getKey();
            int quantity = materialData.getValue().getKey();
            double cost = materialData.getValue().getValue();

            Material material = new Material(materialName, quantity, cost);
            materialsTable.getItems().add(material);
            log.addMaterial(material);

            logsDAO.addMaterial(logId, material);

            getTotalCost(log.getMaterials());

            LogsView logsView;
            try {
                logsView = new LogsView();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            LogEvent event = new MaterialEvent(0, app.getLoggedInUserID(), logId, materialName, new ArrayList<>(), new ArrayList<>());
            logsView.addEventToProgressLog(event);
        });
    }
/**
     * Show alert
     * @param title
     * @param message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Display log events
     * @param logId
     */
    public void displayLogEvents(int logId) {
        try {
            List<LogEvent> logEvents = logEventDAO.getLogEventsForLog(logId);

            ObservableList<LogEvent> eventItems = FXCollections.observableArrayList(logEvents);
            logEventsListView.setItems(eventItems);

            logEventsListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(LogEvent event, boolean empty) {
                    super.updateItem(event, empty);
                    if (empty || event == null) {
                        setGraphic(null);
                    } else {
                        Contact contact = getContactForUserId(event.getUserId());
                        LogEventCell eventCell = new LogEventCell(event, contact.getFirstName(), contact.getPhoto(), 0, 0);
                        setGraphic(eventCell);
                    }
                }
            });

            logEventsBox.getChildren().clear();
            logEventsBox.getChildren().add(logEventsListView);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get contact for user id
     * @param userId
     * @return
     */
    private Contact getContactForUserId(int userId) {
        contactDAO = new ContactDAO();
        Contact contact = contactDAO.getContactById(userId);

        ProfileDAO profileDAO = new ProfileDAO();
        try {
            profileDAO.insertProfile(userId, " ", " ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        profileDAO.getProfileByUserId(contact, userId);
        return contact;
    }
}
