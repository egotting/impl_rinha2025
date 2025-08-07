package com.github.egotting.impl_rinha2025.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentSummary(
                             @JsonProperty("default")
                             PaymentProcessorResponse valueDefault,
                             PaymentProcessorResponse valueFallback) {
}
