package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;

import java.util.List;

public interface IPatientService {

    public List<PatientResponseDTO> getPatients();

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) throws Exception;
}
