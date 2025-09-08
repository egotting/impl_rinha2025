package com.github.egotting.impl_rinha2025.domain.job;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestPaymentProcessor;
//import com.github.egotting.impl_rinha2025.core.Executer.Interface.ITaskSimpleConfig;
import com.github.egotting.impl_rinha2025.domain.ENUM.TypePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.job.Interface.IPaymentWorker;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class PendingPaymentsWorker implements IPaymentWorker {
    private final AtomicBoolean run = new AtomicBoolean(false);

    private final IQueuePaymentProcessorRepository _queue;
    private final IMemoryPaymentProcessorRepository _memory;
    private final IPaymentProcessorService _service;
    private final IRequestPaymentProcessor _request;

    // private final ITaskSimpleConfig _exec;
    public PendingPaymentsWorker(
            IQueuePaymentProcessorRepository queue,
            IMemoryPaymentProcessorRepository memory,
            IPaymentProcessorService service,
            // ITaskSimpleConfig exec
            IRequestPaymentProcessor request) {
        _queue = queue;
        _memory = memory;
        _service = service;
        _request = request;
    }

    @Scheduled(fixedDelay = 15)
    public void pendent() {
        if (!run.compareAndSet(false, true))
            return;

        int size = Math.min(_queue.sizeQueue(), 20);

        Flux.range(0, size)
                .map(i -> _queue.poll())
                .filter(Objects::nonNull)
                .flatMap(payment -> _request.payment(payment)
                        .timeout(Duration.ofMillis(1000))
                        .retry(1)
                        .onErrorResume(e -> {
                            _queue.add(payment);
                            return Mono.empty();
                        }), 20)
                .doFinally(signal -> run.set(false))
                .subscribe();
    }
}
