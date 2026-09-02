package com.pripe.paymentsSystem.gateway;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class paymentGatewaySimulator {

    public record gatewayResult(boolean success, String failureReason) {}
    private final Random Random = new Random();

    public gatewayResult charge(BigDecimal Amount, String Currency) {
        try {
            Thread.sleep(1000 + Random.nextInt(2000)); // simulate 1-3 second network latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        boolean Success = Random.nextInt(100) < 85;
        if (Success) {
            return new gatewayResult(true, null);
        }
        else {
            return new gatewayResult(false, "Gateway declined: insufficient funds (simulated)");
        }
    }
}
