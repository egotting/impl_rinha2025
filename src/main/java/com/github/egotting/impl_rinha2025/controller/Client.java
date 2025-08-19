package com.github.egotting.impl_rinha2025.controller;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.service.PaymentProcessorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class Client {

    private final PaymentProcessorService _service;

    public Client(PaymentProcessorService service) {
        _service = service;
    }

    @PostMapping("/payments")
    public ResponseEntity sendPayment(@RequestBody PaymentRequest request) throws IOException, URISyntaxException, InterruptedException {
        _service.processPayment(request);
        return ResponseEntity.ok().build();
    }
}
