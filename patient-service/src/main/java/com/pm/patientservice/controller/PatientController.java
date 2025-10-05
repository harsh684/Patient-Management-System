package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import com.pm.patientservice.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/getAll")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return new ResponseEntity<>(patientService.getPatients(), HttpStatus.OK);
    }

    @PostMapping("/createPatient")
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) throws Exception{
        return new ResponseEntity<>(patientService.createPatient(patientRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/updatePatient/{patientId}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable String patientId,@Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) throws Exception {
        return new ResponseEntity<>(patientService.updatePatient(UUID.fromString(patientId),patientRequestDTO),HttpStatus.OK);
    }

    @DeleteMapping("/deletePatient/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable String patientId) throws Exception {
        patientService.deletePatient(UUID.fromString(patientId));
        return ResponseEntity.noContent().build();
    }
}
