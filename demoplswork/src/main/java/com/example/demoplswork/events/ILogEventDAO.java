package com.example.demoplswork.events;

import java.sql.SQLException;
import java.util.List;

public interface ILogEventDAO {
    void insertLogEvent(LogEvent event) throws SQLException;
    List<LogEvent> getLogEventsForLog(int logId) throws SQLException;
}

