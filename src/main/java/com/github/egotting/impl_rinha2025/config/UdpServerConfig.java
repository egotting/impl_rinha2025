package com.github.egotting.impl_rinha2025.config;

import com.github.egotting.impl_rinha2025.config.Interface.IUdpServerConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class UdpServerConfig implements IUdpServerConfig {
    private final AtomicBoolean lastCheck = new AtomicBoolean(false);
    private DatagramSocket socket;
    private final int PORT = 8080;

    @Scheduled(initialDelay = 100, fixedDelay = 300)
    public AtomicBoolean checkApiJob(String url) {
        boolean result = checkApi(url);
        lastCheck.set(result);
        return lastCheck;
    }

    @PostConstruct
    private void udpCheckHealth() throws IOException {
        DatagramSocket socket = new DatagramSocket(PORT);
        new Thread(() -> {
            byte[] buffer = new byte[4 * 1024 * 1024];
            while (true) {
                try {
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                    socket.receive(request);

                    byte[] response = String.valueOf(lastCheck.get()).getBytes();
                    DatagramPacket responseDefault = new DatagramPacket(
                            response,
                            response.length,
                            request.getAddress(),
                            request.getPort()
                    );
                    socket.send(responseDefault);
                } catch (Exception ignore) {
                }
            }
        }).start();
    }


    private boolean checkApi(String url) {
        try {
            HttpURLConnection cnc = (HttpURLConnection) new URL(url).openConnection();
            cnc.setRequestMethod("HEAD");
            cnc.setConnectTimeout(1000);
            cnc.setReadTimeout(2000);
            return cnc.getResponseCode() < 500;
        } catch (Exception e) {
            return false;
        }
    }

}



