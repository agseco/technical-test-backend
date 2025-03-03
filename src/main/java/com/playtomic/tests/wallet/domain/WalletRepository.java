package com.playtomic.tests.wallet.domain;

import java.util.Optional;

public interface WalletRepository {
    Optional<Wallet> findById(Wallet.Id id);
    Optional<Wallet> findByIdWithPessimisticLocking(Wallet.Id id);
    Wallet save(Wallet wallet);
}
