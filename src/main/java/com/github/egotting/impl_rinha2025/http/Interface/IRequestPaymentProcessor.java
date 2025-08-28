package com.github.egotting.impl_rinha2025.http.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import reactor.core.publisher.Mono;

import java.io.IOException;

public interface IRequestPaymentProcessor {
    Mono<Boolean> paymentDefault(PaymentRequest request) throws IOException, InterruptedException;

    Mono<Boolean> paymentFallback(PaymentRequest request) throws IOException, InterruptedException;
}
