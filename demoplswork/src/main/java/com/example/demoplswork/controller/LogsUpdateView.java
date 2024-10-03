package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.model.Logs;
import com.example.demoplswork.model.LogsDAO;
import com.example.demoplswork.model.Material;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

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

public class LogsUpdateView {

    private HelloApplication app;

    private ContextMenu accountMenu;

    private int logId;  // The ID of the log being updated

    private LogsDAO logsDAO = new LogsDAO();

    @FXML
    private Button accountButton;

    @FXML
    private VBox toDoListVBox;

    @FXML
    private ImageView mediaImageView;  // Assuming this ImageView exists in your FXML for media display

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

    public LogsUpdateView() throws SQLException {
    }

    @FXML
    public void setApplication(HelloApplication app) {
        this.app = app;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public void setLog(Logs log) {
        this.log = log;  // Set the log object

        // Populate the page with log details
        populateLogDetails();
    }

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

    // Populate the log details on the page
    private void populateLogDetails() {
        // Set the log title in the label
        logTitleLabel.setText(log.getLogName());
        progressBar.setProgress(log.getProgress() / 100);

        // Clear existing items in the VBox and Table to prevent duplication
        toDoListVBox.getChildren().clear();
        materialsTable.getItems().clear();

        // Populate To-Do items in the VBox
        for (Pair<String, Boolean> toDoItem : log.getToDoItems()) {
            String task = toDoItem.getKey();      // The task description
            Boolean isChecked = toDoItem.getValue();  // The task checked state

            CheckBox checkBox = new CheckBox(task);
            checkBox.setSelected(isChecked);      // Set checkbox to the stored checked state
            toDoListVBox.getChildren().add(checkBox);

            // Event listener to update the log when a checkbox is checked/unchecked
            checkBox.setOnAction(event -> {
                log.updateToDoItemStatus(task, checkBox.isSelected());  // Custom method to update the task's checked state
                double progress = logsDAO.updateToDoItemStatus(logId, task, checkBox.isSelected());
                progressBar.setProgress(progress / 100);
            });
        }

        // Populate the materials in the table
        materialsTable.getItems().addAll(log.getMaterials());

        // Load and display the images
        loadImagesFromLog();

        getTotalCost(log.getMaterials());

    }

    // Method to calculate total cost and update the label
    public void getTotalCost(List<Material> materials) {
        double totalCost = materials.stream()
                .mapToDouble(material -> material.getPrice() * material.getQuantity()) // Multiply price and quantity
                .sum(); // Calculate total cost

        // Update the label with the formatted total cost
        totalCostLabel.setText(String.format("Total Cost: $%.2f", totalCost));
    }




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
            logsDAO.addToDoItem(logId, task, isChecked);
            double newProgress = logsDAO.updateToDoItemStatus(logId, task, newTask.isSelected());
            progressBar.setProgress(newProgress / 100);


            // Event listener to update the log when a checkbox is checked/unchecked
            newTask.setOnAction(event -> {
                log.updateToDoItemStatus(task, newTask.isSelected());  // Custom method to update the task's checked state
                double progress = logsDAO.updateToDoItemStatus(logId, task, newTask.isSelected());
                progressBar.setProgress(progress / 100);
            });
        });
    }

    // Method to load images from the log
    private void loadImagesFromLog() {
        images.clear();  // Clear any previously loaded images
        List<String> imageFileNames = log.getImages();

        if (imageFileNames != null && !imageFileNames.isEmpty()) {
            for (String imageName : imageFileNames) {

                String imagePath = "/images/" + imageName;

                // Load the image
                Image image = new Image(getClass().getResourceAsStream(imagePath));

                // Add to the images list
                images.add(image);
            }

            // Display the first image
            currentIndex = 0;
            mediaImageView.setImage(images.get(currentIndex));

            // Update the navigation button states
            updateButtonState();
        } /*else {
            // No images available, set default behavior if necessary
            mediaImageView.setImage(null);  // Clear the ImageView or show a placeholder image
            backButton.setDisable(true);
            nextButton.setDisable(true);
        }*/
    }


    @FXML
    public void handleAddImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Media File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Get the current working directory (project root)
            String projectDirectory = System.getProperty("user.dir");

            // Define the target directory relative to the project directory
            String targetDirectory = projectDirectory + "/src/main/resources/images";

            // Construct the target file path
            File targetFile = new File(targetDirectory, selectedFile.getName());

            try {
                // Copy the selected image to the target directory
                Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image copied to " + targetFile.getAbsolutePath());

                // Add the selected image to the list
                Image image = new Image(targetFile.toURI().toString());
                images.add(image);

                // Set the current index to the last image in the list (newly added)
                currentIndex = images.size() - 1;
                mediaImageView.setImage(images.get(currentIndex));

                // Update the button state
                updateButtonState();

                // Store the image path in the database
                String imageName = selectedFile.getName(); // Get the name of the file only
                logsDAO.addImage(logId, imageName);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to add image");
            }
        }
    }

    // Method to handle clicking the Next button
    @FXML
    public void handleNext() {
        if (currentIndex < images.size() - 1) {
            currentIndex++;
            mediaImageView.setImage(images.get(currentIndex));
            updateButtonState();
        }
    }

    // Method to handle clicking the Back button
    @FXML
    public void handleBack() {
        if (currentIndex > 0) {
            currentIndex--;
            mediaImageView.setImage(images.get(currentIndex));
            updateButtonState();
        }
    }

    // Update the state of Next/Back buttons based on the current index
    private void updateButtonState() {
        backButton.setDisable(currentIndex == 0);
        nextButton.setDisable(currentIndex == images.size() - 1);
    }



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
        });
    }

    // Helper method to show alert dialogs
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}





