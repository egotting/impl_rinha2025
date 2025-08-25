package com.github.egotting.impl_rinha2025.domain.model;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.math.BigDecimal;

@RegisterReflectionForBinding(PaymentRequest.class)
public record PaymentRequest(String correlationId, BigDecimal amount) {
}
