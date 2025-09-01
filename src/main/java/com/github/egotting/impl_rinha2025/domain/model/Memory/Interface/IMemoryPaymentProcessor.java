package com.github.egotting.impl_rinha2025.domain.model.Memory.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;

import java.time.Instant;

public interface IMemoryPaymentProcessor {
    void addInMemoryDefault(PaymentRequest request);

    void addInMemoryFallback(PaymentRequest request);

    PaymentSummary summary(Instant from, Instant to);

    void deleteAll();
}
