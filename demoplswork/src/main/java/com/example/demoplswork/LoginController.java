package com.example.demoplswork;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private HelloApplication mainApp;

    private final String credentialsFile = "credentials.txt";

    public void setMainApp(HelloApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (validateLogin(username, password)) {
            try {
                mainApp.showExploreView();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Failed to load the Explore View.");
            }
        } else {
            showLoginError();
        }
    }

    @FXML
    private void handleForgotPassword() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText(null);
        alert.setContentText("Forgot password functionality is not implemented yet.");
        alert.showAndWait();
    }

    @FXML
    private void handleCreateAccount() {
        try {
            mainApp.showSignUpView();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the Sign Up View.");
        }
    }


    private boolean validateLogin(String username, String password) {
        String[] storedCredentials = readCredentials();
        if (storedCredentials == null || storedCredentials.length < 2) {
            showError("No account found. Please sign up first.");
            return false;
        }
        return storedCredentials[0].equals(username) && storedCredentials[1].equals(password);
    }

    private void showLoginError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText("Invalid Credentials");
        alert.setContentText("Please check your username and password.");
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String[] readCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFile))) {
            String username = reader.readLine();
            String password = reader.readLine();
            if (username != null && password != null) {
                return new String[]{username, password};
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
