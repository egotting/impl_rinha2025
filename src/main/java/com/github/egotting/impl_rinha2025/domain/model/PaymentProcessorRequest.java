package com.github.egotting.impl_rinha2025.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentProcessorRequest(
        String correlationId,
        BigDecimal amount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
        @Nullable Instant requestAt,
        StatusPayment status
) {
    public static PaymentProcessorRequest of(PaymentRequest paymentRequest, StatusPayment type) {
        return new PaymentProcessorRequest(
                paymentRequest.correlationId(),
                paymentRequest.amount(),
                Instant.now(),
                type
        );
    }
}
