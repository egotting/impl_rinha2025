package com.github.egotting.impl_rinha2025.domain.repository;

import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IMemoryPaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
public class MemoryPaymentProcessorRepository implements IMemoryPaymentProcessorRepository {
    private final IMemoryPaymentProcessor _memory;

    public MemoryPaymentProcessorRepository(IMemoryPaymentProcessor memory) {
        _memory = memory;
    }


    @Override
    public void saveDefault(PaymentRequest request) {
        _memory.addInMemoryDefault(request);
    }

    @Override
    public void saveFallback(PaymentRequest request) {
        _memory.addInMemoryFallback(request);
    }

    @Override
    public PaymentSummary summary(Instant from, Instant to) {
        return _memory.summary(from, to);
    }

    @Override
    public void prune() {
        _memory.deleteAll();
    }
}
