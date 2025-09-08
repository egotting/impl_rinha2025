package com.github.egotting.impl_rinha2025.domain.model.Memory;

import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IQueuePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class QueuePaymentProcessor implements IQueuePaymentProcessor {
    private ConcurrentLinkedQueue<PaymentRequest> _queue = new ConcurrentLinkedQueue<>();

    @Override
    public void addInQueue(PaymentRequest request) {
        _queue.offer(request);
    }

    @Override
    public int size() {
        return _queue.size();
    }

    @Override
    public boolean isEmpty() {
        return _queue.isEmpty();
    }

    @Override
    public PaymentRequest pollValue() {
        return _queue.poll();
    }


    @Override
    public void deleteAll() {
        _queue.clear();
    }

}
