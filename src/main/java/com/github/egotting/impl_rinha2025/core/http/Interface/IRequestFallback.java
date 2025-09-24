package com.github.egotting.impl_rinha2025.core.http.Interface;

import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

public interface IRequestFallback {
    StatusPayment payment(PaymentRequest request);
}
