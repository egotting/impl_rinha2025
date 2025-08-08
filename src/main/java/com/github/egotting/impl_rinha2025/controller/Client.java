package com.github.egotting.impl_rinha2025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.service.PaymentProcessorService;

@RestController
public class Client {
    @Autowired
    private PaymentProcessorService _service;

    @PostMapping("/payments")
    public ResponseEntity post(@RequestBody PaymentRequest request) {
        // var payment = _service.sendPayment(request);
        return ResponseEntity.ok().build();
    }
}
