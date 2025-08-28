package com.github.egotting.impl_rinha2025.controller;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IQueuePaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class Client {

    private final IPaymentProcessorService _service;
    private final IQueuePaymentProcessorRepository _queue;

    public Client(
            IPaymentProcessorService service, IQueuePaymentProcessorRepository queue) {
        _service = service;
        _queue = queue;
    }

    @PostMapping("/payments")
    public Mono<Void> sendPayment(@RequestBody PaymentRequest request) {
//        _queue.add(request);
//        _service.pay(request);
        _queue.add(request);
        return Mono.empty();
    }
}
