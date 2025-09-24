package com.github.egotting.impl_rinha2025.controller;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentSummaryService;
import com.github.egotting.impl_rinha2025.domain.service.Interface.ISemaphoreService;
import com.github.egotting.impl_rinha2025.domain.worker.Interface.IAddQueue;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
public class Client {

    private final IPaymentSummaryService _service;
    private final ISemaphoreService _semaphore;
    private final IAddQueue add_queue;

    public Client(
            IPaymentSummaryService service,
            ISemaphoreService semaphore, IAddQueue addQueue) {
        _service = service;
        _semaphore = semaphore;
        add_queue = addQueue;
    }

    @PostMapping("/payments")
    public void sendPayment(@RequestBody PaymentRequest value) {
        add_queue.add(value);
    }

    @GetMapping("/payments-summary")
    public String getSummary(
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) throws InterruptedException {
        _semaphore.setRun(true);
        return _service.getSummary(from, to);
    }
}
