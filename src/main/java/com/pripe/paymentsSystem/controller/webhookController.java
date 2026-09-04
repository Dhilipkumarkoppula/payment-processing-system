package com.pripe.paymentsSystem.controller;

import com.pripe.paymentsSystem.DTO.webhookPayload;
import com.pripe.paymentsSystem.entity.webhookEvent;
import com.pripe.paymentsSystem.repository.webhookEventRepository;
import com.pripe.paymentsSystem.service.paymentTransactionService;
import com.pripe.paymentsSystem.webhook.webhookSignatureVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/webhooks")
public class webhookController {
    private final webhookSignatureVerifier WebhookSignatureVerifier;
    private final webhookEventRepository WebhookEventRepository;
    private final paymentTransactionService PaymentTransactionService;
    private final ObjectMapper ObjectMapper;
    public webhookController(webhookSignatureVerifier webhookSignatureVerifier, webhookEventRepository webhookEventRepository, paymentTransactionService paymentTransactionService, ObjectMapper objectMapper) {
        WebhookSignatureVerifier = webhookSignatureVerifier;
        WebhookEventRepository = webhookEventRepository;
        PaymentTransactionService = paymentTransactionService;
        ObjectMapper = objectMapper;
    }
    @PostMapping("/payment-gateway")
    public ResponseEntity<String> handleWebhook(@RequestBody String RawBody, @RequestHeader("X-Webhook-Signature") String Signature) throws Exception {
        if (!WebhookSignatureVerifier.isValid(RawBody, Signature)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }
        webhookPayload Payload = ObjectMapper.readValue(RawBody, webhookPayload.class);
        if (WebhookEventRepository.existsByEventId(Payload.EventId())) {
            return ResponseEntity.ok("Already processed");
        }
        PaymentTransactionService.applyWebhookResult(Payload);
        webhookEvent Event = new webhookEvent();
        Event.setEventId(Payload.EventId());
        Event.setEventType(Payload.EventType());
        WebhookEventRepository.save(Event);
        return ResponseEntity.ok("Processed");
    }

}



