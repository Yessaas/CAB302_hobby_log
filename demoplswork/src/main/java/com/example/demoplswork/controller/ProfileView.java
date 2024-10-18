package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

public class ProfileView {
    private HelloApplication app;
    private ContextMenu accountMenu;
    private ContactDAO contactDAO;
    private LogsDAO logsDAO;

    // Constructor
    public ProfileView() throws SQLException {
        logsDAO = new LogsDAO();
    }

    @FXML
    private Button accountButton;

    @FXML
    private Label nameLabel;

    @FXML
    private VBox logsContainer;

    @FXML
    private Text bioText;

    @FXML
    private ImageView profileImageView;




    private String userName;
    private String userBio;


    public void setApplication(HelloApplication app) {
        this.app = app;

        loadUser();
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

    private void loadUser() {
        int userID = app.getLoggedInUserID();
        contactDAO = new ContactDAO();

        // Fetch the contact information for the logged-in user
        Contact contact = contactDAO.getContactById(userID);

        // Now update the contact with profile details (bio, photo)
        ProfileDAO profileDAO = new ProfileDAO();
        try {
            profileDAO.insertProfile(userID, " ", " ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        profileDAO.getProfileByUserId(contact, userID);

        if (contact != null) {
            // Display first and last name
            nameLabel.setText(contact.getFirstName() + " " + contact.getLastName());
            bioText.setText(contact.getBio());
            String imageName = contact.getPhoto();
            String relativePath = "/images/" + imageName;

            // Load the image using the image name
            InputStream imageStream = getClass().getResourceAsStream(relativePath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                profileImageView.setImage(image);
            }

        }
        loadUserLogs();

    }

    // Load and display user logs
    private void loadUserLogs() {
        // Retrieve the userID from the main application class
        int userID = app.getLoggedInUserID();

        // Fetch logs from the database for the specific user
        List<Object[]> logsList = logsDAO.getLogsForUser(userID);

        // Loop through the logs and add them to the VBox
        for (Object[] logData : logsList) {
            int logID = (int) logData[0]; // Extract the log ID
            Logs log = (Logs) logData[1]; // Extract the Logs object
            HBox logEntry = createLogEntry(log, logID);
            logsContainer.getChildren().add(logEntry);
        }
    }

    // Create a dynamic HBox for each log entry
    private HBox createLogEntry(Logs log, int logID) {
        HBox logEntry = new HBox();
        logEntry.setSpacing(30);
        logEntry.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-padding: 10;");

        // Log name label
        Label logNameLabel = new Label(log.getLogName());
        logNameLabel.setStyle("-fx-font-size: 18px; -fx-font-family: 'Roboto';");
        logNameLabel.setPrefWidth(250);

        // Progress bar
        ProgressBar progressBar = new ProgressBar(log.getProgress()/ 100.0);
        progressBar.setPrefWidth(400);

        // Likes and comments section
        HBox likesComments = new HBox();
        likesComments.setSpacing(10);

        Label likesLabel = new Label(String.valueOf(0));
        InputStream imageStream = getClass().getResourceAsStream("/images/like-icon.png");
        ImageView likeIcon = new ImageView(new Image(imageStream));

        likeIcon.setFitHeight(20);
        likeIcon.setFitWidth(20);

        Label commentsLabel = new Label(String.valueOf(0));
        InputStream imgStream = getClass().getResourceAsStream("/images/comment-icon.png");
        ImageView commentIcon = new ImageView(new Image(imgStream));
        commentIcon.setFitHeight(20);
        commentIcon.setFitWidth(20);

        likesComments.getChildren().addAll(likesLabel, likeIcon, commentsLabel, commentIcon);

        // "View Log" button
        Button viewLogButton = new Button("View Log");
        viewLogButton.setPrefWidth(120);
        viewLogButton.setStyle("-fx-background-color: #d3d3d3;");
        viewLogButton.setOnAction(e -> {
            try {
                goToUpdateLogs(logID, log);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });  // Implement viewLog method to handle button click

        // Add all elements to the HBox
        logEntry.getChildren().addAll(logNameLabel, progressBar, likesComments, viewLogButton);

        return logEntry;
    }


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
    private void showEditProfileDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Bio");
        dialog.setHeaderText("Edit your profile information");

        VBox vbox = new VBox(10);
        vbox.setPrefSize(300, 200);


        TextArea bioArea = new TextArea(userBio);
        bioArea.setPrefHeight(100); // Set the preferred height to allow multiple lines
        bioArea.setWrapText(true);  // Ensure text wraps within the TextArea
        bioArea.setText(bioText.getText());

        vbox.getChildren().addAll(new Label("Bio:"), bioArea);

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                userBio = bioArea.getText();



                bioText.setText(userBio);
                // Update bio in the database
                int userID = app.getLoggedInUserID();
                ProfileDAO profileDAO = new ProfileDAO();
                try {
                    profileDAO.updateProfile(userID, userBio, null);  // update bio only
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleChangeProfilePhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {

        }

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

                // display image
                Image profileImage = new Image(selectedFile.toURI().toString());
                profileImageView.setImage(profileImage);

                int userID = app.getLoggedInUserID();
                ProfileDAO profileDAO = new ProfileDAO();
                String imageName = selectedFile.getName();
                profileDAO.updateProfile(userID, null, imageName);  // update bio only

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to add image");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void goToHome() throws IOException {
        if (app != null) {
            app.showHomeView();
        }
    }

    @FXML
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();
        }
    }

    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();
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
}