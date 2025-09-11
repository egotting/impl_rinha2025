package com.github.egotting.impl_rinha2025.domain.job;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestPaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.job.Interface.IPaymentWorker;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Configuration
public class PendingPaymentsWorker implements IPaymentWorker {
    private final AtomicBoolean run = new AtomicBoolean(false);
    private final IQueuePaymentProcessorRepository _queue;
    private final IRequestPaymentProcessor _request;
    private final PaymentRequest _dto = new PaymentRequest();

    public PendingPaymentsWorker(
            IQueuePaymentProcessorRepository queue,
            IRequestPaymentProcessor request
           ) {
        _queue = queue;
        _request = request;
    }

    @Scheduled(fixedDelay = 500)
    public void pendent() {
        if (!run.compareAndSet(false, true))
            return;
        int size = Math.min(_queue.sizeQueue(), 20);

        Flux.range(0, size)
            .map(i -> _queue.poll())
            .filter(Objects::nonNull)
            .flatMapSequential(payment -> _request.payment(payment)
                     .timeout(Duration.ofMillis(800))
                     .retryWhen(Retry.backoff(2, Duration.ofMillis(200)))
                     .onErrorResume(e -> {
                             if (_dto.canRetry()) {
                                 _queue.add(payment);
                                 return Mono.empty();
                             }
                             return Mono.empty();
                         }), 20)
            .doFinally(signal -> run.set(false))
            .subscribe();
    }

}
