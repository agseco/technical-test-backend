package com.playtomic.tests.wallet.infrastructure.jdbc;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JdbcWalletRepository extends CrudRepository<WalletEntity, UUID> {
    @Query("SELECT * FROM wallets WHERE id = :id FOR UPDATE")
    Optional<WalletEntity> findByIdForUpdate(UUID id);
}
