package com.github.egotting.impl_rinha2025.domain.Worker;

import com.github.egotting.impl_rinha2025.domain.Worker.Interface.IPaymentWorker;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class PendingPaymentsWorker implements IPaymentWorker {
    private final ExecutorService _exec = Executors.newSingleThreadExecutor();
    private final IQueuePaymentProcessorRepository _queue;
    private final IMemoryPaymentProcessorRepository _memory;
    private final IPaymentProcessorService _service;

    public PendingPaymentsWorker(
            IQueuePaymentProcessorRepository queue,
            IMemoryPaymentProcessorRepository memory,
            IPaymentProcessorService service) {
        _queue = queue;
        _memory = memory;
        _service = service;
    }

    //    @Override
//    public void init() {
//        _exec.submit(this::pendent);
//    }
    @Scheduled(initialDelay = 100, fixedDelay = 15)
    public void pendent() {
        if (_queue.isEmpty()) return;
        int size = Math.min(_queue.sizeQueue(), 20);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            futures.add(payAsync());
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private CompletableFuture<Void> payAsync() {
        return CompletableFuture.runAsync(() -> {
            try {
                var data = _queue.take();
                var response = _service.pay(data);
                if (response == 1) {
                    _memory.saveDefault(data);
                    return;
                } else if (response == 2) {
                    _memory.saveFallback(data);
                    return;
                }
                return;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, _exec);
    }

}
