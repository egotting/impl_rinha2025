package com.github.egotting.impl_rinha2025.domain.repository.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;

import java.time.Instant;

public interface IMemoryPaymentProcessorRepository {

    void saveDefault(PaymentRequest request);

    void saveFallback(PaymentRequest request);

    PaymentSummary summary(Instant from, Instant to);

    void prune();
}
