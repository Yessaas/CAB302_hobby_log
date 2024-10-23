package com.example.demoplswork.model;

import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * LogsDAO class is responsible for managing logs in the database.
 * It extends the BaseDAO class and implements the ILogsDAO interface.
 * It has a method to insert a log for a user.
 * It has a method to check if a log exists.
 * It has a method to add a to-do item to a log.
 * It has a method to add an image to a log.
 * It has a method to add material to a log.
 * It has a method to retrieve logs for a user.
 * It has a method to retrieve images for a log.
 * It has a method to get the log name by ID.
 * It has a method to update the status of a to-do item in a log.
 * It has a method to update the name of a log.
 * It has a method to delete a log.
 * It has a method to add media to a log.
 */
public class LogsDAO extends BaseDAO implements ILogsDAO {

    public LogsDAO() {
        try {
            createMediaTable();
        } catch (SQLException e) {
            System.err.println("Failed to create media table: " + e.getMessage());
        }
    }

    private void createMediaTable() throws SQLException {
        String createMediaTableSQL = "CREATE TABLE IF NOT EXISTS media_table (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "log_id INTEGER, " +
                "media_name TEXT, " +
                "FOREIGN KEY (log_id) REFERENCES logs(id) ON DELETE CASCADE" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createMediaTableSQL);
        }
    }

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

            String serializedToDoItems = serializeToDoItems(log.getToDoItems());
            pstmt.setString(3, serializedToDoItems);
            pstmt.setString(4, String.join(",", log.getImages()));
            pstmt.setDouble(5, log.getProgress());
            pstmt.setString(6, serializeMaterials(log.getMaterials()));

            int rowsInserted = pstmt.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                logId = rs.getInt(1);
                System.out.println("Generated Log ID: " + logId);
            } else {
                System.out.println("Log ID not generated.");
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

    private String serializeToDoItems(List<Pair<String, Boolean>> toDoItems) {
        StringBuilder sb = new StringBuilder();

        for (Pair<String, Boolean> toDoItem : toDoItems) {
            sb.append(toDoItem.getKey())
                    .append(":")
                    .append(toDoItem.getValue())
                    .append(",");
        }

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
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void addToDoItem(int logId, String toDoItem, boolean isChecked) throws SQLException {
        if (logId <= 0) {
            throw new SQLException("Invalid log ID.");
        }
        String query = "UPDATE logs SET to_do_items = CASE " +
                "WHEN to_do_items IS NULL OR to_do_items = '' THEN ? " +
                "ELSE to_do_items || ',' || ? " +
                "END WHERE id = ?";

        String toDoWithState = toDoItem + ":" + (isChecked ? "true" : "false");

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, toDoWithState);
            pstmt.setString(2, toDoWithState);
            pstmt.setInt(3, logId);
            pstmt.executeUpdate();
            System.out.println("To-do item added to log " + logId + " with state: " + isChecked);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            pstmt.setString(1, imagePath);
            pstmt.setString(2, imagePath);
            pstmt.setInt(3, logId);
            pstmt.executeUpdate();
            System.out.println("Image added to log " + logId + ": " + imagePath);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addMaterial(int logId, Material material) {
        String query = "UPDATE logs SET materials = CASE " +
                "WHEN materials IS NULL OR materials = '' THEN ? " +
                "ELSE materials || ',' || ? " +
                "END WHERE id = ?";

        String serializedMaterial = material.getName() + ":" + material.getQuantity() + ":" + material.getPrice();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, serializedMaterial);
            pstmt.setString(2, serializedMaterial);
            pstmt.setInt(3, logId);
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
                int logID = rs.getInt("id");
                String logName = rs.getString("log_name");
                List<Pair<String, Boolean>> toDoItems = parseToDoItems(rs.getString("to_do_items"));
                List<String> images = parseImages(rs.getString("images"));
                List<Material> materials = parseMaterials(rs.getString("materials"));
                double progress = rs.getDouble("progress");

                Logs log = new Logs(logName, toDoItems, images, materials);
                log.setProgress(progress);

                System.out.println("Log retrieved: " + logName + ". Progress: " + progress);

                logsList.add(new Object[]{logID, log});
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

    private String serializeMaterials(List<Material> materials) {
        StringBuilder sb = new StringBuilder();
        for (Material material : materials) {
            sb.append(material.getName()).append(":")
                    .append(material.getQuantity()).append(":")
                    .append(material.getPrice()).append(",");
        }
        return sb.toString();
    }

    private List<Material> parseMaterials(String materialsStr) {
        List<Material> materials = new ArrayList<>();

        if (materialsStr == null || materialsStr.isEmpty()) {
            return materials;
        }

        String[] materialArr = materialsStr.split(",");

        for (String mat : materialArr) {
            String[] details = mat.split(":");

            if (details.length == 3) {
                try {
                    String name = details[0];
                    int quantity = Integer.parseInt(details[1]);
                    double price = Double.parseDouble(details[2]);
                    materials.add(new Material(name, quantity, price));
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing material: " + mat);
                }
            } else {
                System.err.println("Invalid material format: " + mat);
            }
        }

        return materials;
    }

    @Override
    public double updateToDoItemStatus(int logId, String task, boolean isChecked) {
        String querySelect = "SELECT to_do_items, progress FROM logs WHERE id = ?";
        String queryUpdate = "UPDATE logs SET to_do_items = ?, progress = ? WHERE id = ?";

        try {
            PreparedStatement pstmtSelect = connection.prepareStatement(querySelect);
            pstmtSelect.setInt(1, logId);
            ResultSet rs = pstmtSelect.executeQuery();

            if (rs.next()) {
                String toDoItemsStr = rs.getString("to_do_items");
                List<Pair<String, Boolean>> toDoItems = parseToDoItems(toDoItemsStr);

                for (int i = 0; i < toDoItems.size(); i++) {
                    if (toDoItems.get(i).getKey().equals(task)) {
                        toDoItems.set(i, new Pair<>(task, isChecked));
                        break;
                    }
                }

                double progress = calculateProgress(toDoItems);
                String updatedToDoItemsStr = serializeToDoItems(toDoItems);

                PreparedStatement pstmtUpdate = connection.prepareStatement(queryUpdate);
                pstmtUpdate.setString(1, updatedToDoItemsStr);
                pstmtUpdate.setDouble(2, progress);
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

    public void addMedia(int logId, String mediaName) throws SQLException {
        String sql = "INSERT INTO media_table (log_id, media_name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, logId);
            statement.setString(2, mediaName);
            statement.executeUpdate();
        }
    }

    private List<Pair<String, Boolean>> parseToDoItems(String toDoItemsStr) {
        List<Pair<String, Boolean>> toDoItems = new ArrayList<>();

        if (toDoItemsStr == null || toDoItemsStr.isEmpty()) {
            return toDoItems;
        }

        String[] itemsArray = toDoItemsStr.split(",");

        for (String item : itemsArray) {
            String[] taskDetails = item.split(":");
            if (taskDetails.length == 2) {
                String taskDescription = taskDetails[0].trim();
                boolean isChecked = Boolean.parseBoolean(taskDetails[1].trim());
                toDoItems.add(new Pair<>(taskDescription, isChecked));
            }
        }

        return toDoItems;
    }

    private List<String> parseImages(String imagesStr) {
        return List.of(imagesStr.split(","));
    }
}
