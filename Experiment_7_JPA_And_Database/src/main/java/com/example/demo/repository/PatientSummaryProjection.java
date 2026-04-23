package com.example.demo.repository;

import java.time.LocalDate;

public interface PatientSummaryProjection {

    Long getId();

    String getFullName();

    String getEmail();

    String getPhoneNumber();

    LocalDate getDateOfBirth();

    long getRecordCount();
}