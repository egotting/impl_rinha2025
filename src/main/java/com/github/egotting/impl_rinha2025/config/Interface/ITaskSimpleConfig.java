package com.github.egotting.impl_rinha2025.config.Interface;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface ITaskSimpleConfig {
    <T> CompletableFuture<T> submit(Supplier<T> task);
}
