package com.github.egotting.impl_rinha2025.controller;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.PaymentProcessorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class Client {

    private final PaymentProcessorService _service;
    private final IQueuePaymentProcessorRepository _queue;

    public Client(PaymentProcessorService service, IQueuePaymentProcessorRepository queue) {
        _service = service;
        _queue = queue;
    }

    @PostMapping(value = "/payments", consumes = "application/json", produces = "text/plain")
    public Mono<Void> sendPayment(@RequestBody PaymentRequest request) {
        _queue.add(request);
        return Mono.empty();
    }
}
