package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.stage.FileChooser;
import java.io.File;

public class ExploreView {

    private HelloApplication app;
    private ContextMenu accountMenu;
    private int likesPost1 = 0;
    private int likesPost2 = 0;
    private int likesPost3 = 0;
    private int likesPost4 = 0;

    private boolean likedPost1 = false;
    private boolean likedPost2 = false;
    private boolean likedPost3 = false;
    private boolean likedPost4 = false;

    ArrayList<String> hobbys = new ArrayList<>(
            Arrays.asList("Woodworking", "PC Building", "Miniatures", "Music Production", "Coding", "Cooking", "Gardening", "Digital Art", "Traditional Art")
    );

    @FXML
    private TextField searchBar;

    @FXML
    private ListView<String> listView;

    @FXML
    private Label likeCount1;

    @FXML
    private Label likeCount2;

    @FXML
    private Label likeCount3;

    @FXML
    private Label likeCount4;

    @FXML
    private TextField commentField1;

    @FXML
    private TextField commentField2;

    @FXML
    private TextField commentField3;

    @FXML
    private TextField commentField4;

    @FXML
    private Label commentLabel1;
    @FXML
    private Label commentLabel2;
    @FXML
    private Label commentLabel3;
    @FXML
    private Label commentLabel4;

    @FXML
    private VBox commentsContainer1;
    @FXML
    private VBox commentsContainer2;
    @FXML
    private VBox commentsContainer3;
    @FXML
    private VBox commentsContainer4;

    @FXML
    private Button accountButton;

    public void setApplication(HelloApplication app) {
        this.app = app;
    }

