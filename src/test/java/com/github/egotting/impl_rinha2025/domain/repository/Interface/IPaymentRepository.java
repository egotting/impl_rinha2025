package com.github.egotting.impl_rinha2025.domain.repository.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;
import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorResponse;
import com.github.egotting.impl_rinha2025.domain.model.PaymentSummary;

import java.time.Instant;
import java.util.List;

public interface IPaymentRepository {

    public void save(PaymentProcessorRequest request);

    public PaymentProcessorResponse paymentSummary(Instant From, Instant to);
    public PaymentSummary summary(List<PaymentProcessorResponse> payments);
}
