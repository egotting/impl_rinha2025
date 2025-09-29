package com.github.egotting.impl_rinha2025.domain.repository;

import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IMemoryPaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MemoryPaymentProcessorRepository implements IMemoryPaymentProcessorRepository {
    private final IMemoryPaymentProcessor _memory;

    public MemoryPaymentProcessorRepository(IMemoryPaymentProcessor memory) {
        _memory = memory;
    }

    @Override
    public void save(PaymentRequest request, StatusPayment type) {
        _memory.addDb(request, type);
    }

    @Override
    public PaymentSummary summary(@Nullable Instant from, @Nullable Instant to) {
        return _memory.summary(from, to);
    }

    @Override
    public Boolean exists(String id) {
        return _memory.exists(id);
    }


    @Override
    public void prune() {
        _memory.deleteAll();
    }

}
