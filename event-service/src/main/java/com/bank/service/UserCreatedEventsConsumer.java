package com.bank.service;

import com.bank.model.Event;
import com.bank.repository.EventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserCreatedEventsConsumer {

    private final EventRepository repository;

    public UserCreatedEventsConsumer(EventRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "user.created", groupId = "event-service")
    public void consume(String message) {

        Event event = new Event();
        event.setEventType("USER_CREATED");
        event.setPayload(message);

        repository.save(event);
    }
}
