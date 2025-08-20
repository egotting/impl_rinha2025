package com.github.egotting.impl_rinha2025.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${payment.processor.default}")
    private String urlDefault;
    @Value("${payment.processor.fallback}")
    private String urlFallback;

    @Bean
    public RestClient restClientDefault() {
        return RestClient.create(urlDefault);
    }

    @Bean
    public RestClient restClientFallback() {
        return RestClient.create(urlFallback);
    }
}
