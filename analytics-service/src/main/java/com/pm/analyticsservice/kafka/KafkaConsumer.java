package com.pm.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

//    group id tells kafka who this consumer is
    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event){
        try {
            PatientEvent patientevent = PatientEvent.parseFrom(event);
            log.info("Received Patient Event: [patientId={}, " +
                    "patientName={}, patientEmail={}]",
                    patientevent.getPatientId(),patientevent.getName(),patientevent.getEmail());
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event {}",e.getMessage());
        }
    }
}
