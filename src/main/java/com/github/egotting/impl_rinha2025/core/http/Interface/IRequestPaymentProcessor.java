package com.github.egotting.impl_rinha2025.core.http.Interface;

import com.github.egotting.impl_rinha2025.domain.ENUM.TypePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface IRequestPaymentProcessor {
    Mono<TypePaymentProcessor> payment(PaymentRequest request);
    //Mono<Boolean> paymentFallback(PaymentRequest request);
}
