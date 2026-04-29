package com.bank.consumer;

import com.bank.model.Event;
import com.bank.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserCreatedEventsConsumer {

    private final EventRepository eventRepository;

    @KafkaListener(topics = "user-created-events", groupId = "event-service-group")
    public void consume(String message) {

        Event event = new Event();
        event.setEventType("USER_CREATED");
        event.setPayload(message);
        event.setCreatedAt(LocalDateTime.now());

        eventRepository.save(event);

        System.out.println("🔥 Event stored: " + message);
    }
}