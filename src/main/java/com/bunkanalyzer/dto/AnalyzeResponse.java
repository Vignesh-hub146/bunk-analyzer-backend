package com.bunkanalyzer.dto;

import java.util.List;

public class AnalyzeResponse {

    private List<SubjectAnalysis> subjectAnalyses;
    private double overallAttendance;
    private int totalBunkableClasses;
    private String overallStatus;
    private String recommendation;

    public AnalyzeResponse() {}

    public List<SubjectAnalysis> getSubjectAnalyses() { return subjectAnalyses; }
    public void setSubjectAnalyses(List<SubjectAnalysis> subjectAnalyses) { this.subjectAnalyses = subjectAnalyses; }

    public double getOverallAttendance() { return overallAttendance; }
    public void setOverallAttendance(double overallAttendance) { this.overallAttendance = overallAttendance; }

    public int getTotalBunkableClasses() { return totalBunkableClasses; }
    public void setTotalBunkableClasses(int totalBunkableClasses) { this.totalBunkableClasses = totalBunkableClasses; }

    public String getOverallStatus() { return overallStatus; }
    public void setOverallStatus(String overallStatus) { this.overallStatus = overallStatus; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
}