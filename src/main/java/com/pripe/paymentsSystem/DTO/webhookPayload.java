package com.pripe.paymentsSystem.DTO;

import java.util.UUID;

public record webhookPayload(String EventId, String EventType, UUID PaymentId) {
}
