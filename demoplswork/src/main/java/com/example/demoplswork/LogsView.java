package com.example.demoplswork;

import javafx.fxml.FXML;
import java.io.IOException;

public class LogsView {
    private HelloApplication app;

    public void setApplication(HelloApplication app) {
        this.app = app;
    }

    @FXML
    public void goToHome() throws IOException {
        if (app != null) {
            app.showHomeView();  // Navigate to Home view
        }
    }

    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();
        }
    }
}