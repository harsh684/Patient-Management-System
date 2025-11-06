package com.pm.patientservice.kafka;

import com.pm.patientservice.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
public class KafkaProducer {

//   <String, byte[]> : this is how we define our message types i.e msgs of type  key value pairs
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED") // event type (helps consumers to process selective events)
                .build();

        log.info("Sending new event to kafka from producer: {}", event.toString());
        try{
            kafkaTemplate.send("patient",event.toByteArray());
            log.info("Event Sent");
        }catch(Exception e){
            log.error("Error sending PatientCreated event: {}",event);
        }
    }
}
