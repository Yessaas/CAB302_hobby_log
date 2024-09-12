package com.example.demoplswork;

import javafx.fxml.FXML;
import java.io.IOException;

public class ExploreView {
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
}

