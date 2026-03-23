package com.bunkanalyzer.controller;

import com.bunkanalyzer.dto.AnalyzeRequest;
import com.bunkanalyzer.dto.AnalyzeResponse;
import com.bunkanalyzer.dto.TimetableBatch;
import com.bunkanalyzer.entity.SubjectRecord;
import com.bunkanalyzer.entity.TimetableRecord;
import com.bunkanalyzer.service.BunkAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bunk")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173" })
public class BunkAnalyzerController {

    @Autowired
    private BunkAnalyzerService service;

    // ── Analyze ──────────────────────────────────────────
    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponse> analyze(@RequestBody AnalyzeRequest req) {
        try { return ResponseEntity.ok(service.analyze(req)); }
        catch (Exception e) { e.printStackTrace(); return ResponseEntity.internalServerError().build(); }
    }

    // ── List all timetable batches ───────────────────────
    @GetMapping("/timetable/batches")
    public ResponseEntity<List<TimetableBatch>> getBatches() {
        try { return ResponseEntity.ok(service.getAllBatches()); }
        catch (Exception e) { e.printStackTrace(); return ResponseEntity.internalServerError().build(); }
    }

    // ── Get entries for a specific batch ─────────────────
    @GetMapping("/timetable/batch/{batchId}")
    public ResponseEntity<List<TimetableRecord>> getBatch(@PathVariable String batchId) {
        try { return ResponseEntity.ok(service.getTimetableByBatch(batchId)); }
        catch (Exception e) { e.printStackTrace(); return ResponseEntity.internalServerError().build(); }
    }

    // ── Get latest timetable ─────────────────────────────
    @GetMapping("/timetable/latest")
    public ResponseEntity<List<TimetableRecord>> getLatest() {
        try { return ResponseEntity.ok(service.getLatestTimetable()); }
        catch (Exception e) { e.printStackTrace(); return ResponseEntity.internalServerError().build(); }
    }

    // ── Delete a batch ───────────────────────────────────
    @DeleteMapping("/timetable/batch/{batchId}")
    public ResponseEntity<Void> deleteBatch(@PathVariable String batchId) {
        try { service.deleteBatch(batchId); return ResponseEntity.ok().build(); }
        catch (Exception e) { e.printStackTrace(); return ResponseEntity.internalServerError().build(); }
    }

    // ── Latest subjects ──────────────────────────────────
    @GetMapping("/subjects/latest")
    public ResponseEntity<List<SubjectRecord>> getLatestSubjects() {
        try { return ResponseEntity.ok(service.getLatestSubjects()); }
        catch (Exception e) { e.printStackTrace(); return ResponseEntity.internalServerError().build(); }
    }

    // ── History ──────────────────────────────────────────
    @GetMapping("/history")
    public ResponseEntity<List<SubjectRecord>> getHistory() {
        try { return ResponseEntity.ok(service.getHistory()); }
        catch (Exception e) { e.printStackTrace(); return ResponseEntity.internalServerError().build(); }
    }

    // ── Health ───────────────────────────────────────────
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Bunk Analyzer is running!");
    }
}