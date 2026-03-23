package com.bunkanalyzer.repository;

import com.bunkanalyzer.entity.SubjectRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRecordRepository extends JpaRepository<SubjectRecord, Long> {

    List<SubjectRecord> findBySubjectNameOrderByCreatedAtDesc(String subjectName);

    List<SubjectRecord> findAllByOrderByCreatedAtDesc();
}