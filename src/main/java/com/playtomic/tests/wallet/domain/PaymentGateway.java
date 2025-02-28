package com.playtomic.tests.wallet.domain;

import lombok.NonNull;

import java.math.BigDecimal;

public interface PaymentGateway {
    Payment charge(@NonNull String creditCardNumber, @NonNull BigDecimal amount);
    void refund(@NonNull String paymentId);
}
