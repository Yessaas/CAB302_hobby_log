package com.example.demoplswork.events;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LogEventCell extends HBox {

    public LogEventCell(LogEvent event, String username, String profileImage, int likes, int comments) {
        super(10);  // Add spacing between elements
        setPadding(new Insets(10));

        // Image for the user profile or avatar
        String relativePath = "/images/" + profileImage;

        // Load the image using the image name
        InputStream imageStream = getClass().getResourceAsStream(relativePath);
        ImageView avatar = null;
        if (imageStream != null) {
            Image image = new Image(imageStream);
            avatar = new ImageView(image);
            avatar.setFitHeight(50);
            avatar.setFitWidth(50);
        }

        // User details (username, event description)
        VBox userDetails = new VBox(5);  // Contains the username and event description
        Label usernameLabel = new Label(username);  // The username that should be displayed
        usernameLabel.setFont(new Font(16));  // Set a larger font size for the username
        Label eventDescription = new Label(event.getDescription());

        userDetails.getChildren().addAll(usernameLabel, eventDescription);  // Ensure both username and description are added

        // Likes and Comments HBox
        HBox likesComments = new HBox();
        likesComments.setSpacing(10);

        // Likes Label and Icon
        Label likesLabel = new Label(String.valueOf(likes)); // Use provided likes count
        InputStream likeStream = getClass().getResourceAsStream("/images/like-icon.png");
        ImageView likeIcon = new ImageView(new Image(likeStream));
        likeIcon.setFitHeight(20);
        likeIcon.setFitWidth(20);

        // Comments Label and Icon
        Label commentsLabel = new Label(String.valueOf(comments) + " Comments"); // Use provided comments count
        InputStream commentStream = getClass().getResourceAsStream("/images/comment-icon.png");
        ImageView commentIcon = new ImageView(new Image(commentStream));
        commentIcon.setFitHeight(20);
        commentIcon.setFitWidth(20);

        // Add like and comment elements to the HBox
        likesComments.getChildren().addAll(likesLabel, likeIcon, commentsLabel, commentIcon);

        // **Right-align the likesComments HBox**
        HBox.setHgrow(likesComments, Priority.ALWAYS);
        likesComments.setAlignment(Pos.CENTER_RIGHT);

        // Right section (timestamp)
        Label timestampLabel = new Label(event.getTimestamp());  // Use the event timestamp
        timestampLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Combine everything in the main layout
        VBox eventContainer = new VBox(5);
        eventContainer.getChildren().addAll(userDetails, timestampLabel);  // Add userDetails which includes the username and event description

        // Align the timestamp and event description on the left
        VBox.setVgrow(eventContainer, Priority.ALWAYS);

        // Add the avatar, event details (userDetails), and right-aligned likes/comments
        getChildren().addAll(avatar, eventContainer, likesComments);

        // Handle click event for both the comments icon and comments text
        EventHandler<MouseEvent> commentClickHandler = e -> showCommentsPopup(event);
        commentIcon.setOnMouseClicked(commentClickHandler);
        commentsLabel.setOnMouseClicked(commentClickHandler);

        // Handle hover effect for comments label and icon
        EventHandler<MouseEvent> underlineHandler = new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                commentsLabel.setStyle("-fx-underline: true;");
            }
        };
        EventHandler<MouseEvent> removeUnderlineHandler = new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                commentsLabel.setStyle("-fx-underline: false;");
            }
        };

        commentIcon.setOnMouseEntered(underlineHandler);
        commentIcon.setOnMouseExited(removeUnderlineHandler);
        commentsLabel.setOnMouseEntered(underlineHandler);
        commentsLabel.setOnMouseExited(removeUnderlineHandler);
    }

    private void showCommentsPopup(LogEvent event) {
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



        // Layout: Add scrollPane and new comment input
        contentBox.getChildren().addAll(scrollPane);
        dialog.getDialogPane().setContent(contentBox);

        // Show the dialog
        dialog.showAndWait();
    }


}


/*// Likes and Comments section
        VBox likesComments = new VBox(5);
        HBox likesBox = new HBox(5);
        HBox commentsBox = new HBox(5);

        // Heart icon for likes
        ImageView likeIcon = new ImageView(new Image(getClass().getResourceAsStream("/path/to/heart_icon.png")));
        likeIcon.setFitHeight(20);
        likeIcon.setFitWidth(20);
        Label likeCount = new Label(String.valueOf(likes));

        // Comment icon
        ImageView commentIcon = new ImageView(new Image(getClass().getResourceAsStream("/path/to/comment_icon.png")));
        commentIcon.setFitHeight(20);
        commentIcon.setFitWidth(20);
        Label commentCount = new Label(String.valueOf(comments));

        // Add icons and labels to the likes and comments boxes
        likesBox.getChildren().addAll(likeIcon, likeCount);
        commentsBox.getChildren().addAll(commentIcon, commentCount);

        likesComments.getChildren().addAll(likesBox, commentsBox);*/

// Add all elements to the HBox