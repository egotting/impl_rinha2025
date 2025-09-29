package com.github.egotting.impl_rinha2025.domain.service;

import com.github.egotting.impl_rinha2025.domain.service.Interface.ISemaphoreService;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Service
public class SemaphoreService implements ISemaphoreService {
    private final Logger logger = Logger.getLogger(SemaphoreService.class.getName());

    private AtomicBoolean run = new AtomicBoolean(false);

    @Override
    public int canISleep() {
        if (run.get()) {
            return 1;
        }
        return 0;
    }

    @Override
    public void set(boolean value) {
        run.set(value);
    }
}
