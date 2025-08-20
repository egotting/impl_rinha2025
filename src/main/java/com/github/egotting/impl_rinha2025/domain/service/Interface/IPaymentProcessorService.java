package com.github.egotting.impl_rinha2025.domain.service.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

import java.util.concurrent.CompletableFuture;

public interface IPaymentProcessorService {
    CompletableFuture<Integer> pay(PaymentRequest request);
}
