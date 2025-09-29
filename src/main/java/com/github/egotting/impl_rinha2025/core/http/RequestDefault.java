package com.github.egotting.impl_rinha2025.core.http;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestDefault;
import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestFallback;
import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.logging.Logger;

@Component
public class RequestDefault implements IRequestDefault {
    private final Logger logger = Logger.getLogger(RequestDefault.class.getName());
    private final IMemoryPaymentProcessorRepository _memory;
    //private final RestClient rest_client;
    private final RestClient rest_client;
    private final IRequestFallback fallback_request;

    public RequestDefault(
            IMemoryPaymentProcessorRepository memory,
            @Qualifier("defaultRestClient") RestClient rest_client,
            IRequestFallback fallbackRequest) {
        _memory = memory;
        this.rest_client = rest_client;
        fallback_request = fallbackRequest;
    }

    @Override
    public StatusPayment payment(PaymentRequest pay) {
        try {
            ResponseEntity<Void> response = rest_client
                    .post()
                    .uri("/payments")
                    .body(pay)
                    .retrieve()
                    .toBodilessEntity();
            if (response.getStatusCode().is2xxSuccessful()) {
                return StatusPayment.DEFAULT;
            } else {
                logger.warning("INDO PARA FALLBACK: " + response.getStatusCode());
                return StatusPayment.FALLBACK;
            }
        } catch (Exception e) {
            logger.severe("Default err: " + e.getMessage());
            return StatusPayment.FALLBACK;
        }
    }

}
