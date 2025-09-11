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

@Configuration
public class PendingPaymentsWorker implements IPaymentWorker {
    private final AtomicBoolean run = new AtomicBoolean(false);
    private final IQueuePaymentProcessorRepository _queue;
    private final IRequestPaymentProcessor _request;
    private final PaymentRequest _payment = new PaymentRequest();

    public PendingPaymentsWorker(
            IQueuePaymentProcessorRepository queue,
            IRequestPaymentProcessor request
           ) {
        _queue = queue;
        _request = request;
    }

    @Scheduled(initialDelay = 100, fixedDelay = 600)
    public void pendent() {
        if (!run.compareAndSet(false, true))
            return;
        int size = Math.min(_queue.sizeQueue(), 20);

        Flux.range(0, size)
            .map(i -> _queue.poll())
            .filter(Objects::nonNull)
            .flatMap(payment -> _request.payment(payment)
                     //.timeout(Duration.ofMillis(2000))
                               .onErrorResume(e -> {
                                       if (_payment.requeue()) {
                                           _queue.add(payment);
                                       }
                                       return Mono.empty();
                                   })
                               //.retryWhen(Retry.backoff(2, Duration.ofMillis(200)))
                               , 20)
            .doFinally(signal -> run.set(false))
            .subscribe();
    }

}
