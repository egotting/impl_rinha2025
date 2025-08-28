package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import com.github.egotting.impl_rinha2025.http.Interface.IRequestPaymentProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class PaymentProcessorService implements IPaymentProcessorService {
    //    private final String Default = "http://localhost:8001";
//    private final String Fallback = "http://localhost:8002";
    //    @Value("${payment.processor.default}")
    //    private static String Default;
    //    @Value("${payment.processor.fallback}")
    //    private static String Fallback;
    private final IRequestPaymentProcessor _requestPayment;

    public PaymentProcessorService(
            IRequestPaymentProcessor _requestPayment
    ) {
        this._requestPayment = _requestPayment;
    }


    @Override
    public Mono<Integer> pay(PaymentRequest request) throws IOException, InterruptedException {
        var defaultRequest = _requestPayment.paymentDefault(request);
        var fallbackRequest = _requestPayment.paymentFallback(request);

        return defaultRequest.flatMap(sucess -> {
            if (sucess) {
                return Mono.just(1);
            } else {
                return fallbackRequest.map(fallbackSuccess -> fallbackSuccess ? 2 : 0);
            }
        }).onErrorResume(e -> Mono.error(new RuntimeException(e)));
    }
}
