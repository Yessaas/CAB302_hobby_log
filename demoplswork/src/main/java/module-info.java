module com.example.demoplswork {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.junit.jupiter.api;
    requires javafx.media;

    opens com.example.demoplswork to javafx.fxml;
    exports com.example.demoplswork;
    exports com.example.demoplswork.controller;
    opens com.example.demoplswork.controller to javafx.fxml;
    exports com.example.demoplswork.model;
    opens com.example.demoplswork.model to javafx.fxml;
    exports com.example.demoplswork.events;
    opens com.example.demoplswork.events to javafx.fxml;
}
