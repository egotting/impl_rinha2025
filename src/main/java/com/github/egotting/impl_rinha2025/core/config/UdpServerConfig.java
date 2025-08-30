package com.github.egotting.impl_rinha2025.core.config;

import com.github.egotting.impl_rinha2025.core.config.Interface.IUdpServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class UdpServerConfig implements IUdpServerConfig {

    @Value("${localhost.payment.processor.default}")
    private String urlDefault;
    @Value("${localhost.payment.processor.fallback}")
    private String urlFallback;
    private final HttpClient _web = HttpClient.newBuilder()
            .connectTimeout(Duration.ofMillis(500))
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    private final AtomicBoolean defaultUp = new AtomicBoolean(false);
    private final AtomicBoolean fallbackUp = new AtomicBoolean(false);
//    private final int PORT = 8080;
//    private final ExecutorService _exec = Executors.newSingleThreadExecutor(r -> {
//        Thread t = new Thread(r, "UDP-Check-health");
//        t.setDaemon(true);
//        return t;
//    });

    private void setLive() {
        defaultUp.set(checkApi(urlDefault).join());
        fallbackUp.set(checkApi(urlFallback).join());
    }

    private CompletableFuture<Boolean> checkApi(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofMillis(2000))
                .build();

        return _web.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenApply(resp -> resp.statusCode() < 500)
                .exceptionally(ex -> false);
    }

    @Override
    public boolean isDefaultUp() {
        setLive();
        return defaultUp.get();
    }

    @Override
    public boolean isFallbackUp() {
        setLive();
        return fallbackUp.get();
    }


//    @PostConstruct
//    private void udpCheckHealth() {
//        _exec.submit(() -> {
//            try (DatagramSocket socket = new DatagramSocket(PORT)) {
//                byte[] buffer = new byte[1024];
//                while (!Thread.currentThread().isInterrupted()) {
//                    try {
//                        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
//                        socket.receive(request);
//
//                        int status;
//                        if (defaultStatus) status = 1;
//                        else if (fallbackStatus) status = 2;
//                        else status = 0;
//                        byte[] response = String.valueOf(status).getBytes();
//                        DatagramPacket responseDefault = new DatagramPacket(
//                                response,
//                                response.length,
//                                request.getAddress(),
//                                request.getPort()
//                        );
//                        socket.send(responseDefault);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (SocketException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
}



