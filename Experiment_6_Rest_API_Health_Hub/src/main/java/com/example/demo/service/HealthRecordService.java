package com.example.demo.service;

import com.example.demo.dto.HealthRecordRequestDTO;
import com.example.demo.dto.HealthRecordResponseDTO;
import com.example.demo.entity.HealthRecord;
import com.example.demo.entity.Patient;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.HealthRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;
    private final PatientService patientService;

    public HealthRecordService(HealthRecordRepository healthRecordRepository, PatientService patientService) {
        this.healthRecordRepository = healthRecordRepository;
        this.patientService = patientService;
    }

    public HealthRecordResponseDTO createRecord(Long patientId, HealthRecordRequestDTO requestDTO) {
        Patient patient = patientService.findPatientOrThrow(patientId);

        HealthRecord record = new HealthRecord();
        record.setDiagnosis(requestDTO.getDiagnosis());
        record.setNotes(requestDTO.getNotes());
        record.setVisitDate(requestDTO.getVisitDate());
        record.setPatient(patient);

        return toResponse(healthRecordRepository.save(record));
    }

    public List<HealthRecordResponseDTO> getRecordsByPatient(Long patientId) {
        patientService.findPatientOrThrow(patientId);
        return healthRecordRepository.findByPatientId(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public HealthRecordResponseDTO getRecordById(Long patientId, Long recordId) {
        HealthRecord record = findRecordForPatientOrThrow(patientId, recordId);
        return toResponse(record);
    }

    public HealthRecordResponseDTO updateRecord(Long patientId, Long recordId, HealthRecordRequestDTO requestDTO) {
        HealthRecord record = findRecordForPatientOrThrow(patientId, recordId);
        record.setDiagnosis(requestDTO.getDiagnosis());
        record.setNotes(requestDTO.getNotes());
        record.setVisitDate(requestDTO.getVisitDate());
        return toResponse(healthRecordRepository.save(record));
    }

    public void deleteRecord(Long patientId, Long recordId) {
        HealthRecord record = findRecordForPatientOrThrow(patientId, recordId);
        healthRecordRepository.delete(record);
    }

    private HealthRecord findRecordForPatientOrThrow(Long patientId, Long recordId) {
        patientService.findPatientOrThrow(patientId);

        HealthRecord record = healthRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Health record not found with id: " + recordId));

        if (!record.getPatient().getId().equals(patientId)) {
            throw new ResourceNotFoundException("Health record " + recordId + " does not belong to patient " + patientId);
        }

        return record;
    }

    private HealthRecordResponseDTO toResponse(HealthRecord record) {
        HealthRecordResponseDTO responseDTO = new HealthRecordResponseDTO();
        responseDTO.setId(record.getId());
        responseDTO.setPatientId(record.getPatient().getId());
        responseDTO.setDiagnosis(record.getDiagnosis());
        responseDTO.setNotes(record.getNotes());
        responseDTO.setVisitDate(record.getVisitDate());
        return responseDTO;
    }
}