package com.playtomic.tests.wallet.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Payment {
    @EqualsAndHashCode.Include
    @NonNull private final Payment.Id id;
    @NonNull private final BigDecimal amount;

    public Payment(
        @NonNull Payment.Id id,
        @NonNull BigDecimal amount
    ) {
        this.id = id;
        this.amount = amount;
    }

    public record Id(@NonNull String id) {
        public static Id of(@NonNull String id) {
            return new Payment.Id(id);
        }
    }
}
