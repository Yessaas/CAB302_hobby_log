package com.example.demoplswork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProfileController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField bioField;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void onSaveProfile() {
        String name = nameField.getText();
        String bio = bioField.getText();

        System.out.println("Saving profile: Name = " + name + ", Bio = " + bio);
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }
}
