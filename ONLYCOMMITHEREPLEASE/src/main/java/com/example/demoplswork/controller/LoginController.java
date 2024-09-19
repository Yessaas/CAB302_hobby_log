package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.model.SqliteContactDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private HelloApplication app;

    // Fields for the login and account creation form
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private Button loginButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private Button createAccountSubmitButton;



    private SqliteContactDAO contactDAO;

    // Constructor
    public LoginController() {
        contactDAO = new SqliteContactDAO();
    }

    public void setApplication(HelloApplication app) {
        this.app = app;
    }

    public void goToHome() throws IOException {
        if (app != null) {
            app.showHomeView();
        }
    }

    // Method for handling user login
    @FXML
    private void onLogin() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email and password cannot be empty.");
            return;
        }

        if (contactDAO.authenticateUser(email, password)) {
            // Handle successful login, such as redirecting to the main view
            System.out.println("Login successful!");
            app.showHomeView();
        } else {
            // Handle failed login, show error message
            System.out.println("Invalid credentials.");
            showAlert("Error", "Invalid email or password.");
        }
    }

    // Method to switch to account creation view
    @FXML
    private void onCreateAccount() {
        try {
            app.showCreateAccountView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method for handling account creation
    @FXML
    private void onCreateAccountSubmit() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields must be filled out.");
            return;
        }

        if (contactDAO.createAccount(firstName, lastName, email, password)) {
            try {
                app.showLoginView();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Account created successfully!");
            showAlert("Success", "Account created successfully. You can now log in.");
        } else {
            // Handle failure in account creation
            System.out.println("Account creation failed.");
            showAlert("Error", "Account creation failed. Email may already be in use.");
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}