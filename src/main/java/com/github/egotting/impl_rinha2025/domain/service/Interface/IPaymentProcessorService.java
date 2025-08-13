package com.github.egotting.impl_rinha2025.domain.service.Interface;

import com.github.egotting.impl_rinha2025.domain.ENUM.TypePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

public interface IPaymentProcessorService {
    TypePaymentProcessor sendPayment(PaymentRequest payment);

    void processPayment(PaymentRequest clientRequest);

    void purgeQueue();

    boolean healthCheck(String url);
}
