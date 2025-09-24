package com.github.egotting.impl_rinha2025.domain.service;

import org.springframework.stereotype.Component;

@Component
public class HealthCheckEngine {
//    private AtomicBoolean isExec = new AtomicBoolean(false);
//
//    private final IHealthCheck _hc;
//    private ExecutorService _exec = Executors.newVirtualThreadPerTaskExecutor();
//
//    public HealthCheckEngine(IHealthCheck _hc) {
//        this._hc = _hc;
//    }
//
//    public boolean isExec() {
//        return isExec.get();
//    }
//
//    public StatusPayment startHealthCheck() {
//        isExec.set(true);
//        _exec.submit(() -> {
//            while (true) {
//                healthCheck();
//                try {
//                    Thread.sleep(5_000);
//                } catch (RuntimeException | InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        return StatusPayment.NONE;
//    }
//
//    private StatusPayment healthCheck() {
//        CompletableFuture<HealthCheckStatus> res_default = CompletableFuture.supplyAsync(_hc::getHealthCheckDefault,
//                _exec);
//        CompletableFuture<HealthCheckStatus> res_fallback = CompletableFuture.supplyAsync(_hc::getHealthCheckFallback,
//                _exec);
//        try {
//            HealthCheckStatus statusDefaultRes = res_default.get();
//            if (!statusDefaultRes.failing() && statusDefaultRes.minResponseTime() < 100) {
//                return StatusPayment.DEFAULT;
//            }
//            HealthCheckStatus statusFallbackRes = res_fallback.get();
//
//            if (statusDefaultRes.failing() && statusFallbackRes.failing())
//                return StatusPayment.NONE;
//            else if (statusDefaultRes.failing() && !statusFallbackRes.failing()
//                    && statusFallbackRes.minResponseTime() <= 1_000)
//                return StatusPayment.FALLBACK;
//            else if (!statusDefaultRes.failing())
//                return StatusPayment.DEFAULT;
//            return StatusPayment.NONE;
//        } catch (ExecutionException | InterruptedException | RuntimeException e) {
//            System.err.println(e);
//        }
//        return StatusPayment.NONE;
//    }
}
