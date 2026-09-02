package com.pripe.paymentsSystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "webhook_events")
@Getter
@Setter
public class webhookEvent {

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Instant getReceivedAt() {
        return ReceivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        ReceivedAt = receivedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false,unique = true)
    private String eventId;
    @Column(nullable = false, updatable = false)
    private Instant ReceivedAt;
    @Column(nullable = false)
    private String EventType;

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    @PrePersist
    void onCreate() {
        ReceivedAt = Instant.now();
    }


}
