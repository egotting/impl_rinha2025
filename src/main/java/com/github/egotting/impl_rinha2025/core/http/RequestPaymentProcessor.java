package com.github.egotting.impl_rinha2025.core.http;

import com.github.egotting.impl_rinha2025.core.Executer.Interface.ITaskSimpleConfig;
import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestPaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.ENUM.TypePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class RequestPaymentProcessor implements IRequestPaymentProcessor {

    private final IMemoryPaymentProcessorRepository _memory;
    private final ITaskSimpleConfig _exec;
    @Value("${payment.processor.default}")
    private String Default;
    @Value("${payment.processor.fallback}")
    private String Fallback;
    private final WebClient webClient;

    public RequestPaymentProcessor(
            ITaskSimpleConfig _exec,
            WebClient client,
            IMemoryPaymentProcessorRepository memory) {
        this.webClient = client;
        this._exec = _exec;
        this._memory = memory;
    }

    @Override
    public Mono<TypePaymentProcessor> payment(PaymentRequest request) {
        String PAYTOSAND = """
            {
                "correlationId": "%s",
                "amount": %s
            }
        """.formatted(request.correlationId(), request.amount());
            return webClient.post()
            .uri(Default + "/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(PAYTOSAND)
            .retrieve()
            .bodyToMono(String.class)
            .map(response -> TypePaymentProcessor.DEFAULT)
            .doOnNext(result -> {
                            if (result == TypePaymentProcessor.DEFAULT)
                                _memory.saveDefault(request);
                })
            .onErrorResume(err -> {
                    System.err.println(err.getMessage());
                    return webClient.post()
                        .uri(Fallback + "/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(PAYTOSAND)
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(response -> TypePaymentProcessor.FALLBACK)
                        .doOnNext(result -> {
                                if (result == TypePaymentProcessor.FALLBACK)
                                    _memory.saveFallback(request);
                            })
                        .onErrorReturn(TypePaymentProcessor.FAILED);
                });
    }
}
