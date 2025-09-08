package com.github.egotting.impl_rinha2025.domain.repository;

import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IQueuePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;
import org.springframework.stereotype.Component;

@Component
public class QueuePaymentProcessorRepository implements IQueuePaymentProcessorRepository {
    private final IQueuePaymentProcessor _queue;

    public QueuePaymentProcessorRepository(IQueuePaymentProcessor queue) {
        _queue = queue;
    }

    @Override
    public void add(PaymentRequest request) {
        _queue.addInQueue(request);
    }

    @Override
    public PaymentRequest poll() {
        return _queue.pollValue();
    }

    @Override
    public boolean isEmpty() {
        return _queue.isEmpty();
    }

    @Override
    public int sizeQueue() {
        return _queue.size();
    }

    @Override
    public void prune() {
        _queue.deleteAll();
    }
}
