package com.pripe.paymentsSystem.controller;

import com.pripe.paymentsSystem.entity.payment;
import com.pripe.paymentsSystem.DTO.createPaymentRequest;
import com.pripe.paymentsSystem.service.paymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class paymentController {

    private final paymentService PaymentService;

    public paymentController(paymentService paymentService) {
        PaymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<payment> createPayment(@RequestHeader("Idempotency-Key") String idempotencyKey,@Valid @RequestBody createPaymentRequest request) {
        paymentService.IdempotentResult Result = PaymentService.createPayment(idempotencyKey, request);
        HttpStatus Status = Result.wasAlreadyCreated() ? HttpStatus.OK : HttpStatus.CREATED;
        return ResponseEntity.status(Status).body(Result.Payment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<payment>> getPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(PaymentService.getPayment(id));
    }
}
