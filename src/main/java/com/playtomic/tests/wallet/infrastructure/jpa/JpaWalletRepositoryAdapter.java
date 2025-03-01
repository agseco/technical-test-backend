package com.playtomic.tests.wallet.infrastructure.jpa;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaWalletRepositoryAdapter implements WalletRepository {
    private final JpaWalletRepository jpaRepository;

    @Autowired
    public JpaWalletRepositoryAdapter(JpaWalletRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Wallet> findById(Wallet.Id id) {
        return jpaRepository.findById(id.id())
                .map(WalletEntity::toDomain);
    }

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = WalletEntity.fromDomain(wallet);
        return jpaRepository.save(entity).toDomain();
    }
}
