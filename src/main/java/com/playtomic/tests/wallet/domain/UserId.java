package com.playtomic.tests.wallet.domain;

import lombok.NonNull;

import java.util.UUID;

public record UserId(@NonNull UUID id) {
    public static @NonNull UserId of(UUID id) {
        return new UserId(id);
    }

    public static @NonNull UserId of(String id) {
        return of(UUID.fromString(id));
    }
}
