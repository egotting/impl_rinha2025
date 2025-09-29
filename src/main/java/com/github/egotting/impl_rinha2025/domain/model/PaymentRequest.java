package com.github.egotting.impl_rinha2025.domain.model;


import java.math.BigDecimal;


public record PaymentRequest(String correlationId,
                             BigDecimal amount
) {
}