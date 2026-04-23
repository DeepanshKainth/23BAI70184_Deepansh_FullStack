package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

public class PatientDetailsResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private List<HealthRecordResponseDTO> healthRecords;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<HealthRecordResponseDTO> getHealthRecords() {
        return healthRecords;
    }

    public void setHealthRecords(List<HealthRecordResponseDTO> healthRecords) {
        this.healthRecords = healthRecords;
    }
}