    @FXML
    public void initialize() {
        accountMenu = new ContextMenu();
        hobbyChoiceBox.getItems().addAll(hobbys);

        MenuItem viewProfile = new MenuItem("View Profile");
        viewProfile.setOnAction(event -> {
            try {
                goToAccount();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        introLine1.setText("This is a detailed view of the blog.");
        introLine1.setStyle("-fx-padding: 0 0 0 10;");
        introLine2.setText("This is a detailed view of the blog.");
        introLine2.setStyle("-fx-padding: 0 0 0 10;");

        MenuItem logout = new MenuItem("Log Out");
        logout.setOnAction(event -> onLogout());

        accountMenu.getItems().addAll(viewProfile, logout);
    }

    private List<String> searchList(String searchHobby, List<String> listOfStrings) {
        List<String> searchHobbyArray = Arrays.asList(searchHobby.trim().split(" "));
        return listOfStrings.stream().filter(input -> {
            return searchHobbyArray.stream().allMatch(hobby ->
                    input.toLowerCase().contains(hobby.toLowerCase()));
        }).collect(Collectors.toList());
    }

    @FXML
    public void goToHome() throws IOException {
        if (app != null) {
            app.showHomeView();
        }
    }

    @FXML
    public void goToLogs() throws IOException {
        if (app != null) {
            app.showLogsView();
        }
    }

    @FXML
    private void showAccountMenu(ActionEvent event) {
        accountMenu.show(accountButton, Side.BOTTOM, 0, 0);
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

    @FXML
    public void handleLike(ActionEvent event) {
        String sourceId = ((Button) event.getSource()).getId();

        switch (sourceId) {
            case "likeButton1":
                if (!likedPost1) {
                    likesPost1++;
                    likedPost1 = true;
                    ((Button) event.getSource()).setText("Unlike");
                } else {
                    likesPost1--;
                    likedPost1 = false;
                    ((Button) event.getSource()).setText("Like");
                }
                updateLikeCount(likeCount1, likesPost1);
                break;
            case "likeButton2":
                if (!likedPost2) {
                    likesPost2++;
                    likedPost2 = true;
                    ((Button) event.getSource()).setText("Unlike");
                } else {
                    likesPost2--;
                    likedPost2 = false;
                    ((Button) event.getSource()).setText("Like");
                }
                updateLikeCount(likeCount2, likesPost2);
                break;
            case "likeButton3":
                if (!likedPost3) {
                    likesPost3++;
                    likedPost3 = true;
                    ((Button) event.getSource()).setText("Unlike");
                } else {
                    likesPost3--;
                    likedPost3 = false;
                    ((Button) event.getSource()).setText("Like");
                }
                updateLikeCount(likeCount3, likesPost3);
                break;
            case "likeButton4":
                if (!likedPost4) {
                    likesPost4++;
                    likedPost4 = true;
                    ((Button) event.getSource()).setText("Unlike");
                } else {
                    likesPost4--;
                    likedPost4 = false;
                    ((Button) event.getSource()).setText("Like");
                }
                updateLikeCount(likeCount4, likesPost4);
                break;
        }
    }

    private void updateLikeCount(Label likeCountLabel, int count) {
        if (likeCountLabel != null) {
            likeCountLabel.setText(count + " Likes");
        }
    }

    @FXML
    public void addCommentDialog(ActionEvent event) {
        String sourceId = ((Button) event.getSource()).getId();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Comment");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter your comment:");

        dialog.showAndWait().ifPresent(comment -> {
            switch (sourceId) {
                case "addCommentButton1":
                    if (!comment.isEmpty()) {
                        Label newComment = new Label(comment);
                        commentsContainer1.getChildren().add(newComment);
                    }
                    break;
                case "addCommentButton3":
                    if (!comment.isEmpty()) {
                        Label newComment = new Label(comment);
                        commentsContainer3.getChildren().add(newComment);
                    }
                    break;
            }
        });
    }

    @FXML
    private Label introLine1;

    @FXML
    private Label introLine2;

    @FXML
    public void viewBlog(ActionEvent event) {
        String blogIntro = "This is a detailed view of the blog.";
        introLine1.setText(blogIntro);
        introLine2.setText(blogIntro);
        String blogContent = blogIntro + "\n\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. \n" +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. \n" +
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Blog Content");
        alert.setHeaderText(null);
        alert.setContentText(blogContent);
        alert.showAndWait();
    }

    @FXML
    private ComboBox<String> hobbyChoiceBox;

    @FXML
    void searchByHobby(ActionEvent event) {
        listView.getItems().clear();

        String selectedHobby = hobbyChoiceBox.getValue();
        if (selectedHobby != null && !selectedHobby.isEmpty()) {
            listView.getItems().addAll(searchList(selectedHobby, hobbys));
        }
    }
    @FXML
    private void showCreateBlogDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create New Blog");

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);


        TextField introField = new TextField();
        introField.setPromptText("Enter Blog Introduction");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Enter Detailed Description");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Woodworking", "PC Building", "Miniatures", "Music Production",
                "Coding", "Cooking", "Gardening", "Digital Art", "Traditional Art");
        categoryComboBox.setPromptText("Select Category");

        Button uploadImageButton = new Button("Upload Image");
        Label imagePathLabel = new Label("No image selected");

        uploadImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                imagePathLabel.setText(selectedFile.getAbsolutePath());
            }
        });

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(
                new Label("Blog Introduction:"), introField,
                new Label("Detailed Description:"), descriptionArea,
                new Label("Category:"), categoryComboBox,
                uploadImageButton, imagePathLabel
        );
        dialog.getDialogPane().setContent(dialogContent);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                String intro = introField.getText();
                String description = descriptionArea.getText();
                String category = categoryComboBox.getValue();
                String imagePath = imagePathLabel.getText();

                if (intro.isEmpty() || description.isEmpty() || category == null || imagePath.equals("No image selected")) {
                    Alert errorAlert = new Alert(AlertType.ERROR, "Please fill all fields, select a category, and upload an image.");
                    errorAlert.showAndWait();
                } else {
                    System.out.println("Blog Created:\nIntro: " + intro + "\nDescription: " + description +
                            "\nCategory: " + category + "\nImage Path: " + imagePath);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

}
