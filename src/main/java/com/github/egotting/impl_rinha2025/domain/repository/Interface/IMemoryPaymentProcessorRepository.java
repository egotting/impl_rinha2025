package com.github.egotting.impl_rinha2025.domain.repository.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

public interface IMemoryPaymentProcessorRepository {

    void saveDefault(PaymentRequest request);

    void saveFallback(PaymentRequest request);

    void prune();
}
