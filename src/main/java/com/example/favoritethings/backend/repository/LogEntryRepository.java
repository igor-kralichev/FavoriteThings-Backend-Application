package com.example.favoritethings.backend.repository;

import com.example.favoritethings.backend.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
}
