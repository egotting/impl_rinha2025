
package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestDefault;
import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestFallback;
import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IIntermediator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Intermadiator implements IIntermediator {

    @Value("${payment.processor.default}")
    private String default_uri;
    @Value("${payment.processor.fallback}")
    private String fallback_uri;

    private AtomicInteger counter = new AtomicInteger(0);
    private final IRequestDefault _default;
    private final IRequestFallback _fallback;
    private final HttpClient _req = HttpClient.newHttpClient();

    public Intermadiator(
            IRequestDefault _default,
            IRequestFallback _fallback
    ) {
        this._default = _default;
        this._fallback = _fallback;
    }


    @Override
    public Mono<StatusPayment> intermadiate() {
        return Mono.defer(() -> {
            var response = better();
            if (response == StatusPayment.DEFAULT) {
                return Mono.just(StatusPayment.DEFAULT);
            } else if (response == StatusPayment.FALLBACK) {
                return Mono.just(StatusPayment.FALLBACK);
            }
            return Mono.just(StatusPayment.NONE);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private StatusPayment better() {
        long defaultTime = ping_default();
        long fallbackTime = ping_fallback();

        if (fallbackTime > defaultTime)
            return StatusPayment.DEFAULT;
        return StatusPayment.FALLBACK;
    }

    private long ping_default() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(default_uri + "/payments"))
                .GET()
                .build();
        try {
            Instant start = Instant.now();
            HttpResponse<Void> response = _req.send(request,
                    HttpResponse.BodyHandlers.discarding());
            Instant end = Instant.now();
            if (response.statusCode() >= 200 && response.statusCode() <= 299) {
                return Duration.between(start, end).toMillis();
            }
        } catch (Exception ignored) {
        }
        return Long.MAX_VALUE;
    }

    private long ping_fallback() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fallback_uri + "/payments"))
                .GET()
                .build();
        try {
            Instant start = Instant.now();
            HttpResponse<Void> response = _req.send(request, HttpResponse.BodyHandlers.discarding());
            Instant end = Instant.now();
            if (response.statusCode() >= 200 && response.statusCode() <= 299) {
                return Duration.between(start, end).toMillis();
            }
        } catch (Exception ignored) {
        }
        return Long.MAX_VALUE;
    }
}
