package com.github.egotting.impl_rinha2025.http;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.http.Interface.IRequestPaymentProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.net.http.HttpRequest.BodyPublishers.ofString;

@Component
public class RequestPaymentProcessor implements IRequestPaymentProcessor {
//    private final Logger logger = Logger.getLogger(RequestPaymentProcessor.class.getName());

    @Value("${payment.processor.default}")
    private String Default;
    @Value("${payment.processor.fallback}")
    private String Fallback;
    private final HttpClient httpClient;

    public RequestPaymentProcessor(@Qualifier("httpClient") HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public int paymentDefault(PaymentRequest request) {
        String PAYTOSAND = new StringBuilder("{")
                .append("\"correlationId\":\"").append(request.correlationId()).append("\", ")
                .append("\"amount\":").append(request.amount()).append("\"")
                .append("}")
                .toString();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(1000))
                .uri(URI.create("http://payment-processor-default:8080/payments"))
                .POST(ofString(PAYTOSAND))
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .build();
        try {
            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding()).statusCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
//    public int paymentDefault(PaymentRequest request) {
//        String PAYTOSAND = new StringBuilder("{")
//                .append("\"correlationId\":\"").append(request.correlationId()).append("\", ")
//                .append("\"amount\":").append(request.amount()).append("\"")
//                .append("}")
//                .toString();
//
//        ResponseEntity<Void> response = Default
//                .post()
//                .uri(Default + "/payments")
//                .body(PAYTOSAND)
//                .retrieve()
//                .onStatus(HttpStatusCode::is2xxSuccessful, (req, res) -> {
//                    logger.info("Success DEFAULT to send : " + res.getStatusCode());
//                })
//                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
//                    logger.info("Error 400 to send: " + res.getStatusCode());
//                })
//                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
//                    logger.info("Error 500 to send: " + res.getStatusCode());
//                })
//                .toBodilessEntity();
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            System.out.println(response);
//            throw new RuntimeException("err");
//        }
//        return response.getStatusCode().value();
//    }

    @Override
    public int paymentFallback(PaymentRequest request) throws IOException, InterruptedException {
        String PAYTOSAND = new StringBuilder("{")
                .append("\"correlationId\":\"").append(request.correlationId()).append("\", ")
                .append("\"amount\":").append(request.amount()).append("\"")
                .append("}")
                .toString();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(1000))
                .uri(URI.create("http://payment-processor-fallback:8080/payments"))
                .POST(ofString(PAYTOSAND))
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .build();
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding()).statusCode();
    }
}
