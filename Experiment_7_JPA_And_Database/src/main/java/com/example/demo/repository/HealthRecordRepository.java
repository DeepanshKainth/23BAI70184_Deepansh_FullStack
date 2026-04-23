package com.example.demo.repository;

import com.example.demo.entity.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    @Query("""
            select hr
            from HealthRecord hr
            where hr.patient.id = :patientId
            order by hr.visitDate desc, hr.id desc
            """)
    List<HealthRecord> findByPatientId(@Param("patientId") Long patientId);

    @Query("""
            select hr
            from HealthRecord hr
            join fetch hr.patient p
            where hr.id = :recordId and p.id = :patientId
            """)
    Optional<HealthRecord> findByIdAndPatientId(@Param("recordId") Long recordId, @Param("patientId") Long patientId);
}