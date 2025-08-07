package com.github.egotting.impl_rinha2025.domain.repository;

import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorResponse;
import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IPaymentRepository;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class PaymentRepositoryImpl implements IPaymentRepository {
    private final ConcurrentLinkedQueue<PaymentProcessorRequest> memory = new ConcurrentLinkedQueue<>();

    @Override
    public void save(PaymentProcessorRequest request) {
        memory.add(request);
    }

    @Override
    public PaymentProcessorResponse paymentSummary(@Nullable Instant from, @Nullable Instant to) {
        List<PaymentProcessorRequest> results = new ArrayList<>(memory);

        if (from != null && to != null) {
            results = results.stream().filter(payment ->
                    !payment.requestAt().isBefore(from) &&
                            !payment.requestAt().isAfter(to)).toList();
        }

        if(results.isEmpty()){
            return new PaymentProcessorResponse(0, BigDecimal.ZERO);
        }
        Map<String, List<PaymentProcessorRequest>> listMap =
                results.stream().collect(Collectors.groupingBy(PaymentProcessorRequest::status));
//        return new PaymentSummary(
//                summary(listMap.getOrDefault("", List.of()));
//        );
        return null;
    }

    @Override
    public PaymentSummary summary(List<PaymentProcessorResponse> payments) {
        return null;
    }
}
