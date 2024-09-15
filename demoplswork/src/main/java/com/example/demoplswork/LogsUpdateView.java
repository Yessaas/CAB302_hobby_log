package com.example.demoplswork;

import com.example.demoplswork.Material;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

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

    @FXML
    private VBox toDoListVBox;

    @FXML
    private ImageView mediaImageView;  // Assuming this ImageView exists in your FXML for media display

    @FXML
    private Button nextButton;  // Button to go to the next image
    @FXML
    private Button backButton;  // Button to go to the previous image

    private List<Image> images = new ArrayList<>();  // List to store the images
    private int currentIndex = -1;  // Track the current image index

    @FXML
    private TableView<Material> materialsTable;  // The TableView for materials

    @FXML
    private TableColumn<Material, String> materialNameCol;  // Material name column
    @FXML
    private TableColumn<Material, Integer> quantityCol;  // Quantity column
    @FXML
    private TableColumn<Material, Double> costCol;  // Cost column

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
            // Assuming you have a VBox for your To-Do list
            toDoListVBox.getChildren().add(newTask);
        });
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
            // Add the selected image to the list
            Image image = new Image(selectedFile.toURI().toString());
            images.add(image);

            // Set the current index to the last image in the list (newly added)
            currentIndex = images.size() - 1;
            mediaImageView.setImage(images.get(currentIndex));

            // Update the button state
            updateButtonState();
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

    // Initialize method to set up columns
    @FXML
    public void initialize() {
        materialNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
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
        costField.setPromptText("Cost");

        // Create a layout and add the input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Material Name:"), 0, 0);
        grid.add(materialNameField, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(new Label("Cost:"), 0, 2);
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
                    showAlert("Invalid Input", "Please enter valid numeric values for Quantity and Cost.");
                    return null;
                }
            }
            return null;
        });

        // Display the dialog and wait for the user to input values
        Optional<Pair<String, Pair<Integer, Double>>> result = dialog.showAndWait();

        // If the user entered valid data, add it to the table
        result.ifPresent(materialData -> {
            String materialName = materialData.getKey();
            int quantity = materialData.getValue().getKey();
            double cost = materialData.getValue().getValue();

            // Add the material to the table
            materialsTable.getItems().add(new Material(materialName, quantity, cost));
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





