package com.github.egotting.impl_rinha2025.domain.repository.Interface;

import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;
import jakarta.annotation.Nullable;

import java.time.Instant;

public interface IMemoryPaymentProcessorRepository {

    void save(PaymentRequest request, StatusPayment type);

    PaymentSummary summary(@Nullable Instant from, @Nullable Instant to);
    Boolean exists(String id);


    void prune();
}
