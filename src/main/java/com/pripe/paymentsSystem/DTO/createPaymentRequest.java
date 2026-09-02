package com.pripe.paymentsSystem.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record createPaymentRequest(
        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amount,
                @NotBlank
                        String currency,
                @NotBlank String paymentMethod
) {}
