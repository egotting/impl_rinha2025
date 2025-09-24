package com.github.egotting.impl_rinha2025.domain.repository;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestDefault;
import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestFallback;
import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IPaymentProcessorRepository;

import org.springframework.stereotype.Component;

@Component
public class PaymentProcessorRepository implements IPaymentProcessorRepository {
    private final IMemoryPaymentProcessorRepository _memory;
    private final IRequestDefault _default;
    private final IRequestFallback _fallback;

    public PaymentProcessorRepository(
            IMemoryPaymentProcessorRepository _memory,
            IRequestDefault _default,
            IRequestFallback _fallback) {
        this._memory = _memory;
        this._default = _default;
        this._fallback = _fallback;
    }

    @Override
    public void process(PaymentRequest data, StatusPayment type) {
        if (type == StatusPayment.DEFAULT) {
            _default.payment(data);
        } else {
            _fallback.payment(data);
        }
    }
}
