package com.github.egotting.impl_rinha2025.domain.repository;

import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IPaymentProcessorRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentLinkedQueue;

@Repository
public class PaymentProcessorRepository implements IPaymentProcessorRepository {

    ConcurrentLinkedQueue<PaymentProcessorRequest> dbMemory = new ConcurrentLinkedQueue<>();

    @Override
    public void save(PaymentProcessorRequest paymentDto) {
        dbMemory.add(paymentDto);
    }
}
