package com.github.egotting.impl_rinha2025.domain.service.Interface;

import java.util.concurrent.CompletableFuture;

public interface IPaymentWorker {
    CompletableFuture<Void> Worker();
}
