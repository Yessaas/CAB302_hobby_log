package com.example.demoplswork;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    private HelloApplication mainApp;

    private final String credentialsFile = "credentials.txt";

    public void setMainApp(HelloApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleSignUp() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (validateInput(username, password, confirmPassword)) {
            saveCredentials(username, password);
            showSuccess();
        }
    }

    @FXML
    private void handleCancel() {
        try {
            mainApp.showLoginView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateInput(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields.");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Created");
        alert.setHeaderText(null);
        alert.setContentText("Account created successfully.");
        alert.setOnHidden(evt -> {
            try {
                mainApp.showLoginView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        alert.showAndWait();
    }

    private void saveCredentials(String username, String password) {
        try (FileWriter writer = new FileWriter(credentialsFile)) {
            writer.write(username + "\n" + password);
        } catch (IOException e) {
            showError("Failed to save credentials.");
            e.printStackTrace();
        }
    }
}
