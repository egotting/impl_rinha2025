package com.github.egotting.impl_rinha2025.core.http;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestDefault;
import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestFallback;
import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Service
public class RequestDefault implements IRequestDefault {
    private final Logger logger = Logger.getLogger(RequestDefault.class.getName());
    private final IRequestFallback _fallback;
    private final IMemoryPaymentProcessorRepository _memory;
    //private final RestClient rest_client;
    private final WebClient web_client;

    public RequestDefault(
            IRequestFallback _fallback,
            IMemoryPaymentProcessorRepository memory,
            @Qualifier("defaultWebClient") WebClient webClient) {
        this._fallback = _fallback;
        _memory = memory;
        web_client = webClient;
    }

    @Override
    public StatusPayment payment(PaymentRequest pay) {

        ResponseEntity<Void> response = web_client
                .post()
                .uri("/payments")
                .bodyValue(pay)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful, res -> {
                    logger.info("Default o send " + res.statusCode());
                    return Mono.empty();
                })
                .onStatus(HttpStatusCode::is5xxServerError, res -> {
                    logger.info("Erro 500: " + res.statusCode());
                    return Mono.empty();
                })
                .onStatus(HttpStatusCode::is4xxClientError, res ->
                        res.bodyToMono(String.class).flatMap(body -> {
                            logger.severe("Erro 4xx: " + res.statusCode() + " Body: " + body);
                            return Mono.error(new RuntimeException("Erro 4xx: " + body));
                        })
                )
                .toBodilessEntity()
                .block();

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            System.out.println("STATUS: " + StatusPayment.NONE);
            return StatusPayment.NONE;
        }

        System.out.println("STATUS: " + StatusPayment.DEFAULT);
        return StatusPayment.DEFAULT;
    }

}
