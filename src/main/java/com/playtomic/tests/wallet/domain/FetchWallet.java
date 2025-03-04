package com.playtomic.tests.wallet.domain;

import com.playtomic.tests.core.domain.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchWallet {
    @NonNull private final WalletRepository repository;

    @Autowired
    public FetchWallet(@NonNull WalletRepository repository) {
        this.repository = repository;
    }

    public Wallet fetch(@NonNull Wallet.Id id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Wallet %s not found", id)));
    }
}
