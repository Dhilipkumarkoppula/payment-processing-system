package com.pripe.paymentsSystem.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(paymentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(paymentNotFoundException Ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND) .body(Map.of("error", Ex.getMessage()));
    }

    @ExceptionHandler(invalidPaymentStateException.class)
    public ResponseEntity<Map<String, String>> handleInvalidState(invalidPaymentStateException Ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) .body(Map.of("error", Ex.getMessage()));
    }
}
