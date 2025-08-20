package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IQueuePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentWorker;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PaymentWorker implements IPaymentWorker {
    private final ExecutorService _exec = Executors.newVirtualThreadPerTaskExecutor();
    private final IQueuePaymentProcessor _queue;
    private final IMemoryPaymentProcessorRepository _repository;
    private final IPaymentProcessorService _service;

    public PaymentWorker(
            IQueuePaymentProcessor queue,
            IMemoryPaymentProcessorRepository memory,
            IPaymentProcessorService service) {
        _queue = queue;
        _repository = memory;
        _service = service;
    }

    @Override
    @Scheduled(initialDelay = 100, fixedRate = 15)
    public CompletableFuture<Void> Worker() {
        return CompletableFuture.runAsync(() -> {
            do {
                var data = _queue.pollValue();
                try {
                    var response = _service.pay(data);
                    System.out.println(response);
                    response.thenAccept(value -> {
                        if (value == 1) {
                            _repository.saveDefault(data);
                        } else if (value == 2) {
                            _repository.saveFallback(data);
                        }
                    });
                } catch (Exception ignored) {
                }
            } while (_queue.notification());
        }, _exec);
    }
}
