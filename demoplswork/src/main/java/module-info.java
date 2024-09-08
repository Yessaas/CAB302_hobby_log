module com.example.demoplswork {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demoplswork to javafx.fxml;
    exports com.example.demoplswork;
}