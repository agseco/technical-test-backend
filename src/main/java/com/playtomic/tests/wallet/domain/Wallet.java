package com.playtomic.tests.wallet.domain;

import lombok.NonNull;

public class Wallet {
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

    public record Id(@NonNull String id) {
        public static Id of(@NonNull String id) {
            return new Wallet.Id(id);
        }
    }

    public Wallet topUp(double amount) {
        // TODO: replace by precondition?
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return new Wallet(this.id, this.userId, this.balance + amount);
    }
}
