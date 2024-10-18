package com.example.demoplswork.model;

import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogsDAO extends BaseDAO implements ILogsDAO{

    // Insert a new log into the database
    @Override
    public int insertLog(int userId, Logs log) throws SQLException {
        if (log == null) {
            throw new SQLException("Log cannot be null.");
        }
        if (userId <= 0) {
            throw new SQLException("Invalid user ID.");
        }

        String query = "INSERT INTO logs(user_id, log_name, to_do_items, images, progress, materials) VALUES(?, ?, ?, ?, ?, ?)";
        ResultSet rs = null;
        int logId = -1;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, userId);
            pstmt.setString(2, log.getLogName());

            // Serialize the to-do items (task:checked format)
            String serializedToDoItems = serializeToDoItems(log.getToDoItems());
            pstmt.setString(3, serializedToDoItems);

            // Serialize images into a comma-separated string
            pstmt.setString(4, String.join(",", log.getImages()));

            // Set progress value
            pstmt.setDouble(5, log.getProgress());

            // Serialize materials
            pstmt.setString(6, serializeMaterials(log.getMaterials()));

            // Execute the update
            pstmt.executeUpdate();

            // Retrieve the generated log ID
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                logId = rs.getInt(1);  // This will be the generated ID
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return logId;
    }

    // Serialize to-do items as task:isChecked format
    private String serializeToDoItems(List<Pair<String, Boolean>> toDoItems) {
        StringBuilder sb = new StringBuilder();

        for (Pair<String, Boolean> toDoItem : toDoItems) {
            sb.append(toDoItem.getKey())  // Task description
                    .append(":")
                    .append(toDoItem.getValue())  // Checked state
                    .append(",");
        }

        // Remove the trailing comma if any
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    public boolean doesLogExist(int logId) {
        String query = "SELECT COUNT(1) FROM logs WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, logId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count is greater than 0, the log exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Log does not exist or an error occurred
    }



    // Method to add a to-do item to a specific log
    @Override
    public void addToDoItem(int logId, String toDoItem, boolean isChecked) throws SQLException {
        if (logId <= 0) {
            throw new SQLException("Invalid log ID.");
        }
        String query = "UPDATE logs SET to_do_items = CASE " +
                "WHEN to_do_items IS NULL OR to_do_items = '' THEN ? " +
                "ELSE to_do_items || ',' || ? " +
                "END WHERE id = ?";

        // Prepare the to-do item with its state
        String toDoWithState = toDoItem + ":" + (isChecked ? "true" : "false");

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, toDoWithState);  // If empty, just add the to-do item
            pstmt.setString(2, toDoWithState);  // Append the new to-do item with its state
            pstmt.setInt(3, logId);  // Specify which log to update
            pstmt.executeUpdate();
            System.out.println("To-do item added to log " + logId + " with state: " + isChecked);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add an image path to a specific log
    @Override
    public void addImage(int logId, String imagePath) throws SQLException {
        if (logId <= 0) {
            throw new SQLException("Invalid log ID.");
        }
        String query = "UPDATE logs SET images = CASE " +
                "WHEN images IS NULL OR images = '' THEN ? " +
                "ELSE images || ',' || ? " +
                "END WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, imagePath);  // If empty, just add the image path
            pstmt.setString(2, imagePath);  // Append the new image path
            pstmt.setInt(3, logId);  // Specify which log to update
            pstmt.executeUpdate();
            System.out.println("Image added to log " + logId + ": " + imagePath);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add a material to a specific log
    @Override
    public void addMaterial(int logId, Material material) {
        String query = "UPDATE logs SET materials = CASE " +
                "WHEN materials IS NULL OR materials = '' THEN ? " +
                "ELSE materials || ',' || ? " +
                "END WHERE id = ?";

        String serializedMaterial = material.getName() + ":" + material.getQuantity() + ":" + material.getPrice();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, serializedMaterial);  // If empty, just add the material
            pstmt.setString(2, serializedMaterial);  // Append the new material
            pstmt.setInt(3, logId);  // Specify which log to update
            pstmt.executeUpdate();
            System.out.println("Material added to log " + logId + ": " + serializedMaterial);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Object[]> getLogsForUser(int userId) {
        String query = "SELECT id, log_name, to_do_items, images, materials, progress FROM logs WHERE user_id = ?";
        List<Object[]> logsList = new ArrayList<>();

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userId);
            var rs = pstmt.executeQuery();

            while (rs.next()) {
                // Extract the log details, including the log ID
                int logID = rs.getInt("id");
                String logName = rs.getString("log_name");
                List<Pair<String, Boolean>> toDoItems = parseToDoItems(rs.getString("to_do_items"));
                List<String> images = parseImages(rs.getString("images")); // Deserialize images
                List<Material> materials = parseMaterials(rs.getString("materials")); // Deserialize materials
                double progress = rs.getDouble("progress");

                // Create a Logs object without the ID
                Logs log = new Logs(logName, toDoItems, images, materials);
                log.setProgress(progress);

                System.out.println("Log retrieved: " + logName + ". Progress: " + progress);

                // Add the log and its ID to the list
                logsList.add(new Object[] { logID, log });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logsList;
    }

    public List<String> getImagesForLog(int logId) {
        String query = "SELECT images FROM logs WHERE id = ?";
        List<String> imagesList = new ArrayList<>();

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, logId);
            var rs = pstmt.executeQuery();

            if (rs.next()) {
                // Deserialize images from the string
                imagesList = parseImages(rs.getString("images"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return imagesList;
    }

    public String getLogNameById(int logId) {
        String query = "SELECT log_name FROM logs WHERE id = ?";
        String logName = null;

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, logId);
            var rs = pstmt.executeQuery();

            if (rs.next()) {
                logName = rs.getString("log_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logName;
    }



    // Helper method to serialize materials into a string format (e.g., name:quantity:cost)
    private String serializeMaterials(List<Material> materials) {
        StringBuilder sb = new StringBuilder();
        for (Material material : materials) {
            sb.append(material.getName()).append(":")
                    .append(material.getQuantity()).append(":")
                    .append(material.getPrice()).append(",");
        }
        return sb.toString();
    }

    // Helper method to deserialize materials from a string format
    private List<Material> parseMaterials(String materialsStr) {
        List<Material> materials = new ArrayList<>();

        // Check if materialsStr is null or empty
        if (materialsStr == null || materialsStr.isEmpty()) {
            return materials;  // Return an empty list if there's no material data
        }

        // Split the string by commas to get each material
        String[] materialArr = materialsStr.split(",");

        for (String mat : materialArr) {
            // Split each material string by colon to get details (name, quantity, price)
            String[] details = mat.split(":");

            // Check if the details array has the correct number of parts (3 parts: name, quantity, price)
            if (details.length == 3) {
                try {
                    String name = details[0];
                    int quantity = Integer.parseInt(details[1]);  // Parse the quantity as an integer
                    double price = Double.parseDouble(details[2]);  // Parse the price as a double

                    // Add the material to the list
                    materials.add(new Material(name, quantity, price));
                } catch (NumberFormatException e) {
                    // Handle any parsing errors gracefully (e.g., log the error)
                    System.err.println("Error parsing material: " + mat);
                }
            } else {
                // Handle the case where details do not have exactly 3 parts
                System.err.println("Invalid material format: " + mat);
            }
        }

        return materials;
    }

    // Update a specific to-do item's status in the database and recalculate progress
    @Override
    public double updateToDoItemStatus(int logId, String task, boolean isChecked) {
        String querySelect = "SELECT to_do_items, progress FROM logs WHERE id = ?";
        String queryUpdate = "UPDATE logs SET to_do_items = ?, progress = ? WHERE id = ?";

        try {
            // Step 1: Retrieve the current to-do items and progress
            PreparedStatement pstmtSelect = connection.prepareStatement(querySelect);
            pstmtSelect.setInt(1, logId);
            ResultSet rs = pstmtSelect.executeQuery();

            if (rs.next()) {
                String toDoItemsStr = rs.getString("to_do_items");
                List<Pair<String, Boolean>> toDoItems = parseToDoItems(toDoItemsStr);

                // Step 2: Update the status of the specific task
                for (int i = 0; i < toDoItems.size(); i++) {
                    if (toDoItems.get(i).getKey().equals(task)) {
                        toDoItems.set(i, new Pair<>(task, isChecked));
                        break;
                    }
                }

                // Step 3: Calculate the new progress
                double progress = calculateProgress(toDoItems);

                // Step 4: Serialize the updated to-do items back into the string format
                String updatedToDoItemsStr = serializeToDoItems(toDoItems);

                // Step 5: Update the database with the new to-do list and progress
                PreparedStatement pstmtUpdate = connection.prepareStatement(queryUpdate);
                pstmtUpdate.setString(1, updatedToDoItemsStr);
                pstmtUpdate.setDouble(2, progress);  // Update progress
                pstmtUpdate.setInt(3, logId);
                pstmtUpdate.executeUpdate();

                System.out.println("Updated to-do item: " + task + " to " + isChecked + ". Progress: " + progress);
                return progress;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updateLogName(int logId, String newLogName) throws SQLException {
        String query = "UPDATE logs SET log_name = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, newLogName);
            pstmt.setInt(2, logId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteLog(int logId) {
        String sql = "DELETE FROM logs WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, logId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to calculate progress as a percentage of completed tasks
    private double calculateProgress(List<Pair<String, Boolean>> toDoItems) {
        int completedCount = 0;
        for (Pair<String, Boolean> item : toDoItems) {
            if (item.getValue()) {
                completedCount++;
            }
        }
        if (toDoItems.isEmpty()) {
            return 0.0;
        }
        return (double) completedCount / toDoItems.size() * 100;
    }

    // Helper method to deserialize to-do items from a comma-separated string
    private List<Pair<String, Boolean>> parseToDoItems(String toDoItemsStr) {
        List<Pair<String, Boolean>> toDoItems = new ArrayList<>();

        if (toDoItemsStr == null || toDoItemsStr.isEmpty()) {
            return toDoItems;  // Return an empty list if there's no data
        }

        // Split the string into individual tasks
        String[] itemsArray = toDoItemsStr.split(",");

        for (String item : itemsArray) {
            String[] taskDetails = item.split(":");
            if (taskDetails.length == 2) {
                String taskDescription = taskDetails[0].trim();
                boolean isChecked = Boolean.parseBoolean(taskDetails[1].trim());
                toDoItems.add(new Pair<>(taskDescription, isChecked));  // Store task and its checked state
            }
        }

        return toDoItems;
    }


    // Helper method to deserialize images from a comma-separated string
    private List<String> parseImages(String imagesStr) {
        return List.of(imagesStr.split(","));
    }


}


