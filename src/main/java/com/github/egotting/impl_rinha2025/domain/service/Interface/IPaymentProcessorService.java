package com.github.egotting.impl_rinha2025.domain.service.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface IPaymentProcessorService {
    CompletableFuture<Integer> pay(PaymentRequest request) throws IOException, InterruptedException;
}
