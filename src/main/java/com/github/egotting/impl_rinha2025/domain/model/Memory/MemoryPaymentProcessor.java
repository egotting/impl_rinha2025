package com.github.egotting.impl_rinha2025.domain.model.Memory;

import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IMemoryPaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
                Instant.now());
        dbMemoryDefault.offer(convert);
    }

    @Override
    public void addInMemoryFallback(PaymentRequest request) {
        PaymentProcessorRequest convert = new PaymentProcessorRequest(
                request.correlationId(),
                request.amount(),
                Instant.now());
        dbMemoryFallback.offer(convert);
    }

    @Override
    public PaymentSummary summary(@Nullable Instant from, @Nullable Instant to) {
        PaymentSummary.PaymentSummaryDetails defaultSummary = processSummaryDefault(from, to);
        PaymentSummary.PaymentSummaryDetails fallbackSummary = processSummaryFallback(from, to);
	System.out.println(new PaymentSummary(defaultSummary, fallbackSummary));
        return new PaymentSummary(defaultSummary, fallbackSummary);
    }

    @Override
    public void deleteAll() {
        dbMemoryDefault.clear();
        dbMemoryFallback.clear();
    }

    private PaymentSummary.PaymentSummaryDetails processSummaryDefault(Instant from, Instant to) {
        var totalDefault = dbMemoryDefault.size();
        var totalDefaultAmount = dbMemoryDefault.stream()
                .filter(t -> t.requestAt() != null &&
                        (from == null || !t.requestAt().isBefore(from)) &&
                        (to == null || !t.requestAt().isAfter(to)))
                .map(PaymentProcessorRequest::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new PaymentSummary.PaymentSummaryDetails(totalDefault, totalDefaultAmount);
    }

    private PaymentSummary.PaymentSummaryDetails processSummaryFallback(Instant from, Instant to) {
        var totalFallback = dbMemoryFallback.size();
        var totalFallbackAmount = dbMemoryFallback.stream().filter(t -> t.requestAt() != null &&
                        (from == null || !t.requestAt().isBefore(from)) &&
                        (to == null || !t.requestAt().isAfter(to)))
                .map(PaymentProcessorRequest::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new PaymentSummary.PaymentSummaryDetails(totalFallback, totalFallbackAmount);
    }


}
