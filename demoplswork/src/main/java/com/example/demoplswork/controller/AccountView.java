package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class AccountView {
    private HelloApplication app;
    private ContextMenu accountMenu;

    @FXML
    private Button accountButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Label bioLabel;

    @FXML
    private Label aboutLabel;

    @FXML
    private ImageView profileImageView;

    private String userName = "Jesse Pinkman";
    private String userBio = "My name is Henry, I mainly build projects based with timber. I love DIY as it is an easy and fun hobby.";
    private String aboutText = "Father of 3 dogs.";

    public void setApplication(HelloApplication app) {
        this.app = app;
    }

    @FXML
    public void initialize() {
        nameLabel.setText(userName);
        bioLabel.setText(userBio);
        aboutLabel.setText(aboutText);
    }

    @FXML
    private void showEditProfileDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.setHeaderText("Edit your profile information");

        VBox vbox = new VBox(10);
        vbox.setPrefSize(500, 200);
        TextField nameField = new TextField(userName);
        TextField bioField = new TextField(userBio);
        TextField aboutField = new TextField(aboutText);

        vbox.getChildren().addAll(new Label("Name:"), nameField, new Label("Bio:"), bioField, new Label("About:"), aboutField);

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                userName = nameField.getText();
                userBio = bioField.getText();
                aboutText = aboutField.getText();

                nameLabel.setText(userName);
                bioLabel.setText(userBio);
                aboutLabel.setText(aboutText);
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
            Image profileImage = new Image(selectedFile.toURI().toString());
            profileImageView.setImage(profileImage);
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
