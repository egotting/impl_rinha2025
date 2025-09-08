package com.github.egotting.impl_rinha2025.controller;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentSummaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
public class Client {

    private final IPaymentSummaryService _service;
    private final IQueuePaymentProcessorRepository _queue;

    public Client(
            IPaymentSummaryService service, IQueuePaymentProcessorRepository queue) {
        _service = service;
        _queue = queue;
    }


    @PostMapping("/payments")
    public Mono<Void> sendPayment(@RequestBody PaymentRequest request) {
    _queue.add(request);
    return Mono.empty();
    }

    @GetMapping("/payments-summary")
    public String getSummary(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant to) {
        return _service.getSummary(from, to);
    }
}
