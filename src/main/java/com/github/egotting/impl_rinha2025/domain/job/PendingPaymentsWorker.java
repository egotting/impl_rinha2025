package com.github.egotting.impl_rinha2025.domain.job;

import com.github.egotting.impl_rinha2025.domain.job.Interface.IPaymentWorker;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class PendingPaymentsWorker implements IPaymentWorker {
    private final IQueuePaymentProcessorRepository _queue;
    private final IMemoryPaymentProcessorRepository _memory;
    private final IPaymentProcessorService _service;
    private final ExecutorService _exec = Executors.newVirtualThreadPerTaskExecutor();

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

    @Scheduled(fixedDelay = 15)
    public void pendent() throws IOException, InterruptedException {
        if (_queue.isEmpty()) return;
        int size = Math.min(_queue.sizeQueue(), 30);
//        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            payAsync();
        }
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private CompletableFuture<Boolean> payAsync() throws InterruptedException, IOException {
        var data = _queue.poll();
        if (data == null) return CompletableFuture.completedFuture(false);


        return _service.pay(data)
                .thenApplyAsync(res -> {
                    if (res == 1) {
                        _memory.saveDefault(data);
                        return true;
                    } else if (res == 2) {
                        _memory.saveFallback(data);
                        return true;
                    }
                    return false;
                }, _exec)
                .exceptionally(ex -> {
                    return false;
                });
    }
}
