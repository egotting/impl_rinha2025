
package com.github.egotting.impl_rinha2025.domain.worker;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IHealthCheckEngine;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IProcessPaymentService;
import com.github.egotting.impl_rinha2025.domain.service.Interface.ISemaphoreService;
import com.github.egotting.impl_rinha2025.domain.worker.Interface.IPendingPaymentsWorker;
import com.github.egotting.impl_rinha2025.domain.worker.Interface.IQueue;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Service
public class PendingPaymentsWorker implements IQueue, IPendingPaymentsWorker {
    private final BlockingQueue<PaymentRequest> _queue = new LinkedBlockingQueue<>();
    private final BlockingQueue<PaymentRequest> retry_queue = new LinkedBlockingQueue<>();

    private final Logger logger = Logger.getLogger(PendingPaymentsWorker.class.getName());

    private final ISemaphoreService _semaphore;
    private final IMemoryPaymentProcessorRepository _memory;
    private final IHealthCheckEngine health_check;
    private final IProcessPaymentService _process;

    private final AtomicBoolean run = new AtomicBoolean(false);
    private final int thread_delay = 5;
    private final ExecutorService _exec = Executors.newVirtualThreadPerTaskExecutor();

    public PendingPaymentsWorker(
            ISemaphoreService semaphore,
            IMemoryPaymentProcessorRepository memory,
            IHealthCheckEngine healthCheck,
            IProcessPaymentService process
    ) {
        _semaphore = semaphore;
        _memory = memory;
        health_check = healthCheck;
        _process = process;
    }

    @Override
    public void worker() {
        if (!run.compareAndSet(false, true)) return;
        _exec.submit(() -> {
            while (true) {
//                if (_semaphore.canISleep() == 1) {
//                    logger.info("WORKER PAUSADO PELO SUMMARY-----------");
//                    _semaphore.set(false);
//                    pauseFor(500);
//                }
                var value = _queue.take();
                logger.info("WORKER VAI INICIAR-----------");

                pauseFor(thread_delay);
                try {
                    _process.process(value);
                } catch (RuntimeException e) {
                    logger.severe("DEU NONE");
                }
            }
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
    public boolean isRun() {
        return run.get();
    }

    @Override
    public void add(PaymentRequest value) {
        if (!_memory.exists(value.correlationId())) _queue.add(value);
    }
}
