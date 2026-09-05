package com.pripe.paymentsSystem.event;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tools.jackson.databind.ObjectMapper;

@Component
public class paymentEventPublisher {

    private final KafkaTemplate<String, String> KafkaTemplate;
    private final ObjectMapper Mapper;

    public paymentEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        KafkaTemplate = kafkaTemplate;
        Mapper = mapper;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentEvent(paymentEvent Event) {
        try {
            String Json = Mapper.writeValueAsString(Event);
            KafkaTemplate.send("payment-events", Event.PaymentId().toString(), Json);
        } catch (Exception e) {
            System.err.println("Failed to publish payment event: " + e.getMessage());
        }
    }
}
