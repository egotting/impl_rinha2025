package com.github.egotting.impl_rinha2025.http.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

import java.io.IOException;

public interface IRequestPaymentProcessor {
    int paymentDefault(PaymentRequest request);

    int paymentFallback(PaymentRequest request) throws IOException, InterruptedException;
}
