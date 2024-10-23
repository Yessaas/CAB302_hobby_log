package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.model.ContactDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller class for handling user login and account creation.
 */
public class LoginController {

    private HelloApplication app;

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
    @FXML
    private Button createAccountCancelButton;

    private ContactDAO contactDAO;

    /**
     * Constructor for LoginController.
     */
    public LoginController() {
        contactDAO = new ContactDAO();
    }

    /**
     * Sets the application instance.
     *
     * @param app the application instance
     */
    public void setApplication(HelloApplication app) {
        this.app = app;
    }

    /**
     * Navigates to the Home view.
     *
     * @throws IOException if an I/O error occurs
     */
    public void goToHome() throws IOException {
        if (app != null) {
            app.showHomeView();
        }
    }

    /**
     * Handles user login.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void onLogin() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email and password cannot be empty.");
            return;
        }

        if (contactDAO.authenticateUser(email, password)) {
            int userID = contactDAO.getUserIDByEmail(email);

            if (userID != -1) {
                app.setLoggedInUserID(userID);
                app.showHomeView();
            } else {
                showAlert("Error", "Unable to retrieve user information.");
            }
        } else {
            showAlert("Error", "Invalid email or password.");
        }
    }

    /**
     * Switches to the account creation view.
     */
    @FXML
    private void onCreateAccount() {
        try {
            app.showCreateAccountView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancels account creation and navigates back to the login view.
     */
    @FXML
    private void onCancelCreateAccount() {
        try {
            app.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles account creation.
     */
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
            showAlert("Success", "Account created successfully. You can now log in.");
        } else {
            showAlert("Error", "Account creation failed. Email may already be in use.");
        }
    }

    /**
     * Shows an alert with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the message of the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}