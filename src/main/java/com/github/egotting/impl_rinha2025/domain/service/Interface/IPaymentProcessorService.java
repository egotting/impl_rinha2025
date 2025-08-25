package com.github.egotting.impl_rinha2025.domain.service.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

public interface IPaymentProcessorService {
    int pay(PaymentRequest request);
}
