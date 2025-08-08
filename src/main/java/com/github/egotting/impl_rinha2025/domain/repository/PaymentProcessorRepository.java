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
    public void save(PaymentProcessorRequest paymentDto) {
        if(!paymentDto.status()){
            dbMemoryDefault.add(paymentDto);
        }
        dbMemoryFallback.add(paymentDto);
    }

    @Override
    public void deleteAll() {
        dbMemoryDefault.clear();
        dbMemoryFallback.clear();
    }
}
