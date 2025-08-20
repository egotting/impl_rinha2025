package com.github.egotting.impl_rinha2025.http.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

public interface IRequestPaymentProcessor {
    void paymentDefault(PaymentRequest request);

    void paymentFallback(PaymentRequest request);
}
