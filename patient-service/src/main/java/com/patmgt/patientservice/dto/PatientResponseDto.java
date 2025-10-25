package com.patmgt.patientservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PatientResponseDto {

    private String id;
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;

}
