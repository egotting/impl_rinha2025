package com.github.egotting.impl_rinha2025.domain.Worker;

import com.github.egotting.impl_rinha2025.config.Interface.ITaskSimpleConfig;
import com.github.egotting.impl_rinha2025.domain.Worker.Interface.IPaymentWorker;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Configuration
public class PendingPaymentsWorker implements IPaymentWorker {
    private final IQueuePaymentProcessorRepository _queue;
    private final IMemoryPaymentProcessorRepository _memory;
    private final IPaymentProcessorService _service;
    //    private final ExecutorService _exec = Executors.newVirtualThreadPerTaskExecutor();
    private final ITaskSimpleConfig _exec;

    public PendingPaymentsWorker(
            IQueuePaymentProcessorRepository queue,
            IMemoryPaymentProcessorRepository memory,
            IPaymentProcessorService service, ITaskSimpleConfig exec) {
        _queue = queue;
        _memory = memory;
        _service = service;
        _exec = exec;
    }

//    @Override
//    public void init() {
//        _exec.submit(this::pendent);
//    }

    @Scheduled(initialDelay = 100, fixedDelay = 15)
    public void pendent() {
        if (_queue.isEmpty()) return;
        int size = Math.min(_queue.sizeQueue(), 30);
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            futures.add(payAsync());
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private CompletableFuture<Boolean> payAsync() {
        return _exec.submit(() -> {
            try {
                var data = _queue.poll();
                if (data == null) return false;
                var response = _service.pay(data);
                response.flatMap(process -> {
                    if (process == 1) {
                        _memory.saveDefault(data);
                        return Mono.just(true);
                    } else if (process == 2) {
                        _memory.saveFallback(data);
                        return Mono.just(true);
                    }
                    return Mono.just(false);
                });
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
            return false;
        });
    }

}
