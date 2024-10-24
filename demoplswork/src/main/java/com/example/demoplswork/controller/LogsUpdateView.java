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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

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
/**
 * Controller class for the Logs Update view.
 */
public class LogsUpdateView {

    private HelloApplication app;
    private ContextMenu accountMenu;
    private int logId;  // The ID of the log being updated
    private LogsDAO logsDAO = new LogsDAO();
    private LogEventDAO logEventDAO;
    // VBox or ListView to display events
    @FXML
    private VBox logEventsBox;
    @FXML
    private ListView<LogEvent> logEventsListView;
    private ContactDAO contactDAO;

    @FXML
    private Button accountButton;

    @FXML
    private Button addMaterialButton;

    @FXML
    private Button addMediaButton;

    @FXML
    private Button addToDoButton;

    @FXML
    private VBox toDoListVBox;

    @FXML
    private ImageView mediaImageView;

    @FXML
    private Button nextButton;  // Button to go to the next image
    @FXML
    private Button backButton;  // Button to go to the previous image

    @FXML
    private Label logTitleLabel;

    @FXML
    private Label totalCostLabel; // Link to your Label

    @FXML
    private ProgressBar progressBar;

    private List<Image> images = new ArrayList<>();  // List to store the images
    private int currentIndex = -1;  // Track the current image index

    @FXML
    private TableView<Material> materialsTable;  // The TableView for materials

    @FXML
    private TableColumn<Material, String> materialNameCol;  // Material name column
    @FXML
    private TableColumn<Material, Integer> quantityCol;  // Quantity column
    @FXML
    private TableColumn<Material, Double> priceCol;  // Cost column
    private Logs log;

    /**
     * Constructor for the LogsUpdateView class.
     * @throws SQLException
     */
    public LogsUpdateView() throws SQLException {
        logEventDAO = new LogEventDAO();
        logEventsBox = new VBox();
        logEventsListView = new ListView<>();
    }

    /**
     * Method to set the application instance.
     * @param app The HelloApplication instance
     */
    @FXML
    public void setApplication(HelloApplication app) {
        this.app = app;
    }

    /**
     * Method to set the log ID.
     * @param logId The ID of the log being updated
     */
    public void setLogId(int logId) {
        this.logId = logId;
    }

    /**
     * Method to set the log object.
     * @param log The log object
     */
    public void setLog(Logs log) {
        this.log = log;  // Set the log object

        // Populate the page with log details
        populateLogDetails();
    }

