package com.github.egotting.impl_rinha2025.http;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.http.Interface.IRequestPaymentProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.logging.Logger;

@Component
public class RequestPaymentProcessor implements IRequestPaymentProcessor {
    private final Logger logger = Logger.getLogger(RequestPaymentProcessor.class.getName());
    @Qualifier("restClientDefault")
    private final RestClient restClientDefault;
    @Qualifier("restClientFallback")
    private final RestClient restClientFallback;

    public RequestPaymentProcessor(RestClient restClientDefault, RestClient restClientFallback) {
        this.restClientDefault = restClientDefault;
        this.restClientFallback = restClientFallback;
    }

    @Override
    public void paymentDefault(PaymentRequest request) {
        String paytosand = new StringBuilder("{")
                .append("\"correlationId\":\"").append(request.correlationId()).append("\", ")
                .append("\"amount\":\"").append(request.amount()).append(",")
                .append("}")
                .toString();
        ResponseEntity<Void> response = restClientDefault
                .post()
                .uri("/payments")
                .header("Content-Type", "application/json")
                .body(paytosand)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful, (req, res) -> {
                    logger.info("Success DEFAULT to send : " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    logger.info("Error 400 to send: " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    logger.info("Error 500 to send: " + res.getStatusCode());
                })
                .toBodilessEntity();
        if (!response.getStatusCode().is2xxSuccessful()) {
            System.out.println(response);
            throw new RuntimeException("err");
        }
    }

    @Override
    public void paymentFallback(PaymentRequest request) {
        String paytosand = new StringBuilder("{")
                .append("\"correlationId\":\"").append(request.correlationId()).append("\", ")
                .append("\"amount\":\"").append(request.amount()).append(",")
                .append("}")
                .toString();
        ResponseEntity<Void> response = restClientFallback
                .post()
                .uri("/payments")
                .header("Content-Type", "application/json")
                .body(paytosand)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful, (req, res) -> {
                    logger.info("Success DEFAULT to send : " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    logger.info("Error 400 to send: " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    logger.info("Error 500 to send: " + res.getStatusCode());
                })
                .toBodilessEntity();
        if (!response.getStatusCode().is2xxSuccessful()) {
            System.out.println(response);
            throw new RuntimeException("err");
        }
    }
}
