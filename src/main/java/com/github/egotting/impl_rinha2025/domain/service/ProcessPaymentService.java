package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestDefault;
import com.github.egotting.impl_rinha2025.core.http.Interface.IRequestFallback;
import com.github.egotting.impl_rinha2025.domain.ENUM.StatusPayment;
import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.domain.repository.Interface.IMemoryPaymentProcessorRepository;
import com.github.egotting.impl_rinha2025.domain.service.Interface.IProcessPaymentService;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ProcessPaymentService implements IProcessPaymentService {
    private final Logger logger = Logger.getLogger(ProcessPaymentService.class.getName());

    private final IMemoryPaymentProcessorRepository _memory;
    private final IRequestDefault default_request;
    private final IRequestFallback fallback_request;

    public ProcessPaymentService(
            IMemoryPaymentProcessorRepository memory,
            IRequestDefault defaultRequest, IRequestFallback fallbackRequest) {
        _memory = memory;
        default_request = defaultRequest;
        fallback_request = fallbackRequest;
    }


    @Override
    public void process(PaymentRequest value) {
        StatusPayment status = StatusPayment.NONE;
        try {
            status = default_request.payment(value);
            if (status == StatusPayment.FALLBACK) {
                pauseFor(50);
                status = fallback_request.payment(value);
            }
        } catch (Exception e) {
            status = StatusPayment.NONE;
        } finally {
            if (status == StatusPayment.NONE) {
                throw new RuntimeException("Pagamento n pode ser processado");
            }
            _memory.save(value, status);
        }
    }


    private void pauseFor(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
