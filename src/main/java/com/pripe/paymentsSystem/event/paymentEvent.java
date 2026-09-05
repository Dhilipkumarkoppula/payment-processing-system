package com.pripe.paymentsSystem.event;

import java.time.Instant;
import java.util.UUID;

public record paymentEvent(UUID PaymentId,
                           String EventType,
                           String Status,
                           Instant Timestamp) {
}
