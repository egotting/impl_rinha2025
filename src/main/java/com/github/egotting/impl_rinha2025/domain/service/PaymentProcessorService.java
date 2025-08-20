package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.config.Interface.IUdpServerConfig;
import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IQueuePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.MemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import com.github.egotting.impl_rinha2025.http.Interface.IRequestPaymentProcessor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PaymentProcessorService implements IPaymentProcessorService {
    private final String Default = "http://payment-processor-default:8080";
    private final String Fallback = "http://payment-processor-fallback:8080";
//    private final String Default = "http://localhost:8001";
//    private final String Fallback = "http://localhost:8002";
    //    @Value("${payment.processor.default}")
    //    private static String Default;
    //    @Value("${payment.processor.fallback}")
    //    private static String Fallback;

    private final MemoryPaymentProcessorRepository _repository;
    private final IUdpServerConfig _udpServerConfig;
    private final ExecutorService _exec = Executors.newVirtualThreadPerTaskExecutor();
    private final IRequestPaymentProcessor _requestPayment;
    private final IQueuePaymentProcessor dbQueue;

    public PaymentProcessorService(
            IUdpServerConfig _udpServerConfig,
            MemoryPaymentProcessorRepository _repository,
            IRequestPaymentProcessor requestPayment, IQueuePaymentProcessor dbQueue
    ) {
        this._repository = _repository;
        this._udpServerConfig = _udpServerConfig;
        this._requestPayment = requestPayment;
        this.dbQueue = dbQueue;
    }

    @Override
    public CompletableFuture<Integer> pay(PaymentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!_udpServerConfig.checkApiJob(Default).get() && !_udpServerConfig.checkApiJob(Fallback).get()) {
                    dbQueue.addInQueue(request);
                }
                if (_udpServerConfig.checkApiJob(Default).get()) {
                    _requestPayment.paymentDefault(request);
                    return 1;
                }
                if (_udpServerConfig.checkApiJob(Fallback).get()) {
                    _requestPayment.paymentFallback(request);
                    return 2;
                }
            } catch (Exception ignored) {
            }
            return 0;
        }, _exec);
    }

}
