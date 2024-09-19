package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.awt.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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




    ArrayList<String> hobbys = new ArrayList<>
            (
            Arrays.asList
                    ("Woodworking", "PC Building", "Miniatures", "Music Production", "Coding", "Cooking","Gardening","Digital Art","Traditional Art")
            );
@FXML
    private TextField searchBar;

@FXML
    private ListView<String> listView;

@FXML
    void search(ActionEvent event)
    {
    listView.getItems().clear();
    listView.getItems().addAll(searchList(searchBar.getText(),hobbys));
    }

    @FXML
    public void initialize()
    {
        listView.getItems().addAll(hobbys);
    }

    private List<String> searchList(String searchHobby, List<String> listOfStrings) {

    List<String> searchHobbyArray = Arrays.asList(searchHobby.trim().split(" "));

       return listOfStrings.stream().filter(input ->{
           return searchHobbyArray.stream().allMatch(hobby ->
                   input.toLowerCase().contains(hobby.toLowerCase()));
       }).collect(Collectors.toList());
    }

}

