package com.github.egotting.impl_rinha2025.domain.repository.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;

public interface IPaymentProcessorRepository {

    void save(PaymentProcessorRequest paymentDto);
    void deleteAll();
}
