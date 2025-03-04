package com.playtomic.tests.wallet.infrastructure.jdbc;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class JdbcWalletRepositoryAdapter implements WalletRepository {
    @NonNull
    private final JdbcWalletRepository repository;
    @NonNull
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcWalletRepositoryAdapter(@NonNull JdbcWalletRepository repository, @NonNull JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Wallet> findById(Wallet.@NonNull Id id) {
        return repository.findById(id.id())
                .map(WalletEntity::toDomain);
    }

    @Override
    @Transactional
    public Optional<Wallet> findByIdAndLock(Wallet.@NonNull Id id) {
        return repository.findByIdForUpdate(id.id())
                .map(WalletEntity::toDomain);
    }

    @Override
    public void insert(@NonNull Wallet wallet) {
        WalletEntity entity = WalletEntity.fromDomain(wallet);

        String sql = "INSERT INTO wallets (id, user_id, balance) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, entity.getId(), entity.getUserId(), entity.getBalance());
    }

    @Override
    @Transactional
    public void update(@NonNull Wallet wallet) {
        WalletEntity entity = WalletEntity.fromDomain(wallet);
        repository.save(entity).toDomain();
    }
}
