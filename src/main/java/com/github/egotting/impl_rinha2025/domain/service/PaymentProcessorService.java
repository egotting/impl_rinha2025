package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

/* TODO: Fazer o HealthCheck usando protocolo UDP */

@Service
public class PaymentProcessorService implements IPaymentProcessorService {
    private RestTemplate restTemplate = new RestTemplate();
    private ConcurrentLinkedQueue<PaymentProcessorRequest> dbQueue = new ConcurrentLinkedQueue<>();
    @Value("{payment.processor.default}")
    private String defaultUrl;
    @Value("{payment.processor.fallback}")
    private String fallbackUrl;
    @Override
    public boolean sendPayment(PaymentRequest payment) {
        var paymentObject = new PaymentProcessorRequest(
                payment.correlationId(),
                payment.amount(),
                true,
                Instant.now()
        );
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(paymentObject, headers);


        var response = restTemplate.postForEntity(
                defaultUrl + "/payments",
                entity,
                PaymentProcessorRequest.class
        );

        if(response.getStatusCode().value() != HttpStatus.OK.value()){
            restTemplate.postForEntity(
                    fallbackUrl + "/payments",
                    entity,
                    PaymentProcessorRequest.class
            );
        }

        return response.getStatusCode().value() == HttpStatus.OK.value();
    }

    @Override
    public void processPayment(PaymentRequest clientRequest) {
        // caso o healthcheck retorne false para a chamada do default, faça uma chamada para o fallback,
        // caso ele retorne erro tambem armazene os request na queue e teste dos de 5ms faça dnv o ping no default
    }

    @Override
    public void purgeData() {
        dbQueue.clear();
    }
}
