package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.core.Executer.Interface.ITaskSimpleConfig;
import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestPaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.ENUM.TypePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class PaymentProcessorService implements IPaymentProcessorService {
    private final IRequestPaymentProcessor _requestPayment;
    private final IMemoryPaymentProcessorRepository _memory;

    public PaymentProcessorService(
            IMemoryPaymentProcessorRepository memory,
            IRequestPaymentProcessor _requestPayment) {
        this._memory = memory;
        this._requestPayment = _requestPayment;
    }

    @Override
    public Mono<Void> pay(PaymentRequest request) {
        return Mono.empty();
    }
}
