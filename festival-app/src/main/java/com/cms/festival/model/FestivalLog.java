package com.cms.festival.log;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "festival_logs")
public class FestivalLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant timestamp;
    private String action;
    private String role;
    private String details;

    public FestivalLog() { }
    public FestivalLog(String action, String role, String details) {
        this.timestamp = Instant.now();
        this.action = action;
        this.role = role;
        this.details = details;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Instant getTimestamp() { return timestamp; }
    public String getAction() { return action; }
    public String getRole() { return role; }
    public String getDetails() { return details; }
}


