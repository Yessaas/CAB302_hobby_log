package com.example.demoplswork;

import com.example.demoplswork.controller.*;
import com.example.demoplswork.model.Logs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.io.IOException;

public class HelloApplication extends Application {
    public static final String TITLE = "Address Book";
    private Stage primaryStage;
    private int loggedInUserID;  // Store the logged-in user's ID

    // Getter for userID
    public int getLoggedInUserID() {
        return loggedInUserID;
    }

    // Setter for userID
    public void setLoggedInUserID(int userID) {
        this.loggedInUserID = userID;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;

        // Get screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the initial view to explore-view.fxml
        showLoginView();

        // Set the stage to the full size of the screen, but not in fullscreen mode
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        primaryStage.show();
    }

    // Method to show the Explore view
    public void showExploreView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("explore-view.fxml"));
        Parent root = fxmlLoader.load();
        ExploreView controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
    }

    // Method to show the Home view
    public void showHomeView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
        Parent root = fxmlLoader.load();
        HomeView controller = fxmlLoader.getController();
        controller.setApplication(this);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void showLogsView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("my-logs.fxml"));
        Parent root = fxmlLoader.load();
        LogsView controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void showLogsUpdateView(int id, Logs log) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("my-logs-view.fxml"));
        Parent root = fxmlLoader.load();
        LogsUpdateView controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        controller.setLogId(id);  // Set the log ID in the controller
        controller.setLog(log);
    }

    public void showLoginView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public void showCreateAccountView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("create-account-view.fxml"));
        Parent root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    public void showAccountView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profile-view.fxml"));
        Parent root = fxmlLoader.load();
        ProfileView controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }


    public static void main(String[] args) {
        launch();
    }
}

