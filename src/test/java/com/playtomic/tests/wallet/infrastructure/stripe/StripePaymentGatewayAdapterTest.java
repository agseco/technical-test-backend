package com.playtomic.tests.wallet.infrastructure.stripe;

import com.playtomic.tests.wallet.domain.Payment;
import com.playtomic.tests.wallet.domain.PaymentGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(SpringExtension.class)
@RestClientTest(StripePaymentGatewayAdapter.class)
class StripePaymentGatewayAdapterTest {

    @Autowired
    private MockRestServiceServer mockServer;

    private final URI chargesUri = URI.create("https://sandbox.playtomic.io/v1/stripe-simulator/charges");

    @Autowired
    private StripePaymentGatewayAdapter paymentGatewayAdapter;

    @Test
    void shouldChargeCreditCardSuccessfully() {
        // Given
        PaymentGateway.CardDetails cardDetails = validCardDetails();
        BigDecimal amount = BigDecimal.valueOf(100);
        String fakePaymentId = UUID.randomUUID().toString();
        String jsonResponse = "{\"id\": \"" + fakePaymentId + "\", \"amount\": " + amount + "}";

        // Expect
        mockServer.expect(requestTo(chargesUri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("{\"credit_card\": \"" + cardDetails.number() + "\", \"amount\": " + amount + "}"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // When
        Payment payment = paymentGatewayAdapter.charge(cardDetails, amount);

        // Then
        assertThat(payment).isNotNull();
        assertThat(payment.getId().id()).isEqualTo(fakePaymentId);
        assertThat(payment.getAmount())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(100.0));

        mockServer.verify();
    }

    @Test
    void shouldThrowWhenTheProvidedAmountIsTooSmall() {
        // Given
        PaymentGateway.CardDetails cardDetails = validCardDetails();
        BigDecimal amount = BigDecimal.valueOf(0.10);

        // Expect
        mockServer.expect(requestTo(chargesUri))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("{\"credit_card\": \"" + cardDetails.number() + "\", \"amount\": " + amount + "}"))
                .andRespond(withStatus(HttpStatusCode.valueOf(422)));

        // Then
        assertThatThrownBy(() -> paymentGatewayAdapter.charge(cardDetails, amount))
                .isExactlyInstanceOf(PaymentGateway.ChargeAmountTooSmallException.class);

        mockServer.verify();
    }

    private PaymentGateway.CardDetails validCardDetails() {
        return new PaymentGateway.CardDetails("4242424242424242");
    }
}
