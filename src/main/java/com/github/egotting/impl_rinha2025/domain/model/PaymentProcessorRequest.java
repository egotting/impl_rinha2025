package com.github.egotting.impl_rinha2025.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.time.Instant;

@RegisterReflectionForBinding(PaymentProcessorRequest.class)
public record PaymentProcessorRequest(
        String correlationId,
        BigDecimal amount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
        @Nullable Instant requestAt
                                      ) {
    public static PaymentProcessorRequest of(PaymentRequest paymentRequest) {
        return new PaymentProcessorRequest(
                                           paymentRequest.correlationId(),
                                           paymentRequest.amount(),
                                           Instant.now()
                                           );
    }
}
