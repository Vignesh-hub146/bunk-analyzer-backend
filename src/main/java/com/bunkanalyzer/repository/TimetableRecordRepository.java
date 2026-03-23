package com.bunkanalyzer.repository;

import com.bunkanalyzer.entity.TimetableRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRecordRepository extends JpaRepository<TimetableRecord, Long> {

    // All entries for a specific batch, sorted by time
    List<TimetableRecord> findByBatchIdOrderByDayAscTimeSlotAsc(String batchId);

    // Delete all entries for a specific batch
    @Modifying
    @Query("DELETE FROM TimetableRecord t WHERE t.batchId = :batchId")
    void deleteByBatchId(@Param("batchId") String batchId);

    // Get all distinct batch info rows (one row per batch)
    @Query("SELECT t FROM TimetableRecord t WHERE t.id IN " +
           "(SELECT MIN(t2.id) FROM TimetableRecord t2 GROUP BY t2.batchId) " +
           "ORDER BY t.createdAt DESC")
    List<TimetableRecord> findOneBatchPerGroup();

    // Get the batchId of the most recently created entry
    @Query("SELECT t.batchId FROM TimetableRecord t ORDER BY t.createdAt DESC")
    List<String> findAllBatchIds();
}