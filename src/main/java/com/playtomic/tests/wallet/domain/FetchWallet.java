package com.playtomic.tests.wallet.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FetchWallet {
    private final WalletRepository repository;

    @Autowired
    public FetchWallet(WalletRepository repository) {
        this.repository = repository;
    }

    public Optional<Wallet> fetch(Wallet.Id id) {
        return repository.findById(id);
    }
}
