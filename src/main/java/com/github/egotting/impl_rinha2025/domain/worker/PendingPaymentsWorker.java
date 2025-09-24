
package com.github.egotting.impl_rinha2025.domain.worker;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestDefault;
import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestFallback;
import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.ISemaphoreService;
import com.github.egotting.impl_rinha2025.domain.worker.Interface.IAddQueue;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Service
public class PendingPaymentsWorker implements IAddQueue {
    private final ConcurrentLinkedQueue<PaymentRequest> _queue = new ConcurrentLinkedQueue<>();
    private final BlockingQueue<PaymentRequest> queue_retry = new LinkedBlockingQueue<>(20);

    private final Logger logger = Logger.getLogger(PendingPaymentsWorker.class.getName());
    private final AtomicBoolean run = new AtomicBoolean(false);

    private final ISemaphoreService _semaphore;
    private final IRequestDefault request_default;
    private final IRequestFallback request_fallback;
    private final IMemoryPaymentProcessorRepository _memory;

    private final int thread_delay = 2;
    private final ExecutorService _exec = Executors.newVirtualThreadPerTaskExecutor();

    public PendingPaymentsWorker(
            ISemaphoreService semaphore,
            IRequestDefault request_default,
            IRequestFallback request_fallback,
            IMemoryPaymentProcessorRepository memory) {
        _semaphore = semaphore;
        this.request_default = request_default;
        this.request_fallback = request_fallback;
        _memory = memory;
    }

    @PostConstruct
    public void init() {
        pauseFor(100);
        worker();
    }

    public void worker() {
        _exec.submit(() -> {
            while (true) {
                if (_semaphore.canISleep() == 1) {
                    logger.severe("PAUSE FOR SUMMARY --------------");
                    pauseFor(100);
                    _semaphore.setRun(false);
                    continue;
                }
                if (_queue.isEmpty()) continue;
                System.out.println("FILA N ESTA LIMPA ---------------------");
                var value = _queue.poll();
                System.out.println("VALUE: " + value.amount());
                pauseFor(thread_delay);
                var response = request_default.payment(value);
                System.out.println("RESPONSE ---------------------: " + response.name());
                if (response == StatusPayment.NONE) continue;
//                if (response == StatusPayment.NONE) {
//                    try {
//                        pauseFor(50);
//                        var fallback_response = request_fallback.payment(value);
//                        if (fallback_response == StatusPayment.NONE) continue;
//                        _memory.saveFallback(value);
//                    } catch (RuntimeException err) {
//                        pauseFor(500);
//                    }
//                }
                _memory.saveDefault(value);
            }
        });
    }


    private void workerToRetryValues() {
        _exec.execute(() -> {
            var value = queue_retry.poll();
            var response = request_default.payment(value);
            if (response == StatusPayment.NONE) return;
            _memory.saveDefault(value);
        });
    }

    private void pauseFor(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(PaymentRequest value) {
        _queue.offer(value);
    }
}
