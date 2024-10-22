package com.example.demoplswork.controller;

import com.example.demoplswork.HelloApplication;
import com.example.demoplswork.events.EndEvent;
import com.example.demoplswork.events.ImageEvent;
import com.example.demoplswork.events.LogEvent;
import com.example.demoplswork.events.LogEventDAO;
import com.example.demoplswork.model.Contact;
import com.example.demoplswork.model.ContactDAO;
import com.example.demoplswork.model.LogsDAO;
import com.example.demoplswork.model.ProfileDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import com.example.demoplswork.model.Blog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExploreView {

    private HelloApplication app;
    private ContextMenu accountMenu;
    private ContactDAO contactDAO;
    private LogEventDAO logEventDAO;
    private List<Blog> blogs = new ArrayList<>();

    @FXML
    private VBox commentsContainer;

    ArrayList<String> hobbys = new ArrayList<>(
            Arrays.asList("Woodworking", "PC Building", "Miniatures", "Music Production", "Coding", "Cooking", "Gardening", "Digital Art", "Traditional Art")
    );

    public ExploreView() throws SQLException {
        this.logEventDAO = new LogEventDAO();
    }

    @FXML
    private VBox commentsContainer1;

    @FXML
    private Button accountButton;

    public void setApplication(HelloApplication app) {
        this.app = app;
        int loggedInUserId = app.getLoggedInUserID();
        loadMyFeed(loggedInUserId);
    }

    @FXML
    public void initialize() {
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> searchBlogs());
        blogContainer.getChildren().clear();
        loadBlogsFromDatabase();
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

        MenuItem logout = new MenuItem("Log Out");
        logout.setOnAction(event -> onLogout());

        accountMenu.getItems().addAll(viewProfile, logout);
    }

    private List<String> searchList(String searchHobby, List<String> listOfStrings) {
        List<String> searchHobbyArray = Arrays.asList(searchHobby.trim().split(" "));
        return listOfStrings.stream().filter(input -> searchHobbyArray.stream().allMatch(hobby -> input.toLowerCase().contains(hobby.toLowerCase()))).collect(Collectors.toList());
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
    private Label introLine1;

    @FXML
    private Label introLine2;

    @FXML
    public void viewBlog(ActionEvent event) {
        String blogIntro = "This is a detailed view of the blog.";

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
        searchBlogs();
    }

    private String saveImageToDirectory(File selectedFile) throws IOException {
        File imageDir = new File("images/uploads");
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        String newFileName = UUID.randomUUID().toString() + "_" + selectedFile.getName();
        File destinationFile = new File(imageDir, newFileName);
        Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return destinationFile.getAbsolutePath();
    }

    @FXML
    private TextField searchTextField;

    @FXML
    private void searchBlogs() {
        String keyword = (searchTextField.getText() != null) ? searchTextField.getText().trim().toLowerCase() : "";
        String selectedCategory = hobbyChoiceBox.getValue();

        if (keyword.isEmpty() && (selectedCategory == null || selectedCategory.isEmpty())) {
            blogContainer.getChildren().clear();
            blogs.forEach(this::displayBlog);
            return;
        }

        List<Blog> filteredBlogs = blogs.stream()
                .filter(blog -> {
                    boolean matchesKeyword = (keyword.isEmpty() ||
                            blog.getIntro().toLowerCase().contains(keyword) ||
                            blog.getDescription().toLowerCase().contains(keyword));
                    boolean matchesCategory = (selectedCategory == null || selectedCategory.isEmpty() ||
                            selectedCategory.equals(blog.getCategory()));
                    return matchesKeyword && matchesCategory;
                })
                .collect(Collectors.toList());

        blogContainer.getChildren().clear();
        filteredBlogs.forEach(this::displayBlog);
    }

    private String getCurrentUsername() {
        int userID = app.getLoggedInUserID();
        contactDAO = new ContactDAO();
        Contact contact = contactDAO.getContactById(userID);

        if (contact != null) {
            return contact.getFirstName() + " " + contact.getLastName();
        }
        return "Unknown User";
    }

    public VBox blogContainer;

    public void showCreateBlogDialog() {
        try {
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

            VBox dialogContent = new VBox(10);
            dialogContent.getChildren().addAll(
                    new Label("Blog Introduction:"), introField,
                    new Label("Detailed Description:"), descriptionArea,
                    new Label("Category:"), categoryComboBox
            );
            dialog.getDialogPane().setContent(dialogContent);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == submitButtonType) {
                    try {
                        String intro = introField.getText();
                        String description = descriptionArea.getText();
                        String category = categoryComboBox.getValue();

                        if (intro.isEmpty() || description.isEmpty() || category == null) {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Please fill all fields and select a category.");
                            errorAlert.showAndWait();
                            return null;
                        } else {
                            String fullName = getCurrentUserFullName();
                            Blog blog = new Blog(intro, description, category, fullName);
                            saveBlogToDatabase(blog);
                            displayBlog(blog);
                            Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION, "Blog Created Successfully!");
                            confirmationAlert.showAndWait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while creating the blog. Check the console for details.");
                        errorAlert.showAndWait();
                    }
                }
                return null;
            });

            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An unexpected error occurred while creating the dialog.");
            errorAlert.showAndWait();
        }
    }

    private void displayBlog(Blog blog) {
        StackPane blogEntry = new StackPane();
        blogEntry.setPrefHeight(200.0);
        blogEntry.setPrefWidth(500.0);
        blogEntry.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        VBox blogContent = new VBox();
        blogContent.setPrefHeight(150.0);
        blogContent.setPrefWidth(300.0);
        blogContent.setSpacing(15);
        blogContent.setStyle("-fx-padding: 30;");

        Label titleLabel = new Label(blog.getIntro());
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-family: 'Roboto'; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);

        HBox blogHeader = new HBox(15);
        Label usernameLabel = new Label(getCurrentUsername());
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        VBox headerBox = new VBox(5);
        headerBox.getChildren().addAll(usernameLabel, titleLabel);

        blogContent.getChildren().add(headerBox);

        Button openArticleButton = new Button("Open Article");
        openArticleButton.setOnAction(e -> viewBlogContent(blog));

        blogContent.getChildren().addAll(openArticleButton);

        blogEntry.getChildren().add(blogContent);

        if (blogContainer == null) {
            System.err.println("Error: blogContainer is null!");
            return;
        }

        blogContainer.getChildren().add(blogEntry);
    }

    private void loadBlogsFromDatabase() {
        blogs.clear();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:contacts.db")) {
            String sql = "SELECT * FROM Blogs";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String intro = resultSet.getString("intro");
                    String description = resultSet.getString("description");
                    String category = resultSet.getString("category");

                    Blog blog = new Blog(intro, description, category, getCurrentUserFullName());
                    blogs.add(blog);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        blogContainer.getChildren().clear();
        blogs.forEach(this::displayBlog);
    }

    private String getCurrentUserFullName() {
        int userID = app.getLoggedInUserID();
        contactDAO = new ContactDAO();
        Contact contact = contactDAO.getContactById(userID);

        if (contact != null) {
            return contact.getFirstName() + " " + contact.getLastName();
        }
        return "Unknown User";
    }

    private void saveBlogToDatabase(Blog blog) {
        String dbUrl = "jdbc:sqlite:contacts.db";

        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            String sql = "INSERT INTO Blogs (intro, description, category, username) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, blog.getIntro());
                statement.setString(2, blog.getDescription());
                statement.setString(3, blog.getCategory());
                statement.setString(4, getCurrentUsername());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewBlogContent(Blog blog) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Blog Content");
        alert.setHeaderText(blog.getIntro());
        alert.setContentText(blog.getDescription());
        alert.showAndWait();
    }

    public void loadMyFeed(int loggedInUserId) {
        try {
            LogEventDAO logEventDAO = new LogEventDAO();
            List<LogEvent> events = logEventDAO.getLogEventsForOtherUsers(loggedInUserId);

            Map<LocalDate, List<LogEvent>> eventsByDate = events.stream()
                    .collect(Collectors.groupingBy(event -> LocalDate.parse(event.getTimestamp().substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

            List<LocalDate> sortedDates = eventsByDate.keySet().stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            for (LocalDate date : sortedDates) {
                List<LogEvent> eventsForDate = eventsByDate.get(date);
                Collections.reverse(eventsForDate);

                Label dateHeader = new Label(date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
                dateHeader.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 0 10 0;");
                commentsContainer1.getChildren().add(dateHeader);

                for (LogEvent event : eventsForDate) {
                    if (event instanceof EndEvent || event instanceof ImageEvent) {
                        addEventToFeed(event, true);
                    } else {
                        addEventToFeed(event, false);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addEventToFeed(LogEvent event, boolean hasImage) {
        LogsDAO logsDAO = new LogsDAO();

        if (!logsDAO.doesLogExist(event.getLogId())) {
            return;
        }

        StackPane postContainer = new StackPane();
        postContainer.setPrefHeight(200.0);
        postContainer.setPrefWidth(500.0);
        postContainer.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        VBox postContent = new VBox();
        postContent.setPrefHeight(150.0);
        postContent.setPrefWidth(300.0);
        postContent.setSpacing(10);
        postContent.setStyle("-fx-padding: 30;");

        Contact contact = getContactForUserId(event.getUserId());
        String username = contact != null ? contact.getFirstName() : "Unknown User";
        String profilePhotoPath = contact != null ? contact.getPhoto() : "/images/account_circle.png";

        HBox header = new HBox(15);
        ImageView profileImage = new ImageView();
        profileImage.setFitHeight(50.0);
        profileImage.setFitWidth(50.0);

        try {
            InputStream profileImageStream = getClass().getResourceAsStream("/images/" + profilePhotoPath);
            if (profileImageStream != null) {
                profileImage.setImage(new Image(profileImageStream));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Label boldUser = new Label(username + ": ");
        boldUser.setStyle("-fx-font-weight: bold;");

        HBox userDescription = new HBox(boldUser, new Label(event.getDescription()));
        userDescription.setSpacing(5);

        Label logNameLabel = new Label("Project: " + event.getLogName(event.getLogId()));
        logNameLabel.setStyle("-fx-font-style: italic;");

        VBox headerText = new VBox(5);
        headerText.getChildren().addAll(userDescription, logNameLabel);

        header.getChildren().addAll(profileImage, headerText);

        StackPane imageContainer = new StackPane();
        if (hasImage && event instanceof ImageEvent) {
            ImageEvent imageEvent = (ImageEvent) event;
            ImageView eventImage = new ImageView();
            eventImage.setFitHeight(150);
            eventImage.setFitWidth(200);
            eventImage.setPreserveRatio(true);
            String imageName = imageEvent.getImagePath();
            String relativePath = "/images/" + imageName;

            InputStream imageStream = getClass().getResourceAsStream(relativePath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                eventImage.setImage(image);
            }
            imageContainer.getChildren().add(eventImage);
        }

        Label likeCountLabel = new Label(event.getLikes().size() + " Likes");
        Label commentCountLabel = new Label(event.getComments().size() + " Comments");
        HBox likeCommentControls = new HBox();
        likeCommentControls.setSpacing(10);

        Button likeButton = new Button(event.getLikes().contains(app.getLoggedInUserID()) ? "Unlike" : "Like");
        likeButton.setOnAction(e -> toggleLike(event, likeButton, likeCountLabel));

        HBox countLabels = new HBox(likeCountLabel, commentCountLabel);
        countLabels.setSpacing(10);

        Button commentButton = new Button("Comment");
        commentButton.setOnAction(e -> showCommentsPopup(event, commentCountLabel));

        likeCommentControls.getChildren().addAll(likeButton, commentButton);

        postContent.getChildren().addAll(header, imageContainer, countLabels, likeCommentControls);
        postContainer.getChildren().add(postContent);
        commentsContainer1.getChildren().add(postContainer);
    }

    private Contact getContactForUserId(int userId) {
        contactDAO = new ContactDAO();
        Contact contact = contactDAO.getContactById(userId);

        if (contact == null) {
            return null;
        }

        ProfileDAO profileDAO = new ProfileDAO();
        try {
            profileDAO.insertProfile(userId, " ", " ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        profileDAO.getProfileByUserId(contact, userId);
        return contact;
    }

    private void showCommentsPopup(LogEvent event, Label commentCountLabel) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Comments");

        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(closeButtonType);

        VBox contentBox = new VBox(10);
        contentBox.setPrefWidth(400);
        contentBox.setStyle("-fx-padding: 20;");

        List<String> comments = new ArrayList<>(event.getCommentsFromDatabase());
        VBox commentsBox = new VBox(5);
        if (comments != null) {
            for (String comment : comments) {
                Label commentLabel = new Label(comment);
                commentsBox.getChildren().add(commentLabel);
            }
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(commentsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);

        TextField newCommentField = new TextField();
        newCommentField.setPromptText("Write a comment...");

        Button addCommentButton = new Button("Add Comment");
        addCommentButton.setOnAction(e -> {
            String newComment = newCommentField.getText().trim();
            if (!newComment.isEmpty()) {
                String currentUsername = getCurrentUsername();
                String formattedComment = currentUsername + ": " + newComment;
                commentsBox.getChildren().add(new Label(formattedComment));
                newCommentField.clear();
                saveCommentForLog(event.getId(), formattedComment);
                comments.add(formattedComment);
                event.setComments(comments);
                commentCountLabel.setText(comments.size() + " Comments");
            }
        });

        contentBox.getChildren().addAll(scrollPane, newCommentField, addCommentButton);
        dialog.getDialogPane().setContent(contentBox);
        dialog.showAndWait();
    }

    private void saveCommentForLog(int eventId, String comment) {
        logEventDAO.saveCommentForLog(eventId, comment);
    }

    private void toggleLike(LogEvent event, Button likeButton, Label likeCountLabel) {
        int userId = app.getLoggedInUserID();
        List<Integer> likes = event.getLikes();

        if (likes.contains(userId)) {
            likes.remove(Integer.valueOf(userId));
            likeButton.setText("Like");
        } else {
            likes.add(userId);
            likeButton.setText("Unlike");
        }

        likeCountLabel.setText(likes.size() + " Likes");
        logEventDAO.updateLikesForLogEvent(event.getId(), likes);
    }
}
