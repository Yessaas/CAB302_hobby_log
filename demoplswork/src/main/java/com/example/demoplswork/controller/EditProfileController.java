package com.example.demoplswork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for editing a user profile.
 */
public class EditProfileController {

    /**
     * TextField for the user's name.
     */
    @FXML
    private TextField nameField;

    /**
     * TextField for the user's bio.
     */
    @FXML
    private TextField bioField;

    /**
     * The dialog stage for this controller.
     */
    private Stage dialogStage;

    /**
     * Sets the dialog stage.
     *
     * @param dialogStage the dialog stage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Called when the save button is clicked.
     * Saves the profile information and closes the dialog.
     */
    @FXML
    private void onSaveProfile() {
        String name = nameField.getText();
        String bio = bioField.getText();

        System.out.println("Saving profile: Name = " + name + ", Bio = " + bio);
        dialogStage.close();
    }

    /**
     * Called when the cancel button is clicked.
     * Closes the dialog without saving.
     */
    @FXML
    private void onCancel() {
        dialogStage.close();
    }
}