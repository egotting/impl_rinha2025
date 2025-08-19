package com.github.egotting.impl_rinha2025.domain.repository;

import com.github.egotting.impl_rinha2025.domain.model.PaymentProcessorRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IPaymentProcessorRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentLinkedQueue;

@Repository
public class PaymentProcessorRepository implements IPaymentProcessorRepository {

    ConcurrentLinkedQueue<PaymentProcessorRequest> dbMemoryDefault = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<PaymentProcessorRequest> dbMemoryFallback = new ConcurrentLinkedQueue<>();


    @Override
    public void saveDefault(PaymentProcessorRequest paymentDto) {
        dbMemoryDefault.add(paymentDto);
    }

    @Override
    public void saveFallback(PaymentProcessorRequest paymentDto) {
        dbMemoryFallback.add(paymentDto);
    }

    @Override
    public void deleteAll() {
        dbMemoryDefault.clear();
        dbMemoryFallback.clear();
    }
}
