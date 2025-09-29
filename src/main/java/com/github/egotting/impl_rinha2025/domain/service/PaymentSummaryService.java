package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentSummaryService;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PaymentSummaryService implements IPaymentSummaryService {
    private final IMemoryPaymentProcessorRepository _repo;

    public PaymentSummaryService(IMemoryPaymentProcessorRepository repo) {
        _repo = repo;
    }

    @Override
    public String getSummary(@Nullable Instant from, @Nullable Instant to) {
        PaymentSummary summary = _repo.summary(from, to);
        return """
                {
                    "default": {
                        "totalRequests": %d,
                        "totalAmount": %s
                    },
                    "fallback": {
                        "totalRequests": %d,
                        "totalAmount": %s
                    }
                }
                """.formatted(
                summary.valueDefault().totalRequests(),
                summary.valueDefault().totalAmount(),
                summary.valueFallback().totalRequests(),
                summary.valueFallback().totalAmount()
        );
    }
}
