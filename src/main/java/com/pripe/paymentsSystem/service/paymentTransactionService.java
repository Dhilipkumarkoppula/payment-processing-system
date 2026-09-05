    package com.pripe.paymentsSystem.service;

    import com.pripe.paymentsSystem.DTO.webhookPayload;
    import com.pripe.paymentsSystem.entity.payment;
    import com.pripe.paymentsSystem.entity.PaymentStatus;
    import com.pripe.paymentsSystem.event.paymentEvent;
    import com.pripe.paymentsSystem.exception.paymentNotFoundException;
    import com.pripe.paymentsSystem.exception.invalidPaymentStateException;
    import com.pripe.paymentsSystem.gateway.paymentGatewaySimulator;
    import com.pripe.paymentsSystem.repository.paymentRepository;
    import jakarta.persistence.Id;
    import lombok.RequiredArgsConstructor;
    import org.springframework.cache.annotation.CacheEvict;
    import org.springframework.context.ApplicationEventPublisher;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import javax.xml.transform.Result;
    import java.time.Instant;
    import java.util.UUID;
    @Service
    public class paymentTransactionService {

        private final paymentRepository PaymentRepository;
        private final ApplicationEventPublisher EventPublisher;
        public paymentTransactionService(paymentRepository paymentRepository, ApplicationEventPublisher eventPublisher) {
            PaymentRepository = paymentRepository;
            EventPublisher = eventPublisher;
        }

        @CacheEvict(value = "payments", key = "#root.args[0]")
        @Transactional
        public payment markAsProcessing(UUID Id) {
            payment Payment = PaymentRepository.findById(Id)
                    .orElseThrow(() -> new paymentNotFoundException(Id));

            if (Payment.getStatus() != PaymentStatus.CREATED) {
                throw new invalidPaymentStateException(
                        "Cannot process payment in status: " + Payment.getStatus()
                );
            }

            Payment.setStatus(PaymentStatus.PROCESSING);
            payment Saved = PaymentRepository.save(Payment);

            EventPublisher.publishEvent(new paymentEvent(
                    Saved.getId(), "payment.processing", Saved.getStatus().toString(), Instant.now()
            ));

            return Saved;
        }

        @CacheEvict(value = "payments", key = "#root.args[0]")
        @Transactional
        public payment finalizeProcessing(UUID Id, paymentGatewaySimulator.gatewayResult Result) {
            payment Payment = PaymentRepository.findById(Id)
                    .orElseThrow(() -> new paymentNotFoundException(Id));

            if (Result.success()) {
                Payment.setStatus(PaymentStatus.SUCCESS);
            } else {
                Payment.setStatus(PaymentStatus.FAILED);
                Payment.setFailureReason(Result.failureReason());
            }
            Payment.setProcessedAt(Instant.now());
            payment Saved = PaymentRepository.save(Payment);

            EventPublisher.publishEvent(new paymentEvent(
                    Saved.getId(),
                    Result.success() ? "payment.success" : "payment.failed",
                    Saved.getStatus().toString(),
                    Instant.now()
            ));

            return Saved;
        }

        @CacheEvict(value = "payments", key = "#root.args[0].PaymentId()")
        @Transactional public void applyWebhookResult(webhookPayload Payload){
            payment Payment = PaymentRepository.findById(Payload.PaymentId()) .orElseThrow(() -> new paymentNotFoundException(Payload.PaymentId()));
            if (Payment.getStatus() == PaymentStatus.SUCCESS || Payment.getStatus() == PaymentStatus.FAILED) {
                return;
            }
            if (Payload.EventType().equals("payment.success")) {
                Payment.setStatus(PaymentStatus.SUCCESS);
            }
            else if (Payload.EventType().equals("payment.failed")) {
                Payment.setStatus(PaymentStatus.FAILED);
                Payment.setFailureReason("Reported failed via webhook");
            }
            Payment.setProcessedAt(Instant.now());
            payment Saved = PaymentRepository.save(Payment);

            EventPublisher.publishEvent(new paymentEvent(
                    Saved.getId(), Payload.EventType(), Saved.getStatus().toString(), Instant.now()
            ));
        }
    }
