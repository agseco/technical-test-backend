package com.playtomic.tests.wallet.infrastructure.stripe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.playtomic.tests.wallet.domain.Payment;
import com.playtomic.tests.wallet.domain.PaymentGateway;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

import static java.util.Objects.requireNonNull;

@Service
public class StripePaymentGatewayAdapter implements PaymentGateway {

    @NonNull
    private final URI chargesUri;

    @NonNull
    private final RestTemplate restTemplate;

    public StripePaymentGatewayAdapter(@Value("${stripe.simulator.charges-uri}") @NonNull URI chargesUri,
                                       @NonNull RestTemplateBuilder restTemplateBuilder) {
        this.chargesUri = chargesUri;
        this.restTemplate =
                restTemplateBuilder
                .errorHandler(new StripeRestTemplateResponseErrorHandler())
                .build();
    }

    public Payment charge(@NonNull String creditCardNumber, @NonNull BigDecimal amount) {
        ChargeRequest body = new ChargeRequest(creditCardNumber, amount);
        ChargeResponse chargeResponse = restTemplate.postForObject(chargesUri, body, ChargeResponse.class);
        return requireNonNull(chargeResponse).asPayment();
    }

    @AllArgsConstructor
    private static class ChargeRequest {

        @NonNull
        @JsonProperty("credit_card")
        String creditCardNumber;

        @NonNull
        @JsonProperty("amount")
        BigDecimal amount;
    }

    private static class ChargeResponse {

        @NonNull
        private String id;
        @NonNull
        private BigDecimal amount;

        @JsonCreator
        public ChargeResponse(
                @JsonProperty(value = "id", required = true) String id,
                @JsonProperty(value = "amount", required = true) BigDecimal amount
        ) {
            this.id = id;
            this.amount = amount;
        }

        public @NonNull String getId() {
            return id;
        }

        public void setId(@NonNull String id) {
            this.id = id;
        }

        public @NonNull BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(@NonNull BigDecimal amount) {
            this.amount = amount;
        }

        public Payment asPayment() {
            return new Payment(
                    Payment.Id.of(id),
                    amount
            );
        }
    }
}
