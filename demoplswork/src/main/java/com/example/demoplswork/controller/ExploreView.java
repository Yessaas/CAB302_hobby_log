package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.events.EndEvent;
import com.example.demoplswork.events.ImageEvent;
import com.example.demoplswork.events.LogEvent;
import com.example.demoplswork.events.LogEventDAO;
import com.example.demoplswork.model.Contact;
import com.example.demoplswork.model.ContactDAO;
import com.example.demoplswork.model.LogsDAO;
import com.example.demoplswork.model.ProfileDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExploreView {

    private HelloApplication app;
    private ContextMenu accountMenu;
    private ContactDAO contactDAO;
    private LogEventDAO logEventDAO;

    @FXML
    private VBox commentsContainer;

    ArrayList<String> hobbys = new ArrayList<>(
            Arrays.asList("Woodworking", "PC Building", "Miniatures", "Music Production", "Coding", "Cooking", "Gardening", "Digital Art", "Traditional Art")
    );

    public ExploreView () throws SQLException {
        this.logEventDAO = new LogEventDAO();
    }

    @FXML
    private VBox commentsContainer1;

    @FXML
    private Button accountButton;

    public void setApplication(HelloApplication app) {
        this.app = app;
        int loggedInUserId = app.getLoggedInUserID(); // Assuming you have a way to get this ID
        loadMyFeed(loggedInUserId);
    }

    @FXML
    public void initialize() {
        accountMenu = new ContextMenu();
        hobbyChoiceBox.getItems().addAll(hobbys);

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

    private List<String> searchList(String searchHobby, List<String> listOfStrings) {
        List<String> searchHobbyArray = Arrays.asList(searchHobby.trim().split(" "));
        return listOfStrings.stream().filter(input -> {
            return searchHobbyArray.stream().allMatch(hobby ->
                    input.toLowerCase().contains(hobby.toLowerCase()));
        }).collect(Collectors.toList());
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
    private Label introLine1;

    @FXML
    private Label introLine2;

    @FXML
    public void viewBlog(ActionEvent event) {
        String blogIntro = "This is a detailed view of the blog.";

        String blogContent = blogIntro + "\n\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. \n" +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. \n" +
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Blog Content");
        alert.setHeaderText(null);
        alert.setContentText(blogContent);
        alert.showAndWait();
    }

    @FXML
    private ComboBox<String> hobbyChoiceBox;

    @FXML
    void searchByHobby(ActionEvent event) {
        /*listView.getItems().clear();

        String selectedHobby = hobbyChoiceBox.getValue();
        if (selectedHobby != null && !selectedHobby.isEmpty()) {
            listView.getItems().addAll(searchList(selectedHobby, hobbys));
        }*/
    }
    @FXML
    private void showCreateBlogDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create New Blog");

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);


        TextField introField = new TextField();
        introField.setPromptText("Enter Blog Introduction");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Enter Detailed Description");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Woodworking", "PC Building", "Miniatures", "Music Production",
                "Coding", "Cooking", "Gardening", "Digital Art", "Traditional Art");
        categoryComboBox.setPromptText("Select Category");

        Button uploadImageButton = new Button("Upload Image");
        Label imagePathLabel = new Label("No image selected");

        uploadImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                imagePathLabel.setText(selectedFile.getAbsolutePath());
            }
        });

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(
                new Label("Blog Introduction:"), introField,
                new Label("Detailed Description:"), descriptionArea,
                new Label("Category:"), categoryComboBox,
                uploadImageButton, imagePathLabel
        );
        dialog.getDialogPane().setContent(dialogContent);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                String intro = introField.getText();
                String description = descriptionArea.getText();
                String category = categoryComboBox.getValue();
                String imagePath = imagePathLabel.getText();

                if (intro.isEmpty() || description.isEmpty() || category == null || imagePath.equals("No image selected")) {
                    Alert errorAlert = new Alert(AlertType.ERROR, "Please fill all fields, select a category, and upload an image.");
                    errorAlert.showAndWait();
                } else {
                    System.out.println("Blog Created:\nIntro: " + intro + "\nDescription: " + description +
                            "\nCategory: " + category + "\nImage Path: " + imagePath);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }



    public void loadMyFeed(int loggedInUserId) {
        try {
            LogEventDAO logEventDAO = new LogEventDAO();
            List<LogEvent> events = logEventDAO.getLogEventsForOtherUsers(loggedInUserId);

            // Group the events by date (yyyy-MM-dd format)
            Map<LocalDate, List<LogEvent>> eventsByDate = events.stream()
                    .collect(Collectors.groupingBy(event -> LocalDate.parse(event.getTimestamp().substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

            // Sort the dates in descending order
            List<LocalDate> sortedDates = eventsByDate.keySet().stream()
                    .sorted(Comparator.reverseOrder()) // Sort in descending order
                    .collect(Collectors.toList());

            // Iterate over each sorted date group and add them to the feed
            for (LocalDate date : sortedDates) {
                List<LogEvent> eventsForDate = eventsByDate.get(date);

                // Reverse the list to show the most recent events first
                Collections.reverse(eventsForDate);


                // Create a date header label
                Label dateHeader = new Label(date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
                dateHeader.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 0 10 0;");

                // Add the date header to the feed
                commentsContainer1.getChildren().add(dateHeader);

                // Add all events for that date
                for (LogEvent event : eventsForDate) {
                    if (event instanceof EndEvent || event instanceof ImageEvent) {
                        addEventToFeed(event, true); // true indicates these can have images
                    } else {
                        addEventToFeed(event, false); // Other events without images
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addEventToFeed(LogEvent event, boolean hasImage) {
        LogsDAO logsDAO = new LogsDAO();

        // Check if the associated log exists
        if (!logsDAO.doesLogExist(event.getLogId())) {
            System.out.println("Log ID " + event.getLogId() + " does not exist. Skipping event.");
            return; // Skip this event if the log does not exist
        }

        StackPane postContainer = new StackPane();
        postContainer.setPrefHeight(200.0);
        postContainer.setPrefWidth(500.0);
        postContainer.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        VBox postContent = new VBox();
        postContent.setPrefHeight(150.0);
        postContent.setPrefWidth(300.0);
        postContent.setSpacing(10);
        postContent.setStyle("-fx-padding: 30;");

        // Fetch user details
        Contact contact = getContactForUserId(event.getUserId());
        String username = contact != null ? contact.getFirstName() : "Unknown User";
        String profilePhotoPath = contact != null ? contact.getPhoto() : "/images/account_circle.png";

        // Profile Image and Username
        HBox header = new HBox(15); // Added spacing
        ImageView profileImage = new ImageView();
        profileImage.setFitHeight(50.0);
        profileImage.setFitWidth(50.0);

        // Load profile image
        try {
            InputStream profileImageStream = getClass().getResourceAsStream("/images/" + profilePhotoPath);
            if (profileImageStream != null) {
                profileImage.setImage(new Image(profileImageStream));
            } else {
                System.out.println("Profile image not found: " + profilePhotoPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create bold label for the username
        Label boldUser = new Label(username + ": ");
        boldUser.setStyle("-fx-font-weight: bold;");

        // Create an HBox to join bold username and description
        HBox userDescription = new HBox(boldUser, new Label(event.getDescription()));
        userDescription.setSpacing(5);


        Label logNameLabel = new Label("Project: " + event.getLogName(event.getLogId())); // Use method to get log name
        logNameLabel.setStyle("-fx-font-style: italic;");

        // Adjust Header Layout
        VBox headerText = new VBox(5); // Vertical arrangement for username + description and project name
        headerText.getChildren().addAll(userDescription, logNameLabel);

        // Add components to the header HBox
        header.getChildren().addAll(profileImage, headerText);

        // Optional Image for specific event types
        StackPane imageContainer = new StackPane();
        if (hasImage && event instanceof ImageEvent) {
            ImageEvent imageEvent = (ImageEvent) event;
            ImageView eventImage = new ImageView();
            eventImage.setFitHeight(150);
            eventImage.setFitWidth(200);
            eventImage.setPreserveRatio(true);
            String imageName = imageEvent.getImagePath();
            String relativePath = "/images/" + imageName;

            // Load the image using the image name
            InputStream imageStream = getClass().getResourceAsStream(relativePath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                eventImage.setImage(image);
            }
            imageContainer.getChildren().add(eventImage);
        }

        // Likes and Comments Controls
        Label likeCountLabel = new Label(event.getLikes().size() + " Likes");
        Label commentCountLabel = new Label(event.getComments().size() + " Comments");
        HBox likeCommentControls = new HBox();
        likeCommentControls.setSpacing(10);

        Button likeButton = new Button(event.getLikes().contains(app.getLoggedInUserID()) ? "Unlike" : "Like");
        likeButton.setOnAction(e -> {
            toggleLike(event, likeButton, likeCountLabel);
        });

        HBox countLabels = new HBox(likeCountLabel,commentCountLabel);
        countLabels.setSpacing(10);

        Button commentButton = new Button("Comment");
        commentButton.setOnAction(e -> showCommentsPopup(event, commentCountLabel)); // Open comments popup

        likeCommentControls.getChildren().addAll(likeButton, commentButton);

        // Add elements to the postContent VBox
        postContent.getChildren().addAll(header, imageContainer, countLabels, likeCommentControls);

        // Add the post content to the main post container
        postContainer.getChildren().add(postContent);

        // Add the post container to the main feed
        commentsContainer1.getChildren().add(postContainer);
    }




    private Contact getContactForUserId(int userId) {
        contactDAO = new ContactDAO();

        // Fetch the contact information for the logged-in user
        Contact contact = contactDAO.getContactById(userId);

        if (contact == null) {
            System.out.println("No contact found for user ID: " + userId);
            return null; // Safely return if no contact found
        }

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

    private void showCommentsPopup(LogEvent event, Label commentCountLabel) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Comments");

        // Set dialog buttons
        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(closeButtonType);

        // Main content VBox
        VBox contentBox = new VBox(10);
        contentBox.setPrefWidth(400);
        contentBox.setStyle("-fx-padding: 20;");

        // Fetch and display existing comments
        List<String> comments = new ArrayList<>(event.getCommentsFromDatabase());
        System.out.println(comments);
        VBox commentsBox = new VBox(5);
        if (comments != null) {
            for (String comment : comments) {
                Label commentLabel = new Label(comment);
                commentsBox.getChildren().add(commentLabel);
            }
        }

        // Add commentsBox to a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(commentsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200); // Adjust height as needed

        // Text field for adding a new comment
        TextField newCommentField = new TextField();
        newCommentField.setPromptText("Write a comment...");

        Button addCommentButton = new Button("Add Comment");
        addCommentButton.setOnAction(e -> {
            String newComment = newCommentField.getText().trim();
            if (!newComment.isEmpty()) {
                // Fetch the current user's username
                String currentUsername = getCurrentUsername(); // Implement this method to get the logged-in user's name

                // Format the new comment as "Username: Comment Text"
                String formattedComment = currentUsername + ": " + newComment;
                commentsBox.getChildren().add(new Label(formattedComment));
                newCommentField.clear();

                // Save the new comment to the database (implement save logic)
                saveCommentForLog(event.getId(), formattedComment);

                // Update the comment list and label
                comments.add(formattedComment);
                event.setComments(comments); // Ensure the event's comments list is updated
                commentCountLabel.setText(comments.size() + " Comments");
            }
        });

        // Layout: Add scrollPane and new comment input
        contentBox.getChildren().addAll(scrollPane, newCommentField, addCommentButton);
        dialog.getDialogPane().setContent(contentBox);

        // Show the dialog
        dialog.showAndWait();
    }



    private String getCurrentUsername() {
        Contact loggedIn = getContactForUserId(app.getLoggedInUserID());

        return loggedIn.getFirstName();
    }

    private void saveCommentForLog(int eventId, String comment) {
        logEventDAO.saveCommentForLog(eventId,comment );
    }

    private void toggleLike(LogEvent event, Button likeButton, Label likeCountLabel) {
        int userId = app.getLoggedInUserID();
        List<Integer> likes = event.getLikes();

        if (likes.contains(userId)) {
            // User already liked the event, so unlike it
            likes.remove(Integer.valueOf(userId));
            likeButton.setText("Like");
        } else {
            // User has not liked the event, so like it
            likes.add(userId);
            likeButton.setText("Unlike");
        }

        // Update like count
        likeCountLabel.setText(likes.size() + " Likes");

        // Save updated likes to the database
        logEventDAO.updateLikesForLogEvent(event.getId(), likes);
    }





}
