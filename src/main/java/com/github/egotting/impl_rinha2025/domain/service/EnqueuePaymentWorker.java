package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IEnqueuePaymentWorker;
import com.github.egotting.impl_rinha2025.domain.worker.Interface.IPendingPaymentsWorker;
import com.github.egotting.impl_rinha2025.domain.worker.Interface.IQueue;
import org.springframework.stereotype.Service;

@Service
public class EnqueuePaymentWorker implements IEnqueuePaymentWorker {

    private final IQueue _queue;
    private final IPendingPaymentsWorker _worker;

    public EnqueuePaymentWorker(IQueue queue, IPendingPaymentsWorker worker) {
        _queue = queue;
        _worker = worker;
    }

    @Override
    public void exec(PaymentRequest value) {
        if (!_worker.isRun()) _worker.worker();
        _queue.add(value);
    }
}
