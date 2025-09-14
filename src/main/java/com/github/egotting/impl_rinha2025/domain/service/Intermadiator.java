package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestDefault;
import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestFallback;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IIntermediator;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class Intermadiator implements IIntermediator {

    private final IRequestDefault _default;
    private final IRequestFallback _fallback;

    public Intermadiator(
            IRequestDefault _default,
            IRequestFallback _fallback) {
        this._default = _default;
        this._fallback = _fallback;
    }

    /*
     * TODO
     * {
     *    Verificar qual API tem a melhor resposta ex: caso default esteja com 15ms de
     *    tempo de resposta mas o fallback esteja com 9ms mandar para o fallback caso
     *    fallback e default estejam iguais sempre optar pelo default so usar fallback
     *    caso default esteja off ou esteja com um tempo de resposta muito grande um do
     *    outro;
     * }
     */
    @Override
    public Mono<Void> intermadiate(PaymentRequest request) {
        return Mono.fromRunnable(() -> {
            Mono.empty();
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

}
