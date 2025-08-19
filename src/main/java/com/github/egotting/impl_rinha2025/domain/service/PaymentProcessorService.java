package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.config.BuildRequest;
import com.github.egotting.impl_rinha2025.config.Interface.IUdpServerConfig;
import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.PaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

/* TODO: DANADO 7000 REQUESTS FALHOS RESOLVER*/
@Service
public class PaymentProcessorService implements IPaymentProcessorService {
    private ConcurrentLinkedQueue<PaymentRequest> dbQueue = new ConcurrentLinkedQueue<>();
    private final String Default = "http://payment-processor-default:8080";
    private final String Fallback = "http://payment-processor-fallback:8080";
//    private final String Default = "http://localhost:8001";
//    private final String Fallback = "http://localhost:8002";
    //    @Value("${payment.processor.default}")
    //    private static String Default;
    //    @Value("${payment.processor.fallback}")
    //    private static String Fallback;

    private final PaymentProcessorRepository _repository;
    private final Executor _exec;
    private final IUdpServerConfig _udpServerConfig;

    public PaymentProcessorService(IUdpServerConfig _udpServerConfig, PaymentProcessorRepository _repository, @Qualifier("worker") Executor _exec) {
        this._repository = _repository;
        this._exec = _exec;
        this._udpServerConfig = _udpServerConfig;
    }

    @Override
    public boolean healthCheck(String url) {
        return false;
    }


    @Override
    public void processPayment(PaymentRequest request) throws IOException, URISyntaxException, InterruptedException {
        var _default = new PaymentProcessorRequest(request.correlationId(), request.amount(), true, Instant.now());
        var _fallback = new PaymentProcessorRequest(request.correlationId(), request.amount(), false, Instant.now());
        var response = sendPayment(request);
        if (response == 0) {
            sendToQueue(request);
        }
        if (response == 1) {
            _repository.saveFallback(_fallback);
            return;
        }
        if (response == 2) {
            _repository.saveDefault(_default);
            return;
        }

    }

    @Override
    @Scheduled(initialDelay = 100, fixedDelay = 15)
    public CompletableFuture<Void> cleanUpQueue() {
        return CompletableFuture.runAsync(() -> {
            do {
                var data = dbQueue.poll();
                if (data == null) return;
                try {
                    sendPayment(data);
                } catch (IOException | URISyntaxException | InterruptedException ignored) {
                }
            } while (!dbQueue.isEmpty());
        }, _exec);
    }

    public void sendToQueue(PaymentRequest request) {
        dbQueue.add(request);
    }

    @Override
    public void purgeQueue() {
        dbQueue.clear();
    }

    @Override
    public int sendPayment(PaymentRequest request) throws IOException, URISyntaxException, InterruptedException {
        String formattedRequest = """
                {
                  "correlationId": "%s",
                  "amount": %.2f
                }
                """.formatted(request.correlationId(), request.amount());
        int returnType = 0;
        try {
            if (!_udpServerConfig.checkApiJob(Default).get()) {
                System.out.println(!_udpServerConfig.checkApiJob(Default).get());
                BuildRequest.buildRequest(formattedRequest, new URI(Fallback));
                returnType += 1;
            }
            if (_udpServerConfig.checkApiJob(Default).get()) {
                BuildRequest.buildRequest(formattedRequest, new URI(Default));
                return returnType += 2;
            }
        } catch (Exception ignored) {
        }
        System.out.println(returnType);
        return returnType;
    }
}
