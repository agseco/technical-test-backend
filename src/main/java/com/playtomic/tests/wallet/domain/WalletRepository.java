package com.playtomic.tests.wallet.domain;

public interface WalletRepository {
    Wallet findById(Wallet.Id id);
    Wallet save(Wallet wallet);
}
