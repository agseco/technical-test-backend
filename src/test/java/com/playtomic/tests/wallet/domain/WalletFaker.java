package com.playtomic.tests.wallet.domain;

import com.playtomic.tests.util.Faker;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletFaker {
    public static Wallet random() {
        return new Wallet(
                Id.random(),
                UserIdFaker.random(),
                randomBalance()
        );
    }

    private static BigDecimal randomBalance() {
        return BigDecimal.valueOf(
                Faker.faker.number().randomDouble(2, 0, 1000)
        );
    }

    public static Wallet withId(Wallet.Id id) {
        return new Wallet(
                id,
                UserId.of(UUID.randomUUID()),
                randomBalance()
        );
    }

    public static Wallet withBalance(Double balance) {
        return new Wallet(
                Id.random(),
                UserId.of(UUID.randomUUID()),
                BigDecimal.valueOf(balance)
        );
    }

    public static class Id {
        public static Wallet.Id random() {
            return Wallet.Id.of(Faker.uuid());
        }
    }

    public static class UserIdFaker {
        public static UserId random() {
            return UserId.of(Faker.uuid());
        }
    }
}
