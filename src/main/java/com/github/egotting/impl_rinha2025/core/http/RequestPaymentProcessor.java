package com.github.egotting.impl_rinha2025.core.http;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestPaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
public class RequestPaymentProcessor implements IRequestPaymentProcessor {
    @Value("${payment.processor.default}")
    private String Default;
    @Value("${payment.processor.fallback}")
    private String Fallback;
    private final WebClient webClient;

    public RequestPaymentProcessor(WebClient client) {
        this.webClient = client;
    }

    @Override
    public Mono<Boolean> paymentDefault(PaymentRequest request) throws IOException, InterruptedException {
        String PAYTOSAND = """
                {
                    "correlationId": "%s",
                    "amount": %s
                }
                """.formatted(request.correlationId(), request.amount());

        return webClient.post()
                .uri(Default + "/payments")
                .bodyValue(PAYTOSAND)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.just(true);
                    } else {
                        return Mono.just(false);
                    }
                });
    }

    @Override
    public Mono<Boolean> paymentFallback(PaymentRequest request) throws IOException, InterruptedException {
        String PAYTOSAND = """
                {
                    "correlationId": "%s",
                    "amount": %s
                }
                """.formatted(request.correlationId(), request.amount());

        return webClient.post()
                .uri(Fallback + "/payments")
                .bodyValue(PAYTOSAND)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.just(true);
                    } else {
                        return Mono.just(false);
                    }
                });
    }

}
