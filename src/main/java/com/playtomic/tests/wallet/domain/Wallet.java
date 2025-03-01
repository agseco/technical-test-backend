package com.playtomic.tests.wallet.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Wallet {
    @EqualsAndHashCode.Include
    @NonNull private final Wallet.Id id;
    @NonNull private final UserId userId;
    @NonNull private final BigDecimal balance;

    public Wallet(
        @NonNull Wallet.Id id,
        @NonNull UserId userId,
        @NonNull BigDecimal balance
    ) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public record Id(@NonNull UUID id) {
        public static Id of(@NonNull UUID id) {
            return new Wallet.Id(id);
        }
    }

    public Wallet topUp(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Top up amount must be positive");
        }
        return new Wallet(
                this.id,
                this.userId,
                this.balance.add(amount)
        );
    }
}
