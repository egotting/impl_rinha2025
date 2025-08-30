package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.core.Executer.Interface.ITaskSimpleConfig;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestPaymentProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentProcessorService implements IPaymentProcessorService {
  // private final String Default = "http://localhost:8001";
  // private final String Fallback = "http://localhost:8002";
  // @Value("${payment.processor.default}")
  // private static String Default;
  // @Value("${payment.processor.fallback}")
  // private static String Fallback;
  private final ITaskSimpleConfig _exec;
  private final IRequestPaymentProcessor _requestPayment;

  public PaymentProcessorService(
      ITaskSimpleConfig exec, IRequestPaymentProcessor _requestPayment) {
    _exec = exec;
    this._requestPayment = _requestPayment;
  }

  @Override
  public CompletableFuture<Integer> pay(PaymentRequest request) {
    return _exec.supply(() -> {
      try {
        return _requestPayment.paymentDefault(request)
            .flatMap(success -> {
              if (success) {
                return Mono.just(1);
              } else {
                try {
                  return _requestPayment.paymentFallback(request)
                      .map(fallbackSuccess -> fallbackSuccess ? 2 : 0);
                } catch (IOException | InterruptedException e) {
                  return Mono.error(new RuntimeException(e));
                }
              }
            })
            .onErrorResume(e -> Mono.error(new RuntimeException(e)));
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }).thenCompose(Mono::toFuture);
  }
}
