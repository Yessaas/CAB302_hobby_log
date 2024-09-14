package com.example.demoplswork;

import javafx.application.Application; // Make sure this import is present
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

import java.io.IOException;

public class HelloApplication extends Application { // Ensure this extends Application
    public static final String TITLE = "Address Book";
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;

        // Get screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the initial view to logs-view.fxml
        showLogsView();

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

    public void showLogsCreatorView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("my-logs-creator.fxml"));
        Parent root = fxmlLoader.load();
        LogsCreatorView controller = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args); // Ensure you pass args to launch()
    }
}
