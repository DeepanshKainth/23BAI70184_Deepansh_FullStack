package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE LOWER(a.doctorName) = LOWER(:doctor)")
    List<Appointment> findByDoctorNameIgnoreCase(@Param("doctor") String doctor);
}
