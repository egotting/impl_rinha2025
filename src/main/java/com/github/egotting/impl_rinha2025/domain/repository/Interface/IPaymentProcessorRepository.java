package com.github.egotting.impl_rinha2025.domain.repository.Interface;

import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

public interface IPaymentProcessorRepository {

    void process(PaymentRequest data, StatusPayment type);
}
