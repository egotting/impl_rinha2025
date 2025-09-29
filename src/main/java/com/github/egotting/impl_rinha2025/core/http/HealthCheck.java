package com.github.egotting.impl_rinha2025.core.http;

import com.github.egotting.impl_rinha2025.core.http.Interface.IHealthCheck;

public class HealthCheck implements IHealthCheck {
//    private final Logger logger = Logger.getLogger(HealthCheck.class.getName());
//    @Value("${payment.processor.default}")
//    private String _default;
//    @Value("${payment.processor.fallback}")
//    private String _fallback;
//
//    private final String HEALTH_CHECK_URL = "/payments/service-health";
//
//    private final WebClient web_client;
//
//    public HealthCheck(WebClient web_client) {
//        this.web_client = web_client;
//    }
//
//    @Override
//    public HealthCheckStatus getHealthCheckDefault() {
//        return getHealthCheck(_default);
//    }
//
//    @Override
//    public HealthCheckStatus getHealthCheckFallback() {
//        return getHealthCheck(_fallback);
//    }
//
//    private HealthCheckStatus getHealthCheck(String url) {
//        ResponseEntity<HealthCheckStatus> request = web_client
//                .get()
//                .uri(url + HEALTH_CHECK_URL)
//                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
//                    logger.severe(String.format("error to get status of healthcheck", clientResponse.statusCode()));
//                    return Mono.empty();
//                })
//                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
//                    logger.severe(String.format("error to get status of healthcheck", clientResponse.statusCode()));
//                    return Mono.empty();
//                })
//                .toEntity(HealthCheckStatus.class)
//                .block();
//        if (request.getStatusCode().is2xxSuccessful()) {
//            return request.getBody();
//        }
//
//        throw new RuntimeException("erro on to get a status of health check");
//    }

}
