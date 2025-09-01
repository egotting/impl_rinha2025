package com.github.egotting.impl_rinha2025.domain.service.Interface;

import java.time.Instant;

public interface IPaymentSummaryService {
    String getSummary(Instant from, Instant to);
}
