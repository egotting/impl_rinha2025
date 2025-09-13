package com.github.egotting.impl_rinha2025.domain.job;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestPaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.job.Interface.IPaymentWorker;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import io.netty.handler.timeout.TimeoutException;
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
            IRequestPaymentProcessor request) {
        _queue = queue;
        _request = request;
    }

    @Scheduled(initialDelay = 100, fixedDelay = 600)
    public void pendent() {
        if (!run.compareAndSet(false, true))
            return;
        //System.out.println(_queue.poll());
        int size = Math.min(_queue.sizeQueue(), 5);
        Flux.range(0, size)
                .map(i -> _queue.poll())
                .filter(Objects::nonNull)
                .flatMapSequential(payment -> _request.payment(payment)
                        .onErrorResume(e -> {
                            if (_payment.requeue()) {
                                _queue.add(payment);
                            }
                            return Mono.empty();
                        }), 5)
            /*TODO: inconsistencias diminuidas para 900 por diminuição de quantidade de processos simultâneos deixar o add na fila de forma reativa */
                .doFinally(signal -> run.set(false))
                .subscribe();
    }

}
