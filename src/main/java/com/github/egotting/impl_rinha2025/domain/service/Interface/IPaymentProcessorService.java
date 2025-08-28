package com.github.egotting.impl_rinha2025.domain.service.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import reactor.core.publisher.Mono;

import java.io.IOException;

public interface IPaymentProcessorService {
    Mono<Integer> pay(PaymentRequest request) throws IOException, InterruptedException;
}
