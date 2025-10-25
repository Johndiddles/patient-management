package com.patmgt.patientservice.mapper;

import com.patmgt.patientservice.dto.PatientRequestDto;
import com.patmgt.patientservice.model.Patient;
import com.patmgt.patientservice.dto.PatientResponseDto;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDto toPatientResponseDto(Patient patient){
        PatientResponseDto patientResponseDto = new PatientResponseDto();

        patientResponseDto.setId(patient.getId().toString());
        patientResponseDto.setName(patient.getName());
        patientResponseDto.setEmail(patient.getEmail());
        patientResponseDto.setAddress(patient.getAddress());
        patientResponseDto.setDateOfBirth(patient.getDateOfBirth().toString());
        return patientResponseDto;
    }

    public static Patient toPatient(PatientRequestDto patientRequestDto){
        Patient patient = new Patient();

        patient.setName(patientRequestDto.getName());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDto.getRegisteredDate()));

        return patient;
    }
}
