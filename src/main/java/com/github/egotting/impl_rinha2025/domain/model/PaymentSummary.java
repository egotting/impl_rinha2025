package com.github.egotting.impl_rinha2025.domain.model;

public record PaymentSummary(
                             PaymentProcessorResponse valueDefault,
                             PaymentProcessorResponse valueFallback) {
}
