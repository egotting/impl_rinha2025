package com.github.egotting.impl_rinha2025.domain.service.Interface;

import com.github.egotting.impl_rinha2025.domain.ENUM.TypePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface IPaymentProcessorService {
    Mono<Void> pay(PaymentRequest request);
}
