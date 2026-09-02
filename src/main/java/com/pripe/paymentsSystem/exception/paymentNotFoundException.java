package com.pripe.paymentsSystem.exception;

import java.util.UUID;

public class paymentNotFoundException extends RuntimeException {
    public paymentNotFoundException(UUID message) {
        super(String.valueOf(message));
    }
}
