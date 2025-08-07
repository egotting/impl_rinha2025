package com.github.egotting.impl_rinha2025;

import com.github.egotting.impl_rinha2025.domain.model.PaymentRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImplRinhaApplicationTests {
    private final String defaultUrl = "http://payment-processor-default:8080";
    @Autowired
    private TestRestTemplate _request;

    @Mock
    PaymentRequest paymentRequest;
//    ConcurrentLinkedQueue<PaymentProcessorRequest> queue = new ConcurrentLinkedQueue<>();
    @Test
    void slaTest(){
        var request = """
                                {
                                  "correlationId": "%s",
                                  "amount": %.2f
                                }
                """.formatted(paymentRequest.correlationId(), paymentRequest.amount());

        HttpHeaders header = new HttpHeaders();

        header.setContentType(MediaType.APPLICATION_JSON);

        var entity = new HttpEntity<>(request, header);

        var response = _request.postForEntity(defaultUrl + "/payments", entity, String.class);
        assertEquals(response, entity);
    }
}
