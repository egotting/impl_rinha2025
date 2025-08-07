package com.github.egotting.impl_rinha2025.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentProcessorRequest(
                                    String correlationId,
                                    BigDecimal amount,
                                    String status,
                                    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
                                    Instant requestAt) {
    public static PaymentProcessorRequest of(PaymentRequest request){
        return new PaymentProcessorRequest(
                request.correlationId(),
                request.amount(),
                "",
                java.time.Instant.now()
        );
    }
}
