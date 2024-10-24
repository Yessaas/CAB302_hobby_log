package com.example.demoplswork.events;

import java.util.ArrayList;
import java.util.List;

public class ProgressLog {
    private List<LogEvent> events;

    public ProgressLog() {
        this.events = new ArrayList<>();
    }

    // Method to add a new event to the log
    public void addEvent(LogEvent event) {
        events.add(event);
    }

    // Method to retrieve the entire log as a formatted string
    public String getLog() {
        StringBuilder log = new StringBuilder();
        for (LogEvent event : events) {
            log.append(event.toString()).append("\n");
        }
        return log.toString();
    }


}

