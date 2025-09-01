package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentSummaryService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PaymentSummaryService implements IPaymentSummaryService {
    private final IMemoryPaymentProcessorRepository _repo;

    public PaymentSummaryService(IMemoryPaymentProcessorRepository repo) {
        _repo = repo;
    }

    @Override
    public String getSummary(Instant from, Instant to) {
        PaymentSummary response = _repo.summary(from, to);
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
                response.valueDefault().totalRequests(),
                response.valueDefault().totalAmount(),
                response.valueFallback().totalRequests(),
                response.valueFallback().totalAmount());
    }
}
