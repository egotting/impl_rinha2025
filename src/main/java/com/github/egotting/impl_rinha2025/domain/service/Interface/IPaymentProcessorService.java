package com.github.egotting.impl_rinha2025.domain.service.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

public interface IPaymentProcessorService {
    int sendPayment(PaymentRequest payment) throws IOException, URISyntaxException, InterruptedException;

    void processPayment(PaymentRequest clientRequest) throws IOException, URISyntaxException, InterruptedException;

    CompletableFuture<Void> cleanUpQueue();

    void purgeQueue();

    boolean healthCheck(String url);
}
