package com.example.demo.controller;

import com.example.demo.dto.FetchPerformanceResponseDTO;
import com.example.demo.dto.PatientCursorPageResponseDTO;
import com.example.demo.dto.PatientDetailsResponseDTO;
import com.example.demo.dto.PatientRequestDTO;
import com.example.demo.dto.PatientResponseDTO;
import com.example.demo.dto.PatientSummaryResponseDTO;
import com.example.demo.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponseDTO createPatient(@Valid @RequestBody PatientRequestDTO requestDTO) {
        return patientService.createPatient(requestDTO);
    }

    @GetMapping
    public List<PatientResponseDTO> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/search")
    public List<PatientResponseDTO> searchPatients(@RequestParam String keyword) {
        return patientService.searchPatients(keyword);
    }

    @GetMapping("/cursor")
    public PatientCursorPageResponseDTO getPatientsByCursor(
            @RequestParam(required = false) Long afterId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return patientService.getPatientsByCursor(afterId, limit);
    }

    @GetMapping("/summary")
    public List<PatientSummaryResponseDTO> getPatientSummaries() {
        return patientService.getPatientSummaries();
    }

    @GetMapping("/with-records")
    public List<PatientDetailsResponseDTO> getPatientsWithRecords() {
        return patientService.getAllPatientsWithRecordsOptimized();
    }

    @GetMapping("/performance/fetch-strategies")
    public FetchPerformanceResponseDTO compareFetchStrategies() {
        return patientService.compareFetchStrategies();
    }

    @GetMapping("/{id}")
    public PatientResponseDTO getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    public PatientResponseDTO updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequestDTO requestDTO) {
        return patientService.updatePatient(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}