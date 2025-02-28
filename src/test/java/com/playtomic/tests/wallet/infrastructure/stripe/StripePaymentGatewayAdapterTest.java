package com.playtomic.tests.wallet.infrastructure.stripe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.math.BigDecimal;
import java.net.URI;

/**
 * This test is failing with the current implementation.
 *
 * How would you test this?
 */
// TODO: possibly test with by mocking HTTP request/response
public class StripePaymentGatewayAdapterTest {

    URI testUri = URI.create("http://how-would-you-test-me.localhost");
    StripePaymentGatewayAdapter s = new StripePaymentGatewayAdapter(testUri, testUri, new RestTemplateBuilder());

    @Test
    public void test_exception() {
        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> {
            s.charge("4242 4242 4242 4242", new BigDecimal(5));
        });
    }

    @Test
    public void test_ok() throws StripeServiceException {
        s.charge("4242 4242 4242 4242", new BigDecimal(15));
    }
}
