package com.playtomic.tests.wallet.domain;

import lombok.NonNull;

import java.math.BigDecimal;

public interface PaymentGateway {
    Payment charge(@NonNull String creditCardNumber, @NonNull BigDecimal amount);

    class Exception extends RuntimeException { }
    class ChargeAmountTooSmallException extends Exception { }
}
