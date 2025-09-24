package com.github.egotting.impl_rinha2025.domain.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;


public class PaymentRequest {
    public PaymentRequest(String correlationId, BigDecimal amount) {
        this.correlationId = correlationId;
        this.amount = amount;
    }

    private String correlationId;
    private BigDecimal amount;
    private AtomicInteger attempts = new AtomicInteger(0);
    private int maxAttempts = 2;

    public boolean requeue() {
        return attempts.incrementAndGet() <= maxAttempts;
    }

    @JsonProperty("correlationId")
    public String correlationId() {
        return this.correlationId;
    }

    @JsonProperty("amount")
    public BigDecimal amount() {
        return this.amount;
    }

    public void amount(BigDecimal amount) {
        this.amount = amount;
    }

    public void correlationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public int getAttempts() {
        return attempts.get();
    }

}
