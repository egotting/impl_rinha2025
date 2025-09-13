package com.github.egotting.impl_rinha2025.core.http.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

import reactor.core.publisher.Mono;

public interface IRequestPaymentProcessor {
    Mono<Void> payment(PaymentRequest request);
}
