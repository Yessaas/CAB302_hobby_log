package com.example.demoplswork.events;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.InputStream;
/**
 * LogEventCell class is a custom JavaFX HBox component for displaying log events.
 * It displays the user's profile image, username, event description, and timestamp.
 * It has a constructor that takes a LogEvent object, username, profile image, likes, and comments.
 * It sets up the layout and styling for the log event cell.
 * It loads the user's profile image from a specified path.
 * It creates labels for the username, event description, and timestamp.
 * It aligns the timestamp to the right and sets the alignment for the HBox.
 */
public class LogEventCell extends HBox {

    /**
     * Constructor for LogEventCell class.
     * @param event LogEvent object containing event details.
     * @param username Username of the user who triggered the event.
     * @param profileImage Profile image of the user.
     * @param likes Number of likes for the event.
     * @param comments Number of comments for the event.
     */
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
        VBox userDetails = new VBox(5);
        Label usernameLabel = new Label(username);
        usernameLabel.setFont(new Font(16));
        Label eventDescription = new Label(event.getDescription());

        userDetails.getChildren().addAll(usernameLabel, eventDescription);

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
        // Right section (timestamp)
        Label timestampLabel = new Label(event.getTimestamp());  // Use the event timestamp
        timestampLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Align the timestamp to the right using HBox's alignment feature
        HBox.setHgrow(timestampLabel, javafx.scene.layout.Priority.ALWAYS);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(avatar, userDetails, timestampLabel);

    }
}

