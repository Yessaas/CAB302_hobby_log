package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import javafx.fxml.FXML;
import java.io.IOException;

public class HomeView {
    private HelloApplication app;

    public void setApplication(HelloApplication app) {
        this.app = app;
    }

    @FXML
    public void goToExplore() throws IOException {
        if (app != null) {
            app.showExploreView();  // Navigate to Explore view
        }
    }

    @FXML
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();
        }
    }
    @FXML
    public void goToAccount() throws IOException {
        if (app != null) {
            app.showAccountView();
        }
    }
    @FXML
    private void onLogout() {
        try {
            app.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
