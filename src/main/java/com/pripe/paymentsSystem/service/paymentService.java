package com.pripe.paymentsSystem.service;
import com.pripe.paymentsSystem.DTO.createPaymentRequest;
import com.pripe.paymentsSystem.entity.PaymentStatus;
import com.pripe.paymentsSystem.entity.payment;
import com.pripe.paymentsSystem.repository.paymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class paymentService {
    private final paymentRepository PaymentRepository;

    public paymentService(paymentRepository paymentRepository) {
        PaymentRepository = paymentRepository;
    }

    public payment createPayment(createPaymentRequest request){
        payment Payment = new payment();
        Payment.setAmount(request.amount());
        Payment.setCurrency(request.currency());
        Payment.setPaymentMethod(request.paymentMethod());
        Payment.setStatus(PaymentStatus.CREATED);
        // idempotencyKey intentionally left unset — Phase 2
        return PaymentRepository.save(Payment);
    }
    public Optional<payment> getPayment(UUID id) {
        try {
            return PaymentRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
