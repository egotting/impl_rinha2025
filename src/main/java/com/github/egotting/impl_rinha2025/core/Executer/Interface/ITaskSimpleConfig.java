package com.github.egotting.impl_rinha2025.core.Executer.Interface;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface ITaskSimpleConfig {
    CompletableFuture<Void> onlyRun(Runnable task);

    <T> CompletableFuture<T> supply(Supplier<T> task);
}
