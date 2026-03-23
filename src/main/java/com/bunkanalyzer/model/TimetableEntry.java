package com.bunkanalyzer.model;

public class TimetableEntry {

    private String day;
    private String timeSlot;
    private String subjectName;
    private String subjectType; // lecture, lab, training

    public TimetableEntry() {}

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectType() { return subjectType; }
    public void setSubjectType(String subjectType) { this.subjectType = subjectType; }
}