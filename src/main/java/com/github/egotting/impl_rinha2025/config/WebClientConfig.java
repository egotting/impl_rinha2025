package com.github.egotting.impl_rinha2025.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.logging.Logger;

@Configuration
public class WebClientConfig {

    @Value("${http-client-connection-timeout}")
    private int connectionTimeout;

    @Value("${http-client-read-timeout}")
    private int readTimeout;

    @Value("${http-client-socket-timeout}")
    private int socketTimeout;

    @Value("${http-client-max-total-connections}")
    private int maxTotalConnections;

    @Value("${http-client-max-per-route-connections}")
    private int maxPerRouteConnections;

    @Value("${http-client-max-idle-time}")
    private int maxIdleTime;

    @Value("${http-client-max-life-time}")
    private int maxLifeTime;

    private final Logger logger = Logger.getLogger(WebClientConfig.class.getName());

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(buildHttpClient()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private HttpClient buildHttpClient() {
        logger.info(String.format(
                "Configuring WebClient properties: connectionTimeout: %s | readTimeout: %s | socketTimeout: %s | maxTotalConnections: %s | maxPerRouteConnections: %s.",
                connectionTimeout, readTimeout, socketTimeout, maxTotalConnections, maxPerRouteConnections));
        ConnectionProvider provider = ConnectionProvider.builder("connection-pool")
                .maxConnections(maxTotalConnections)
                .pendingAcquireTimeout(Duration.ofMillis(0))
                .pendingAcquireMaxCount(-1)
                .maxIdleTime(Duration.ofMillis(maxIdleTime))
                .maxLifeTime(Duration.ofMillis(maxLifeTime))
                .build();


        return HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                .keepAlive(true)
                .compress(false)
                .responseTimeout(Duration.ofMillis(socketTimeout))
                .doOnConnected(
                        con -> con.addHandlerLast(new ReadTimeoutHandler(readTimeout / 1_000))
                                .addHandlerLast(new WriteTimeoutHandler(readTimeout / 1_000)));

    }
}
