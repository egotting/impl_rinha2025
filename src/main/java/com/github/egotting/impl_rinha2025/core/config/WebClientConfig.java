package com.github.egotting.impl_rinha2025.core.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
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


    private final int connection_timeout = 5000;
    private final int read_timeout = 10000;
    private final int socket_timeout = 10000;
    private final int max_total_connections = 400;
    private final int max_per_route_connections = 400;
    private final int max_idle_time = 60000;
    private final int max_life_time = 60000;

    private final Logger logger = Logger.getLogger(WebClientConfig.class.getName());


    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(buildClientReactor()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private HttpClient buildClientReactor() {
        ConnectionProvider provider = ConnectionProvider.builder("connection-pool")
                .maxConnections(max_total_connections)
                .pendingAcquireTimeout(Duration.ofMillis(0))
                .pendingAcquireMaxCount(-1)
                .maxIdleTime(Duration.ofMillis(max_idle_time))
                .maxLifeTime(Duration.ofMillis(max_life_time))
                .build();
        return HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connection_timeout)
                .keepAlive(true)
                .compress(false)
                .responseTimeout(Duration.ofMillis(socket_timeout))
                .doOnConnected(
                        conn -> conn.addHandlerLast(new ReadTimeoutHandler(read_timeout / 1_000))
                                .addHandlerLast(new WriteTimeoutHandler(read_timeout / 1000)));
    }
}
