package com.playtomic.tests.wallet.domain;

import lombok.NonNull;

import java.util.Optional;

public interface WalletRepository {
    Optional<Wallet> findById(@NonNull Wallet.Id id);
    Optional<Wallet> findByIdAndLock(@NonNull Wallet.Id id);
    void insert(@NonNull Wallet wallet);
    void update(@NonNull Wallet wallet);
}
