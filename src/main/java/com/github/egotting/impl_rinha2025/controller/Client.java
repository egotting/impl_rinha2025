package com.github.egotting.impl_rinha2025.controller;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IEnqueuePaymentWorker;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentSummaryService;
import com.github.egotting.impl_rinha2025.domain.service.Interface.ISemaphoreService;
import com.github.egotting.impl_rinha2025.domain.worker.Interface.IQueue;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.concurrent.ExecutionException;

@RestController
public class Client {

    private final IPaymentSummaryService _service;
    private final ISemaphoreService _semaphore;
    private final IEnqueuePaymentWorker _worker;
    private IQueue _queue;

    public Client(
            IPaymentSummaryService service,
            ISemaphoreService semaphore,
            IEnqueuePaymentWorker worker, IQueue queue) {
        _service = service;
        _semaphore = semaphore;
        _worker = worker;
        _queue = queue;
    }

    @PostMapping("/payments")
    private void sendPayment(@RequestBody PaymentRequest value) {
        _worker.exec(value);
    }

    @GetMapping("/payments-summary")
    private String getSummary(
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) throws ExecutionException, InterruptedException {
        _semaphore.set(true);
        return _service.getSummary(from, to);
    }
}
