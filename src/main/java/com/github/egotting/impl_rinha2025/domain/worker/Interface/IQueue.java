package com.github.egotting.impl_rinha2025.domain.worker.Interface;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;

public interface IQueue {
    void add(PaymentRequest value);
}
