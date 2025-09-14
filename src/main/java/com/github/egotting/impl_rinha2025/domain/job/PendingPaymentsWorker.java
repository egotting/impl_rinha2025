package com.github.egotting.impl_rinha2025.domain.job;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.egotting.impl_rinha2025.domain.job.Interface.IPendingPaymentWorker;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IIntermediator;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import reactor.core.publisher.Flux;

@Configuration
public class PendingPaymentsWorker implements IPendingPaymentWorker {
    private final AtomicBoolean run = new AtomicBoolean(false);
    private final IQueuePaymentProcessorRepository _queue;
    private final IIntermediator _intermediator;

    public PendingPaymentsWorker(
            IQueuePaymentProcessorRepository _queue,
            IIntermediator _intermediator) {
        this._queue = _queue;
        this._intermediator = _intermediator;
    }

    @Override
    @Scheduled(initialDelay = 100, fixedDelay = 50)
    public void pendent() {
        if (!run.compareAndSet(false, true))
            return;
        int size = Math.min(_queue.sizeQueue(), 1);
        Flux.range(0, size)
                .map(i -> _queue.poll())
                .filter(Objects::nonNull)
                .flatMapSequential(payment -> _intermediator.intermadiate(payment))
                .doFinally(signal -> run.set(false))
                .subscribe();
    }

}
