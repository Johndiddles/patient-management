package com.patmgt.patientservice.controller;

import com.patmgt.patientservice.dto.PatientRequestDto;
import com.patmgt.patientservice.dto.PatientResponseDto;
import com.patmgt.patientservice.dto.validators.CreatePatientValidationGroup;
import com.patmgt.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patients", description = "API for managing patients")
public class PatientController {
    private final PatientService patientService;
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        List<PatientResponseDto> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    @Operation(summary = "Create Patient")
    public ResponseEntity<PatientResponseDto> savePatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody
            PatientRequestDto patientRequestDto
    ) {
        PatientResponseDto patient = patientService.createPatient(patientRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Patient")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable UUID id,
            @Validated({Default.class}) @RequestBody PatientRequestDto patientRequestDto
    ) {
        PatientResponseDto patient = patientService.updatePatient(id, patientRequestDto);
        return ResponseEntity.ok().body(patient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Patient")
    public ResponseEntity<Void> deletePatient(
            @PathVariable UUID id
    ) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