    /**
     * Initialize the LogsUpdateView controller.
     */
    @FXML
    public void initialize() {
        // init columns for materials table
        materialNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

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

    /**
     * Method to navigate to the Home view.
     * @throws IOException
     */
    @FXML
    public void goToHome() throws IOException {
        if (app != null) {
            app.showHomeView();  // Navigate to Home view
        }
    }

    /**
     * Method to navigate to the Explore view.
     * @throws IOException
     */
    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();
        }
    }

    /**
     * Method to navigate to the Logs view.
     * @throws IOException
     */
    @FXML
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();  // Navigate to Explore view
        }
    }
    /*Method to show the account menu.
     *
     */
    @FXML
    private void showAccountMenu(ActionEvent event) {
        accountMenu.show(accountButton, Side.BOTTOM, 0, 0);
    }

    /**
     * Method to navigate to the Account view.
     * @throws IOException
     */
    @FXML
    public void goToAccount() throws IOException {
        if (app != null) {
            app.showAccountView();
        }
    }
    /**
     * Method to log out of the account.
     *
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
     * Method to populate the log details.
     * @throws IOException
     */
    // Populate the log details on the page
    private void populateLogDetails() {
        // Set the log title in the label
        logTitleLabel.setText(log.getLogName());
        progressBar.setProgress(log.getProgress() / 100);

        // Clear existing items in the VBox and Table to prevent duplication
        toDoListVBox.getChildren().clear();
        materialsTable.getItems().clear();

        if(logsDAO.getUserIdForLog(logId)!= app.getLoggedInUserID()){
            addMediaButton.setVisible(false);
            addMaterialButton.setVisible(false);
            addToDoButton.setVisible(false);
        }

        // Populate To-Do items in the VBox
        for (Pair<String, Boolean> toDoItem : log.getToDoItems()) {
            String task = toDoItem.getKey();      // The task description
            Boolean isChecked = toDoItem.getValue();  // The task checked state

            CheckBox checkBox = new CheckBox(task);
            checkBox.setSelected(isChecked);      // Set checkbox to the stored checked state
            toDoListVBox.getChildren().add(checkBox);

            if(logsDAO.getUserIdForLog(logId)!= app.getLoggedInUserID()){
                checkBox.setDisable(true);
            }

            // Event listener to update the log when a checkbox is checked/unchecked
            checkBox.setOnAction(event -> {
                boolean wasChecked = checkBox.isSelected();  // Capture the new state of the checkbox

                // Update the task's checked state in the log
                log.updateToDoItemStatus(task, checkBox.isSelected());  // Custom method to update the task's checked state
                double progress = logsDAO.updateToDoItemStatus(logId, task, checkBox.isSelected());

                LogsView logsView;
                try {
                    logsView = new LogsView();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Add a ToDoEvent only if the box goes from unchecked to checked
                if (wasChecked) {
                    LogEvent newEvent = new ToDoEvent(0,app.getLoggedInUserID(), logId, task, new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(newEvent);
                }

                // Update the progress bar
                progressBar.setProgress(progress / 100);

                // If the progress reaches 100%, log an EndEvent
                if (progress / 100 == 1) {
                    LogEvent endEvent = new EndEvent(0, app.getLoggedInUserID(), logId, log.getLogName(), new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(endEvent);
                }
            });

        }

        // Populate the materials in the table
        materialsTable.getItems().addAll(log.getMaterials());

        // Load and display the images
        loadImagesFromLog();

        getTotalCost(log.getMaterials());

        displayLogEvents(logId);

    }

    /**
     * Method to calculate the total cost of materials and update the label.
     *
     * @param materials the list of materials
     */
    // Method to calculate total cost and update the label
    public void getTotalCost(List<Material> materials) {
        double totalCost = materials.stream()
                .mapToDouble(material -> material.getPrice() * material.getQuantity()) // Multiply price and quantity
                .sum(); // Calculate total cost

        // Update the label with the formatted total cost
        totalCostLabel.setText(String.format("Total Cost: $%.2f", totalCost));
    }



    /**
     * Handles the addition of a new to-do item.
     * This method creates a dialog to capture user input for a new task,
     * adds the task to the to-do list, and updates the database and progress bar.
     */
    public void handleAddToDo() {
        // Create a TextInputDialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add To-Do Item");
        dialog.setHeaderText("Add a new task");
        dialog.setContentText("Enter your task:");

        // Show the dialog and capture the user input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(task -> {
            // Add the new task to your to-do list (e.g., a VBox or ListView)
            CheckBox newTask = new CheckBox(task);

            // Store the checkbox state (unchecked by default) along with the task
            boolean isChecked = newTask.isSelected();  // This will be false by default

            // Add to VBox
            toDoListVBox.getChildren().add(newTask);

            // Store the to-do item and its state in the database for the specific log
            try {
                logsDAO.addToDoItem(logId, task, isChecked);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            double newProgress = logsDAO.updateToDoItemStatus(logId, task, newTask.isSelected());
            progressBar.setProgress(newProgress / 100);

            // Event listener to update the log when a checkbox is checked/unchecked
            newTask.setOnAction(event -> {
                boolean wasChecked = newTask.isSelected();  // Capture the new state of the checkbox

                // Update the task's checked state in the log
                log.updateToDoItemStatus(task, newTask.isSelected());  // Custom method to update the task's checked state
                double progress = logsDAO.updateToDoItemStatus(logId, task, newTask.isSelected());

                LogsView logsView;
                try {
                    logsView = new LogsView();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Add a ToDoEvent only if the box goes from unchecked to checked
                if (wasChecked) {
                    LogEvent newEvent = new ToDoEvent(0, app.getLoggedInUserID(), logId, task, new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(newEvent);
                }

                // Update the progress bar
                progressBar.setProgress(progress / 100);

                // If the progress reaches 100%, log an EndEvent
                if (progress / 100 == 1) {
                    LogEvent endEvent = new EndEvent(0, app.getLoggedInUserID(), logId, log.getLogName(), new ArrayList<>(), new ArrayList<>());
                    logsView.addEventToProgressLog(endEvent);
                }
            });

        });
    }

    // Method to load images and videos from the log
    private void loadImagesFromLog() {
        images.clear();  // Clear any previously loaded images
        List<String> mediaFileNames = log.getImages();  // Assume this retrieves both image and video files

        if (mediaFileNames != null && !mediaFileNames.isEmpty()) {
            for (String fileName : mediaFileNames) {
                String filePath = "/images/" + fileName;  // The path where media is stored
                File mediaFile = new File(System.getProperty("user.dir") + "/src/main/resources" + filePath);

                if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                    // It's an image, load it in the ImageView
                    InputStream imageStream = getClass().getResourceAsStream(filePath);
                    if (imageStream != null) {
                        Image image = new Image(imageStream);
                        images.add(image);  // Add the image to the list
                    }
                } else if (fileName.endsWith(".mp4")) {
                    // It's a video, store a placeholder in the image list
                    images.add(null);  // Add placeholder for video
                }
            }

            if (!images.isEmpty()) {
                // Display the first media (image or video)
                currentIndex = 0;
                updateMediaView();  // Dynamically update the media view
                updateButtonState();  // Update the navigation button states
            }
        }
    }


    @FXML
    private VBox mediaContainer;  // Reference to the VBox containing the media

    private void updateMediaView() {
        if (currentIndex >= 0 && currentIndex < log.getImages().size()) {
            String currentFileName = log.getImages().get(currentIndex);  // Get the file name based on the index
            String filePath = "/images/" + currentFileName;  // Full file path

            // Clear the mediaContainer to remove the current media (image/video)
            mediaContainer.getChildren().removeIf(node -> node instanceof ImageView || node instanceof MediaView);

            if (currentFileName.endsWith(".jpg") || currentFileName.endsWith(".png")) {
                System.out.println("Displaying image: " + currentFileName);

                // Create a new ImageView for the image
                ImageView newImageView = new ImageView();
                newImageView.setFitHeight(200);
                newImageView.setFitWidth(300);
                newImageView.setPreserveRatio(true);

                // Load the image
                newImageView.setImage(images.get(currentIndex));  // Assuming images are preloaded
                mediaContainer.getChildren().add(1, newImageView);  // Add it right after the label

            } else if (currentFileName.endsWith(".mp4")) {
                System.out.println("Displaying video: " + currentFileName);

                File mediaFile = new File(System.getProperty("user.dir") + "/src/main/resources" + filePath);
                if (mediaFile.exists()) {
                    Media media = new Media(mediaFile.toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    MediaView mediaView = new MediaView(mediaPlayer);
                    mediaView.setFitHeight(200);
                    mediaView.setFitWidth(300);
                    mediaView.setPreserveRatio(true);

                    // Add click listener to play/pause the video
                    mediaView.setOnMouseClicked(e -> {
                        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                            mediaPlayer.pause();
                        } else {
                            mediaPlayer.play();
                        }
                    });

                    // Add the MediaView to the VBox
                    mediaContainer.getChildren().add(1, mediaView);

                    // Start playing the video
                    mediaPlayer.play();
                } else {
                    System.out.println("Video file not found: " + mediaFile.getAbsolutePath());
                }
            } else {
                System.out.println("Unknown file type: " + currentFileName);
            }

            // Update button state after adding new media
            updateButtonState();
        }
    }

    /**
     * Handles the addition of images to the log.
     * This method opens a file chooser dialog to select an image file,
     * copies the selected image to the project directory, updates the image list,
     * and stores the image path in the database.
     */
    @FXML
    public void handleAddImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Media File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Media Files", "*.png", "*.jpg", "*.mp4")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String projectDirectory = System.getProperty("user.dir");
            String targetDirectory = projectDirectory + "/src/main/resources/images";

            File targetFile = new File(targetDirectory, selectedFile.getName());

            try {
                // Copy the selected media (image or video) to the target directory
                Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Media copied to " + targetFile.getAbsolutePath());

                // Store the media path in the database
                String mediaName = selectedFile.getName();
                logsDAO.addImage(logId, mediaName);

                // Ensure the images list is mutable
                List<String> imageList = new ArrayList<>(log.getImages());  // Make a copy of the image list if it's immutable

                // Add the media name to the list
                imageList.add(mediaName);
                log.setImages(imageList);  // Update the log with the new list

                // Update the media list (images list for image/video slideshow)
                if (mediaName.endsWith(".mp4")) {
                    System.out.println("Video added: " + mediaName);
                } else {
                    System.out.println("Image added: " + mediaName);
                    Image image = new Image(targetFile.toURI().toString());
                    images.add(image);  // Add the image to the image list
                }

                // Reset the current index to the latest added media and update the view
                currentIndex = log.getImages().size() - 1;
                updateMediaView();  // Update the slideshow view to reflect the new media

                // Log the event
                LogsView logsView = new LogsView();
                LogEvent event = new ImageEvent(0, app.getLoggedInUserID(), logId, mediaName, new ArrayList<>(), new ArrayList<>());
                logsView.addEventToProgressLog(event);

            } catch (IOException | SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to add media");
            }
        }
    }

    // Method to handle clicking the Next button
    @FXML
    public void handleNext() {
        if (currentIndex < images.size() - 1) {
            currentIndex++;
            updateMediaView();  // Update the display based on current media (image or video)
            updateButtonState();  // Update the button states
        }
    }

    // Method to handle clicking the Back button
    @FXML
    public void handleBack() {
        if (currentIndex > 0) {
            currentIndex--;
            updateMediaView();  // Update the display based on current media (image or video)
            updateButtonState();  // Update the button states
        }
    }

    private void updateButtonState() {
        backButton.setDisable(currentIndex == 0);  // Disable Back button if on the first item
        nextButton.setDisable(currentIndex == images.size() - 1);  // Disable Next button if on the last item
    }


    /**
     * Method to handle the addition of a new material.
     */
    // Method to handle adding a new material
    @FXML
    public void handleAddMaterial() {
        // Create a custom dialog for adding materials
        Dialog<Pair<String, Pair<Integer, Double>>> dialog = new Dialog<>();
        dialog.setTitle("Add Material");
        dialog.setHeaderText("Enter material details");

        // Set the button types for OK and Cancel
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create input fields for Material Name, Quantity, and Cost
        TextField materialNameField = new TextField();
        materialNameField.setPromptText("Material Name");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        TextField costField = new TextField();
        costField.setPromptText("Price (Each)");

        // Create a layout and add the input fields
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

        // Enable/Disable the Add button depending on user input
        Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Add listeners to ensure all fields are filled in before enabling Add button
        materialNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || quantityField.getText().trim().isEmpty() || costField.getText().trim().isEmpty());
        });
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || materialNameField.getText().trim().isEmpty() || costField.getText().trim().isEmpty());
        });
        costField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || materialNameField.getText().trim().isEmpty() || quantityField.getText().trim().isEmpty());
        });

        // Convert the result when the Add button is clicked
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

        // Display the dialog and wait for the user to input values
        Optional<Pair<String, Pair<Integer, Double>>> result = dialog.showAndWait();

        // If the user entered valid data, add it to the table and database
        result.ifPresent(materialData -> {
            String materialName = materialData.getKey();
            int quantity = materialData.getValue().getKey();
            double cost = materialData.getValue().getValue();

            // Add the material to the table
            Material material = new Material(materialName, quantity, cost);
            materialsTable.getItems().add(material);
            log.addMaterial(material);

            // Add the material to the database (replace logId with the actual log ID you're working with)
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
     * Method to show alert dialogs.
     */
    // Helper method to show alert dialogs
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Method to display log events.
     */
    public void displayLogEvents(int logId) {
        try {
            // Fetch log events from the database
            List<LogEvent> logEvents = logEventDAO.getLogEventsForLog(logId);
            // Create an observable list to populate the ListView
            ObservableList<LogEvent> eventItems = FXCollections.observableArrayList(logEvents);
            // Set the items in the ListView
            logEventsListView.setItems(eventItems);
            // Set a custom cell factory for the ListView to display LogEventCell
            logEventsListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(LogEvent event, boolean empty) {
                    super.updateItem(event, empty);
                    if (empty || event == null) {
                        setGraphic(null);
                    } else {
                        // Fetch user details
                        Contact contact = getContactForUserId(event.getUserId());
                        // Create a custom LogEventCell
                        LogEventCell eventCell = new LogEventCell(event, contact.getFirstName(), contact.getPhoto(), event.getLikes().size(), event.getComments().size());
                        setGraphic(eventCell);  // Set the custom cell graphic
                    }
                }
            });
            // **Clear existing content before adding new content**
            logEventsBox.getChildren().clear();
            // Add the ListView to the VBox or the container where you want to display it
            logEventsBox.getChildren().add(logEventsListView);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*
* Method to get the contact for the user
* */
    private Contact getContactForUserId(int userId) {
        contactDAO = new ContactDAO();

        // Fetch the contact information for the logged-in user
        Contact contact = contactDAO.getContactById(userId);

        // Now update the contact with profile details (bio, photo)
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





