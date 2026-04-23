package com.example.demo.controller;

import com.example.demo.dto.HealthRecordRequestDTO;
import com.example.demo.dto.HealthRecordResponseDTO;
import com.example.demo.service.HealthRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/records")
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    public HealthRecordController(HealthRecordService healthRecordService) {
        this.healthRecordService = healthRecordService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HealthRecordResponseDTO createRecord(
            @PathVariable Long patientId,
            @Valid @RequestBody HealthRecordRequestDTO requestDTO
    ) {
        return healthRecordService.createRecord(patientId, requestDTO);
    }

    @GetMapping
    public List<HealthRecordResponseDTO> getRecordsByPatient(@PathVariable Long patientId) {
        return healthRecordService.getRecordsByPatient(patientId);
    }

    @GetMapping("/{recordId}")
    public HealthRecordResponseDTO getRecordById(@PathVariable Long patientId, @PathVariable Long recordId) {
        return healthRecordService.getRecordById(patientId, recordId);
    }

    @PutMapping("/{recordId}")
    public HealthRecordResponseDTO updateRecord(
            @PathVariable Long patientId,
            @PathVariable Long recordId,
            @Valid @RequestBody HealthRecordRequestDTO requestDTO
    ) {
        return healthRecordService.updateRecord(patientId, recordId, requestDTO);
    }

    @DeleteMapping("/{recordId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecord(@PathVariable Long patientId, @PathVariable Long recordId) {
        healthRecordService.deleteRecord(patientId, recordId);
    }
}