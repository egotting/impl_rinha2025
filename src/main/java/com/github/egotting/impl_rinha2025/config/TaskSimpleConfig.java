package com.github.egotting.impl_rinha2025.config;

import com.github.egotting.impl_rinha2025.config.Interface.ITaskSimpleConfig;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Component
public class TaskSimpleConfig implements ITaskSimpleConfig {
    private final ExecutorService _exec;

    public TaskSimpleConfig() {
        _exec = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Override
    public <T> CompletableFuture<T> submit(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, _exec);
    }
}
