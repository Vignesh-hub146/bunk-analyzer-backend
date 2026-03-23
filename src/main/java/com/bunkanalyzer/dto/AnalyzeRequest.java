package com.bunkanalyzer.dto;
import com.bunkanalyzer.model.Subject;
import com.bunkanalyzer.model.TimetableEntry;
import java.util.List;
public class AnalyzeRequest {
    private List<Subject> subjects;
    private List<TimetableEntry> timetable;
    private int totalWeeks;
    private int currentWeek;
    private String timetableLabel;
    private int targetPercentage = 75; // ← NEW (defaults to 75 if frontend doesn't send it)

    public List<Subject> getSubjects()               { return subjects; }
    public void setSubjects(List<Subject> v)          { this.subjects = v; }
    public List<TimetableEntry> getTimetable()         { return timetable; }
    public void setTimetable(List<TimetableEntry> v)   { this.timetable = v; }
    public int getTotalWeeks()                         { return totalWeeks; }
    public void setTotalWeeks(int v)                   { this.totalWeeks = v; }
    public int getCurrentWeek()                        { return currentWeek; }
    public void setCurrentWeek(int v)                  { this.currentWeek = v; }
    public String getTimetableLabel()                  { return timetableLabel; }
    public void setTimetableLabel(String v)            { this.timetableLabel = v; }
    public int getTargetPercentage()                   { return targetPercentage; }  // ← NEW
    public void setTargetPercentage(int v)             { this.targetPercentage = v; } // ← NEW
}