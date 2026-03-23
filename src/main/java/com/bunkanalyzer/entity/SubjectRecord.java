package com.bunkanalyzer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subject_records")
public class SubjectRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String subjectType; // lecture, lab, training

    @Column(nullable = false)
    private int totalClasses;

    @Column(nullable = false)
    private int attendedClasses;

    @Column(nullable = false)
    private double attendancePercentage;

    @Column(nullable = false)
    private int canBunkClasses;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int totalWeeks;

    @Column(nullable = false)
    private int currentWeek;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ── Getters & Setters ─────────────────────────────
    public Long getId() { return id; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectType() { return subjectType; }
    public void setSubjectType(String subjectType) { this.subjectType = subjectType; }

    public int getTotalClasses() { return totalClasses; }
    public void setTotalClasses(int totalClasses) { this.totalClasses = totalClasses; }

    public int getAttendedClasses() { return attendedClasses; }
    public void setAttendedClasses(int attendedClasses) { this.attendedClasses = attendedClasses; }

    public double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public int getCanBunkClasses() { return canBunkClasses; }
    public void setCanBunkClasses(int canBunkClasses) { this.canBunkClasses = canBunkClasses; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTotalWeeks() { return totalWeeks; }
    public void setTotalWeeks(int totalWeeks) { this.totalWeeks = totalWeeks; }

    public int getCurrentWeek() { return currentWeek; }
    public void setCurrentWeek(int currentWeek) { this.currentWeek = currentWeek; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}