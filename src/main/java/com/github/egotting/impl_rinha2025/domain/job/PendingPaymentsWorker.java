
package com.github.egotting.impl_rinha2025.domain.job;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.egotting.impl_rinha2025.domain.job.Interface.IPendingPaymentWorker;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IIntermediator;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

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

    @Scheduled(initialDelay = 100, fixedDelay = 15)
    public void pendent() {
        /* TODO: Implementar um job melhor*/
    }

}
