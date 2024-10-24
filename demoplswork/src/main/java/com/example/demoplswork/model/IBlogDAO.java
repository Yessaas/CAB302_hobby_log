package com.example.demoplswork.model;


import java.sql.SQLException;
import java.util.List;

public interface IBlogDAO {
    void insertBlog(Blog blog) throws SQLException;
    List<Blog> getAllBlogs() throws SQLException;
}
