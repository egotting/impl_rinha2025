package com.github.egotting.impl_rinha2025.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PaymentSummary(
        @JsonProperty("default") PaymentSummaryDetails valueDefault,
        @JsonProperty("fallback") PaymentSummaryDetails valueFallback) {

    public record PaymentSummaryDetails(
            @JsonProperty("totalRequests") long totalRequests, @JsonProperty("totalAmount") BigDecimal totalAmount) {
    }
}

