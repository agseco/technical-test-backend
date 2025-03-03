package com.playtomic.tests.wallet.domain;

import java.util.Optional;

public interface WalletRepository {
    Optional<Wallet> findById(Wallet.Id id);
    Optional<Wallet> findByIdAndLock(Wallet.Id id);
    void insert(Wallet wallet);
    void update(Wallet wallet);
}
