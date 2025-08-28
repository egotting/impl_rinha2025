package com.github.egotting.impl_rinha2025.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HttpClientConfig {

    //    @Bean
//    public HttpClient httpClient() {
//        return HttpClient.newBuilder()
//                .followRedirects(HttpClient.Redirect.NEVER)
//                .version(HttpClient.Version.HTTP_1_1)
//                .build();
//    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
