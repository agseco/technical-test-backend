package com.playtomic.tests.wallet.infrastructure;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaWalletRepositoryAdapter implements WalletRepository {
    @Override
    public Wallet findById(Wallet.Id id) {
        return null;
    }

    @Override
    public Wallet findByIdForUpdate(Wallet.Id id) {
        return null;
    }

    @Override
    public Wallet save(Wallet wallet) {
        return null;
    }
}
