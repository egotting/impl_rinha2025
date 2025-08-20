package com.github.egotting.impl_rinha2025.domain.model.Memory.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

public interface IMemoryPaymentProcessor {
    void addInMemoryDefault(PaymentRequest request);

    void addInMemoryFallback(PaymentRequest request);

    void deleteAll();
}
