package com.playtomic.tests.wallet.infrastructure.jdbc;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.WalletFaker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class JdbcWalletRepositoryAdapterIT {

    @Autowired
    private JdbcWalletRepositoryAdapter walletRepositoryAdapter;

    @Test
    void insertAndFindByIdShouldWork() {
        // Given
        Wallet wallet = WalletFaker.withBalance(100.0);

        // When
        walletRepositoryAdapter.insert(wallet);
        Optional<Wallet> retrievedWallet = walletRepositoryAdapter.findById(wallet.getId());

        // Then
        assertThat(retrievedWallet).isPresent()
                .hasValueSatisfying(w -> {
                    assertThat(w.getId()).isEqualTo(wallet.getId());
                    assertThat(w.getUserId()).isEqualTo(wallet.getUserId());
                    assertThat(w.getBalance())
                            .usingComparator(BigDecimal::compareTo)
                            .isEqualTo(wallet.getBalance());
                });
    }

    @Test
    void findByIdShouldReturnEmptyWhenWalletNotFound() {
        // Given
        Wallet.Id nonExistentWalletId = WalletFaker.Id.random();

        // When
        Optional<Wallet> result = walletRepositoryAdapter.findById(nonExistentWalletId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByIdAndLockShouldWork() {
        // Given
        Wallet wallet = WalletFaker.withBalance(200.0);
        walletRepositoryAdapter.insert(wallet);

        // When
        Optional<Wallet> lockedWallet = walletRepositoryAdapter.findByIdAndLock(wallet.getId());

        // Then
        assertThat(lockedWallet).isPresent()
                .hasValueSatisfying(w -> assertThat(w.getId()).isEqualTo(wallet.getId()));
    }

    @Test
    void updateWalletShouldWork() {
        // Given
        Wallet wallet = WalletFaker.withBalance(300.0);
        walletRepositoryAdapter.insert(wallet);

        // When
        walletRepositoryAdapter.update(wallet.topUp(BigDecimal.TEN));
        Optional<Wallet> retrievedWallet = walletRepositoryAdapter.findById(wallet.getId());

        // Then
        assertThat(retrievedWallet).isPresent()
                .hasValueSatisfying(w -> assertThat(w.getBalance())
                        .usingComparator(BigDecimal::compareTo)
                        .isEqualTo(BigDecimal.valueOf(310.0))
                );
    }
}
