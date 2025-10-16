package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService implements IPatientService {

    @Autowired
    private PatientRepository patientRepository;

    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientService(BillingServiceGrpcClient billingServiceGrpcClient) {
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        System.out.println(patients);
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) throws Exception {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail()))
            throw new EmailAlreadyExistsException("Patient wth this email already Exists "+ patientRequestDTO.getEmail());

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString()
                ,newPatient.getName()
                ,newPatient.getEmail());

        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID patientId, PatientRequestDTO patientRequestDTO) throws Exception {
        Patient patient = patientRepository.findById(patientId).orElseThrow(()-> new PatientNotFoundException("Patient not found with id: "+patientId));

        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),patientId))
            throw new EmailAlreadyExistsException("Patient wth this email already Exists "+ patientRequestDTO.getEmail());

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID patientId) throws Exception {
        Patient patient = patientRepository.findById(patientId).orElseThrow(()-> new PatientNotFoundException("No Patient Record found for id: "+patientId));
        patientRepository.deleteById(patientId);
    }
}
