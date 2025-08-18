package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.config.BuildRequest;
import com.github.egotting.impl_rinha2025.config.UdpServerConfig;
import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.PaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private PaymentProcessorRepository _repository;
    private final Executor _exec;

    public PaymentProcessorService(PaymentProcessorRepository _repository, @Qualifier("taskExecutor") Executor _exec) {
        this._repository = _repository;
        this._exec = _exec;
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
        if (response == 0) sendToQueue(request);
        if (response == 1) {
            _repository.save(_fallback);
        }
        _repository.save(_default);
        /* TODO: CHAMAR O HEALTHCHECK PARA VERIFICAR SE AINDA ESTA CAIDO E CASO N ESTEJA
         *   CHAMA O SENDPAYMENT MANDANDO AS REQUESTS */
    }

    @Override
    public CompletableFuture<Void> cleanUpQueue() {
        /* TODO: vai ler/processar os valores da queue em segundo plano e fazer o request dnv */
        return CompletableFuture.runAsync(() -> {
            var data = dbQueue.poll();
            if (data == null) return;
            try {
                sendPayment(data);
            } catch (IOException | URISyntaxException | InterruptedException ignored) {
            }
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

        if (!UdpServerConfig.udpCheckHealth(Default) && !UdpServerConfig.udpCheckHealth(Fallback)) {
            return 0;
        }
        if (!UdpServerConfig.udpCheckHealth(Default)) {
            BuildRequest.buildRequest(formattedRequest, new URI(Fallback));
            return 1;
        }
        BuildRequest.buildRequest(formattedRequest, new URI(Default));
        return 2;
    }
}
