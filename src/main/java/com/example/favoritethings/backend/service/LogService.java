package com.example.favoritethings.backend.service;

import com.example.favoritethings.backend.entity.LogEntry;
import com.example.favoritethings.backend.repository.LogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class LogService {

    @Autowired
    private LogEntryRepository logEntryRepository;

    public void log(String operation, String details) {
        LogEntry logEntry = new LogEntry();
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setOperation(operation);
        logEntry.setDetails(details);
        logEntryRepository.save(logEntry);
    }
}
