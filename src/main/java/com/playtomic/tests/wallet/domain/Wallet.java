package com.playtomic.tests.wallet.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Wallet {
    @EqualsAndHashCode.Include
    @NonNull private final Wallet.Id id;
    @NonNull private final UserId userId;
    @NonNull private final Double balance;

    public Wallet(
        @NonNull Wallet.Id id,
        @NonNull UserId userId,
        @NonNull Double balance
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

    public Wallet topUp(double amount) {
        // TODO: replace by precondition?
        if (amount <= 0) {
            throw new IllegalArgumentException("Top up amount must be positive");
        }
        return new Wallet(
                this.id,
                this.userId,
                this.balance + amount
        );
    }
}
