package com.github.egotting.impl_rinha2025.core.http;

import java.time.Duration;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestPaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Component
public class RequestPaymentProcessor implements IRequestPaymentProcessor {

    private final IMemoryPaymentProcessorRepository _memory;
    @Value("${payment.processor.default}")
    private String Default;
    @Value("${payment.processor.fallback}")
    private String Fallback;
    private final WebClient webClient;

    public RequestPaymentProcessor(
            WebClient client,
            IMemoryPaymentProcessorRepository memory) {
        this.webClient = client;
        this._memory = memory;
    }

    @Override
    public Mono<Void> payment(PaymentRequest request) {
        return webClient.post()
                .uri(Default + "/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofMillis(2000))
                .then(_memory.saveDefault(request))
                .onErrorResume(err -> {
                    if (err instanceof WebClientResponseException ex
                            && ex.getStatusCode().is5xxServerError()) {
                        return webClient.post()
                                .uri(Fallback + "/payments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .bodyValue(request)
                                .retrieve()
                                .bodyToMono(Void.class)
                                .timeout(Duration.ofMillis(2000))
                                .then(_memory.saveFallback(request))
                                .onErrorResume(er -> {
                                    System.out.println("Erro no Fallback também");
                                    return Mono.empty();
                                });
                    }
                    return Mono.empty();
                });
    }
}
