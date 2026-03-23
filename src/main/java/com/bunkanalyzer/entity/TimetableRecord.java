package com.bunkanalyzer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "timetable_records")
public class TimetableRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Groups all entries from one import session together
    @Column(name = "batch_id", nullable = false)
    private String batchId;

    // Human-readable label e.g. "Timetable - 20 Mar 2026"
    @Column(name = "label")
    private String label;

    @Column(nullable = false)
    private String day;

    @Column(nullable = false)
    private String timeSlot;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String subjectType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId()            { return id; }
    public String getBatchId()     { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public String getLabel()       { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getDay()         { return day; }
    public void setDay(String day) { this.day = day; }
    public String getTimeSlot()    { return timeSlot; }
    public void setTimeSlot(String s) { this.timeSlot = s; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String s) { this.subjectName = s; }
    public String getSubjectType() { return subjectType; }
    public void setSubjectType(String s) { this.subjectType = s; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}