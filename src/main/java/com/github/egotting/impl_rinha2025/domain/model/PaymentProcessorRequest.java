package com.github.egotting.impl_rinha2025.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentProcessorRequest(
                                    String correlationId,
                                    BigDecimal amount,
                                    boolean status,
                                    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
                                    Instant requestAt) {
    public static PaymentProcessorRequest of(PaymentRequest paymentRequest){
        return new PaymentProcessorRequest(
                paymentRequest.correlationId(),
                paymentRequest.amount(),
                true,
                Instant.now()
        );
    }
}
