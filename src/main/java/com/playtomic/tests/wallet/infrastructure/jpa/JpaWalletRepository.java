package com.playtomic.tests.wallet.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaWalletRepository extends JpaRepository<WalletEntity, UUID> { }
