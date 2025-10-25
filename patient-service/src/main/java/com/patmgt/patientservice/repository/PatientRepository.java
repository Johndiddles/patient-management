package com.patmgt.patientservice.repository;

import com.patmgt.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    public boolean existsByEmail(String email);
    public boolean existsByEmailAndIdNot(String email, UUID id);
}
