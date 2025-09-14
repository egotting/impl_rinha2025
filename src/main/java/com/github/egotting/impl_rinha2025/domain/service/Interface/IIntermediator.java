package com.github.egotting.impl_rinha2025.domain.service.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

import reactor.core.publisher.Mono;

public interface IIntermediator {
    Mono<Void> intermadiate(PaymentRequest request);
}
