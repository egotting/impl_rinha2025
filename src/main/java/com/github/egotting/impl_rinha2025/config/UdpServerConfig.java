package com.github.egotting.impl_rinha2025.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class UdpServerConfig {
    @Value("${payment.processor.default}")
    private static String Default;
    @Value("${payment.processor.fallback}")
    private static String Fallback;

    @Scheduled(initialDelay = 100, fixedDelay = 300)
    public static boolean udpCheckHealth(String url) throws IOException {
        final int PORT = 8080;
        DatagramSocket socket = new DatagramSocket(PORT);
        byte[] buffer = new byte[4 * 1024 * 1024];
        try {
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            socket.receive(request);

            boolean apiCheck = checkApi(url);
//            boolean checkFallbackIsOn = checkApi(Fallback);
//            if (!checkDefaultIsOn) {
//                byte[] responseData = String.valueOf(checkFallbackIsOn).getBytes();
//                DatagramPacket responseFallback = new DatagramPacket(
//                        responseData,
//                        responseData.length,
//                        request.getAddress(),
//                        request.getPort()
//                );
//                socket.send(responseFallback);
//                return false;
//            }
            byte[] responseData = String.valueOf(apiCheck).getBytes();
            DatagramPacket responseDefault = new DatagramPacket(
                    responseData,
                    responseData.length,
                    request.getAddress(),
                    request.getPort()
            );
            socket.send(responseDefault);
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }

    private static boolean checkApi(String url) {
        try {
            HttpURLConnection cnc = (HttpURLConnection) new URL(url).openConnection();
            cnc.setRequestMethod("HEAD");
            cnc.setConnectTimeout(3000);
            cnc.setReadTimeout(2000);
            return cnc.getResponseCode() != 500;
        } catch (Exception e) {
            return false;
        }
    }

}



