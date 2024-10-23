package com.example.demoplswork.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlogDAO extends BaseDAO implements IBlogDAO {

    @Override
    public void insertBlog(Blog blog) throws SQLException {
        String query = "INSERT INTO blogs (user_id, title, description, image_path, tag) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, blog.getUserId());
            statement.setString(2, blog.getTitle());
            statement.setString(3, blog.getDescription());
            statement.setString(4, blog.getImagePath());
            statement.setString(5, blog.getTag());  // Insert the tag
            statement.executeUpdate();
        }
    }

    @Override
    public List<Blog> getAllBlogs() throws SQLException {
        String query = "SELECT * FROM blogs";
        List<Blog> blogs = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Blog blog = new Blog(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("image_path"),
                        rs.getString("tag")  // Retrieve the tag
                );
                blogs.add(blog);
            }
        }
        return blogs;
    }

}

