package com.github.egotting.impl_rinha2025.http;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import com.github.egotting.impl_rinha2025.http.Interface.IRequestPaymentProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
public class RequestPaymentProcessor implements IRequestPaymentProcessor {
    @Value("${payment.processor.default}")
    private String Default;
    @Value("${payment.processor.fallback}")
    private String Fallback;
    private final WebClient webClient;

    public RequestPaymentProcessor(WebClient client) {
        this.webClient = client;
    }


    @Override
    public Mono<Boolean> paymentDefault(PaymentRequest request) throws IOException, InterruptedException {
        String PAYTOSAND = """
                {
                    "correlationId": "%s",
                    "amount": %s
                }
                """.formatted(request.correlationId(), request.amount());
//        HttpRequest httpRequest = HttpRequest.newBuilder()
////                .timeout(Duration.ofMillis(1000))
//                .uri(URI.create(Default + "/payments"))
//                .POST(ofString(PAYTOSAND))
//                .header("Content-Type", "application/json")
//                .build();
//
//        return client.send(httpRequest, HttpResponse.BodyHandlers.discarding()).statusCode();
        return webClient.post()
                .uri(Default + "/payments")
                .bodyValue(PAYTOSAND)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.just(true);
                    } else {
                        return Mono.just(false);
                    }
                });
    }

    @Override
    public Mono<Boolean> paymentFallback(PaymentRequest request) throws IOException, InterruptedException {
        String PAYTOSAND = """
                {
                    "correlationId": "%s",
                    "amount": %s
                }
                """.formatted(request.correlationId(), request.amount());


        return webClient.post()
                .uri(Fallback + "/payments")
                .bodyValue(PAYTOSAND)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.just(true);
                    } else {
                        return Mono.just(false);
                    }
                });


//        HttpRequest httpRequest = HttpRequest.newBuilder()
////                .timeout(Duration.ofMillis(1000))
//                .uri(URI.create(Fallback + "/payments"))
//                .POST(ofString(PAYTOSAND))
//                .header("Content-Type", "application/json")
//                .build();
//
//        return client.send(httpRequest, HttpResponse.BodyHandlers.discarding()).statusCode();
//
    }


    // public int paymentDefault(PaymentRequest request) {
// String PAYTOSAND = new StringBuilder("{")
// .append("\"correlationId\":\"").append(request.correlationId()).append("\",
// ")
// .append("\"amount\":").append(request.amount()).append("\"")
// .append("}")
// .toString();
//
// ResponseEntity<Void> response = Default
// .post()
// .uri(Default + "/payments")
// .body(PAYTOSAND)
// .retrieve()
// .onStatus(HttpStatusCode::is2xxSuccessful, (req, res) -> {
// logger.info("Success DEFAULT to send : " + res.getStatusCode());
// })
// .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
// logger.info("Error 400 to send: " + res.getStatusCode());
// })
// .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
// logger.info("Error 500 to send: " + res.getStatusCode());
// })
// .toBodilessEntity();
// if (!response.getStatusCode().is2xxSuccessful()) {
// System.out.println(response);
// throw new RuntimeException("err");
// }
// return response.getStatusCode().value();
// }
}
