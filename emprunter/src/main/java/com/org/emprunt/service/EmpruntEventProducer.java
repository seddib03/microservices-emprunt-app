package com.org.emprunt.service;

import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import com.org.emprunt.DTO.EmpruntEvent;

@Service
public class EmpruntEventProducer {
    private final KafkaTemplate<String, EmpruntEvent> kafkaTemplate;

    public EmpruntEventProducer(KafkaTemplate<String, EmpruntEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(EmpruntEvent event) {
        kafkaTemplate.send("emprunt-created", event);
    }
}