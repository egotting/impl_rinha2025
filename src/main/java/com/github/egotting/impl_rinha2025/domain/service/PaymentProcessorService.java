package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.config.BuildRequest;
import com.github.egotting.impl_rinha2025.config.UdpServerConfig;
import com.github.egotting.impl_rinha2025.domain.ENUM.TypePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.PaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

/* TODO: Fazer o HealthCheck usando protocolo UDP */

@Service
public class PaymentProcessorService implements IPaymentProcessorService {
    private final PaymentProcessorRepository _repository;
    private ConcurrentLinkedQueue<PaymentRequest> dbQueue = new ConcurrentLinkedQueue<>();
    private final String Default = "http://payment-processor-default:8080";
    private final String Fallback = "http://payment-processor-fallback:8080";
    //    private final String Default = "http://localhost:8001";
    //    private final String Fallback = "http://localhost:8002";
//    @Value("${payment.processor.default}")
//    private static String Default;
//    @Value("${payment.processor.fallback}")
//    private static String Fallback;

    public PaymentProcessorService(PaymentProcessorRepository _repository) {
        this._repository = _repository;
    }

    @Override
    public boolean healthCheck(String url) {
        return false;
    }

    public void sendToQueue(PaymentRequest request) {
        dbQueue.add(request);
    }


    @Override
    public void processPayment(PaymentRequest request) {
        // caso o healthcheck retorne false para a chamada do default, faça uma chamada para o fallback,
        // caso ele retorne erro tambem armazene os request na queue e teste dos de 5ms faça dnv o ping no default

        if (sendPayment(request) == TypePaymentProcessor.FAILED) {
            sendToQueue(request);
        }
        var _default = new PaymentProcessorRequest(request.correlationId(), request.amount(), true, Instant.now());
        var _fallback = new PaymentProcessorRequest(request.correlationId(), request.amount(), false, Instant.now());
        if (sendPayment(request) == TypePaymentProcessor.DEFAULT_OFF) _repository.save(_fallback);
        if (sendPayment(request) == TypePaymentProcessor.DEFAULT_ON) _repository.save(_default);
        /* TODO: CHAMAR O HEALTHCHECK PARA VERIFICAR SE AINDA ESTA CAIDO E CASO N ESTEJA
         *   CHAMA O SENDPAYMENT MANDANDO AS REQUESTS */
    }

    @Override
    public void purgeQueue() {
        dbQueue.clear();
    }

    @Override
    public TypePaymentProcessor sendPayment(PaymentRequest request) {
        String formattedRequest = """
                {
                  "correlationId": "%s",
                  "amount": %.2f
                }
                """.formatted(request.correlationId(), request.amount());

        try {
            if (!UdpServerConfig.udpCheckHealth(Default)) {
                BuildRequest.buildRequest(formattedRequest, new URI(Fallback));
                return TypePaymentProcessor.DEFAULT_OFF;
            }
            BuildRequest.buildRequest(formattedRequest, new URI(Default));
            return TypePaymentProcessor.DEFAULT_ON;
        } catch (InterruptedException | IOException | URISyntaxException ignored) {
        }
        return TypePaymentProcessor.FAILED;
    }
}
