package com.bunkanalyzer.dto;

import java.time.LocalDateTime;

public class TimetableBatch {
    private String batchId;
    private String label;
    private LocalDateTime createdAt;
    private int entryCount;

    public TimetableBatch() {}

    public TimetableBatch(String batchId, String label, LocalDateTime createdAt, int entryCount) {
        this.batchId    = batchId;
        this.label      = label;
        this.createdAt  = createdAt;
        this.entryCount = entryCount;
    }

    public String getBatchId()          { return batchId; }
    public void setBatchId(String v)    { this.batchId = v; }
    public String getLabel()            { return label; }
    public void setLabel(String v)      { this.label = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public int getEntryCount()          { return entryCount; }
    public void setEntryCount(int v)    { this.entryCount = v; }
}