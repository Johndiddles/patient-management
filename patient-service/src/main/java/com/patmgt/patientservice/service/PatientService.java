package com.patmgt.patientservice.service;

import com.patmgt.patientservice.dto.PatientRequestDto;
import com.patmgt.patientservice.exception.EmailAlreadyExistsException;
import com.patmgt.patientservice.exception.PatientNotFoundException;
import com.patmgt.patientservice.grpc.BillingServiceGrpcClient;
import com.patmgt.patientservice.kafka.KafkaProducer;
import com.patmgt.patientservice.mapper.PatientMapper;
import com.patmgt.patientservice.model.Patient;
import com.patmgt.patientservice.repository.PatientRepository;
import com.patmgt.patientservice.dto.PatientResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private static final Logger log = LoggerFactory.getLogger(PatientService.class);
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient,  KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDto> getPatients() {
        List<Patient> patients =  patientRepository.findAll();
        return patients.stream().map(PatientMapper::toPatientResponseDto).toList();
    }

    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        if(patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists" + patientRequestDto.getEmail());
        }
        Patient patient = patientRepository.save(PatientMapper.toPatient(patientRequestDto));

        var billingAccount = billingServiceGrpcClient.createBillingAccount(
                patient.getId().toString(),
                patient.getName(),
                patient.getEmail()
        );

        log.info("Created billing account for patient with id {}:{}", patient.getId().toString(), billingAccount.toString());

        kafkaProducer.sendEvent(patient);
        return PatientMapper.toPatientResponseDto(patient);
    }

    public PatientResponseDto updatePatient(UUID id, PatientRequestDto patientRequestDto) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id)
        );

        if(patientRepository.existsByEmailAndIdNot(patientRequestDto.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists" + patientRequestDto.getEmail());
        }

        patient.setName(patientRequestDto.getName());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));

        Patient updatatedPatient = patientRepository.save(patient);
        return PatientMapper.toPatientResponseDto(updatatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
