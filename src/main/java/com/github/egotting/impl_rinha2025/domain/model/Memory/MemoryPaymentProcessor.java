package com.github.egotting.impl_rinha2025.domain.model.Memory;

import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IMemoryPaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MemoryPaymentProcessor implements IMemoryPaymentProcessor {
    ConcurrentLinkedQueue<PaymentProcessorRequest> dbMemoryDefault = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<PaymentProcessorRequest> dbMemoryFallback = new ConcurrentLinkedQueue<>();


    @Override
    public void addInMemoryDefault(PaymentRequest request) {
        PaymentProcessorRequest convert = new PaymentProcessorRequest(
                request.correlationId(),
                request.amount(),
                true,
                Instant.now());
        dbMemoryDefault.add(convert);
    }

    @Override
    public void addInMemoryFallback(PaymentRequest request) {
        PaymentProcessorRequest convert = new PaymentProcessorRequest(
                request.correlationId(),
                request.amount(),
                true,
                Instant.now());
        dbMemoryFallback.add(convert);
    }

    @Override
    public void deleteAll() {
        dbMemoryDefault.clear();
        dbMemoryFallback.clear();
    }
}
