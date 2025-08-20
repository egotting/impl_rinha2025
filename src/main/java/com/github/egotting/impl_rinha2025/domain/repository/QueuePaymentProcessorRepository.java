package com.github.egotting.impl_rinha2025.domain.repository;

import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IQueuePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;

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
    public void prune() {
        _queue.deleteAll();
    }

    @Override
    public boolean notification() {
        return _queue.notification();
    }
}
