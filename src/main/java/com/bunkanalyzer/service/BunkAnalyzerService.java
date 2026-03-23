package com.bunkanalyzer.service;

import com.bunkanalyzer.dto.AnalyzeRequest;
import com.bunkanalyzer.dto.AnalyzeResponse;
import com.bunkanalyzer.dto.SubjectAnalysis;
import com.bunkanalyzer.dto.TimetableBatch;
import com.bunkanalyzer.entity.SubjectRecord;
import com.bunkanalyzer.entity.TimetableRecord;
import com.bunkanalyzer.model.Subject;
import com.bunkanalyzer.model.TimetableEntry;
import com.bunkanalyzer.repository.SubjectRecordRepository;
import com.bunkanalyzer.repository.TimetableRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class BunkAnalyzerService {

    // Target is now passed per-request (default 75)
    private static final DateTimeFormatter LABEL_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    @Autowired
    private SubjectRecordRepository subjectRepo;

    @Autowired
    private TimetableRecordRepository timetableRepo;

    // ── Analyze & save ────────────────────────────────────────────────────
    @Transactional
    public AnalyzeResponse analyze(AnalyzeRequest request) {
        List<SubjectAnalysis> analyses = new ArrayList<>();
        Map<String, Integer> perWeek = countPerWeek(request.getTimetable());

        // Save timetable as a NEW batch (does NOT delete old ones)
        saveTimetableBatch(request.getTimetable(), request.getTimetableLabel());

        double target = request.getTargetPercentage();
        for (Subject s : request.getSubjects()) {
            SubjectAnalysis a = analyzeSubject(s, request.getTotalWeeks(),
                    request.getCurrentWeek(), perWeek.getOrDefault(s.getName(), 0), target);
            analyses.add(a);
            saveSubjectRecord(s, a, request.getTotalWeeks(), request.getCurrentWeek());
        }

        int attended = 0, total = 0, bunkable = 0;
        for (SubjectAnalysis a : analyses) {
            attended += a.getAttendedClasses();
            total    += a.getTotalClasses();
            bunkable += Math.max(0, a.getCanBunkClasses());
        }
        double overall = total > 0 ? Math.round((attended * 100.0 / total) * 100) / 100.0 : 0;

        AnalyzeResponse resp = new AnalyzeResponse();
        resp.setSubjectAnalyses(analyses);
        resp.setOverallAttendance(overall);
        resp.setTotalBunkableClasses(bunkable);
        resp.setOverallStatus(overallStatus(overall));
        resp.setRecommendation(recommendation(analyses, overall));
        return resp;
    }

    // ── Get all timetable batches (for picker) ────────────────────────────
    public List<TimetableBatch> getAllBatches() {
        // Get one representative row per batch
        List<TimetableRecord> representatives = timetableRepo.findOneBatchPerGroup();
        List<TimetableBatch> batches = new ArrayList<>();
        for (TimetableRecord rep : representatives) {
            if (rep.getBatchId() == null) continue;
            int count = timetableRepo.findByBatchIdOrderByDayAscTimeSlotAsc(rep.getBatchId()).size();
            batches.add(new TimetableBatch(rep.getBatchId(), rep.getLabel(), rep.getCreatedAt(), count));
        }
        return batches;
    }

    // ── Get timetable entries for a specific batch ────────────────────────
    public List<TimetableRecord> getTimetableByBatch(String batchId) {
        return timetableRepo.findByBatchIdOrderByDayAscTimeSlotAsc(batchId);
    }

    // ── Get latest batch entries ──────────────────────────────────────────
    public List<TimetableRecord> getLatestTimetable() {
        List<String> ids = timetableRepo.findAllBatchIds();
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        String latestId = ids.get(0);
        if (latestId == null) return new ArrayList<>();
        return timetableRepo.findByBatchIdOrderByDayAscTimeSlotAsc(latestId);
    }

    // ── Delete a batch ────────────────────────────────────────────────────
    @Transactional
    public void deleteBatch(String batchId) {
        timetableRepo.deleteByBatchId(batchId);
    }

    // ── Latest subject records ────────────────────────────────────────────
    public List<SubjectRecord> getLatestSubjects() {
        List<SubjectRecord> all = subjectRepo.findAllByOrderByCreatedAtDesc();
        Set<String> seen = new HashSet<>();
        List<SubjectRecord> latest = new ArrayList<>();
        for (SubjectRecord r : all) {
            if (seen.add(r.getSubjectName())) latest.add(r);
        }
        return latest;
    }

    public List<SubjectRecord> getHistory() {
        return subjectRepo.findAllByOrderByCreatedAtDesc();
    }

    // ── Core calculation ──────────────────────────────────────────────────
    private SubjectAnalysis analyzeSubject(Subject s, int totalWeeks, int currentWeek, int weekly, double target) {
        SubjectAnalysis a = new SubjectAnalysis();
        a.setSubjectName(s.getName());
        a.setSubjectType(s.getType() != null ? s.getType() : "lecture");
        a.setTotalClasses(s.getTotalClasses());
        a.setAttendedClasses(s.getAttendedClasses());

        double pct = s.getTotalClasses() > 0
            ? Math.round((s.getAttendedClasses() * 100.0 / s.getTotalClasses()) * 100) / 100.0 : 0;
        a.setCurrentPercentage(pct);

        int remaining = Math.max(0, totalWeeks - currentWeek) * weekly;
        a.setRemainingClasses(remaining);
        a.setClassesNeededFor75((int) Math.ceil(target * s.getTotalClasses() / 100.0));

        int projected = s.getTotalClasses() + remaining;
        int needed    = (int) Math.ceil(target * projected / 100.0);
        int needMore  = Math.max(0, needed - s.getAttendedClasses());
        a.setClassesNeededToReach75(needMore);
        a.setCanBunkClasses(Math.max(0, remaining - needMore));
        a.setStatus(subjectStatus(pct, remaining - needMore, needMore, target));
        return a;
    }

    // ── Save timetable as a new batch ─────────────────────────────────────
    private void saveTimetableBatch(List<TimetableEntry> entries, String customLabel) {
        if (entries == null || entries.isEmpty()) return;
        try {
            String batchId = UUID.randomUUID().toString();
            String label   = (customLabel != null && !customLabel.isBlank())
                ? customLabel
                : "Timetable - " + LocalDateTime.now().format(LABEL_FMT);

            for (TimetableEntry e : entries) {
                TimetableRecord r = new TimetableRecord();
                r.setBatchId(batchId);
                r.setLabel(label);
                r.setDay(e.getDay());
                r.setTimeSlot(e.getTimeSlot());
                r.setSubjectName(e.getSubjectName());
                r.setSubjectType(e.getSubjectType() != null ? e.getSubjectType() : "lecture");
                timetableRepo.save(r);
            }
        } catch (Exception ex) {
            System.err.println("Could not save timetable batch: " + ex.getMessage());
        }
    }

    private void saveSubjectRecord(Subject s, SubjectAnalysis a, int totalWeeks, int currentWeek) {
        try {
            SubjectRecord r = new SubjectRecord();
            r.setSubjectName(s.getName());
            r.setSubjectType(s.getType() != null ? s.getType() : "lecture");
            r.setTotalClasses(a.getTotalClasses());
            r.setAttendedClasses(a.getAttendedClasses());
            r.setAttendancePercentage(a.getCurrentPercentage());
            r.setCanBunkClasses(a.getCanBunkClasses());
            r.setStatus(a.getStatus());
            r.setTotalWeeks(totalWeeks);
            r.setCurrentWeek(currentWeek);
            subjectRepo.save(r);
        } catch (Exception ex) {
            System.err.println("Could not save subject record: " + ex.getMessage());
        }
    }

    private Map<String, Integer> countPerWeek(List<TimetableEntry> timetable) {
        Map<String, Integer> map = new HashMap<>();
        if (timetable == null) return map;
        for (TimetableEntry e : timetable) {
            if (e.getSubjectName() != null && !e.getSubjectName().isBlank())
                map.merge(e.getSubjectName(), 1, Integer::sum);
        }
        return map;
    }

    private String subjectStatus(double pct, int canBunk, int needed, double target) {
        if (pct >= target + 10) return "SAFE";
        if (pct >= target)      return "WARNING";
        if (needed <= 5)        return "DANGER";
        return "CRITICAL";
    }

    private String overallStatus(double pct) {
        // Use fixed thresholds for overall label
        if (pct >= 85) return "EXCELLENT";
        if (pct >= 75) return "GOOD";
        if (pct >= 65) return "WARNING";
        return "DANGER";
    }

    private String recommendation(List<SubjectAnalysis> list, double overall) {
        long critical = list.stream().filter(a -> "CRITICAL".equals(a.getStatus()) || "DANGER".equals(a.getStatus())).count();
        if (critical > 0) return "You have " + critical + " subject(s) in danger zone! Attend all classes immediately.";
        if (overall >= 85) return "You are doing great! You can afford to bunk a few classes.";
        if (overall >= 75) return "You are above 75%. Be careful, a few more bunks could drop you below.";
        return "Your overall attendance is below 75%. Attend all remaining classes.";
    }
}