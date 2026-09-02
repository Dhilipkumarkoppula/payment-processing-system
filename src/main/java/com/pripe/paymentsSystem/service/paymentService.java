package com.pripe.paymentsSystem.service;
import com.pripe.paymentsSystem.DTO.createPaymentRequest;
import com.pripe.paymentsSystem.entity.PaymentStatus;
import com.pripe.paymentsSystem.entity.payment;
import com.pripe.paymentsSystem.repository.paymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class paymentService {
    public record IdempotentResult(payment Payment, boolean wasAlreadyCreated) {}
    private final paymentRepository PaymentRepository;

    public paymentService(paymentRepository paymentRepository) {
        PaymentRepository = paymentRepository;
    }

    public IdempotentResult createPayment(String idempotencyKey,createPaymentRequest request){
        Optional<payment> existing = PaymentRepository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            return new IdempotentResult(existing.get(), true);
        }
        payment Payment = new payment();
        Payment.setIdempotencyKey(idempotencyKey);
        Payment.setAmount(request.amount());
        Payment.setCurrency(request.currency());
        Payment.setPaymentMethod(request.paymentMethod());
        Payment.setStatus(PaymentStatus.CREATED);
        // idempotencyKey intentionally left unset — Phase 2
        try {
            payment saved = PaymentRepository.save(Payment);
            return new IdempotentResult(saved, false);
        } catch (DataIntegrityViolationException e) {
            // Race condition: another request with the same key was saved microseconds before us. // The DB's UNIQUE constraint caught it — fall back to returning that existing record.
            payment raceWinner = PaymentRepository.findByIdempotencyKey(idempotencyKey) .orElseThrow(() -> e);
             return new IdempotentResult(raceWinner, true);
            // }
        }
    }
    public Optional<payment> getPayment(UUID id) {
        try {
            return PaymentRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
