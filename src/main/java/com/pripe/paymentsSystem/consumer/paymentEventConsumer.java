package com.pripe.paymentsSystem.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class paymentEventConsumer {

    @KafkaListener(topics = "payment-events", groupId = "payments-analytics-service")
    public void consume(String Message) {
        System.out.println("[Analytics Service] Received event: " + Message);
    }
}
