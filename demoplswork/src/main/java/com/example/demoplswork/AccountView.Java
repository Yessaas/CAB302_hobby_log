package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import javafx.fxml.FXML;
import java.io.IOException;

public class AccountView {
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
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();
        }
    }

    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();  // Navigate to Explore view
        }
    }


}

