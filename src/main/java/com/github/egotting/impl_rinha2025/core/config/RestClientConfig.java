package com.github.egotting.impl_rinha2025.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${payment.processor.default}")
    private String _default;
    @Value("${payment.processor.fallback}")
    private String _fallback;

    @Bean
    public RestClient defaultRestClient() {
        return RestClient.builder()
                .baseUrl(_default)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public RestClient fallbackRestClient() {
        return RestClient.builder()
                .baseUrl(_fallback)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
