package com.pripe.paymentsSystem.exception;

public class invalidPaymentStateException extends RuntimeException {
    public invalidPaymentStateException(String message) {
        super(message);
    }
}
