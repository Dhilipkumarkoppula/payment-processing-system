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
    public ResponseEntity<payment> createPayment(@Valid @RequestBody createPaymentRequest request) {
        payment Payment = PaymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Payment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<payment>> getPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(PaymentService.getPayment(id));
    }
}
