package com.example.demoplswork.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogEventUtils {

    // Serialize List to a comma-separated string
    public static String serializeComments(List<String> comments) {
        return String.join("||", comments); // Use "||" as a delimiter to handle cases where comments may contain commas
    }

    // Deserialize a comma-separated string to a List
    public static List<String> deserializeComments(String commentsData) {
        if (commentsData == null || commentsData.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(commentsData.split("\\|\\|")); // Split by "||" delimiter
    }

    // Serialize List of Integers to a comma-separated string
    public static String serializeLikes(List<Integer> likes) {
        List<String> likeStrings = new ArrayList<>();
        for (Integer like : likes) {
            likeStrings.add(String.valueOf(like));
        }
        return String.join(",", likeStrings); // Use comma to separate user IDs
    }

    // Deserialize a comma-separated string to a List of Integers
    public static List<Integer> deserializeLikes(String likesData) {
        List<Integer> likes = new ArrayList<>();
        if (likesData == null || likesData.isEmpty()) {
            return likes;
        }
        String[] likeStrings = likesData.split(",");
        for (String like : likeStrings) {
            try {
                likes.add(Integer.parseInt(like.trim()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return likes;
    }
}
