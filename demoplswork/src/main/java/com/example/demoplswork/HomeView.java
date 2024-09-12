package com.example.demoplswork;

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
}



