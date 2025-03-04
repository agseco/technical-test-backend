package com.playtomic.tests.wallet.domain;

import lombok.NonNull;

import java.math.BigDecimal;

public interface PaymentGateway {
    Payment charge(@NonNull CardDetails cardDetails, @NonNull BigDecimal amount);

    record CardDetails(
            @NonNull String number
    ) { }

    class Exception extends RuntimeException { }
    class ChargeAmountTooSmallException extends Exception { }
}
