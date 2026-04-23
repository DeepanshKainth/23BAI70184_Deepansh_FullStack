package com.example.demo.service;

import com.example.demo.dto.PatientRequestDTO;
import com.example.demo.dto.PatientResponseDTO;
import com.example.demo.entity.Patient;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO) {
        Patient patient = toEntity(requestDTO);
        return toResponse(patientRepository.save(patient));
    }

    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

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
}