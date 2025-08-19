package com.github.egotting.impl_rinha2025.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class workerConfig {
    @Bean
    public ExecutorService worker() {
        return Executors.newFixedThreadPool(5);
    }
}
