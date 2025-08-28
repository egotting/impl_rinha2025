package com.github.egotting.impl_rinha2025.domain.model.Memory;

import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IQueuePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class QueuePaymentProcessor implements IQueuePaymentProcessor {
    private BlockingQueue<PaymentRequest> _queue = new LinkedBlockingQueue<>(10000);

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
    public PaymentRequest pollValue() throws InterruptedException {
        return _queue.poll(200, TimeUnit.MILLISECONDS);
    }


    @Override
    public void deleteAll() {
        _queue.clear();
    }

}
