package com.playtomic.tests.wallet.infrastructure.jpa;

import com.playtomic.tests.core.ResourceNotFoundException;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaWalletRepositoryAdapter implements WalletRepository {
    private final JpaWalletRepository jpaRepository;

    public JpaWalletRepositoryAdapter(JpaWalletRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Wallet findById(Wallet.Id id) {
        return jpaRepository.findById(id.id())
                .map(WalletEntity::toDomain)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = WalletEntity.fromDomain(wallet);
        return jpaRepository.save(entity).toDomain();
    }
}
