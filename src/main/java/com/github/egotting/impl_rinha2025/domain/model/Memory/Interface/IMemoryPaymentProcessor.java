package com.github.egotting.impl_rinha2025.domain.model.Memory.Interface;

import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;
import jakarta.annotation.Nullable;

import java.time.Instant;

public interface IMemoryPaymentProcessor {
    void addDb(PaymentRequest request, StatusPayment type);

    PaymentSummary summary(@Nullable Instant from, @Nullable Instant to);

    Boolean exists(String id);

    void deleteAll();
}
