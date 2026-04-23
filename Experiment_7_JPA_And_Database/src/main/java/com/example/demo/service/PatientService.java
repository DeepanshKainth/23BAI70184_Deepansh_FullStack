package com.example.demo.service;

import com.example.demo.dto.FetchPerformanceResponseDTO;
import com.example.demo.dto.HealthRecordResponseDTO;
import com.example.demo.dto.PatientCursorPageResponseDTO;
import com.example.demo.dto.PatientDetailsResponseDTO;
import com.example.demo.dto.PatientRequestDTO;
import com.example.demo.dto.PatientResponseDTO;
import com.example.demo.dto.PatientSummaryResponseDTO;
import com.example.demo.entity.HealthRecord;
import com.example.demo.entity.Patient;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PatientSummaryProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO) {
        Patient patient = toEntity(requestDTO);
        return toResponse(patientRepository.save(patient));
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = findPatientOrThrow(id);
        return toResponse(patient);
    }

    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO requestDTO) {
        Patient patient = findPatientOrThrow(id);
        patient.setFullName(requestDTO.getFullName());
        patient.setEmail(requestDTO.getEmail());
        patient.setPhoneNumber(requestDTO.getPhoneNumber());
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        return toResponse(patientRepository.save(patient));
    }

    public void deletePatient(Long id) {
        Patient patient = findPatientOrThrow(id);
        patientRepository.delete(patient);
    }

    @Transactional(readOnly = true)
    public List<PatientResponseDTO> searchPatients(String keyword) {
        String searchKeyword = keyword == null ? "" : keyword.trim();
        return patientRepository.searchPatients(searchKeyword)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PatientCursorPageResponseDTO getPatientsByCursor(Long afterId, int limit) {
        int pageSize = Math.min(Math.max(limit, 1), 20);
        List<Patient> patients = patientRepository.findPatientsPageAfterId(afterId, PageRequest.of(0, pageSize + 1));

        boolean hasNext = patients.size() > pageSize;
        List<Patient> currentPage = hasNext ? patients.subList(0, pageSize) : patients;

        PatientCursorPageResponseDTO responseDTO = new PatientCursorPageResponseDTO();
        responseDTO.setPatients(currentPage.stream().map(this::toResponse).toList());
        responseDTO.setHasNext(hasNext);
        responseDTO.setNextCursor(hasNext ? currentPage.get(currentPage.size() - 1).getId() : null);
        return responseDTO;
    }

    @Transactional(readOnly = true)
    public List<PatientDetailsResponseDTO> getAllPatientsWithRecordsOptimized() {
        return patientRepository.findAllWithHealthRecords()
                .stream()
                .map(this::toDetailsResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PatientSummaryResponseDTO> getPatientSummaries() {
        return patientRepository.findPatientSummaries()
                .stream()
                .map(this::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FetchPerformanceResponseDTO compareFetchStrategies() {
        entityManager.clear();
        long lazyStart = System.nanoTime();
        List<Patient> lazyPatients = patientRepository.findAll();
        long lazyTotalRecords = lazyPatients.stream()
                .mapToLong(patient -> patient.getHealthRecords().size())
                .sum();
        long lazyElapsed = System.nanoTime() - lazyStart;

        entityManager.clear();
        long optimizedStart = System.nanoTime();
        List<Patient> optimizedPatients = patientRepository.findAllWithHealthRecords();
        long optimizedTotalRecords = optimizedPatients.stream()
                .mapToLong(patient -> patient.getHealthRecords().size())
                .sum();
        long optimizedElapsed = System.nanoTime() - optimizedStart;

        FetchPerformanceResponseDTO responseDTO = new FetchPerformanceResponseDTO();
        responseDTO.setPatientCount(optimizedPatients.size());
        responseDTO.setTotalRecords(optimizedTotalRecords);
        responseDTO.setLazyFetchTimeMs(nanosToMillis(lazyElapsed));
        responseDTO.setOptimizedFetchTimeMs(nanosToMillis(optimizedElapsed));
        responseDTO.setRecommendation(buildRecommendation(lazyTotalRecords, lazyElapsed, optimizedElapsed));
        return responseDTO;
    }

    @Transactional(readOnly = true)
    public Patient findPatientOrThrow(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    private Patient toEntity(PatientRequestDTO requestDTO) {
        Patient patient = new Patient();
        patient.setFullName(requestDTO.getFullName());
        patient.setEmail(requestDTO.getEmail());
        patient.setPhoneNumber(requestDTO.getPhoneNumber());
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        return patient;
    }

    private PatientResponseDTO toResponse(Patient patient) {
        PatientResponseDTO responseDTO = new PatientResponseDTO();
        responseDTO.setId(patient.getId());
        responseDTO.setFullName(patient.getFullName());
        responseDTO.setEmail(patient.getEmail());
        responseDTO.setPhoneNumber(patient.getPhoneNumber());
        responseDTO.setDateOfBirth(patient.getDateOfBirth());
        return responseDTO;
    }

    private PatientSummaryResponseDTO toSummaryResponse(PatientSummaryProjection summary) {
        PatientSummaryResponseDTO responseDTO = new PatientSummaryResponseDTO();
        responseDTO.setId(summary.getId());
        responseDTO.setFullName(summary.getFullName());
        responseDTO.setEmail(summary.getEmail());
        responseDTO.setPhoneNumber(summary.getPhoneNumber());
        responseDTO.setDateOfBirth(summary.getDateOfBirth());
        responseDTO.setRecordCount(Math.toIntExact(summary.getRecordCount()));
        return responseDTO;
    }

    private PatientDetailsResponseDTO toDetailsResponse(Patient patient) {
        PatientDetailsResponseDTO responseDTO = new PatientDetailsResponseDTO();
        responseDTO.setId(patient.getId());
        responseDTO.setFullName(patient.getFullName());
        responseDTO.setEmail(patient.getEmail());
        responseDTO.setPhoneNumber(patient.getPhoneNumber());
        responseDTO.setDateOfBirth(patient.getDateOfBirth());
        responseDTO.setHealthRecords(patient.getHealthRecords().stream().map(this::toRecordResponse).toList());
        return responseDTO;
    }

    private HealthRecordResponseDTO toRecordResponse(HealthRecord record) {
        HealthRecordResponseDTO responseDTO = new HealthRecordResponseDTO();
        responseDTO.setId(record.getId());
        responseDTO.setPatientId(record.getPatient().getId());
        responseDTO.setDiagnosis(record.getDiagnosis());
        responseDTO.setNotes(record.getNotes());
        responseDTO.setVisitDate(record.getVisitDate());
        return responseDTO;
    }

    private long nanosToMillis(long nanoseconds) {
        return nanoseconds / 1_000_000;
    }

    private String buildRecommendation(long lazyTotalRecords, long lazyElapsed, long optimizedElapsed) {
        if (lazyTotalRecords == 0) {
            return "Add more health records to see a clearer difference between lazy loading and fetch optimization.";
        }

        if (optimizedElapsed <= lazyElapsed) {
            return "Use the optimized fetch endpoint when patient data and health records are needed together to reduce repeated queries.";
        }

        return "Current dataset is small, so timing differences are limited. The optimized fetch will matter more as records grow.";
    }
}