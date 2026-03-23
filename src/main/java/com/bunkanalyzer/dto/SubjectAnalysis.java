package com.bunkanalyzer.dto;

public class SubjectAnalysis {

    private String subjectName;
    private String subjectType;
    private int totalClasses;
    private int attendedClasses;
    private double currentPercentage;
    private int classesNeededFor75;
    private int canBunkClasses;
    private String status;
    private int remainingClasses;
    private int classesNeededToReach75;

    public SubjectAnalysis() {}

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectType() { return subjectType; }
    public void setSubjectType(String subjectType) { this.subjectType = subjectType; }

    public int getTotalClasses() { return totalClasses; }
    public void setTotalClasses(int totalClasses) { this.totalClasses = totalClasses; }

    public int getAttendedClasses() { return attendedClasses; }
    public void setAttendedClasses(int attendedClasses) { this.attendedClasses = attendedClasses; }

    public double getCurrentPercentage() { return currentPercentage; }
    public void setCurrentPercentage(double currentPercentage) { this.currentPercentage = currentPercentage; }

    public int getClassesNeededFor75() { return classesNeededFor75; }
    public void setClassesNeededFor75(int classesNeededFor75) { this.classesNeededFor75 = classesNeededFor75; }

    public int getCanBunkClasses() { return canBunkClasses; }
    public void setCanBunkClasses(int canBunkClasses) { this.canBunkClasses = canBunkClasses; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getRemainingClasses() { return remainingClasses; }
    public void setRemainingClasses(int remainingClasses) { this.remainingClasses = remainingClasses; }

    public int getClassesNeededToReach75() { return classesNeededToReach75; }
    public void setClassesNeededToReach75(int classesNeededToReach75) { this.classesNeededToReach75 = classesNeededToReach75; }
}