package com.playtomic.tests.wallet.infrastructure.jdbc;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class JdbcWalletRepositoryAdapter implements WalletRepository {
    private final JdbcWalletRepository repository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcWalletRepositoryAdapter(JdbcWalletRepository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Wallet> findById(Wallet.Id id) {
        return repository.findById(id.id())
                .map(WalletEntity::toDomain);
    }

    @Override
    @Transactional
    public Optional<Wallet> findByIdWithPessimisticLocking(Wallet.Id id) {
        return repository.findByIdForUpdate(id.id())
                .map(WalletEntity::toDomain);
    }

    @Override
    public Wallet insert(Wallet wallet) {
        WalletEntity entity = WalletEntity.fromDomain(wallet);

        String sql = "INSERT INTO wallets (id, user_id, balance) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, entity.getId(), entity.getUserId(), entity.getBalance());

        return wallet;
    }

    @Override
    @Transactional
    public Wallet save(Wallet wallet) {
        WalletEntity entity = WalletEntity.fromDomain(wallet);
        return repository.save(entity).toDomain();
    }
}
