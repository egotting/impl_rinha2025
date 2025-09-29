package com.github.egotting.impl_rinha2025.domain.model.Memory;

import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IMemoryPaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MemoryPaymentProcessor implements IMemoryPaymentProcessor {
    ConcurrentHashMap<String, PaymentProcessorRequest> db = new ConcurrentHashMap<>();


    @Override
    public void addDb(PaymentRequest value, StatusPayment type) {
        db.compute(value.correlationId(), (id, exist) -> {
            if (exist == null) return PaymentProcessorRequest.of(value, type);

            if (exist.status() == StatusPayment.DEFAULT) return exist;
            return PaymentProcessorRequest.of(value, type);
        });
    }

    public PaymentSummary summary(@Nullable Instant from, @Nullable Instant to) {
        long totalDefault = 0, totalFallback = 0;
        BigDecimal amountDefault = BigDecimal.ZERO, amountFallback = BigDecimal.ZERO;

        for (PaymentProcessorRequest req : db.values()) {
            Instant ts = req.requestAt();
            if (ts != null) {
                if (from != null && ts.isBefore(from)) continue;
                if (to != null && ts.isAfter(to)) continue;
            }

            if (req.status() == StatusPayment.DEFAULT) {
                totalDefault++;
                amountDefault = amountDefault.add(req.amount());
            }
            if (req.status() == StatusPayment.FALLBACK) {
                totalFallback++;
                amountFallback = amountFallback.add(req.amount());
            }
        }
        PaymentSummary.PaymentSummaryDetails defaultSummary =
                new PaymentSummary.PaymentSummaryDetails(totalDefault, amountDefault);
        PaymentSummary.PaymentSummaryDetails fallbackSummary =
                new PaymentSummary.PaymentSummaryDetails(totalFallback, amountFallback);
        pauseFor(20);
        return new PaymentSummary(defaultSummary, fallbackSummary);
    }

    @Override
    public Boolean exists(String id) {
        return db.containsKey(id);
    }

    @Override
    public void deleteAll() {
        db.clear();
    }


    void pauseFor(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
