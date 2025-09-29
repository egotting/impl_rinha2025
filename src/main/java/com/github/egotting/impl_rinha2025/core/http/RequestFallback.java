package com.github.egotting.impl_rinha2025.core.http;

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
public class RequestFallback implements IRequestFallback {
    private final Logger logger = Logger.getLogger(RequestFallback.class.getName());
    private final RestClient rest_client;
    private final IMemoryPaymentProcessorRepository _memory;

    public RequestFallback(
            @Qualifier("fallbackRestClient") RestClient rest_client,
            IMemoryPaymentProcessorRepository _memory
    ) {
        this.rest_client = rest_client;
        this._memory = _memory;
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
                return StatusPayment.FALLBACK;
            } else {
                logger.warning("FALLBACK FAIL:  " + response.getStatusCode());
                return StatusPayment.NONE;
            }
        } catch (Exception e) {
            logger.severe("Erro fallback: " + e.getMessage());
            return StatusPayment.NONE;
        }
    }

}
