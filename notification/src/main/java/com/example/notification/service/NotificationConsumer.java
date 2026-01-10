package com.example.notification.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import com.example.notification.event.EmpruntEvent;
@Service
@Slf4j
public class NotificationConsumer {

    @KafkaListener(topics = "emprunt-created", groupId = "notification-group")
    public void consume(EmpruntEvent event) {
        log.info("📢 Notification reçue : Emprunt ID = {}, User = {}, Book = {}",
                event.getEmpruntId(),
                event.getUserId(),
                event.getBookId());
    }
}

  