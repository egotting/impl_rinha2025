package com.github.egotting.impl_rinha2025.domain.model.Memory.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

public interface IQueuePaymentProcessor {
    void addInQueue(PaymentRequest request);

    PaymentRequest pollValue();

    void deleteAll();

    boolean notification();
}
