package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.config.Interface.IUdpServerConfig;
import com.github.egotting.impl_rinha2025.domain.model.Memory.Interface.IQueuePaymentProcessor;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IPaymentProcessorService;
import com.github.egotting.impl_rinha2025.http.Interface.IRequestPaymentProcessor;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessorService implements IPaymentProcessorService {
//    private final String Default = "http://localhost:8001";
//    private final String Fallback = "http://localhost:8002";
    //    @Value("${payment.processor.default}")
    //    private static String Default;
    //    @Value("${payment.processor.fallback}")
    //    private static String Fallback;

    private final IUdpServerConfig _udpServerConfig;
    private final IRequestPaymentProcessor _requestPayment;
    private final IQueuePaymentProcessor _queue;

    public PaymentProcessorService(
            IUdpServerConfig _udpServerConfig,
            IRequestPaymentProcessor _requestPayment,
            IQueuePaymentProcessor _queue
    ) {
        this._udpServerConfig = _udpServerConfig;
        this._requestPayment = _requestPayment;
        this._queue = _queue;
    }

    @Override
    public int pay(PaymentRequest request) {
        try {
            var defaultRequest = _requestPayment.paymentDefault(request);
            var fallbackRequest = _requestPayment.paymentFallback(request);
            if (defaultRequest == 200) {
                System.out.println("fez um default");
                return 1;
            } else if (fallbackRequest == 200) {
                System.out.println("fez um fallback");
                return 2;
            }
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
