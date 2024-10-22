package com.example.demoplswork.model;

import com.example.demoplswork.model.Blog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlogDAO {

    private Connection getConnection() throws SQLException {
        // Define the path to your SQLite database file
        String dbPath = "jdbc:sqlite:contacts.db";
        return DriverManager.getConnection(dbPath);
    }

    // Method to save a blog to the database
    public void saveBlog(Blog blog) {
        String sql = "INSERT INTO blogs (intro, description, category, imagePath) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, blog.getIntro());
            pstmt.setString(2, blog.getDescription());
            pstmt.setString(3, blog.getCategory());
            pstmt.setString(4, blog.getImagePath());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all blogs from the database
    public List<Blog> getAllBlogs() {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT * FROM blogs";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Blog blog = new Blog(
                        rs.getString("intro"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getString("imagePath")
                );
                blogs.add(blog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blogs;
    }
}
