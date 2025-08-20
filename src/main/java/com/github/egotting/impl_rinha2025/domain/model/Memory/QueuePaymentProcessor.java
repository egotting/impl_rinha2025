package com.github.egotting.impl_rinha2025.domain.model.Memory;

import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IQueuePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class QueuePaymentProcessor implements IQueuePaymentProcessor {
    private ConcurrentLinkedQueue<PaymentRequest> dbQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void addInQueue(PaymentRequest request) {
        dbQueue.add(request);
    }

    @Override
    public PaymentRequest pollValue() {
        return dbQueue.poll();
    }

    @Override
    public void deleteAll() {
        dbQueue.clear();
    }

    @Override
    public boolean notification() {
        if (dbQueue.isEmpty()) pauseFor(300);
        return !dbQueue.isEmpty();
    }

    private void pauseFor(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {
        }
    }
}
