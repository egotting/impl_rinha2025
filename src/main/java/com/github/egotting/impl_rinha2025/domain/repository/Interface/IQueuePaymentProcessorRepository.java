package com.github.egotting.impl_rinha2025.domain.repository.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

public interface IQueuePaymentProcessorRepository {
    void add(PaymentRequest request);

    PaymentRequest poll() throws InterruptedException;

    boolean isEmpty();

    int sizeQueue();

    void prune();
}
