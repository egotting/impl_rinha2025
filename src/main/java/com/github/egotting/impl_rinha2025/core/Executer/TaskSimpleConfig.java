package com.github.egotting.impl_rinha2025.core.Executer;

import com.github.egotting.impl_rinha2025.core.Executer.Interface.ITaskSimpleConfig;
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
    public CompletableFuture<Void> onlyRun(Runnable task) {
        return CompletableFuture.runAsync(task);
    }

    @Override
    public <T> CompletableFuture<T> supply(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, _exec);
    }
}
