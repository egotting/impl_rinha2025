package com.github.egotting.impl_rinha2025.domain.service.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

public interface IPaymentProcessorService {
    boolean sendPayment(PaymentRequest payment);

    void processPayment(PaymentRequest clientRequest);
    void purgeData();
}
